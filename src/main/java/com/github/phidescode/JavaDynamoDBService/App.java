package com.github.phidescode.JavaDynamoDBService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Lambda function entry point. You can change to use other pojo type or
 * implement a different RequestHandler.
 *
 * @see
 * <a href=https://docs.aws.amazon.com/lambda/latest/dg/java-handler.html>Lambda
 * Java Handler</a> for more information
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static HashMap<String, String> headers;
    private static final String ORIGIN_URL = "http://localhost:3000";
    private static final DynamoDBHandler dbHandler = new DynamoDBHandler();

    public App() {
        headers = new HashMap<>();
        headers.put("Access-Control-Allow-Origin", ORIGIN_URL);
        headers.put("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        Logger.setLogger(context.getLogger());

        String httpMethod = request.getHttpMethod();
        Logger.log("Processing " + httpMethod + " request");

        return switch (httpMethod) {
            case "GET" ->
                processGet(request);
            case "POST" ->
                processPost(request);
            case "PUT" ->
                processPut(request);
            case "DELETE" ->
                processDelete(request);
            case "OPTIONS" ->
                processOptions();
            default ->
                returnError(HttpStatus.METHOD_NOT_ALLOWED);
        };
    }

    private APIGatewayProxyResponseEvent returnError(HttpStatus httpStatus) {
        Logger.log("Running returnError");
        String errorMessage = httpStatus.getReasonPhrase();
        ResponseStructure responseContent = new ResponseStructure(null, errorMessage);

        return createResponse(httpStatus, responseContent);
    }

    private APIGatewayProxyResponseEvent processGet(APIGatewayProxyRequestEvent request) {
        String[] pathSegments = request.getPath().split("/");

        if (pathSegments.length == 3) {
            String id = pathSegments[2];
            return processGetById(id);
        }

        return processGetAll();
    }

    private APIGatewayProxyResponseEvent processGetById(String id) {
        try {
            Entity entity = dbHandler.getEntity(id);

            ResponseStructure responseContent = new ResponseStructure(entity, null);
            return createResponse(HttpStatus.OK, responseContent);
        } catch (NoSuchElementException e) {
            Logger.logError("processGetById caught error: ", e);
            return returnError(HttpStatus.BAD_REQUEST);
        } catch (InterruptedException | ExecutionException e) {
            Logger.logError("processGetById caught error: ", e);
            return returnError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIGatewayProxyResponseEvent processGetAll() {
        try {
            List<Entity> entities = dbHandler.listEntities();

            ResponseStructure responseContent = new ResponseStructure(entities, null);

            return createResponse(HttpStatus.OK, responseContent);
        } catch (InterruptedException | ExecutionException e) {
            Logger.logError("processGetAll caught error: ", e);
            return returnError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIGatewayProxyResponseEvent processPost(APIGatewayProxyRequestEvent request) {
        try {
            String requestBody = request.getBody();
            BaseEntity newEntity = EntityUtils.validateRequestBody(requestBody);
            Entity entity = dbHandler.putEntity(newEntity);

            ResponseStructure responseContent = new ResponseStructure(entity, null);

            return createResponse(HttpStatus.OK, responseContent);
        } catch (ClassCastException | JsonProcessingException e) {
            Logger.logError("processPost caught error: ", e);
            return returnError(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            Logger.logError("processPost caught error: ", e);
            return returnError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIGatewayProxyResponseEvent processPut(APIGatewayProxyRequestEvent request) {
        try {
            String[] pathSegments = request.getPath().split("/");
            String id = pathSegments[2];

            String requestBody = request.getBody();
            BaseEntity updatedEntity = EntityUtils.validateRequestBody(requestBody);

            dbHandler.updateEntity(id, updatedEntity);

            ResponseStructure responseContent = new ResponseStructure(new Entity(id, updatedEntity), null);

            return createResponse(HttpStatus.OK, responseContent);
        } catch (ClassCastException | JsonProcessingException | NoSuchElementException e) {
            Logger.logError("processPost caught error: ", e);
            return returnError(HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException e) {
            Logger.logError("processPost caught error: ", e);
            return returnError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIGatewayProxyResponseEvent processDelete(APIGatewayProxyRequestEvent request) {
        try {
            String[] pathSegments = request.getPath().split("/");
            String id = pathSegments[2];
            dbHandler.deleteEntity(id);
            ResponseStructure responseContent = new ResponseStructure("OK", null);
            return createResponse(HttpStatus.OK, responseContent);

        } catch (NoSuchElementException e) {
            Logger.logError("processDelete caught error: ", e);
            return returnError(HttpStatus.BAD_REQUEST);
        } catch (InterruptedException | ExecutionException e) {
            Logger.logError("processDelete caught error: ", e);
            return returnError(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private APIGatewayProxyResponseEvent processOptions() {
        headers.put("Access-Control-Allow-Methods", "OPTIONS, POST, GET, PUT, DELETE");
        ResponseStructure responseContent = new ResponseStructure(null, null);

        return createResponse(HttpStatus.OK, responseContent);
    }

    private APIGatewayProxyResponseEvent createResponse(HttpStatus httpStatus, ResponseStructure responseContent) {

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        response.setIsBase64Encoded(false);
        response.setStatusCode(httpStatus.value());

        Map<String, Object> responseBody = new HashMap<>();

        try {
            Object responseData = responseContent.getData();
            String responseErrorMessage = responseContent.getErrorMessage();

            if (responseData != null) {
                responseBody.put("data", responseData);
                responseBody.put("errorMessage", null);
            } else {
                responseBody.put("data", null);
                responseBody.put("errorMessage", responseErrorMessage);
            }

            ObjectMapper objectMapper = new ObjectMapper();

            String responseBodyString = objectMapper.writeValueAsString(responseBody);
            response.setBody(responseBodyString);
        } catch (JsonProcessingException e) {
            Logger.logError("createResponse caught error: ", e);
            response.setStatusCode(500);

            response.setBody("{\"data\": null, \"errorMessage\": \"Internal Server Error\"}");
        }

        return response;
    }
}

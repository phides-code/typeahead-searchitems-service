package com.github.phidescode.JavaDynamoDBService;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;

public class EntityUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static BaseEntity validateRequestBody(String requestBody) throws JsonMappingException, JsonProcessingException {
        if (requestBody == null || requestBody.isEmpty()) {
            throw new ClassCastException("Invalid data format");
        }

        JsonNode jsonNode = objectMapper.readTree(requestBody);

        if (!jsonNode.has("description") || !jsonNode.get("description").isTextual() || !jsonNode.has("quantity") || !jsonNode.get("quantity").isInt()) {
            throw new ClassCastException("Invalid data format");
        }

        BaseEntity newEntity = objectMapper.treeToValue(jsonNode, BaseEntity.class);

        if (newEntity.getQuantity() < 0) {
            throw new ClassCastException("Invalid data format");
        }

        return newEntity;
    }

    public static Entity getEntityFromDBItem(Map<String, AttributeValue> item) {
        String itemId = item.get("id").s();
        String itemDescription = item.get("description").s();
        int itemQuantity = Integer.parseInt(item.get("quantity").n());

        return new Entity(itemId, new BaseEntity(itemDescription, itemQuantity));
    }

    public static HashMap<String, AttributeValueUpdate> getUpdatedValues(BaseEntity entity) {
        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();

        updatedValues.put("description", AttributeValueUpdate.builder()
                .value(AttributeValue.builder()
                        .s(entity.getDescription())
                        .build())
                .action(AttributeAction.PUT)
                .build());

        updatedValues.put("quantity", AttributeValueUpdate.builder()
                .value(AttributeValue.builder()
                        .n(entity.getQuantity() + "")
                        .build())
                .action(AttributeAction.PUT)
                .build());

        return updatedValues;
    }

    public static HashMap<String, AttributeValue> getItemValues(Entity entity) {
        HashMap<String, AttributeValue> itemValues = new HashMap<>();

        itemValues.put("id", AttributeValue.builder()
                .s(entity.getId())
                .build());

        itemValues.put("description", AttributeValue.builder()
                .s(entity.getDescription())
                .build());

        itemValues.put("quantity", AttributeValue.builder()
                .n(entity.getQuantity() + "")
                .build());

        return itemValues;
    }
}

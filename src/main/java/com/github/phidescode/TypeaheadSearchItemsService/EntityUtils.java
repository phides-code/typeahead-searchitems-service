package com.github.phidescode.TypeaheadSearchItemsService;

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

        if (!jsonNode.has("content") || !jsonNode.get("content").isTextual()) {
            throw new ClassCastException("Invalid data format");
        }

        BaseEntity newEntity = objectMapper.treeToValue(jsonNode, BaseEntity.class);

        return newEntity;
    }

    public static Entity getEntityFromDBItem(Map<String, AttributeValue> item) {
        String itemId = item.get("id").s();
        String itemContent = item.get("content").s();

        return new Entity(itemId, new BaseEntity(itemContent));
    }

    public static HashMap<String, AttributeValueUpdate> getUpdatedValues(BaseEntity entity) {
        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();

        updatedValues.put("content", AttributeValueUpdate.builder()
                .value(AttributeValue.builder()
                        .s(entity.getContent())
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

        itemValues.put("content", AttributeValue.builder()
                .s(entity.getContent())
                .build());

        return itemValues;
    }

    public static boolean isEmpty(Map<String, String> map) {
        return map == null || map.isEmpty();
    }
}

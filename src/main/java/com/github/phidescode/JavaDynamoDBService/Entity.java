package com.github.phidescode.JavaDynamoDBService;

import java.util.UUID;

public class Entity extends BaseEntity {

    private String id;

    public Entity(BaseEntity newEntity) {
        super(newEntity);
        this.id = UUID.randomUUID().toString();
    }

    public Entity(String id, BaseEntity newEntity) {
        super(newEntity);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

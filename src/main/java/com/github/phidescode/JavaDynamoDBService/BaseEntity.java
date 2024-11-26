package com.github.phidescode.JavaDynamoDBService;

public class BaseEntity {

    protected String content;

    // Jackson requires a default (no-argument) constructor to create an instance of BaseEntity during deserialization
    public BaseEntity() {
    }

    // this constructor exists so that we can call super(newEntity); in the Entity class 
    public BaseEntity(BaseEntity newBaseEntity) {
        this.content = newBaseEntity.getContent();
    }

    public BaseEntity(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

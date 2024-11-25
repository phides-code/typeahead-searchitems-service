package com.github.phidescode.JavaDynamoDBService;

public class ResponseStructure {

    private Object data;
    private String errorMessage;

    public ResponseStructure(Object data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

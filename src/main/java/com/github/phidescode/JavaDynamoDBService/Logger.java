package com.github.phidescode.JavaDynamoDBService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class Logger {

    private static LambdaLogger lambdaLogger;

    public static void setLogger(LambdaLogger logger) {
        lambdaLogger = logger;
    }

    public static void log(String message) {
        String timestampedMessage = getTimestamp() + " " + message;
        if (lambdaLogger != null) {
            lambdaLogger.log(timestampedMessage + "\n");
        } else {
            System.out.println(timestampedMessage);
        }
    }

    public static void logError(String message, Throwable throwable) {
        String timestampedMessage = getTimestamp() + " " + message + ": " + throwable.getMessage();
        if (lambdaLogger != null) {
            lambdaLogger.log(timestampedMessage + "\n");
        } else {
            System.err.println(timestampedMessage);
        }
    }

    private static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.now().format(formatter);
    }
}

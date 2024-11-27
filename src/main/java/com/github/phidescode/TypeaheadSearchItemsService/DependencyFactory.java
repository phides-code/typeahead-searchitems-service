package com.github.phidescode.TypeaheadSearchItemsService;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.http.crt.AwsCrtAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

/**
 * The module containing all dependencies required by the {@link App}.
 */
public class DependencyFactory {

    private DependencyFactory() {
    }

    /**
     * @return an instance of DynamoDbAsyncClient
     */
    public static DynamoDbAsyncClient dynamoDbClient() {
        return DynamoDbAsyncClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .httpClientBuilder(AwsCrtAsyncHttpClient.builder())
                .build();
    }
}

package com.amcglynn.myenergi.aws;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;

import java.util.Optional;

public class ZappiCredentialRepository {

    private final AmazonDynamoDB dbClient;
    private final String tableName;

    public ZappiCredentialRepository() {
        dbClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.fromName("us-east-1"))
                .build();
        tableName = "zappi-login-creds";
    }

    public Optional<ZappiCredentials> readData(String userId) {
        var request = new GetItemRequest()
                .withTableName(tableName)
                .addKeyEntry("amazon-user-id", new AttributeValue(userId));

        var result = dbClient.getItem(request);
        if (result.getItem() == null) {
            return Optional.empty();
        }
        var apiKey = result.getItem().get("api-key").getS();
        var serialNumber = result.getItem().get("serial-number").getS();
        if (apiKey == null || serialNumber == null) {
            return Optional.empty();
        }
        return Optional.of(new ZappiCredentials(userId, serialNumber, apiKey));
    }
}

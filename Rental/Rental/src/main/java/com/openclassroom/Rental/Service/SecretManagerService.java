package com.openclassroom.Rental.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Service
public class SecretManagerService {

    @Value("${AWS_ACCESS_KEY_ID}")
    private String accessKeyId;

    @Value("${AWS_SECRET_ACCESS_KEY}")
    private String secretKeyID;

    @Value("${AWS_REGION}")
    private String regionName;

    @Value("${AWS_SECRET_ID}")
    private String secretID;

    public byte[] getSecret() {

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretKeyID);

        Region region = Region.of(regionName);

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretID)
                .build();

        GetSecretValueResponse getSecretValueResponse;


        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }

        return  getSecretValueResponse.secretString().getBytes();
    }
}

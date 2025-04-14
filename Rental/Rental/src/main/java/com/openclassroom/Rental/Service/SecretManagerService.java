package com.openclassroom.Rental.Service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.net.http.WebSocket;
import java.security.Key;

@Service
public class SecretManagerService {



    public static byte[] getSecret() {

        Dotenv dotenv = Dotenv.load();

        String accessKeyId = dotenv.get("AWS_ACCESS_KEY_ID");
        System.out.println(accessKeyId);
        if (accessKeyId == null || accessKeyId.isEmpty()) {
            throw new IllegalArgumentException("La variable 'AWS_ACCESS_KEY_ID' est manquante.");
        }

        String secretKeyID = dotenv.get("AWS_SECRET_ACCESS_KEY");
        System.out.println(secretKeyID);
        if (secretKeyID == null || secretKeyID.isEmpty()) {
            throw new IllegalArgumentException("La variable 'AWS_SECRET_ACCESS_KEY' est manquante.");
        }

        String regionName = dotenv.get("AWS_REGION");
        System.out.println(regionName);
        if (regionName == null || regionName .isEmpty()) {
            throw new IllegalArgumentException("La variable  'AWS_REGION' est manquante.");
        }
        Region region = Region.of(regionName);
        System.out.println(region.toString());

        String secretID = dotenv.get("AWS_SECRET_ID");
        System.out.println(secretID);
        if (secretID == null || secretID .isEmpty()) {
            throw new IllegalArgumentException("La variable  'AWS_SECRET_ID' est manquante.");
        }

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretKeyID);

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretID)
                .build();;

        GetSecretValueResponse getSecretValueResponse;


        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }

        return  getSecretValueResponse.secretString().getBytes();
    }
}

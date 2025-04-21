package com.openclassroom.Rental.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class S3Service {

    private final S3Client s3Client;

    Dotenv dotenv = Dotenv.load();


    public S3Service() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                dotenv.get("AWS_ACCESS_KEY_ID"),
                dotenv.get("AWS_SECRET_ACCESS_KEY")
        );

        this.s3Client = S3Client.builder()
                .region(Region.of(dotenv.get("AWS_BUCKET_REGION")))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String uploadFile(MultipartFile file, String key) {
        try {
            // Convertir le fichier MultipartFile en un fichier temporaire
            String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath));

            // Créer une requête pour télécharger le fichier
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(dotenv.get("AWS_BUCKET_NAME"))
                    .key(key)
                    .build();

            // Télécharger le fichier dans S3
            s3Client.putObject(putObjectRequest, Paths.get(filePath));

            return "https://" + dotenv.get("AWS_BUCKET_NAME") + ".s3." + dotenv.get("AWS_BUCKET_REGION") + ".amazonaws.com/" + key;
        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Erreur lors du téléchargement du fichier dans S3", e);
        }

    }
}

package com.openclassroom.Rental.Service;

import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final String regionName;





    public S3Service(@Value("${AWS_ACCESS_KEY_ID}") String accessKeyId,
                     @Value("${AWS_SECRET_ACCESS_KEY}") String secretKeyID,
                     @Value("${AWS_BUCKET_REGION}") String regionName,
                     @Value("${AWS_BUCKET_NAME}") String bucketName)  {

        this.bucketName = bucketName;
        this.regionName = regionName;

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(
                accessKeyId,
                secretKeyID
        );

        this.s3Client = S3Client.builder()
                .region(Region.of(regionName))
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
                    .bucket(bucketName)
                    .key(key)
                    .build();

            // Télécharger le fichier dans S3
            s3Client.putObject(putObjectRequest, Paths.get(filePath));

            return "https://" + bucketName  + ".s3." + regionName + ".amazonaws.com/" + key;
        } catch (S3Exception | IOException e) {
            throw new RuntimeException("Erreur lors du téléchargement du fichier dans S3", e);
        }

    }

    @PreDestroy
    public void shutdown() {
        if (s3Client != null) {
        s3Client.close();}
    }
}

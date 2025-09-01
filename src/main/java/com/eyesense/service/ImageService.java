package com.eyesense.service;

import com.eyesense.ai.AiClient;
import com.eyesense.ai.dto.AiResponse;
import com.eyesense.model.ImageRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class ImageService {

    private final S3Client s3Client;
    private final AiClient aiClient;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.publicBaseUrl}")
    private String publicBaseUrl;

    public ImageService(S3Client s3Client, AiClient aiClient) {
        this.s3Client = s3Client;
        this.aiClient = aiClient;
    }

    public ImageRecord processImage(MultipartFile file) throws IOException {
        // ID
        String internalId = UUID.randomUUID().toString();

        // S3 key pro filename
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String key = "uploads/" + internalId + "-" + Instant.now().toEpochMilli() + "-" + filename;

        // Upload pro S3
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(put, RequestBody.fromBytes(file.getBytes()));

        // URL publica pra imagem
        // s3_uri no formato esperado pela API Python
        String s3Uri = "s3://" + bucket + "/" + key;

        // chama API com a URL do s3 + request_id
        AiResponse response = aiClient.analyzeImage(s3Uri, internalId);

        // wrap up
        return new ImageRecord(internalId, s3Uri, response);
    }
}

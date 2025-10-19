package com.eyesense.service;

import com.eyesense.ai.AiClient;
import com.eyesense.ai.dto.AiResponse;
import com.eyesense.model.ImageRecord;
import com.eyesense.model.Objeto;
import com.eyesense.model.Result;
import com.eyesense.model.User;
import com.eyesense.repository.ObjectRepository;
import com.eyesense.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
public class ImageService {

    private String getExtension(String filename) {
        int lastDotIdx = filename.lastIndexOf('.');
        if (lastDotIdx > 0) {
            return filename.substring(lastDotIdx + 1);
        }
        return "";
    }

    private final S3Client s3Client;
    private final AiClient aiClient;
    private final AuthService authService;
    private final ObjectRepository objectRepository;
    private final ResultRepository resultRepository;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.publicBaseUrl}")
    private String publicBaseUrl;

    public ImageService(S3Client s3Client, 
                       AiClient aiClient, 
                       AuthService authService,
                       ObjectRepository objectRepository,
                       ResultRepository resultRepository) {
        this.s3Client = s3Client;
        this.aiClient = aiClient;
        this.authService = authService;
        this.objectRepository = objectRepository;
        this.resultRepository = resultRepository;
    }

    @Transactional
    public ImageRecord processImage(MultipartFile file) throws IOException {
        User currentUser = authService.getCurrentUser();

        // Generate ID and prepare S3 path
        String internalId = UUID.randomUUID().toString();
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String key = "uploads/" + internalId + "-" + Instant.now().toEpochMilli() + "-" + filename;

        // Upload to S3
        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(put, RequestBody.fromBytes(file.getBytes()));

        // Create and save Objeto entity
        Objeto imageObject = new Objeto();
        imageObject.setName(filename);
        imageObject.setExtension(getExtension(filename));
        imageObject.setPathS3(key);
        imageObject.setUser(currentUser);
        
        objectRepository.save(imageObject);

        // URL publica pra imagem
        String s3Uri = "s3://" + bucket + "/" + key;
        String publicUrl = publicBaseUrl + "/" + key;

        // Chama API ML
        AiResponse response = aiClient.analyzeImage(s3Uri, internalId);

        // Cria novo resultado
        Result result = new Result();

        if (response.probabilities() != null) {
            for (var probMap : response.probabilities()) {
                for (var entry : probMap.entrySet()) {
                    String label = entry.getKey();
                    BigDecimal probability = BigDecimal.valueOf(entry.getValue() * 100);

                    switch (label) {
                        case "saudavel" -> result.setHealthyProbability(probability);
                        case "catarata" -> result.setCataractProbability(probability);
                        case "glaucoma" -> result.setGlaucomaProbability(probability);
                    }
                }
            }
        }

        result.setRawJson(response.toString());
        result.setModelVersion(response.model_version());
        result.setObjeto(imageObject);
        resultRepository.save(result);

        return new ImageRecord(internalId, publicUrl, response);
    }
}

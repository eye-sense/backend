package com.eyesense.ai;

import com.eyesense.ai.dto.AiRequest;
import com.eyesense.ai.dto.AiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AiClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public AiClient(RestTemplate restTemplate,
                    @Value("${ai.api.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public AiResponse analyzeImage(String s3Url, String requestId) {
        AiRequest request = new AiRequest(s3Url, requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AiRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(
                baseUrl + "/predict",
                entity,
                AiResponse.class
        );
    }
}

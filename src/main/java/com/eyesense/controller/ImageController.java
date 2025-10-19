package com.eyesense.controller;

import com.eyesense.ai.dto.AiResponse;
import com.eyesense.model.ImageRecord;
import com.eyesense.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Selecione um arquivo de imagem."));
        }

        ImageRecord imageRecord = imageService.processImage(file);
        AiResponse aiResponse = imageRecord.aiResponse();

        Map<String, Object> analysisResult = new HashMap<>();
        if (aiResponse.probabilities() != null) {
            for (var probMap : aiResponse.probabilities()) {
                for (var entry : probMap.entrySet()) {
                    analysisResult.put(entry.getKey(), Map.of("confidence", (int)(entry.getValue() * 100)));
                }
            }
        }


        Map<String, Object> response = new HashMap<>();
        response.put("imageUrl", imageRecord.s3Url());
        response.put("analysisResult", analysisResult);
        response.put("label", aiResponse.label());
        response.put("requestId", aiResponse.request_id());
        response.put("modelVersion", aiResponse.model_version());

        return ResponseEntity.ok(response);
    }
}
package com.eyesense.ai.dto;

import java.util.List;
import java.util.Map;

public record AiResponse(
        String request_id,
        String label,
        List<Map<String, Double>> probabilities,
        String model_version
) {}

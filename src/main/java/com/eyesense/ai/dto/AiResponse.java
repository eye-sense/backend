package com.eyesense.ai.dto;

import java.util.List;

public record AiResponse(
        String request_id,
        String label,
        List<Probability> probabilities,
        String model_version
) {}

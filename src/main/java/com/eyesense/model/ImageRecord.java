package com.eyesense.model;

import com.eyesense.ai.dto.AiResponse;

public record ImageRecord(String id, String s3Url, AiResponse aiResponse) {}

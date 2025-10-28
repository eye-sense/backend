package com.eyesense.dto;

import java.math.BigDecimal;

public class UploadHistoryDTO {
    private Long objectId;
    private String modelVersion;
    private BigDecimal catarataProbability;
    private BigDecimal glaucomaProbability;
    private BigDecimal healthyProbability;

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public BigDecimal getCatarataProbability() {
        return catarataProbability;
    }

    public void setCatarataProbability(BigDecimal catarataProbability) {
        this.catarataProbability = catarataProbability;
    }

    public BigDecimal getGlaucomaProbability() {
        return glaucomaProbability;
    }

    public void setGlaucomaProbability(BigDecimal glaucomaProbability) {
        this.glaucomaProbability = glaucomaProbability;
    }

    public BigDecimal getHealthyProbability() {
        return healthyProbability;
    }

    public void setHealthyProbability(BigDecimal healthyProbability) {
        this.healthyProbability = healthyProbability;
    }
}

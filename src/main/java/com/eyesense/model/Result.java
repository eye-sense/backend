package com.eyesense.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "resultado", schema = "eye_sense")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "probabilidade_saudavel", nullable = false, precision = 5, scale = 2)
    private BigDecimal healthyProbability;

    @Column(name = "probabilidade_catarata", nullable = false, precision = 5, scale = 2)
    private BigDecimal cataractProbability;

    @Column(name = "probabilidade_glaucoma", nullable = false, precision = 5, scale = 2)
    private BigDecimal glaucomaProbability;

    @Column(name = "raw", nullable = false, columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Object rawJson;

    @Column(name = "model_version", nullable = false, length = 32)
    private String modelVersion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objeto_id", nullable = false)
    private Objeto objeto;

    public Result() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getHealthyProbability() {
        return healthyProbability;
    }

    public void setHealthyProbability(BigDecimal healthyProbability) {
        this.healthyProbability = healthyProbability;
    }

    public BigDecimal getCataractProbability() {
        return cataractProbability;
    }

    public void setCataractProbability(BigDecimal cataractProbability) {
        this.cataractProbability = cataractProbability;
    }

    public BigDecimal getGlaucomaProbability() {
        return glaucomaProbability;
    }

    public void setGlaucomaProbability(BigDecimal glaucomaProbability) {
        this.glaucomaProbability = glaucomaProbability;
    }

    public Object getRawJson() {
        return rawJson;
    }

    public void setRawJson(Object rawJson) {
        this.rawJson = rawJson;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }
}
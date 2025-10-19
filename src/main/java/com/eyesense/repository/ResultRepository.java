package com.eyesense.repository;

import com.eyesense.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    Optional<Result> findByObjetoId(Long objectId);
    Optional<Result> findByObjetoIdAndDeletedAtIsNull(Long objectId);
}
package com.eyesense.repository;

import com.eyesense.model.Objeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ObjectRepository extends JpaRepository<Objeto, Long> {
    List<Objeto> findByUserId(Long userId);
    List<Objeto> findByUserIdAndDeletedAtIsNull(Long userId);
}
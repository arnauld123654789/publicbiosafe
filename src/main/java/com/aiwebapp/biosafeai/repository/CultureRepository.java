package com.aiwebapp.biosafeai.repository;

import com.aiwebapp.biosafeai.entity.Culture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CultureRepository extends JpaRepository<Culture, Long> {
    List<Culture> findByType(String type);
} 
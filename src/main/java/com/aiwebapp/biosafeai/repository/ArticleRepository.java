package com.aiwebapp.biosafeai.repository;

import com.aiwebapp.biosafeai.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCultureId(Long cultureId);
    List<Article> findByPublisherId(Long publisherId);
    List<Article> findByType(String type);
} 
package com.aiwebapp.biosafeai.repository;

import com.aiwebapp.biosafeai.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String email);
    Optional<Admin> findByPhoneNumber(String phoneNumber);
    Optional<Admin> findByUtilisateurId(Long utilisateurId);
} 
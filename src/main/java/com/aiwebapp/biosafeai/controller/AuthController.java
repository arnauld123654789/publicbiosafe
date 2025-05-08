package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.AuthResponse;
import com.aiwebapp.biosafeai.dto.LoginRequest;
import com.aiwebapp.biosafeai.dto.RegisterRequest;
import com.aiwebapp.biosafeai.entity.Utilisateur;
import com.aiwebapp.biosafeai.repository.UtilisateurRepository;
import com.aiwebapp.biosafeai.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (utilisateurRepository.findByUsername(request.getUsername()).isPresent() ||
            utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username or email already exists");
        }
        Utilisateur user = Utilisateur.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        utilisateurRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Utilisateur user = utilisateurRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return ResponseEntity.ok(new AuthResponse(token));
    }
} 
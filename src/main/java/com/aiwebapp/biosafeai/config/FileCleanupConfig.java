package com.aiwebapp.biosafeai.config;

import com.aiwebapp.biosafeai.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class FileCleanupConfig {

    private final FileStorageService fileStorageService;

    @Scheduled(cron = "0 0 0 1 * *") // Run at midnight on the first day of every month
    public void cleanupOldFiles() {
        fileStorageService.cleanupOldFiles();
    }
} 
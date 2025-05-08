package com.aiwebapp.biosafeai.service;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.backup-dir}")
    private String backupDir;

    @Value("${ffmpeg.path.windows}")
    private String ffmpegPathWindows;

    @Value("${ffmpeg.path.linux}")
    private String ffmpegPathLinux;

    private static final long MAX_IMAGE_SIZE = 15 * 1024 * 1024; // 15MB
    private static final long MAX_VIDEO_DOC_SIZE = 65 * 1024 * 1024; // 65MB
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
    private static final List<String> VIDEO_EXTENSIONS = Arrays.asList("mp4", "avi", "mov", "wmv", "flv", "mkv");
    private static final List<String> DOCUMENT_EXTENSIONS = Arrays.asList("pdf", "doc", "docx", "xls", "xlsx", "txt");

    private String getFFmpegPath() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("windows") ? ffmpegPathWindows : ffmpegPathLinux;
    }

    public String storeFile(MultipartFile file, String entityType, String entityId) throws IOException {
        validateFile(file);
        
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = generateUniqueFilename(originalFilename);
        
        // Create directory structure: uploadDir/entityType/entityId/YYYY-MM/
        String relativePath = String.format("%s/%s/%s", entityType, entityId, 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
        Path uploadPath = Paths.get(uploadDir, relativePath);
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(newFilename);
        
        // Process and save file based on type
        if (isImage(fileExtension)) {
            saveImage(file, filePath);
        } else if (isVideo(fileExtension)) {
            saveVideo(file, filePath);
        } else {
            Files.copy(file.getInputStream(), filePath);
        }

        // Create backup
        createBackup(filePath, relativePath);

        return relativePath + "/" + newFilename;
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }

        String extension = getFileExtension(file.getOriginalFilename());
        long fileSize = file.getSize();

        if (isImage(extension) && fileSize > MAX_IMAGE_SIZE) {
            throw new IOException("Image file size exceeds 15MB limit");
        } else if ((isVideo(extension) || isDocument(extension)) && fileSize > MAX_VIDEO_DOC_SIZE) {
            throw new IOException("File size exceeds 65MB limit");
        }
    }

    private void saveImage(MultipartFile file, Path filePath) throws IOException {
        // Compress image if it's larger than 5MB
        if (file.getSize() > 5 * 1024 * 1024) {
            Thumbnails.of(file.getInputStream())
                    .size(1920, 1080) // Max dimensions
                    .outputQuality(0.8) // 80% quality
                    .toFile(filePath.toFile());
        } else {
            Files.copy(file.getInputStream(), filePath);
        }
    }

    private void saveVideo(MultipartFile file, Path filePath) throws IOException {
        // First save the original file
        Path tempPath = filePath.getParent().resolve("temp_" + filePath.getFileName());
        Files.copy(file.getInputStream(), tempPath);

        try {
            // Compress video if it's larger than 20MB
            if (file.getSize() > 20 * 1024 * 1024) {
                FFmpeg ffmpeg = new FFmpeg(getFFmpegPath());
                FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(tempPath.toString())
                    .overrideOutputFiles(true)
                    .addOutput(filePath.toString())
                    .setFormat("mp4")
                    .setVideoCodec("libx264")
                    .setVideoResolution(1280, 720) // 720p
                    .setVideoBitRate(2000000) // 2Mbps
                    .setAudioCodec("aac")
                    .setAudioBitRate(128000) // 128kbps
                    .setAudioChannels(2)
                    .setAudioSampleRate(44100)
                    .done();

                FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
                executor.createJob(builder).run();
            } else {
                // If file is small enough, just move it
                Files.move(tempPath, filePath);
            }
        } finally {
            // Clean up temp file
            Files.deleteIfExists(tempPath);
        }
    }

    private void createBackup(Path filePath, String relativePath) throws IOException {
        Path backupPath = Paths.get(backupDir, relativePath);
        Files.createDirectories(backupPath);
        Files.copy(filePath, backupPath.resolve(filePath.getFileName()));
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(uploadDir, filePath);
        Files.deleteIfExists(path);
    }

    public void cleanupOldFiles() {
        try {
            LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
            Path uploadPath = Paths.get(uploadDir);
            Path backupPath = Paths.get(backupDir);

            // Delete old files from upload directory
            Files.walk(uploadPath)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toInstant()
                            .isBefore(oneMonthAgo.toInstant(java.time.ZoneOffset.UTC));
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        log.error("Error deleting file: " + path, e);
                    }
                });

            // Delete old files from backup directory
            Files.walk(backupPath)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    try {
                        return Files.getLastModifiedTime(path).toInstant()
                            .isBefore(oneMonthAgo.toInstant(java.time.ZoneOffset.UTC));
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        log.error("Error deleting backup file: " + path, e);
                    }
                });
        } catch (IOException e) {
            log.error("Error during file cleanup", e);
        }
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean isImage(String extension) {
        return IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }

    private boolean isVideo(String extension) {
        return VIDEO_EXTENSIONS.contains(extension.toLowerCase());
    }

    private boolean isDocument(String extension) {
        return DOCUMENT_EXTENSIONS.contains(extension.toLowerCase());
    }
} 
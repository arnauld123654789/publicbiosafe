package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.MessageDto;
import com.aiwebapp.biosafeai.entity.Message;
import com.aiwebapp.biosafeai.enums.MessageType;
import com.aiwebapp.biosafeai.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final FileStorageService fileStorageService;

    public MessageDto createMessage(MessageDto messageDto, MultipartFile mediaFile) throws IOException {
        Message message = new Message();
        // Set basic message properties
        message.setContent(messageDto.getContent());
        message.setType(messageDto.getType());
        
        // Handle media file if present
        if (mediaFile != null && !mediaFile.isEmpty()) {
            // Store the file and get the path
            String filePath = fileStorageService.storeFile(mediaFile, "messages", message.getId().toString());
            
            // Set media properties based on file type
            if (messageDto.getType() == MessageType.IMAGE) {
                message.setMediaUrl(filePath);
                message.setMediaType(mediaFile.getContentType());
                message.setMediaSize(mediaFile.getSize());
            } else if (messageDto.getType() == MessageType.VIDEO) {
                message.setMediaUrl(filePath);
                message.setMediaType(mediaFile.getContentType());
                message.setMediaSize(mediaFile.getSize());
            } else if (messageDto.getType() == MessageType.DOCUMENT) {
                message.setMediaUrl(filePath);
                message.setMediaType(mediaFile.getContentType());
                message.setMediaSize(mediaFile.getSize());
            }
        }

        // Save the message
        Message savedMessage = messageRepository.save(message);
        return convertToDto(savedMessage);
    }

    public void deleteMessage(Long messageId) throws IOException {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        // Delete associated media file if exists
        if (message.getMediaUrl() != null) {
            fileStorageService.deleteFile(message.getMediaUrl());
        }

        messageRepository.delete(message);
    }

    private MessageDto convertToDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setType(message.getType());
        dto.setMediaUrl(message.getMediaUrl());
        dto.setMediaType(message.getMediaType());
        dto.setMediaSize(message.getMediaSize());
        // Set other properties as needed
        return dto;
    }
} 
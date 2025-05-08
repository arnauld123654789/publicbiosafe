package com.aiwebapp.biosafeai.dto;

import com.aiwebapp.biosafeai.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class MessageDto {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private MessageType type;
    private String content;
    private String mediaUrl;
    private String mediaType;
    private Long mediaSize;
    private Set<String> tags;
    private Set<Long> mentionIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isEdited;
    private boolean isDeleted;
} 
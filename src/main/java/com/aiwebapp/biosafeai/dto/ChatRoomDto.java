package com.aiwebapp.biosafeai.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ChatRoomDto {
    private Long id;
    private String name;
    private String description;
    private boolean isGroup;
    private Set<Long> participantIds;
    private Set<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;
    private Long lastMessageId;
    private String lastMessageContent;
    private String lastMessageType;
    private LocalDateTime lastMessageTime;
} 
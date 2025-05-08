package com.aiwebapp.biosafeai.controller;

import com.aiwebapp.biosafeai.dto.ChatRoomDto;
import com.aiwebapp.biosafeai.dto.MessageDto;
import com.aiwebapp.biosafeai.enums.MessageType;
import com.aiwebapp.biosafeai.service.ChatRoomService;
import com.aiwebapp.biosafeai.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/rooms")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ChatRoomDto>> getUserChats() {
        // Get current user ID from security context
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(chatRoomService.getUserChats(userId));
    }

    @PostMapping("/direct/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatRoomDto> createDirectChat(@PathVariable Long userId) {
        Long currentUserId = getCurrentUserId();
        return ResponseEntity.ok(chatRoomService.createDirectChat(currentUserId, userId));
    }

    @PostMapping("/group")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatRoomDto> createGroupChat(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam Set<Long> participantIds,
            @RequestParam(required = false) Set<String> tags) {
        return ResponseEntity.ok(chatRoomService.createGroupChat(name, description, participantIds, tags));
    }

    @PostMapping("/group/{chatRoomId}/participants")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatRoomDto> addParticipants(
            @PathVariable Long chatRoomId,
            @RequestParam Set<Long> participantIds) {
        return ResponseEntity.ok(chatRoomService.addParticipants(chatRoomId, participantIds));
    }

    @DeleteMapping("/group/{chatRoomId}/participants/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ChatRoomDto> removeParticipant(
            @PathVariable Long chatRoomId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(chatRoomService.removeParticipant(chatRoomId, userId));
    }

    @PostMapping("/messages")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageDto> sendMessage(
            @RequestParam Long chatRoomId,
            @RequestParam(required = false) MultipartFile mediaFile,
            @RequestParam String content,
            @RequestParam String type) throws IOException {
        MessageDto messageDto = new MessageDto();
        messageDto.setChatRoomId(chatRoomId);
        messageDto.setContent(content);
        messageDto.setType(MessageType.valueOf(type));
        messageDto.setSenderId(getCurrentUserId());

        MessageDto savedMessage = messageService.createMessage(messageDto, mediaFile);
        
        // Send message to all participants in the chat room
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, savedMessage);
        
        return ResponseEntity.ok(savedMessage);
    }

    @MessageMapping("/chat.send")
    public void handleMessage(@Payload MessageDto messageDto) {
        // This method handles WebSocket messages
        messagingTemplate.convertAndSend("/topic/chat/" + messageDto.getChatRoomId(), messageDto);
    }

    private Long getCurrentUserId() {
        // TODO: Implement getting current user ID from security context
        return 1L; // Placeholder
    }
} 
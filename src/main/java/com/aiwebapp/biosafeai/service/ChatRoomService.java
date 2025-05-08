package com.aiwebapp.biosafeai.service;

import com.aiwebapp.biosafeai.dto.ChatRoomDto;
import com.aiwebapp.biosafeai.entity.ChatRoom;
import com.aiwebapp.biosafeai.entity.Utilisateur;
import com.aiwebapp.biosafeai.repository.ChatRoomRepository;
import com.aiwebapp.biosafeai.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UtilisateurRepository utilisateurRepository;

    public List<ChatRoomDto> getUserChats(Long userId) {
        return chatRoomRepository.findByParticipantId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ChatRoomDto createDirectChat(Long userId1, Long userId2) {
        ChatRoom existingChat = chatRoomRepository.findDirectChat(userId1, userId2);
        if (existingChat != null) {
            return convertToDto(existingChat);
        }

        Utilisateur user1 = utilisateurRepository.findById(userId1)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Utilisateur user2 = utilisateurRepository.findById(userId2)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom chatRoom = ChatRoom.builder()
                .name(user1.getUsername() + " & " + user2.getUsername())
                .isGroup(false)
                .participants(Set.of(user1, user2))
                .build();

        return convertToDto(chatRoomRepository.save(chatRoom));
    }

    @Transactional
    public ChatRoomDto createGroupChat(String name, String description, Set<Long> participantIds, Set<String> tags) {
        Set<Utilisateur> participants = participantIds.stream()
                .map(id -> utilisateurRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found: " + id)))
                .collect(Collectors.toSet());

        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .description(description)
                .isGroup(true)
                .participants(participants)
                .tags(tags)
                .build();

        return convertToDto(chatRoomRepository.save(chatRoom));
    }

    @Transactional
    public ChatRoomDto addParticipants(Long chatRoomId, Set<Long> participantIds) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!chatRoom.isGroup()) {
            throw new RuntimeException("Cannot add participants to a direct chat");
        }

        Set<Utilisateur> newParticipants = participantIds.stream()
                .map(id -> utilisateurRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found: " + id)))
                .collect(Collectors.toSet());

        chatRoom.getParticipants().addAll(newParticipants);
        return convertToDto(chatRoomRepository.save(chatRoom));
    }

    @Transactional
    public ChatRoomDto removeParticipant(Long chatRoomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        if (!chatRoom.isGroup()) {
            throw new RuntimeException("Cannot remove participants from a direct chat");
        }

        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        chatRoom.getParticipants().remove(user);
        return convertToDto(chatRoomRepository.save(chatRoom));
    }

    private ChatRoomDto convertToDto(ChatRoom chatRoom) {
        ChatRoomDto dto = new ChatRoomDto();
        dto.setId(chatRoom.getId());
        dto.setName(chatRoom.getName());
        dto.setDescription(chatRoom.getDescription());
        dto.setGroup(chatRoom.isGroup());
        dto.setParticipantIds(chatRoom.getParticipants().stream()
                .map(Utilisateur::getId)
                .collect(Collectors.toSet()));
        dto.setTags(chatRoom.getTags());
        dto.setCreatedAt(chatRoom.getCreatedAt());
        dto.setLastActivity(chatRoom.getLastActivity());
        
        // Set last message info if available
        if (!chatRoom.getMessages().isEmpty()) {
            var lastMessage = chatRoom.getMessages().stream()
                    .max((m1, m2) -> m1.getCreatedAt().compareTo(m2.getCreatedAt()))
                    .orElse(null);
            if (lastMessage != null) {
                dto.setLastMessageId(lastMessage.getId());
                dto.setLastMessageContent(lastMessage.getContent());
                dto.setLastMessageType(lastMessage.getType().name());
                dto.setLastMessageTime(lastMessage.getCreatedAt());
            }
        }
        
        return dto;
    }
} 
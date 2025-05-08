package com.aiwebapp.biosafeai.repository;

import com.aiwebapp.biosafeai.entity.Message;
import com.aiwebapp.biosafeai.enums.MessageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId AND " +
           "(:tag MEMBER OF m.tags OR EXISTS (SELECT 1 FROM m.mentions men WHERE men.id = :userId))")
    List<Message> findByChatRoomIdAndTagOrMention(
            @Param("chatRoomId") Long chatRoomId,
            @Param("tag") String tag,
            @Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId AND m.type = :type")
    List<Message> findByChatRoomIdAndType(
            @Param("chatRoomId") Long chatRoomId,
            @Param("type") MessageType type);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId AND " +
           "m.content LIKE %:searchTerm% OR EXISTS (SELECT 1 FROM m.tags t WHERE t LIKE %:searchTerm%)")
    List<Message> searchInChatRoom(
            @Param("chatRoomId") Long chatRoomId,
            @Param("searchTerm") String searchTerm);

    @Query("SELECT m FROM Message m WHERE m.sender.id = :userId AND m.isDeleted = false")
    List<Message> findBySenderId(@Param("userId") Long userId);

    @Query("SELECT m FROM Message m WHERE m.chatRoom.id = :chatRoomId AND " +
           "m.createdAt > :since AND m.isDeleted = false")
    List<Message> findNewMessages(
            @Param("chatRoomId") Long chatRoomId,
            @Param("since") java.time.LocalDateTime since);
} 
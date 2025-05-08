package com.aiwebapp.biosafeai.repository;

import com.aiwebapp.biosafeai.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT cr FROM ChatRoom cr JOIN cr.participants p WHERE p.id = :userId")
    List<ChatRoom> findByParticipantId(@Param("userId") Long userId);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.isGroup = true AND :tag MEMBER OF cr.tags")
    List<ChatRoom> findByTag(@Param("tag") String tag);

    @Query("SELECT DISTINCT cr FROM ChatRoom cr WHERE cr.isGroup = true AND EXISTS (SELECT 1 FROM cr.tags t WHERE t IN :tags)")
    List<ChatRoom> findByTags(@Param("tags") Set<String> tags);

    @Query("SELECT cr FROM ChatRoom cr WHERE cr.isGroup = false AND " +
           "EXISTS (SELECT 1 FROM cr.participants p1 WHERE p1.id = :userId1) AND " +
           "EXISTS (SELECT 1 FROM cr.participants p2 WHERE p2.id = :userId2)")
    ChatRoom findDirectChat(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
} 
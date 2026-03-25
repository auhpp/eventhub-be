package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.MessageStatus;
import com.auhpp.event_management.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m " +
            "WHERE m.recipient.conversation.id = :conversationId")
    Page<Message> filter(@Param("conversationId") Long conversationId, Pageable pageable);

    @Query("SELECT COUNT(m) From Message m " +
            "WHERE m.recipient.appUser.email = :memberEmail " +
            "AND (:conversationId IS NULL OR m.recipient.conversation.id = :conversationId ) " +
            "AND m.seenAt = null")
    Long countUnseenMessage(
            @Param("memberEmail") String memberEmail,
            @Param("conversationId") Long conversationId
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Message m SET m.seenAt = current_timestamp, m.status = :status " +
            "WHERE m.recipient.conversation.id = :conversationId " +
            "AND m.recipient.appUser.email = :currentUserEmail " +
            "AND (m.seenAt IS NULL OR m.status != :status) ")
    int markAllAsSeenByConversation(
            @Param("status") MessageStatus status,
            @Param("conversationId") Long conversationId,
            @Param("currentUserEmail") String currentUserEmail);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Message m SET m.status = :status " +
            "WHERE m.recipient.appUser.email = :currentUserEmail " +
            "AND m.status = :statusCondition ")
    int markAllAsReceiveByConversation(
            @Param("statusCondition") MessageStatus statusCondition,
            @Param("status") MessageStatus status,
            @Param("currentUserEmail") String currentUserEmail);
}

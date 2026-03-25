package com.auhpp.event_management.repository;

import com.auhpp.event_management.constant.MessageStatus;
import com.auhpp.event_management.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT DISTINCT c FROM Conversation c " +
            "LEFT JOIN c.conversationMembers cm " +
            "WHERE cm.appUser.id = :memberId1 " +
            "AND c.id IN ( " +
            " SELECT DISTINCT c.id FROM Conversation c " +
            " LEFT JOIN c.conversationMembers cm " +
            " WHERE cm.appUser.id = :memberId2) ")
    Optional<Conversation> existsConversation(@Param("memberId1") Long memberId1,
                                              @Param("memberId2") Long memberId2);

    @Query("SELECT c FROM Conversation c " +
            "WHERE (:hasPin IS NULL OR c.hasPin = :hasPin) " +
            "AND EXISTS (" +
            "   SELECT 1 FROM ConversationMember cm " +
            "   LEFT JOIN cm.receivedMessages rm " +
            "   WHERE cm.conversation.id = c.id " +
            "   AND (:status IS NULL OR rm.status = :status) " +
            "   AND cm.appUser.email = :currentUserEmail " +
            ") " +
            "AND (:nameMember IS NULL OR EXISTS (" +
            "   SELECT 1 FROM ConversationMember otherCm " +
            "   WHERE otherCm.conversation.id = c.id " +
            "   AND LOWER(otherCm.appUser.fullName) " +
            "       LIKE LOWER(CONCAT('%', CAST(:nameMember AS string), '%')) " +
            "   AND otherCm.appUser.email != :currentUserEmail " +
            ") ) " +
            "ORDER BY c.hasPin DESC, c.latestMessage.createdAt DESC "
    )
    Page<Conversation> filter(@Param("hasPin") Boolean hasPin,
                              @Param("status") MessageStatus status,
                              @Param("nameMember") String nameMember,
                              @Param("currentUserEmail") String currentUserEmail,
                              Pageable pageable
    );

    @Query("SELECT c.conversation FROM ConversationMember c " +
            "WHERE c.appUser.email = :currentUserEmail " +
            "AND c.conversation.id IN (" +
            "   SELECT oc.conversation.id FROM ConversationMember oc " +
            "   WHERE oc.appUser.id = :otherMemberId)")
    Optional<Conversation> findByOtherMember(
            @Param("currentUserEmail") String currentUserEmail,
            @Param("otherMemberId") Long otherMemberId
    );
}

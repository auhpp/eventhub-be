package com.auhpp.event_management.repository;

import com.auhpp.event_management.entity.ConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {
    @Query("SELECT cm FROM ConversationMember cm " +
            "WHERE cm.appUser.id = :memberId " +
            "AND cm.conversation.id = :conversationId")
    Optional<ConversationMember> existsConversationMember(
            @Param("memberId") Long memberId,
            @Param("conversationId") Long conversationId
    );

    @Query("SELECT cm FROM ConversationMember cm " +
            "WHERE cm.appUser.email != :currentUserEmail " +
            "AND cm.conversation.id = :conversationId")
    Optional<ConversationMember> existsOtherConversationMember(
            @Param("currentUserEmail") String currentUserEmail,
            @Param("conversationId") Long conversationId
    );
}

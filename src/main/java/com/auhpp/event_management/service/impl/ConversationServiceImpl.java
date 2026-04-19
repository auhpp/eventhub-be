package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.CommonStatus;
import com.auhpp.event_management.dto.request.ConversationCreateRequest;
import com.auhpp.event_management.dto.request.ConversationSearchRequest;
import com.auhpp.event_management.dto.request.ConversationUpdateRequest;
import com.auhpp.event_management.dto.response.ConversationResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Conversation;
import com.auhpp.event_management.entity.ConversationMember;
import com.auhpp.event_management.entity.Message;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.ConversationMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.ConversationRepository;
import com.auhpp.event_management.repository.MessageRepository;
import com.auhpp.event_management.service.ConversationService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ConversationServiceImpl implements ConversationService {
    ConversationRepository conversationRepository;
    ConversationMapper conversationMapper;
    MessageRepository messageRepository;
    AppUserRepository appUserRepository;

    @Override
    @Transactional
    public ConversationResponse create(ConversationCreateRequest request) {
        // check exists conversation by member ids
        Optional<Conversation> existsConversation = conversationRepository.existsConversation(request.getMemberIds().getFirst(),
                request.getMemberIds().getLast());
        if (existsConversation.isPresent()) {
            return conversationMapper.toConversationResponse(existsConversation.get());
        }

        Conversation conversation = new Conversation();

        // latest message
        if (request.getLatestMessageId() != null) {
            Message latestMessage = messageRepository.findById(request.getLatestMessageId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            conversation.setLatestMessage(latestMessage);
        }

        // conversation member
        List<ConversationMember> members = new ArrayList<>();
        for (Long memberId : request.getMemberIds()) {
            AppUser member = appUserRepository.findById(memberId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            members.add(ConversationMember.builder()
                    .appUser(member)
                    .conversation(conversation)
                    .build());
        }
        conversation.setConversationMembers(members);

        conversation.setStatus(CommonStatus.ACTIVE);
        // save
        conversationRepository.save(conversation);
        return conversationMapper.toConversationResponse(conversation);
    }

    @Override
    public PageResponse<ConversationResponse> getConversations(ConversationSearchRequest request, int page, int size) {
        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Conversation> pageData = conversationRepository.filter(request.getStatus(),
                request.getNameMember(), currentUserEmail, pageable);
        List<ConversationResponse> responses = pageData.getContent().stream().map(
                conversationMapper::toConversationResponse
        ).toList();
        return PageResponse.<ConversationResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public ConversationResponse findById(Long id) {
        Conversation conversation = conversationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return conversationMapper.toConversationResponse(conversation);
    }

    @Override
    @Transactional
    public ConversationResponse update(Long id, ConversationUpdateRequest request) {
        Conversation conversation = conversationRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        conversationMapper.updateConversation(request, conversation);
        conversationRepository.save(conversation);
        return conversationMapper.toConversationResponse(conversation);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        conversationRepository.deleteById(id);
    }

    @Override
    public ConversationResponse findByOtherMember(Long otherMemberId) {
        String email = SecurityUtils.getCurrentUserLogin();
        Optional<Conversation> res = conversationRepository.findByOtherMember(email, otherMemberId);
        return res.map(conversationMapper::toConversationResponse).orElse(null);
    }
}

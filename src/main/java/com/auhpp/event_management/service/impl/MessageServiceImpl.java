package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.MessageStatus;
import com.auhpp.event_management.dto.request.ConversationCreateRequest;
import com.auhpp.event_management.dto.request.CountUnseenMessageRequest;
import com.auhpp.event_management.dto.request.MessageCreateRequest;
import com.auhpp.event_management.dto.request.MessageSearchRequest;
import com.auhpp.event_management.dto.response.BulkSeenResponse;
import com.auhpp.event_management.dto.response.ConversationResponse;
import com.auhpp.event_management.dto.response.MessageResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Conversation;
import com.auhpp.event_management.entity.ConversationMember;
import com.auhpp.event_management.entity.Message;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.MessageMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.ConversationMemberRepository;
import com.auhpp.event_management.repository.ConversationRepository;
import com.auhpp.event_management.repository.MessageRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.ConversationService;
import com.auhpp.event_management.service.MessageService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    MessageRepository messageRepository;
    MessageMapper messageMapper;
    CloudinaryService cloudinaryService;
    ConversationMemberRepository conversationMemberRepository;
    ConversationService conversationService;
    AppUserRepository appUserRepository;
    SimpMessageSendingOperations simpMessageSendingOperations;
    ConversationRepository conversationRepository;

    @Override
    @Transactional
    public MessageResponse create(MessageCreateRequest request) {
        Message message = new Message();
        message.setStatus(MessageStatus.SENT);
        message.setType(request.getType());
        messageRepository.save(message);

        // current user
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser currentUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        // handle conversation
        Long conversationId;
        if (request.getConversationId() != null) {
            conversationId = request.getConversationId();
            Conversation conversation = conversationRepository.findById(conversationId).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            conversation.setLatestMessage(message);
            conversationRepository.save(conversation);
        } else {
            ConversationResponse conversationResponse = conversationService.create(ConversationCreateRequest.builder()
                    .latestMessageId(message.getId())
                    .memberIds(List.of(request.getRecipientId(), currentUser.getId()))
                    .build());
            conversationId = conversationResponse.getId();
        }

        // sender
        conversationMemberRepository.existsConversationMember(currentUser.getId(), conversationId).ifPresent(
                message::setSender
        );

        // handle recipient
        conversationMemberRepository.existsConversationMember(request.getRecipientId(), conversationId).ifPresent(
                message::setRecipient
        );

        // handle reply message
        if (request.getReplyMessageId() != null) {
            Message replyMessage = messageRepository.findById(request.getReplyMessageId()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            message.setReplyMessage(replyMessage);
        }

        // handle file and content
        if (request.getFile() != null) {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getFile(),
                    FolderName.MESSAGE.getValue() + conversationId, true);
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");
            message.setPathUrl(imageUrl);
            message.setPublicId(publicId);
        } else {
            if (request.getContent() == null) throw new AppException(ErrorCode.INVALID_PARAMS);
            message.setContent(request.getContent());
        }
        messageRepository.save(message);

        // send notify to topic
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messageResponse.setTempId(request.getTempId());
        simpMessageSendingOperations.convertAndSendToUser(
                message.getRecipient().getAppUser().getEmail(),
                "/queue/messages",
                messageResponse
        );
        return messageResponse;
    }

    @Override
    @Transactional
    public void seen(Long id) {
        String email = SecurityUtils.getCurrentUserLogin();
        Message message = messageRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (!Objects.equals(message.getRecipient().getAppUser().getEmail(), email)) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        message.setSeenAt(LocalDateTime.now());
        message.setStatus(MessageStatus.SEEN);
        messageRepository.save(message);

        String senderEmail = message.getSender().getAppUser().getEmail();
        simpMessageSendingOperations.convertAndSendToUser(
                senderEmail,
                "/queue/message-status",
                messageMapper.toMessageResponse(message)
        );
    }

    @Override
    @Transactional
    public void receive(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        message.setStatus(MessageStatus.RECEIVED);
        messageRepository.save(message);

        // send notify to topic
        String senderEmail = message.getSender().getAppUser().getEmail();
        simpMessageSendingOperations.convertAndSendToUser(
                senderEmail,
                "/queue/message-status",
                messageMapper.toMessageResponse(message)
        );
    }

    @Override
    public Long countUnSeenMessage(CountUnseenMessageRequest request) {
        String email = SecurityUtils.getCurrentUserLogin();
        return messageRepository.countUnseenMessage(email, request.getConversationId());
    }

    @Override
    @Transactional
    public void seenByConversation(Long conversationId) {
        String email = SecurityUtils.getCurrentUserLogin();
        int count = messageRepository.markAllAsSeenByConversation(MessageStatus.SEEN, conversationId,
                email);
        if (count > 0) {
            ConversationMember sender = conversationMemberRepository
                    .existsOtherConversationMember(email, conversationId).orElseThrow(
                            () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
                    );
            simpMessageSendingOperations.convertAndSendToUser(
                    sender.getAppUser().getEmail(),
                    "/queue/message-status",
                    BulkSeenResponse.builder().conversationId(conversationId)
                            .isBulk(true)
                            .status(MessageStatus.SEEN)
                            .build()
            );
        }
    }

    @Override
    @Transactional
    public void receiveAll() {
        String email = SecurityUtils.getCurrentUserLogin();
        messageRepository.markAllAsReceiveByConversation(MessageStatus.SENT,
                MessageStatus.RECEIVED,
                email);
    }

    @Override
    public PageResponse<MessageResponse> getMessages(MessageSearchRequest request, int page, int size) {
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser currentUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        // check valid current user
        conversationMemberRepository.existsConversationMember(currentUser.getId(), request.getConversationId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<Message> pageData = messageRepository.filter(request.getConversationId(), pageable);
        List<MessageResponse> responses = pageData.getContent().stream().map(
                messageMapper::toMessageResponse
        ).toList();
        return PageResponse.<MessageResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }
}

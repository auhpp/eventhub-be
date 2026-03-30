package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.QAStatus;
import com.auhpp.event_management.constant.QuestionStatus;
import com.auhpp.event_management.dto.request.QuestionCreateRequest;
import com.auhpp.event_management.dto.request.QuestionSearchRequest;
import com.auhpp.event_management.dto.request.QuestionUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.QuestionResponse;
import com.auhpp.event_management.entity.*;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.QuestionMapper;
import com.auhpp.event_management.repository.*;
import com.auhpp.event_management.service.QuestionService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    EventSessionRepository eventSessionRepository;
    AppUserRepository appUserRepository;
    SimpMessageSendingOperations simpMessageSendingOperations;
    UpvoteQuestionRepository upvoteQuestionRepository;
    AttendeeRepository attendeeRepository;

    private void broadcastToOrganizer(Question question, Long currentUserId) {
        Event event = question.getEventSession().getEvent();
        for (EventStaff staff : event.getEventStaffs()) {
            simpMessageSendingOperations.convertAndSend(
                    "/topic/question/organizer/" + staff.getAppUser().getId(),
                    questionMapper.toQuestionResponse(question, currentUserId)
            );
        }
    }

    private void broadcastToAllAttendee(Question question, Long currentUserId) {
        QuestionResponse response = questionMapper.toQuestionResponse(question, currentUserId);
        response.setUpVoted(upvoteQuestionRepository.existsByAppUserIdAndQuestionId(currentUserId, question.getId()));
        simpMessageSendingOperations.convertAndSend(
                "/topic/question/" + question.getEventSession().getId(),
                response
        );
    }

    @Override
    @Transactional
    public QuestionResponse create(QuestionCreateRequest request) {
        Question question = new Question();
        question.setContent(request.getContent());

        // handle user
        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        question.setAppUser(currentUser);

        // handle event session
        EventSession eventSession = eventSessionRepository.findById(request.getEventSessionId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventSession.isExpired()) {
            throw new AppException(ErrorCode.EXPIRED_EVENT_SESSION);
        }
        if (eventSession.getQaStatus() == null || eventSession.getQaStatus().equals(QAStatus.DISABLED)) {
            throw new AppException(ErrorCode.QA_EVENT_SESSION_DISABLED);
        }
        if (eventSession.getQaStatus().equals(QAStatus.CLOSED)) {
            throw new AppException(ErrorCode.QA_EVENT_SESSION_DISABLED);
        }
        question.setEventSession(eventSession);

        // check is attendee
        if (!attendeeRepository.existsByAppUserIdAndEventSessionId(currentUser.getId(), eventSession.getId())) {
            throw new AppException(ErrorCode.ATTENDEE_OWNER_INVALID);
        }

        // handle anonymous
        if (eventSession.getAllowAnonymous()) {
            question.setHasAnonymous(request.getHasAnonymous());
        } else {
            question.setHasAnonymous(false);
        }


        // handle broadcast
        if (eventSession.getRequireModerationQuestion() != null && eventSession.getRequireModerationQuestion()) {
            question.setStatus(QuestionStatus.PENDING);
            questionRepository.save(question);

            // send to organizers
            broadcastToOrganizer(question, currentUser.getId());
        } else {
            question.setStatus(QuestionStatus.APPROVED);
            questionRepository.save(question);

            // broadcast to all
            broadcastToAllAttendee(question, currentUser.getId());
        }
        return questionMapper.toQuestionResponse(question, currentUser.getId());
    }

    @Override
    @Transactional
    public QuestionResponse update(Long id, QuestionUpdateRequest request) {
        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        boolean updateStatus = false;
        // handle anonymous
        if (request.getHasAnonymous() && question.getHasAnonymous()) {
            question.setHasAnonymous(true);
        } else {
            question.setHasAnonymous(false);
        }

        // content
        if (request.getContent() != null && !request.getContent().isEmpty()
                && question.getStatus() == QuestionStatus.PENDING) {
            question.setContent(request.getContent());
        }

        // status
        if (request.getStatus() != null) {
            if (question.getEventSession().getRequireModerationQuestion()) {
                // approve
                if (request.getStatus() == QuestionStatus.APPROVED && question.getStatus() == QuestionStatus.PENDING) {
                    updateStatus = true;
                    question.setStatus(QuestionStatus.APPROVED);
                }

                // reject
                if (request.getStatus() == QuestionStatus.REJECTED && question.getStatus() == QuestionStatus.PENDING) {
                    updateStatus = true;
                    question.setStatus(QuestionStatus.REJECTED);
                }
            }


            // pinned
            if (request.getStatus() == QuestionStatus.PINNED) {
                updateStatus = true;
                if (question.getStatus() == QuestionStatus.APPROVED) {
                    question.setHasPin(true);
                    question.setStatus(QuestionStatus.PINNED);
                } else if (question.getStatus() == QuestionStatus.PINNED) {
                    question.setHasPin(false);
                    question.setStatus(QuestionStatus.APPROVED);
                }
            }

            // pinned
            if (request.getStatus() == QuestionStatus.REPLYING) {
                updateStatus = true;
                if (question.getStatus() == QuestionStatus.APPROVED || question.getStatus() == QuestionStatus.PINNED) {
                    question.setHasPin(true);
                    question.setStatus(QuestionStatus.REPLYING);
                } else if (question.getStatus() == QuestionStatus.REPLYING) {
                    question.setHasPin(false);
                    question.setStatus(QuestionStatus.APPROVED);
                }
            }
            // resolved
            if (request.getStatus() == QuestionStatus.RESOLVED && question.getStatus() == QuestionStatus.REPLYING) {
                updateStatus = true;
                question.setStatus(QuestionStatus.RESOLVED);
            }
        }
        questionRepository.save(question);
        // broadcast
        if (!updateStatus) {
            // send to organizers
            broadcastToOrganizer(question, currentUser.getId());
        } else {
            //  broad cast all
            if (question.getStatus() == QuestionStatus.APPROVED || question.getStatus() == QuestionStatus.RESOLVED
                    || question.getStatus() == QuestionStatus.PINNED || question.getStatus() == QuestionStatus.REPLYING) {
                broadcastToAllAttendee(question, currentUser.getId());
            } else {
                // send to owner question
                simpMessageSendingOperations.convertAndSendToUser(
                        question.getAppUser().getEmail(),
                        "/queue/question",
                        questionMapper.toQuestionResponse(question, currentUser.getId())
                );
            }
        }
        return questionMapper.toQuestionResponse(question, currentUser.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if ((question.getUserNotes() == null || question.getUserNotes().isEmpty())
                && question.getStatus() == QuestionStatus.PENDING) {
            questionRepository.delete(question);
            question.setStatus(null);
            broadcastToOrganizer(question, currentUser.getId());
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
        }
    }

    @Override
    @Transactional
    public void upvote(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        // check question status
        if (question.getStatus() == QuestionStatus.APPROVED || question.getStatus() == QuestionStatus.PINNED) {
            // check exists
            AppUser currentUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                    () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
            );
            if (!upvoteQuestionRepository.existsByAppUserIdAndQuestionId(currentUser.getId(), id)) {
                // vote
                UpvoteQuestion upvoteQuestion = UpvoteQuestion.builder()
                        .question(question)
                        .appUser(currentUser)
                        .build();
                if (question.getUpvoteQuestions() == null) {
                    question.setUpvoteQuestions(new ArrayList<>());
                }
                question.getUpvoteQuestions().add(upvoteQuestion);
                upvoteQuestionRepository.save(upvoteQuestion);
            } else {
                // don't vote
                Optional<UpvoteQuestion> upvoteQuestionDeleted =
                        upvoteQuestionRepository.deleteByAppUserIdAndQuestionId(currentUser.getId(), id);
                upvoteQuestionDeleted.ifPresent(
                        upvoteQuestion -> question.getUpvoteQuestions().remove(upvoteQuestion));
            }
            // broadcast
            broadcastToAllAttendee(question, currentUser.getId());
        }
    }

    @Override
    public PageResponse<QuestionResponse> filter(QuestionSearchRequest request, int page, int size) {
        String currentUserEmail = SecurityUtils.getCurrentUserLogin();
        AppUser currentUser = appUserRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Question> pageData = questionRepository.filter(request.getEventSessionId(), request.getUserId(),
                request.getStatuses(), pageable);
        List<QuestionResponse> responses = pageData.getContent().stream().map(
                question -> questionMapper.toQuestionResponse(question, currentUser.getId())
        ).toList();
        return PageResponse.<QuestionResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }
}

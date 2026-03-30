package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.dto.request.UserNoteCreateRequest;
import com.auhpp.event_management.dto.request.UserNoteSearchRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.UserNoteResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.Question;
import com.auhpp.event_management.entity.UserNote;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.UserNoteMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.QuestionRepository;
import com.auhpp.event_management.repository.UserNoteRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.UserNoteService;
import com.auhpp.event_management.util.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserNoteServiceImpl implements UserNoteService {
    CloudinaryService cloudinaryService;
    UserNoteRepository userNoteRepository;
    AppUserRepository appUserRepository;
    QuestionRepository questionRepository;
    UserNoteMapper userNoteMapper;

    @Override
    @Transactional
    public UserNoteResponse create(UserNoteCreateRequest request) {
        if (Objects.equals(request.getNoteContent(), "") && request.getAudioFile() == null) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        UserNote userNote = new UserNote();
        if (!Objects.equals(request.getNoteContent(), "")) {
            userNote.setNoteContent(request.getNoteContent());
        }

        // app user
        AppUser appUser = appUserRepository.findByEmail(SecurityUtils.getCurrentUserLogin()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        userNote.setAppUser(appUser);
        // question
        Question question = questionRepository.findById(request.getQuestionId()).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        userNote.setQuestion(question);
        // handle audio
        if (request.getAudioFile() != null) {
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getAudioFile(),
                    FolderName.AUDIO_NOTE.getValue(), false);
            String publicId = (String) uploadResult.get("public_id");
            String pathUrl = (String) uploadResult.get("secure_url");
            userNote.setAudioUrl(pathUrl);
            userNote.setPublicId(publicId);
        }
        userNoteRepository.save(userNote);
        return userNoteMapper.toUserNoteResponse(userNote);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        UserNote userNote = userNoteRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (userNote.getPublicId() != null) {
            cloudinaryService.deleteFile(userNote.getPublicId());
        }
        userNoteRepository.delete(userNote);
    }

    @Override
    public PageResponse<UserNoteResponse> filter(UserNoteSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<UserNote> pageData = userNoteRepository.filter(request.getUserId(), request.getQuestionId(), pageable);
        List<UserNoteResponse> responses = pageData.getContent().stream().map(
                userNoteMapper::toUserNoteResponse
        ).toList();
        return PageResponse.<UserNoteResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }
}

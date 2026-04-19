package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.EventSeriesStatus;
import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.dto.request.EventSeriesCreateRequest;
import com.auhpp.event_management.dto.request.EventSeriesSearchRequest;
import com.auhpp.event_management.dto.request.EventSeriesUpdateRequest;
import com.auhpp.event_management.dto.response.EventSeriesResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.AppUser;
import com.auhpp.event_management.entity.EventSeries;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventSeriesMapper;
import com.auhpp.event_management.repository.AppUserRepository;
import com.auhpp.event_management.repository.EventSeriesRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.EventSeriesService;
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

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventSeriesServiceImpl implements EventSeriesService {

    EventSeriesRepository eventSeriesRepository;
    EventSeriesMapper eventSeriesMapper;
    CloudinaryService cloudinaryService;
    AppUserRepository appUserRepository;


    @Override
    @Transactional
    public EventSeriesResponse createEventSeries(EventSeriesCreateRequest request) {
        EventSeries eventSeries = eventSeriesMapper.toEventSeries(request);
        Map<String, Object> uploadResult = cloudinaryService.uploadFile(request.getAvatar(),
                FolderName.EVENT_SERIES.getValue(), true);
        String publicId = (String) uploadResult.get("public_id");
        String imageUrl = (String) uploadResult.get("secure_url");
        eventSeries.setAvatar(imageUrl);
        eventSeries.setAvatarPublicId(publicId);
        String email = SecurityUtils.getCurrentUserLogin();
        AppUser appUser = appUserRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        eventSeries.setAppUser(appUser);
        if (request.getCoverImage() != null) {
            uploadResult = cloudinaryService.uploadFile(request.getCoverImage(),
                    FolderName.EVENT_SERIES.getValue(), true);
            publicId = (String) uploadResult.get("public_id");
            imageUrl = (String) uploadResult.get("secure_url");
            eventSeries.setCoverImage(imageUrl);
            eventSeries.setCoverImagePublicId(publicId);
        }

        eventSeries.setStatus(EventSeriesStatus.ACTIVE);
        eventSeries.setHasPublic(true);

        eventSeriesRepository.save(eventSeries);
        return eventSeriesMapper.toEventSeriesResponse(eventSeries);
    }

    @Override
    @Transactional
    public void deleteEventSeries(Long id) {
        EventSeries eventSeries = eventSeriesRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (!eventSeries.getEvents().isEmpty()) {
            eventSeriesRepository.delete(eventSeries);
        } else {
            throw new AppException(ErrorCode.RESOURCE_CAN_NOT_DELETE);
        }
    }

    @Override
    @Transactional
    public EventSeriesResponse updateEventSeries(Long id, EventSeriesUpdateRequest request) {
        EventSeries eventSeries = eventSeriesRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        eventSeriesMapper.updateEventSeries(request, eventSeries);

        Map<String, Object> uploadResult;
        String publicId;
        String imageUrl;
        if (request.getAvatar() != null) {
            uploadResult = cloudinaryService.uploadFile(request.getAvatar(),
                    FolderName.EVENT_SERIES.getValue(), true);
            publicId = (String) uploadResult.get("public_id");
            imageUrl = (String) uploadResult.get("secure_url");
            eventSeries.setAvatar(imageUrl);
            eventSeries.setAvatarPublicId(publicId);
        }

        if (request.getCoverImage() != null) {
            uploadResult = cloudinaryService.uploadFile(request.getCoverImage(),
                    FolderName.EVENT_SERIES.getValue(), true);
            publicId = (String) uploadResult.get("public_id");
            imageUrl = (String) uploadResult.get("secure_url");
            eventSeries.setCoverImage(imageUrl);
            eventSeries.setCoverImagePublicId(publicId);
        }

        eventSeriesRepository.save(eventSeries);
        return eventSeriesMapper.toEventSeriesResponse(eventSeries);
    }

    @Override
    public PageResponse<EventSeriesResponse> getEventSeries(EventSeriesSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<EventSeries> pageData = eventSeriesRepository.filterEventSeries(request.getName(),
                request.getUserId(), request.getUserFollowerId(),
                request.getStatuses(),
                pageable);
        List<EventSeriesResponse> responses = pageData.getContent().stream().map(
                eventSeriesMapper::toEventSeriesResponse
        ).toList();
        return PageResponse.<EventSeriesResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public EventSeriesResponse getEventSeriesById(Long id) {
        EventSeries eventSeries = eventSeriesRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        return eventSeriesMapper.toEventSeriesResponse(eventSeries);
    }

    @Override
    public List<EventSeriesResponse> getAllEventSeries() {
        String email = SecurityUtils.getCurrentUserLogin();
        List<EventSeries> eventSeries = eventSeriesRepository.findAllByUserEmail(email);
        return eventSeries.stream().map(eventSeriesMapper::toEventSeriesResponse).toList();
    }
}

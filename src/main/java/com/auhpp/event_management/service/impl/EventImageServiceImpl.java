package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.FolderName;
import com.auhpp.event_management.constant.ProcessStatus;
import com.auhpp.event_management.dto.response.EventImageResponse;
import com.auhpp.event_management.dto.response.FaceSearchResult;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.entity.Event;
import com.auhpp.event_management.entity.EventImage;
import com.auhpp.event_management.entity.EventSession;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.mapper.EventImageMapper;
import com.auhpp.event_management.repository.EventImageRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.repository.FaceDataRepository;
import com.auhpp.event_management.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventImageServiceImpl implements EventImageService {
    EventImageRepository eventImageRepository;
    EventImageMapper eventImageMapper;
    CloudinaryService cloudinaryService;
    EventSessionRepository eventSessionRepository;
    FaceDataService faceDataService;
    FaceDataRepository faceDataRepository;
    SpotterService spotterService;
    EventStaffService eventStaffService;

    @Override
    @Transactional
    public List<EventImageResponse> uploadEventImages(
            Long eventSessionId,
            List<MultipartFile> files) {
        EventSession eventSession = eventSessionRepository.findById(eventSessionId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        Event event = eventSession.getEvent();
        if (eventStaffService.isCheckInStaff(event.getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }


        List<EventImageResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            // Upload to cloudinary
            String folderName = FolderName.EVENT.getValue() + event.getId()
                    + "/event-session/" + eventSession.getId() + "/images";

            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file,
                    folderName, true);
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");

            // save to event image
            EventImage eventImage = EventImage.builder()
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .processStatus(ProcessStatus.PENDING)
                    .eventSession(eventSession)
                    .build();

            eventImageRepository.save(eventImage);
            responses.add(eventImageMapper.toEventImageResponse(eventImage));

            // call AI service handle image
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    faceDataService.processEventImage(eventImage.getId(), eventImage.getImageUrl());
                }
            });
        }
        return responses;
    }

    @Override
    public List<EventImageResponse> searchPhotos(Long eventSessionId, MultipartFile selfie) {
        List<Double> userVector = spotterService.extractUserVector(selfie);

        if (userVector == null || userVector.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        String vectorStr = userVector.toString();
        double threshold = 0.6;
        int limit = 50;
        List<FaceSearchResult> results = faceDataRepository.searchFace(
                vectorStr, threshold, eventSessionId, limit
        );
        return results.stream().map(
                faceSearchResult ->
                        EventImageResponse.builder()
                                .imageUrl(faceSearchResult.getImageUrl())
                                .id(faceSearchResult.getEventImageId())
                                .build()
        ).toList();
    }

    @Override
    public PageResponse<EventImageResponse> findAll(Long eventSessionId, ProcessStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<EventImage> pageData = eventImageRepository
                .findByProcessStatusAndEventSessionId(status, eventSessionId, pageable);
        List<EventImageResponse> responses = pageData.getContent().stream().map(
                eventImageMapper::toEventImageResponse
        ).toList();
        return PageResponse.<EventImageResponse>builder()
                .currentPage(page)
                .totalElements(pageData.getTotalElements())
                .totalPage(pageData.getTotalPages())
                .pageSize(pageData.getSize())
                .data(responses)
                .build();
    }

    @Override
    public void refreshProcessImages(Long eventSessionId) {
        EventSession eventSession = eventSessionRepository.findById(eventSessionId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );
        if (eventStaffService.isCheckInStaff(eventSession.getEvent().getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        List<EventImage> eventImages = eventImageRepository.findAllByProcessStatusAndEventSessionId(
                List.of(ProcessStatus.FAILED, ProcessStatus.PENDING), eventSessionId);
        // call AI service handle image
        for (EventImage eventImage : eventImages) {
            faceDataService.processEventImage(eventImage.getId(), eventImage.getImageUrl());
        }
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        eventImageRepository.deleteById(imageId);
    }

}

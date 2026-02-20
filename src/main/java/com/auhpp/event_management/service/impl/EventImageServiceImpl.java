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
import com.auhpp.event_management.repository.EventRepository;
import com.auhpp.event_management.repository.EventSessionRepository;
import com.auhpp.event_management.repository.FaceDataRepository;
import com.auhpp.event_management.service.CloudinaryService;
import com.auhpp.event_management.service.EventImageService;
import com.auhpp.event_management.service.FaceDataService;
import com.auhpp.event_management.service.SpotterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class EventImageServiceImpl implements EventImageService {
    EventImageRepository eventImageRepository;
    EventImageMapper eventImageMapper;
    CloudinaryService cloudinaryService;
    EventRepository eventRepository;
    EventSessionRepository eventSessionRepository;
    FaceDataService faceDataService;
    FaceDataRepository faceDataRepository;
    SpotterService spotterService;

    @Override
    @Transactional
    public List<EventImageResponse> uploadEventImages(
            Long eventId, Long eventSessionId,
            List<MultipartFile> files) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        Optional<EventSession> eventSessionOptional = Optional.empty();
        if (eventSessionId != null) {
            eventSessionOptional = eventSessionRepository.findById(eventSessionId);
        }
        List<EventImageResponse> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            // Upload to cloudinary
            String folderName = "";
            if (eventOptional.isPresent()) {
                folderName = FolderName.EVENT.getValue() + eventOptional.get().getId() + "/images";
            } else if (eventSessionOptional.isPresent()) {
                folderName = FolderName.EVENT.getValue() + eventSessionOptional.get().getEvent().getId()
                        + "/event-session/" + eventSessionOptional.get().getId() + "/images";
            }
            Map<String, Object> uploadResult = cloudinaryService.uploadFile(file,
                    folderName);
            String publicId = (String) uploadResult.get("public_id");
            String imageUrl = (String) uploadResult.get("secure_url");

            // save to event image
            EventImage eventImage = EventImage.builder()
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .processStatus(ProcessStatus.PENDING)
                    .build();
            if (eventOptional.isPresent()) {
                eventImage.setEvent(eventOptional.get());
            } else if (eventSessionOptional.isPresent()) {
                eventImage.setEventSession(eventSessionOptional.get());
                eventImage.setEvent(eventSessionOptional.get().getEvent());
            }

            eventImageRepository.save(eventImage);
            responses.add(eventImageMapper.toEventImageResponse(eventImage));

            // call AI service handle image
            faceDataService.processEventImage(eventImage.getId(), file);
        }
        return responses;
    }

    @Override
    public List<EventImageResponse> searchPhotos(Long eventId, MultipartFile selfie) {
        List<Double> userVector = spotterService.extractUserVector(selfie);

        if (userVector == null || userVector.isEmpty()) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        String vectorStr = userVector.toString();
        double threshold = 0.6;
        int limit = 50;
        List<FaceSearchResult> results = faceDataRepository.searchFace(
                vectorStr, threshold, eventId, limit
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
    public PageResponse<EventImageResponse> findAll(Long eventId, ProcessStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC,
                "createdAt"));
        Page<EventImage> pageData = eventImageRepository.findByProcessStatusAndEventId(status, eventId, pageable);
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

}

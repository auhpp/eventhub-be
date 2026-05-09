package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.ProcessStatus;
import com.auhpp.event_management.dto.response.FaceResult;
import com.auhpp.event_management.entity.EventImage;
import com.auhpp.event_management.entity.FaceData;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.repository.EventImageRepository;
import com.auhpp.event_management.repository.FaceDataRepository;
import com.auhpp.event_management.service.FaceDataService;
import com.auhpp.event_management.service.SpotterService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FaceDataServiceImpl implements FaceDataService {
    FaceDataRepository faceDataRepository;
    SpotterService spotterService;
    EventImageRepository eventImageRepository;

    @Async("taskExecutor")
    @Override
    public void processEventImage(Long eventImageId, String imageUrl) {
        EventImage eventImage = eventImageRepository.findById(eventImageId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        try {
            eventImage.setProcessStatus(ProcessStatus.PROCESSING);
            eventImageRepository.saveAndFlush(eventImage);

            List<FaceResult> faces = spotterService.detectFacesByUrl(imageUrl);

            List<FaceData> faceDataToSave = new ArrayList<>();
            for (FaceResult face : faces) {
                List<Float> floatVector = face.getEmbedding().stream()
                        .map(Double::floatValue)
                        .toList();
                FaceData faceData = FaceData.builder()
                        .eventImage(eventImage)
                        .faceEncoding(floatVector)
                        .build();
                faceDataToSave.add(faceData);
            }
            faceDataRepository.saveAll(faceDataToSave);

            eventImage.setProcessStatus(ProcessStatus.COMPLETED);
            eventImageRepository.saveAndFlush(eventImage);
        } catch (Exception e) {
            System.err.println("Lỗi xử lý ảnh ID " + eventImageId + ": " + e.getMessage());
            eventImage.setProcessStatus(ProcessStatus.FAILED);
            eventImageRepository.save(eventImage);
        }

    }
}

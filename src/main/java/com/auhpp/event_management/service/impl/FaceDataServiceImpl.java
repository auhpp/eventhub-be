package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.constant.ProcessStatus;
import com.auhpp.event_management.dto.pojo.FaceCoordinate;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class FaceDataServiceImpl implements FaceDataService {
    FaceDataRepository faceDataRepository;
    SpotterService spotterService;
    EventImageRepository eventImageRepository;

    @Override
    @Transactional
    public void processEventImage(Long eventImageId, MultipartFile file) {
        EventImage eventImage = eventImageRepository.findById(eventImageId).orElseThrow(
                () -> new AppException(ErrorCode.RESOURCE_NOT_FOUND)
        );

        try {
            eventImage.setProcessStatus(ProcessStatus.PROCESSING);
            eventImageRepository.saveAndFlush(eventImage);

            // call spotter service
            List<FaceResult> faces = spotterService.detectFaces(file);

            List<FaceData> faceDataToSave = new ArrayList<>();
            for (FaceResult face : faces) {
                List<Float> floatVector = face.getEmbedding().stream()
                        .map(Double::floatValue)
                        .toList();
                List<Integer> bbox = face.getBbox();
                FaceCoordinate cord = new FaceCoordinate();
                if (bbox != null && bbox.size() == 4) {
                    cord.setX1(bbox.get(0).doubleValue());
                    cord.setY1(bbox.get(1).doubleValue());
                    cord.setX2(bbox.get(2).doubleValue());
                    cord.setY2(bbox.get(3).doubleValue());
                }
                FaceData faceData = FaceData.builder()
                        .eventImage(eventImage)
                        .faceEncoding(floatVector)
                        .coordinate(cord)
                        .detectionScore(face.getDetScore())
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

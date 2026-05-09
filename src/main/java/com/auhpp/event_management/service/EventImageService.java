package com.auhpp.event_management.service;

import com.auhpp.event_management.constant.ProcessStatus;
import com.auhpp.event_management.dto.response.EventImageResponse;
import com.auhpp.event_management.dto.response.PageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EventImageService {
    List<EventImageResponse> uploadEventImages(Long eventSessionId, List<MultipartFile> files);

    List<EventImageResponse> searchPhotos(Long eventSessionId, MultipartFile selfie);

    PageResponse<EventImageResponse> findAll(Long eventSessionId, ProcessStatus status, int page, int size);

    void refreshProcessImages(Long eventSessionId);

    void deleteImage(Long imageId);
}

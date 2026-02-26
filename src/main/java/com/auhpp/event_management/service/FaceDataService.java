package com.auhpp.event_management.service;

import org.springframework.web.multipart.MultipartFile;

public interface FaceDataService {
    void processEventImage(Long eventImageId, MultipartFile file);
}

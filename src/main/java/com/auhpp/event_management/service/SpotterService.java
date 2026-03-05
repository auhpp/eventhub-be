package com.auhpp.event_management.service;

import com.auhpp.event_management.dto.response.FaceResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SpotterService {
    List<FaceResult> detectFaces(MultipartFile file);

    List<FaceResult> detectFacesByUrl(String url);

    List<Double> extractUserVector(MultipartFile file);
}

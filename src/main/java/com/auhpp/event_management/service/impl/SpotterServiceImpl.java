package com.auhpp.event_management.service.impl;

import com.auhpp.event_management.dto.response.FaceResponse;
import com.auhpp.event_management.dto.response.FaceResult;
import com.auhpp.event_management.exception.AppException;
import com.auhpp.event_management.exception.ErrorCode;
import com.auhpp.event_management.service.SpotterService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SpotterServiceImpl implements SpotterService {
    final RestClient restClient;

    public SpotterServiceImpl(@Qualifier("spotterRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<FaceResult> detectFaces(MultipartFile file) {
        try {
            // prepare body
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            FaceResponse response = restClient.post()
                    .uri("/extract-faces")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .body(FaceResponse.class);
            if (response != null && response.getCode() == 200) {
                return response.getFaces();
            }
        } catch (Exception e) {
            System.err.println("Lỗi gọi AI spotter Service: " + e.getMessage());
            throw e;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Double> extractUserVector(MultipartFile file) {
        try {
            // prepare body
            LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", file.getResource());

            FaceResponse response = restClient.post()
                    .uri("/extract-user-face")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(body)
                    .retrieve()
                    .onStatus(status -> status.value() == 400, (request, resp) -> {
                        throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
                    })
                    .body(FaceResponse.class);
            if (response != null && response.getCode() == 200) {
                return response.getEmbedding();
            }
        } catch (Exception e) {
            System.err.println("Lỗi trích xuất vector user: " + e.getMessage());
            throw e;
        }
        return Collections.emptyList();
    }
}

package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.OrganizerType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizerUpdateRequest {

    private String businessName;

    private MultipartFile businessAvatar;

    private String representativeFullName;

    private String phoneNumber;

    private String email;

    private String biography;

    private String contactAddress;

    private OrganizerType type;

    private String taxCode;
}


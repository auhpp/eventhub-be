package com.auhpp.event_management.dto.request;

import jakarta.validation.constraints.Email;
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
public class OrganizerCreateRequest {
    @NotEmpty(message = "Business name cannot be empty")
    private String businessName;

    @NotNull(message = "Business avatar cannot be null")
    private MultipartFile businessAvatar;

    @NotEmpty(message = "Representative full name cannot be empty")
    private String representativeFullName;

    @NotEmpty(message = "Phone number cannot be empty")
    private String phoneNumber;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Biography cannot be empty")
    private String biography;

    @NotEmpty(message = "Contact address cannot be empty")
    private String contactAddress;

}

package com.auhpp.event_management.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendeeCheckinResponse {
    private Long userId;

    private String email;

    private String fullName;

    private String avatar;

    private int checkedInCount;


}

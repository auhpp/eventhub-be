package com.auhpp.event_management.dto.request;

import com.auhpp.event_management.constant.WithdrawalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalRequestUpdateRequest {
    private String bankCode;

    private String bankAccountNo;

    private String bankAccountName;

    private WithdrawalStatus status;

    private MultipartFile proofImage;

    private String adminNote;
}

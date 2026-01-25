package com.auhpp.event_management.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VnPayQueryDrResponse {
    @JsonProperty("vnp_ResponseCode")
    private String responseCode;

    @JsonProperty("vnp_Message")
    private String message;

    @JsonProperty("vnp_TransactionStatus")
    private String transactionStatus;
}

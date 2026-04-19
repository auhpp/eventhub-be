package com.auhpp.event_management.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.dto.request.WithdrawalRequestCreateRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestSearchRequest;
import com.auhpp.event_management.dto.request.WithdrawalRequestUpdateRequest;
import com.auhpp.event_management.dto.response.PageResponse;
import com.auhpp.event_management.dto.response.WithdrawalRequestExcelReportResponse;
import com.auhpp.event_management.dto.response.WithdrawalRequestResponse;
import com.auhpp.event_management.service.WithdrawalRequestService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/api/v1/withdrawal-request")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalRequestController {
    WithdrawalRequestService withdrawalRequestService;

    @PostMapping
    public ResponseEntity<WithdrawalRequestResponse> create(
            @RequestBody WithdrawalRequestCreateRequest request
    ) {
        WithdrawalRequestResponse result = withdrawalRequestService.create(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WithdrawalRequestResponse> update(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute WithdrawalRequestUpdateRequest request
    ) {
        WithdrawalRequestResponse result = withdrawalRequestService.update(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<WithdrawalRequestResponse>> filter(
            @RequestBody WithdrawalRequestSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<WithdrawalRequestResponse> result = withdrawalRequestService.filter(
                request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<WithdrawalRequestResponse> findById(
            @PathVariable("id") Long id
    ) {
        WithdrawalRequestResponse result = withdrawalRequestService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


    @PostMapping("/reports/export")
    public void exportWithdrawal(
            @RequestBody WithdrawalRequestSearchRequest request,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("danh_sach_yeu_cau_rut_tien", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(),
                    WithdrawalRequestExcelReportResponse.class).build();
            withdrawalRequestService.exportReportWithdrawal(excelWriter, request);

        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{ \"message\": \"Lỗi trong quá trình xuất Excel: " + e.getMessage() + "\" }");
        }
    }
}

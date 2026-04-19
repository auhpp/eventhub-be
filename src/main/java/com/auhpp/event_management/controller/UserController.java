package com.auhpp.event_management.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.auhpp.event_management.dto.request.*;
import com.auhpp.event_management.dto.response.*;
import com.auhpp.event_management.service.AppUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class UserController {
    AppUserService userService;

    @PutMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserBasicResponse> updateInfoUser(
            @PathVariable("userId") Long id,
            @Valid @ModelAttribute UserUpdateRequest request
    ) {
        UserBasicResponse result = userService.updateInfoUser(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PutMapping(value = "/change-password/{userId}")
    public ResponseEntity<UserBasicResponse> changePassword(
            @PathVariable("userId") Long id,
            @Valid @RequestBody PasswordChangeRequest request
    ) {
        userService.changePassword(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(value = "/social-link")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<List<SocialLinkResponse>> createSocialLink(
            @RequestBody List<@Valid SocialLinkCreateRequest> requests
    ) {
        List<SocialLinkResponse> responses = userService.createSocialLink(requests);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responses);
    }

    @PutMapping(value = "/social-link")
    @PreAuthorize("hasAnyRole('ORGANIZER', 'USER')")
    public ResponseEntity<Void> updateSocialLink(
            @RequestBody List<@Valid SocialLinkCreateRequest> requests
    ) {
        userService.updateSocialLink(requests);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<UserBasicResponse> getByEmail(
            @RequestParam("email") String email
    ) {
        UserBasicResponse result = userService.getByEmail(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(
            @PathVariable("id") Long id
    ) {
        UserResponse result = userService.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/change-role/{id}")
    public ResponseEntity<Void> changeRole(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserChangeRoleRequest request
    ) {
        userService.changeRole(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/change-status/{id}")
    public ResponseEntity<Void> changeStatus(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserChangeStatusRequest request
    ) {
        userService.changeStatus(id, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/send/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> sendEmailCreateAdminUser(
            @Valid @RequestBody AdminUserCreateRequest request
    ) {
        userService.sendEmailCreateAdminUser(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/confirm/create-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> confirmAdminUserAccount(
            @Valid @RequestBody RegisterRequest request
    ) {
        userService.confirmAdminUserAccount(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UserBasicResponse>> filter(
            @RequestBody UserSearchRequest request,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        PageResponse<UserBasicResponse> result = userService.filter(request, page, size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @PostMapping("/send-email/reset-password")
    public ResponseEntity<Void> sendEmailResetPassword(
            @RequestBody EmailSendRequest request
    ) {
        userService.sendOtpResetPassword(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        userService.resetPassword(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


    @PostMapping("/reports/export")
    public void exportUser(
            @RequestBody UserSearchRequest request,
            HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("danh_sach_tai_khoan", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), UserExcelReportResponse.class).build();
            userService.exportReportAppUser(excelWriter, request);

        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("{ \"message\": \"Lỗi trong quá trình xuất Excel: " + e.getMessage() + "\" }");
        }
    }
}

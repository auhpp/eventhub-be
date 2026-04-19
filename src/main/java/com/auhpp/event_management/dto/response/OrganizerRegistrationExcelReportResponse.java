package com.auhpp.event_management.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.enums.BooleanEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 12, bold = BooleanEnum.TRUE)
@ContentFontStyle(fontName = "Arial", fontHeightInPoints = 11)
public class OrganizerRegistrationExcelReportResponse {
    @ExcelProperty("Mã hồ sơ")
    @ColumnWidth(15)
    private Long id;

    @ExcelProperty("Tên doanh nghiệp/Tổ chức")
    @ColumnWidth(40)
    private String businessName;

    @ExcelProperty("Người đại diện")
    @ColumnWidth(30)
    private String representativeFullName;

    @ExcelProperty("Loại hình")
    @ColumnWidth(20)
    private String type;

    @ExcelProperty("Mã số thuế")
    @ColumnWidth(25)
    private String taxCode;

    @ExcelProperty("Số điện thoại")
    @ColumnWidth(20)
    private String phoneNumber;

    @ExcelProperty("Email liên hệ")
    @ColumnWidth(35)
    private String email;

    @ExcelProperty("Tài khoản liên kết (AppUser)")
    @ColumnWidth(35)
    private String appUserEmail;

    @ExcelProperty("Địa chỉ liên hệ")
    @ColumnWidth(50)
    private String contactAddress;

    @ExcelProperty("Trạng thái")
    @ColumnWidth(20)
    private String status;

    @ExcelProperty("Ngày nộp hồ sơ")
    @ColumnWidth(25)
    private String createdAt;

    @ExcelProperty("Ngày cập nhật")
    @ColumnWidth(25)
    private String updatedAt;

    @ExcelProperty("Lý do từ chối (Nếu có)")
    @ColumnWidth(40)
    private String rejectionReason;
}

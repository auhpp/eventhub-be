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
public class UserExcelReportResponse {
    @ExcelProperty("Mã ND")
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty("Họ và tên")
    @ColumnWidth(30)
    private String fullName;

    @ExcelProperty("Email")
    @ColumnWidth(35)
    private String email;

    @ExcelProperty("Số điện thoại")
    @ColumnWidth(20)
    private String phoneNumber;

    @ExcelProperty("Vai trò")
    @ColumnWidth(20)
    private String roleName;

    @ExcelProperty("Trạng thái")
    @ColumnWidth(20)
    private boolean status;

    @ExcelProperty("Ngày đăng ký")
    @ColumnWidth(25)
    private String createdAt;

    @ExcelProperty("Truy cập lần cuối")
    @ColumnWidth(25)
    private String lastSeen;
}

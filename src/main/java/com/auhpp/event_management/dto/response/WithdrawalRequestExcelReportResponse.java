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
public class WithdrawalRequestExcelReportResponse {
    @ExcelProperty("Mã YC")
    @ColumnWidth(15)
    private Long id;

    @ExcelProperty("Email tài khoản")
    @ColumnWidth(35)
    private String userEmail;

    @ExcelProperty("Tên người dùng")
    @ColumnWidth(30)
    private String userName;

    @ExcelProperty("Loại ví")
    @ColumnWidth(20)
    private String walletType;

    @ExcelProperty("Số tiền rút (VND)")
    @ColumnWidth(25)
    private Double amount;

    @ExcelProperty("Ngân hàng")
    @ColumnWidth(20)
    private String bankCode;

    @ExcelProperty("Tên tài khoản NH")
    @ColumnWidth(35)
    private String bankAccountName;

    @ExcelProperty("Số tài khoản NH")
    @ColumnWidth(25)
    private String bankAccountNo;

    @ExcelProperty("Trạng thái")
    @ColumnWidth(20)
    private String status;

    @ExcelProperty("Ngày yêu cầu")
    @ColumnWidth(25)
    private String createdAt;

    @ExcelProperty("Ngày xử lý")
    @ColumnWidth(25)
    private String updatedAt;

    @ExcelProperty("Ghi chú của Admin")
    @ColumnWidth(40)
    private String adminNote;
}

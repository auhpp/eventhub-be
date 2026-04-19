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
@AllArgsConstructor
@NoArgsConstructor
@HeadFontStyle(fontName = "Arial", fontHeightInPoints = 12, bold = BooleanEnum.TRUE)
@ContentFontStyle(fontName = "Arial", fontHeightInPoints = 11)
public class AttendeeExportResponse {
    @ExcelProperty("Mã vé (Mã QR)")
    @ColumnWidth(20)
    private String ticketCode;

    @ExcelProperty("Tên người sở hữu")
    @ColumnWidth(25)
    private String ownerName;

    @ExcelProperty("SĐT người sở hữu")
    @ColumnWidth(15)
    private String ownerPhone;

    @ExcelProperty("Email người sở hữu")
    @ColumnWidth(30)
    private String ownerEmail;

    @ExcelProperty("Loại vé")
    @ColumnWidth(40)
    private String ticketName;

    @ExcelProperty("Giá vé")
    @ColumnWidth(15)
    private Double price;

    @ExcelProperty("Phân loại")
    @ColumnWidth(15)
    private String type;

    @ExcelProperty("Trạng thái check-in")
    @ColumnWidth(25)
    private String status;

    @ExcelProperty("Thời gian check-in")
    @ColumnWidth(25)
    private String checkInAt;

}


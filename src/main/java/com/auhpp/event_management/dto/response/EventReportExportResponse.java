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
public class EventReportExportResponse {
    @ExcelProperty("Mã sự kiện")
    @ColumnWidth(15)
    private Long id;

    @ExcelProperty("Tên sự kiện")
    @ColumnWidth(40)
    private String eventName;

    @ExcelProperty("Loại sự kiện")
    @ColumnWidth(20)
    private String eventType;

    @ExcelProperty("Danh mục")
    @ColumnWidth(25)
    private String categoryName;

    @ExcelProperty("Người tổ chức")
    @ColumnWidth(30)
    private String organizerName;

    @ExcelProperty("Email tổ chức")
    @ColumnWidth(35)
    private String organizerEmail;

    @ExcelProperty("Trạng thái")
    @ColumnWidth(20)
    private String status;

    @ExcelProperty("Ngày tạo")
    @ColumnWidth(25)
    private String createdAt;

    @ExcelProperty("Đã kết thúc")
    @ColumnWidth(15)
    private boolean expired;

    @ExcelProperty("Đang diễn ra")
    @ColumnWidth(15)
    private boolean onGoing;

    @ExcelProperty("Số khung giờ")
    @ColumnWidth(15)
    private int totalSessions;

    @ExcelProperty("Địa chỉ")
    @ColumnWidth(50)
    private String location;

    @ExcelProperty("Địa chỉ chi tiết")
    @ColumnWidth(30)
    private String address;

    @ExcelProperty("Tỉ lệ hoa hồng (%)")
    @ColumnWidth(20)
    private Double commissionRate;

    @ExcelProperty("Phí cố định/Vé")
    @ColumnWidth(20)
    private Double commissionFixedPerTicket;

    @ExcelProperty("Cho phép bán lại")
    @ColumnWidth(20)
    private String hasResalable;

    @ExcelProperty("Lý do từ chối (Nếu có)")
    @ColumnWidth(40)
    private String rejectionReason;
}

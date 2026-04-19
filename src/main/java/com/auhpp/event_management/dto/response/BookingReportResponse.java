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
public class BookingReportResponse {
    @ExcelProperty("Mã đơn")
    @ColumnWidth(15)
    private Long id;

    @ExcelProperty("Ngày đặt")
    @ColumnWidth(20)
    private String createdAt;

    @ExcelProperty("Tên khách hàng")
    @ColumnWidth(25)
    private String customerName;

    @ExcelProperty("Số điện thoại")
    @ColumnWidth(15)
    private String customerPhone;

    @ExcelProperty("Email")
    @ColumnWidth(30)
    private String customerEmail;

    @ExcelProperty("Loại đơn")
    @ColumnWidth(15)
    private String type;

    @ExcelProperty("Số lượng vé")
    @ColumnWidth(15)
    private Integer ticketQuantity;

    @ExcelProperty("Tạm tính")
    @ColumnWidth(15)
    private Double totalAmount;

    @ExcelProperty("Giảm giá")
    @ColumnWidth(15)
    private Double discountAmount;

    @ExcelProperty("Thực thu")
    @ColumnWidth(20)
    private Double finalAmount;

    @ExcelProperty("Trạng thái")
    @ColumnWidth(20)
    private String status;

    @ExcelProperty("Phương thức TT")
    @ColumnWidth(20)
    private String walletType;

    @ExcelProperty("Mã giao dịch")
    @ColumnWidth(20)
    private String transactionId;

    @ExcelProperty("Mã đơn hàng mua lại")
    @ColumnWidth(20)
    private Long sellBookingId;

    @ExcelProperty("Ghi chú")
    @ColumnWidth(30)
    private String note;


}

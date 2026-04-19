package com.auhpp.event_management.service.impl;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

public class EventTitleWriteHandler implements SheetWriteHandler {
    private final String title;


    public EventTitleWriteHandler(String title) {
        this.title = title;
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = writeSheetHolder.getSheet();

        Row row = sheet.createRow(0);
        row.setHeight((short) 600);

        Cell cell = row.createCell(0);
        cell.setCellValue(title);

        // style for cell
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(style);

        // Merge Cells
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 14));
    }
}

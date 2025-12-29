package com.ordep.syncotable.sheets.impl.writer;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.SpreadsheetWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class XlsxWriter implements SpreadsheetWriter {
    @Override
    public OutputStream write(File file, Card card) {

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(file)) {

            Sheet sheet = workbook.createSheet(card.getTitle());
            CellStyle boldStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            boldStyle.setFont(font);

            Row headerRow = sheet.createRow(0);

            int headerCol = 0;
            for (String key : card.getRows().get(0).getValuesJson().keySet()) {
                Cell cell = headerRow.createCell(headerCol++);
                cell.setCellValue(key);
                cell.setCellStyle(boldStyle);
            }

            int rowIndex = 1;
            for (CardRow cardRow : card.getRows()) {
                Row row = sheet.createRow(rowIndex++);

                int colIndex = 0;
                for (Object value : cardRow.getValuesJson().values()) {
                    Cell cell = row.createCell(colIndex++);
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }

            for (int i = 0; i < headerCol; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(fos);

            return fos;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar arquivo XLSX", e);
        }
    }

}


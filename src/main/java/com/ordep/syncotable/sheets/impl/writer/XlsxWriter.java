package com.ordep.syncotable.sheets.impl.writer;

import com.ordep.syncotable.model.Card;
import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.SpreadsheetWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class XlsxWriter implements SpreadsheetWriter {

    @Override
    public byte[] write(Card card) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream bao = new ByteArrayOutputStream()) {

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

                System.out.println("chave: "+ key);
            }

            int rowIndex = 1;
            for (CardRow cardRow : card.getRows()) {
                Row row = sheet.createRow(rowIndex++);

                int colIndex = 0;
                for (Object value : cardRow.getValuesJson(). values()) {
                    Cell cell = row.createCell(colIndex++);
                    cell.setCellValue(value != null ? value.toString() : "");
                }
            }

            for (int i = 0; i < headerCol; i++) {
                sheet.autoSizeColumn(i);
            }
            workbook.write(bao);

            return bao.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar arquivo XLSX", e);
        }
    }

}


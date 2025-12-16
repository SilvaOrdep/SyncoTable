package com.ordep.syncotable.sheets.impl;

import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.Spreadsheet;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XlsxHandler implements Spreadsheet {

    public List<CardRow> read(InputStream file) throws IOException {
        List<CardRow> lines = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            Row header = iterator.next();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                CardRow cardRow = new CardRow();
                Map<String, Object> rowValues = new LinkedHashMap<>();

                for (int col = 0; col < row.getLastCellNum(); col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    switch (cell.getCellType()) {
                        case STRING:
                            rowValues.put(header.getCell(col).getStringCellValue(), cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowValues.put(header.getCell(col).getStringCellValue(), cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            rowValues.put(header.getCell(col).getStringCellValue(), cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            switch (cell.getCachedFormulaResultType()) {
                                case STRING:
                                    rowValues.put(header.getCell(col).getStringCellValue(), cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    rowValues.put(header.getCell(col).getStringCellValue(), cell.getNumericCellValue());
                                    break;
                                default:
                                    rowValues.put(header.getCell(col).getStringCellValue(), "");
                            }
                            break;
                        default:
                            rowValues.put(header.getCell(col).getStringCellValue(), "");
                    }
                }

                cardRow.setValuesJson(rowValues);
                lines.add(cardRow);

                System.out.println(rowValues);
            }
        }

        return lines;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int col = 0; col < row.getLastCellNum(); col++) {
            Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }


}

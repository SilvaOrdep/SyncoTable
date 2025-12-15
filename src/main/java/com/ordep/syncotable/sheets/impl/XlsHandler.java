package com.ordep.syncotable.sheets.impl;

import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.Spreadsheet;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XlsHandler implements Spreadsheet {

    public List<CardRow> read(InputStream file) {
        List<CardRow> cardRows = new ArrayList<>();

        try (Workbook workbook = new HSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Row> rows = (List<Row>) toList(sheet.iterator());
            Row header = rows.get(0);
            rows.remove(0);
            for (Row row : rows) {
                Map<String, Object> cellValues = new LinkedHashMap<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            cellValues.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            cellValues.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            cellValues.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            if (cell.getCellType().equals(CellType.NUMERIC)) {
                                cellValues.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getStringCellValue());
                            }
                            cellValues.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), cell.getNumericCellValue());
                            break;
                        default:
                            cellValues.put("","");
                    }
                }

                CardRow cardRow = new CardRow();
                cardRow.setValuesJson(cellValues);

                cardRows.add(cardRow);
                System.out.println(cardRow.getValuesJson().entrySet());
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return cardRows;
    }

    public List<?> toList(Iterator<Row> iterator) {
        return IteratorUtils.toList(iterator);
    }
}

package com.ordep.syncotable.sheets.impl.reader;

import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.SpreadsheetReader;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XlsxReader implements SpreadsheetReader {

    public List<CardRow> read(InputStream file) throws IOException {
        List<CardRow> lines = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            Row header = iterator.next();
            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            while (iterator.hasNext()) {
                Row row = iterator.next();

                if (isRowEmpty(row)) {
                    continue;
                }

                CardRow cardRow = new CardRow();
                Map<String, Object> cellValues = new LinkedHashMap<>();

                for (int col = 0; col < row.getLastCellNum(); col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    cellValues.put(header.getCell(cell.getColumnIndex()).getStringCellValue(), formatter.formatCellValue(cell, evaluator));

                }

                cardRow.setValuesJson(cellValues);
                lines.add(cardRow);

                System.out.println(cellValues);
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

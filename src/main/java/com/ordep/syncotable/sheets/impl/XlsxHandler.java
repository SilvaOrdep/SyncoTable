package com.ordep.syncotable.sheets.impl;

import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.Spreadsheet;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XlsxHandler implements Spreadsheet {

    //posso usar essa função para ler linhas e retornar para outra função que cria a planilha
    public List<CardRow> read(InputStream file) throws IOException {
        List<CardRow> lines = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();
            if (iterator.hasNext()) iterator.next();

            int rowIndex = 1;

            while (iterator.hasNext()) {
                Row row = iterator.next();
                List<Object> values = new ArrayList<>();
                Map<String, Object> rowValues = new HashMap<>();
                CardRow cardRow = new CardRow();

                for (int col = 0; col < row.getLastCellNum(); col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    switch (cell.getCellType()) {
                        case STRING:
                            values.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            values.add(cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            values.add(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            values.add(cell.getCellFormula());
                            break;
                        default:
                            values.add(null);
                    }
                }

                rowValues.put("Linha " + rowIndex, values);
                cardRow.setValuesJson(rowValues);
                lines.add(cardRow);

                System.out.println(cardRow.getValuesJson());
                rowIndex++;
            }
        }

        return lines;
    }

}

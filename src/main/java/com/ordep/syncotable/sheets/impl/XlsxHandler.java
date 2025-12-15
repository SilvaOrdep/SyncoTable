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
            Row header = iterator.next();
            if (iterator.hasNext()) iterator.next();

            int rowIndex = 1;
            Map<String, Object> rowValues = new HashMap<>();


            while (iterator.hasNext()) {
                Row row = iterator.next();
                List<Object> values = new ArrayList<>();
                CardRow cardRow = new CardRow();

                for (int col = 0; col < row.getLastCellNum(); col++) {
                    Cell cell = row.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    switch (cell.getCellType()) {
                        case STRING:
                            rowValues.put(header.getCell(col).getStringCellValue() , cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowValues.put(header.getCell(col).getStringCellValue() ,cell.getNumericCellValue());
                            break;
                        case BOOLEAN:
                            rowValues.put(header.getCell(col).getStringCellValue() ,cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            rowValues.put(header.getCell(col).getStringCellValue() ,cell.getCellFormula());
                            break;
                        default:
                            rowValues.put(null ,null);
                    }
                }

                cardRow.setValuesJson(rowValues);
                lines.add(cardRow);

                System.out.println(cardRow.getValuesJson());
                rowIndex++;
            }
        }

        return lines;
    }

}

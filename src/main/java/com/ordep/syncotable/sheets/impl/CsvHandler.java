package com.ordep.syncotable.sheets.impl;

import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.Spreadsheet;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvHandler implements Spreadsheet {

    @Override
    public List<CardRow> read(  InputStream inputStream) {
        List<CardRow> rows = new ArrayList<>();

        try (
                InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            boolean isHeader = true;
            while ((line = br.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(",");
                CardRow row = new CardRow();
                Map<String, Object> values = new HashMap<>();
                for (int i = 0; i < columns.length; i++) {
                    values.put("Linha " + i, columns[i].trim());
                }
                row.setValuesJson(values);
                rows.add(row);
                rows.stream().map(CardRow::getValuesJson).forEach(System.out::println);
            }
        } catch (IOException e) {
            throw new RuntimeException("error: " + e.getMessage(), e);
        }

        return rows;
    }


}

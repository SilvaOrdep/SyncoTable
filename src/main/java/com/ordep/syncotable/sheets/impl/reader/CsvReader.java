package com.ordep.syncotable.sheets.impl.reader;

import com.ordep.syncotable.model.CardRow;
import com.ordep.syncotable.sheets.Spreadsheet;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvReader implements Spreadsheet {

    @Override
    public List<CardRow> read(InputStream inputStream) {
        List<CardRow> rows = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreHeaderCase(true)
                     .setTrim(true)
                     .build())) {

            List<String> headers = csvParser.getHeaderNames();

            for (CSVRecord csvRecord : csvParser) {
                Map<String, Object> values = new LinkedHashMap<>();

                for (String header : headers) {
                    values.put(header, csvRecord.get(header));
                }

                CardRow cardRow = new CardRow();
                cardRow.setValuesJson(values);
                rows.add(cardRow);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }


}

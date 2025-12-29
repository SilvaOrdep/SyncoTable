package com.ordep.syncotable.sheets;

import com.ordep.syncotable.sheets.impl.reader.CsvReader;
import com.ordep.syncotable.sheets.impl.reader.XlsReader;
import com.ordep.syncotable.sheets.impl.reader.XlsxReader;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpreadsheetFactory {

    private final Map<String, SpreadsheetReader> handlers = Map.of(
            "csv", new CsvReader(),
            "xls", new XlsReader(),
            "xlsx", new XlsxReader()
    );

    public SpreadsheetReader getHandler(String filename){
        String ext = extractExtension(filename);
        return handlers.get(ext);
    }

    private String extractExtension(String filename){
        int i = filename.lastIndexOf(".");
        return filename.substring(i + 1);
    }
}


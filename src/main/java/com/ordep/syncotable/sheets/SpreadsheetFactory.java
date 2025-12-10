package com.ordep.syncotable.sheets;

import com.ordep.syncotable.sheets.impl.CsvHandler;
import com.ordep.syncotable.sheets.impl.XlsHandler;
import com.ordep.syncotable.sheets.impl.XlsxHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Map;

@Service
public class SpreadsheetFactory {

    private final Map<String, Spreadsheet> handlers = Map.of(
            "csv", new CsvHandler(),
            "xls", new XlsHandler(),
            "xlsx", new XlsxHandler()
    );

    public Spreadsheet getHandler(String filename){
        String ext = extractExtension(filename);
        return handlers.get(ext);
    }

    private String extractExtension(String filename){
        int i = filename.lastIndexOf(".");
        return filename.substring(i + 1);
    }
}


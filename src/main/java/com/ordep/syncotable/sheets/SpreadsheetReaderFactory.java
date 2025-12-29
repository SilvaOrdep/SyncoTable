package com.ordep.syncotable.sheets;

import com.ordep.syncotable.sheets.impl.reader.CsvReader;
import com.ordep.syncotable.sheets.impl.reader.XlsReader;
import com.ordep.syncotable.sheets.impl.reader.XlsxReader;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpreadsheetReaderFactory {

    private final Map<String, SpreadsheetReader> readers = Map.of(
            "csv", new CsvReader(),
            "xls", new XlsReader(),
            "xlsx", new XlsxReader()
    );

    public SpreadsheetReader getReader(String filename){
        String ext = extractExtension(filename);
        return readers.get(ext);
    }

    private String extractExtension(String filename){
        int i = filename.lastIndexOf(".");
        return filename.substring(i + 1);
    }
}


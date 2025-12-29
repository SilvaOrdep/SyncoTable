package com.ordep.syncotable.sheets;

import com.ordep.syncotable.model.CardRow;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface SpreadsheetReader {
    List<CardRow> read(InputStream file) throws IOException;
}

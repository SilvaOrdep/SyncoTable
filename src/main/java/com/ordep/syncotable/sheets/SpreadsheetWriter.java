package com.ordep.syncotable.sheets;

import com.ordep.syncotable.model.Card;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface SpreadsheetWriter {
    OutputStream write(File file, Card card);
}

package com.ordep.syncotable.sheets;

import com.ordep.syncotable.model.CardRow;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

public interface Spreadsheet {
    List<CardRow> read(InputStream file) throws IOException;
}

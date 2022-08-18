package com.nice.avishkar.dao;

import com.univocity.parsers.common.processor.BatchedColumnProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class DaoUtils {

    // Method to read data from CSV file
    public static List<String[]> getData(Path filePath) throws IOException {
        List<String[]> parsedRows = null;
        try (Reader inputReader = new InputStreamReader(new FileInputStream(filePath.toFile()), StandardCharsets.UTF_8)) {
            CsvParserSettings settings = new CsvParserSettings();

            //Settings to read CSVs
            settings.setProcessor(new BatchedColumnProcessor(10000) {
                @Override
                public void batchProcessed(int rowsInThisBatch) {
                }
            });
            settings.setLineSeparatorDetectionEnabled(true);
            settings.setHeaderExtractionEnabled(true);
            settings.setSkipEmptyLines(true);
            CsvParser parser = new CsvParser(settings);
            parsedRows = parser.parseAll(inputReader);

        }
        return parsedRows;
    }
}


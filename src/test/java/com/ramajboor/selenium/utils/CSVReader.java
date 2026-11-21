package com.ramajboor.selenium.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CSVReader {

    private CSVReader() {
    }

    public static List<Map<String, String>> read(String resourcePath) {
        try (InputStream input = CSVReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("CSV resource not found: " + resourcePath);
            }

            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setTrim(true)
                    .build();

            try (CSVParser parser = format.parse(new InputStreamReader(input, StandardCharsets.UTF_8))) {
                List<Map<String, String>> records = new ArrayList<>();
                for (CSVRecord record : parser) {
                    Map<String, String> row = new LinkedHashMap<>();
                    for (String header : parser.getHeaderNames()) {
                        row.put(header, record.get(header));
                    }
                    records.add(row);
                }
                return records;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read CSV resource: " + resourcePath, e);
        }
    }
}

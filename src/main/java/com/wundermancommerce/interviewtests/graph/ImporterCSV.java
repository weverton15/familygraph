package com.wundermancommerce.interviewtests.graph;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.wundermancommerce.interviewtests.graph.models.ImportEntry;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ImporterCSV {

    public static final char CSV_SEPARATOR = ',';

    private ImporterCSV() {
    }

    public static List<ImportEntry> importer(File file) {
        List<ImportEntry> listImporterEntry = new ArrayList<>();
        try {
            log.info("Create entries all data from csv file");
            final CSVParser parser = new CSVParserBuilder().withSeparator(CSV_SEPARATOR)
                .withIgnoreQuotations(true).build();
            final CSVReader reader = new CSVReaderBuilder(new BufferedReader(
                new FileReader(file.getAbsolutePath()))).withSkipLines(0).withCSVParser(parser)
                .build();

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<String> entries = new ArrayList<>();
                long line = reader.getLinesRead() - 1;
                Collections.addAll(entries, nextLine);
                Optional<String> rowWithValue = entries.stream().filter(StringUtils::isNotBlank)
                    .findAny();
                if (rowWithValue.isPresent()) {
                    ImportEntry importerEntry = new ImportEntry((int) line, entries);
                    listImporterEntry.add(importerEntry);
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Error read/open file: {} | Message: {}", file.getName(), e.getMessage());
        }

        return listImporterEntry;
    }

}

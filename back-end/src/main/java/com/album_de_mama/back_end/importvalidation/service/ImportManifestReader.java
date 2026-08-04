package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportManifestReader {

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT
            .builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .get();

    private final Path importRoot;

    public ImportManifestReader(ImportProperties importProperties) {
        this.importRoot = importProperties.getRoot()
                .toAbsolutePath()
                .normalize();
    }

    public List<ImportManifestRow> read(String manifestPath) {
        Path resolvedManifest = importRoot
                .resolve(manifestPath)
                .normalize();

        if (!resolvedManifest.startsWith(importRoot)) {
            throw new IllegalArgumentException(
                    "La ruta del manifiesto no es válida."
            );
        }

        if (!Files.exists(resolvedManifest)
                || !Files.isRegularFile(resolvedManifest)) {
            throw new IllegalArgumentException(
                    "El manifiesto solicitado no existe."
            );
        }

        try (
                Reader reader = Files.newBufferedReader(
                        resolvedManifest,
                        StandardCharsets.UTF_8
                );
                CSVParser parser = CSV_FORMAT.parse(reader)
        ) {
            List<ImportManifestRow> rows = new ArrayList<>();

            for (CSVRecord record : parser) {
                rows.add(toManifestRow(record));
            }

            return List.copyOf(rows);
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "No fue posible leer el manifiesto.",
                    exception
            );
        }
    }

    private ImportManifestRow toManifestRow(CSVRecord record) {
        return new ImportManifestRow(
                Math.toIntExact(record.getRecordNumber() + 1),
                record.get("id"),
                record.get("title"),
                record.get("type"),
                record.get("category"),
                record.get("date"),
                record.get("place"),
                record.get("file"),
                record.get("thumbnail"),
                record.get("description")
        );
    }
}

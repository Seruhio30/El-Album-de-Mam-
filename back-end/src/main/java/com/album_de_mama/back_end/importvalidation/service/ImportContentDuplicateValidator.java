package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportContentDuplicateValidator {

    private static final String HASH_READ_ERROR =
            "No fue posible calcular el hash SHA-256 del archivo.";

    private final ImportMediaPathResolver pathResolver;
    private final ImportFileHashCalculator hashCalculator;

    public ImportContentDuplicateValidator(
            ImportMediaPathResolver pathResolver,
            ImportFileHashCalculator hashCalculator
    ) {
        this.pathResolver = pathResolver;
        this.hashCalculator = hashCalculator;
    }

    public List<ImportValidationIssue> validate(
            List<ImportManifestRow> rows
    ) {
        List<ImportValidationIssue> issues = new ArrayList<>();

        Map<Path, String> hashesByRealPath = new HashMap<>();
        Map<String, FirstOccurrence> firstOccurrencesByHash =
                new HashMap<>();

        for (ImportManifestRow row : rows) {
            validateField(
                    row,
                    "file",
                    row.file(),
                    hashesByRealPath,
                    firstOccurrencesByHash,
                    issues
            );

            validateField(
                    row,
                    "thumbnail",
                    row.thumbnail(),
                    hashesByRealPath,
                    firstOccurrencesByHash,
                    issues
            );
        }

        return List.copyOf(issues);
    }

    private void validateField(
            ImportManifestRow row,
            String field,
            String value,
            Map<Path, String> hashesByRealPath,
            Map<String, FirstOccurrence> firstOccurrencesByHash,
            List<ImportValidationIssue> issues
    ) {
        if (value == null || value.isBlank()) {
            return;
        }

        List<ImportValidationIssue> pathIssues =
                new ArrayList<>();

        Path realPath = pathResolver.resolve(
                row.rowNumber(),
                field,
                value,
                pathIssues
        );

        if (realPath == null) {
            return;
        }

        String hash;

        try {
            hash = hashCalculator.calculate(
                    realPath,
                    hashesByRealPath
            );
        } catch (IllegalStateException exception) {
            if (!(exception.getCause() instanceof IOException)) {
                throw exception;
            }

            issues.add(
                    new ImportValidationIssue(
                            row.rowNumber(),
                            field,
                            HASH_READ_ERROR
                    )
            );
            return;
        }

        FirstOccurrence firstOccurrence =
                firstOccurrencesByHash.get(hash);

        if (firstOccurrence == null) {
            firstOccurrencesByHash.put(
                    hash,
                    new FirstOccurrence(
                            row.rowNumber(),
                            field,
                            realPath
                    )
            );
            return;
        }

        boolean samePhysicalFile =
                firstOccurrence.realPath().equals(realPath);

        if (samePhysicalFile) {
            return;
        }

        issues.add(
                new ImportValidationIssue(
                        row.rowNumber(),
                        field,
                        "El archivo tiene contenido duplicado; apareció primero en el campo "
                                + firstOccurrence.field()
                                + " de la fila "
                                + firstOccurrence.rowNumber()
                                + "."
                )
        );
    }

    private record FirstOccurrence(
            int rowNumber,
            String field,
            Path realPath
    ) {
    }
}

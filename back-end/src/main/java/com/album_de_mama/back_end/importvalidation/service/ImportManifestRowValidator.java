package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImportManifestRowValidator {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ISO_LOCAL_DATE
                    .withResolverStyle(ResolverStyle.STRICT);

    public List<ImportValidationIssue> validate(
            ImportManifestRow row
    ) {
        List<ImportValidationIssue> issues = new ArrayList<>();

        validateRequired(row, issues);
        validateLengths(row, issues);
        validateId(row, issues);
        validateDate(row, issues);

        return List.copyOf(issues);
    }

    private void validateRequired(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        requireValue(row.rowNumber(), "id", row.id(), issues);
        requireValue(row.rowNumber(), "title", row.title(), issues);
        requireValue(row.rowNumber(), "type", row.type(), issues);
        requireValue(row.rowNumber(), "category", row.category(), issues);
        requireValue(row.rowNumber(), "date", row.date(), issues);
        requireValue(row.rowNumber(), "place", row.place(), issues);
        requireValue(row.rowNumber(), "file", row.file(), issues);
        requireValue(
                row.rowNumber(),
                "thumbnail",
                row.thumbnail(),
                issues
        );
        requireValue(
                row.rowNumber(),
                "description",
                row.description(),
                issues
        );
    }

    private void validateLengths(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        validateMaximumLength(
                row.rowNumber(),
                "title",
                row.title(),
                150,
                issues
        );
        validateMaximumLength(
                row.rowNumber(),
                "type",
                row.type(),
                20,
                issues
        );
        validateMaximumLength(
                row.rowNumber(),
                "category",
                row.category(),
                50,
                issues
        );
        validateMaximumLength(
                row.rowNumber(),
                "place",
                row.place(),
                100,
                issues
        );
        validateMaximumLength(
                row.rowNumber(),
                "file",
                row.file(),
                255,
                issues
        );
        validateMaximumLength(
                row.rowNumber(),
                "thumbnail",
                row.thumbnail(),
                255,
                issues
        );
        validateMaximumLength(
                row.rowNumber(),
                "description",
                row.description(),
                500,
                issues
        );
    }

    private void validateId(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(row.id())) {
            return;
        }

        try {
            long id = Long.parseLong(row.id().trim());

            if (id <= 0) {
                addIssue(
                        row.rowNumber(),
                        "id",
                        "El identificador debe ser un número entero positivo.",
                        issues
                );
            }
        } catch (NumberFormatException exception) {
            addIssue(
                    row.rowNumber(),
                    "id",
                    "El identificador debe ser un número entero positivo.",
                    issues
            );
        }
    }

    private void validateDate(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(row.date())) {
            return;
        }

        try {
            LocalDate.parse(row.date().trim(), DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            addIssue(
                    row.rowNumber(),
                    "date",
                    "La fecha debe usar el formato válido YYYY-MM-DD.",
                    issues
            );
        }
    }

    private void requireValue(
            int rowNumber,
            String field,
            String value,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(value)) {
            addIssue(
                    rowNumber,
                    field,
                    "El campo es obligatorio.",
                    issues
            );
        }
    }

    private void validateMaximumLength(
            int rowNumber,
            String field,
            String value,
            int maximumLength,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(value)) {
            return;
        }

        if (value.trim().length() > maximumLength) {
            addIssue(
                    rowNumber,
                    field,
                    "El campo no puede superar "
                            + maximumLength
                            + " caracteres.",
                    issues
            );
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void addIssue(
            int rowNumber,
            String field,
            String message,
            List<ImportValidationIssue> issues
    ) {
        issues.add(
                new ImportValidationIssue(
                        rowNumber,
                        field,
                        message
                )
        );
    }
}

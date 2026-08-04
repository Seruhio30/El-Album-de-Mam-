package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportManifestRowValidatorTests {

    private final ImportManifestRowValidator validator =
            new ImportManifestRowValidator();

    @Test
    void shouldAcceptValidBasicFields() {
        ImportManifestRow row = validRow();

        List<ImportValidationIssue> issues = validator.validate(row);

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldReportAllRequiredFieldsWhenBlank() {
        ImportManifestRow row = new ImportManifestRow(
                4,
                " ",
                "",
                null,
                " ",
                "",
                null,
                "",
                " ",
                ""
        );

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(9, issues.size());

        assertIssue(
                issues,
                4,
                "id",
                "El campo es obligatorio."
        );
        assertIssue(
                issues,
                4,
                "title",
                "El campo es obligatorio."
        );
        assertIssue(
                issues,
                4,
                "description",
                "El campo es obligatorio."
        );
    }

    @Test
    void shouldReportExceededMaximumLengths() {
        ImportManifestRow row = new ImportManifestRow(
                5,
                "10",
                "a".repeat(151),
                "p".repeat(21),
                "c".repeat(51),
                "2024-03-15",
                "l".repeat(101),
                "f".repeat(256),
                "t".repeat(256),
                "d".repeat(501)
        );

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(7, issues.size());

        assertIssue(
                issues,
                5,
                "title",
                "El campo no puede superar 150 caracteres."
        );
        assertIssue(
                issues,
                5,
                "file",
                "El campo no puede superar 255 caracteres."
        );
        assertIssue(
                issues,
                5,
                "description",
                "El campo no puede superar 500 caracteres."
        );
    }

    @Test
    void shouldRejectNonNumericId() {
        ImportManifestRow row = withId("abc");

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(1, issues.size());
        assertIssue(
                issues,
                2,
                "id",
                "El identificador debe ser un número entero positivo."
        );
    }

    @Test
    void shouldRejectZeroAndNegativeIds() {
        List<ImportValidationIssue> zeroIssues =
                validator.validate(withId("0"));

        List<ImportValidationIssue> negativeIssues =
                validator.validate(withId("-5"));

        assertEquals(1, zeroIssues.size());
        assertEquals(1, negativeIssues.size());

        assertIssue(
                zeroIssues,
                2,
                "id",
                "El identificador debe ser un número entero positivo."
        );
        assertIssue(
                negativeIssues,
                2,
                "id",
                "El identificador debe ser un número entero positivo."
        );
    }

    @Test
    void shouldRejectInvalidDateFormat() {
        ImportManifestRow row = withDate("15-03-2024");

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(1, issues.size());
        assertIssue(
                issues,
                2,
                "date",
                "La fecha debe usar el formato válido YYYY-MM-DD."
        );
    }

    @Test
    void shouldRejectNonexistentCalendarDate() {
        ImportManifestRow row = withDate("2024-02-30");

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(1, issues.size());
        assertIssue(
                issues,
                2,
                "date",
                "La fecha debe usar el formato válido YYYY-MM-DD."
        );
    }

    @Test
    void shouldAcceptAllAllowedTypesAndCategories() {
        List<ImportValidationIssue> photoIssues =
                validator.validate(
                        withTypeAndCategory(
                                "photo",
                                "familia"
                        )
                );

        List<ImportValidationIssue> videoIssues =
                validator.validate(
                        withTypeAndCategory(
                                "video",
                                "celebraciones"
                        )
                );

        assertTrue(photoIssues.isEmpty());
        assertTrue(videoIssues.isEmpty());
    }

    @Test
    void shouldRejectUnsupportedType() {
        ImportManifestRow row =
                withTypeAndCategory("audio", "viajes");

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(1, issues.size());
        assertIssue(
                issues,
                2,
                "type",
                "El tipo debe ser photo o video."
        );
    }

    @Test
    void shouldRejectUnsupportedCategory() {
        ImportManifestRow row =
                withTypeAndCategory("photo", "mascotas");

        List<ImportValidationIssue> issues = validator.validate(row);

        assertEquals(1, issues.size());
        assertIssue(
                issues,
                2,
                "category",
                "La categoría debe ser viajes, familia o celebraciones."
        );
    }

    @Test
    void shouldTrimTypeAndCategoryBeforeValidation() {
        ImportManifestRow row =
                withTypeAndCategory(
                        " photo ",
                        " viajes "
                );

        List<ImportValidationIssue> issues = validator.validate(row);

        assertTrue(issues.isEmpty());
    }

    private ImportManifestRow validRow() {
        return new ImportManifestRow(
                2,
                "10",
                "Viaje familiar",
                "photo",
                "viajes",
                "2024-03-15",
                "Guanacaste",
                "photos/viaje.jpg",
                "photos/viaje.jpg",
                "Un día especial en familia."
        );
    }

    private ImportManifestRow withTypeAndCategory(
            String type,
            String category
    ) {
        ImportManifestRow row = validRow();

        return new ImportManifestRow(
                row.rowNumber(),
                row.id(),
                row.title(),
                type,
                category,
                row.date(),
                row.place(),
                row.file(),
                row.thumbnail(),
                row.description()
        );
    }

    private ImportManifestRow withId(String id) {
        ImportManifestRow row = validRow();

        return new ImportManifestRow(
                row.rowNumber(),
                id,
                row.title(),
                row.type(),
                row.category(),
                row.date(),
                row.place(),
                row.file(),
                row.thumbnail(),
                row.description()
        );
    }

    private ImportManifestRow withDate(String date) {
        ImportManifestRow row = validRow();

        return new ImportManifestRow(
                row.rowNumber(),
                row.id(),
                row.title(),
                row.type(),
                row.category(),
                date,
                row.place(),
                row.file(),
                row.thumbnail(),
                row.description()
        );
    }

    private void assertIssue(
            List<ImportValidationIssue> issues,
            int rowNumber,
            String field,
            String message
    ) {
        assertTrue(
                issues.contains(
                        new ImportValidationIssue(
                                rowNumber,
                                field,
                                message
                        )
                )
        );
    }
}

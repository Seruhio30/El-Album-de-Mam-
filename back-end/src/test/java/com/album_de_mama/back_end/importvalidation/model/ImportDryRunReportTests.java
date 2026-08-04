package com.album_de_mama.back_end.importvalidation.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImportDryRunReportTests {

    @Test
    void shouldPreserveReportValuesAndCopyIssues() {
        List<ImportValidationIssue> sourceIssues =
                new ArrayList<>();

        sourceIssues.add(
                new ImportValidationIssue(
                        2,
                        "date",
                        "La fecha no es válida."
                )
        );

        ImportDryRunReport report =
                new ImportDryRunReport(
                        false,
                        3,
                        2,
                        1,
                        sourceIssues
                );

        sourceIssues.clear();

        assertFalse(report.valid());
        assertEquals(3, report.totalRows());
        assertEquals(2, report.validRows());
        assertEquals(1, report.invalidRows());
        assertEquals(1, report.issues().size());

        assertThrows(
                UnsupportedOperationException.class,
                () -> report.issues().add(
                        new ImportValidationIssue(
                                3,
                                "id",
                                "El identificador no es válido."
                        )
                )
        );
    }
}

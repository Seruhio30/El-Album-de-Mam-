package com.album_de_mama.back_end.importvalidation.model;

import java.util.List;

public record ImportDryRunReport(
        boolean valid,
        int totalRows,
        int validRows,
        int invalidRows,
        List<ImportValidationIssue> issues
) {
    public ImportDryRunReport {
        issues = List.copyOf(issues);
    }
}

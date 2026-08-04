package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportDryRunReport;
import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ImportDryRunService {

    private final ImportManifestReader manifestReader;
    private final ImportManifestRowValidator rowValidator;
    private final ImportMediaFileValidator mediaFileValidator;
    private final ImportManifestDuplicateValidator duplicateValidator;

    public ImportDryRunService(
            ImportManifestReader manifestReader,
            ImportManifestRowValidator rowValidator,
            ImportMediaFileValidator mediaFileValidator,
            ImportManifestDuplicateValidator duplicateValidator
    ) {
        this.manifestReader = manifestReader;
        this.rowValidator = rowValidator;
        this.mediaFileValidator = mediaFileValidator;
        this.duplicateValidator = duplicateValidator;
    }

    public ImportDryRunReport validate(String manifestPath) {
        List<ImportManifestRow> rows =
                manifestReader.read(manifestPath);

        List<ImportValidationIssue> issues =
                new ArrayList<>();

        for (ImportManifestRow row : rows) {
            issues.addAll(rowValidator.validate(row));
            issues.addAll(mediaFileValidator.validate(row));
        }

        issues.addAll(duplicateValidator.validate(rows));

        Set<Integer> invalidRowNumbers = new HashSet<>();

        for (ImportValidationIssue issue : issues) {
            if (issue.rowNumber() > 0) {
                invalidRowNumbers.add(issue.rowNumber());
            }
        }

        int totalRows = rows.size();
        int invalidRows = invalidRowNumbers.size();
        int validRows = totalRows - invalidRows;
        boolean valid = issues.isEmpty();

        return new ImportDryRunReport(
                valid,
                totalRows,
                validRows,
                invalidRows,
                issues
        );
    }
}

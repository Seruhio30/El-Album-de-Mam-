package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImportManifestDuplicateValidator {

    public List<ImportValidationIssue> validate(
            List<ImportManifestRow> rows
    ) {
        List<ImportValidationIssue> issues = new ArrayList<>();

        detectRepeatedIds(rows, issues);
        detectRepeatedRows(rows, issues);
        detectRepeatedMainFiles(rows, issues);
        detectRepeatedThumbnails(rows, issues);
        detectIncompatiblePathUsage(rows, issues);

        return List.copyOf(issues);
    }

    private void detectRepeatedIds(
            List<ImportManifestRow> rows,
            List<ImportValidationIssue> issues
    ) {
        Map<String, Integer> firstRowsById = new HashMap<>();

        for (ImportManifestRow row : rows) {
            String normalizedId = normalizeText(row.id());

            if (normalizedId == null) {
                continue;
            }

            Integer firstRow = firstRowsById.putIfAbsent(
                    normalizedId,
                    row.rowNumber()
            );

            if (firstRow != null) {
                addIssue(
                        row.rowNumber(),
                        "id",
                        "El identificador está repetido; apareció primero en la fila "
                                + firstRow
                                + ".",
                        issues
                );
            }
        }
    }

    private void detectRepeatedRows(
            List<ImportManifestRow> rows,
            List<ImportValidationIssue> issues
    ) {
        Map<RowSignature, Integer> firstRowsBySignature =
                new HashMap<>();

        for (ImportManifestRow row : rows) {
            RowSignature signature = RowSignature.from(row);

            Integer firstRow = firstRowsBySignature.putIfAbsent(
                    signature,
                    row.rowNumber()
            );

            if (firstRow != null) {
                addIssue(
                        row.rowNumber(),
                        "row",
                        "La fila está completamente repetida; apareció primero en la fila "
                                + firstRow
                                + ".",
                        issues
                );
            }
        }
    }

    private void detectRepeatedMainFiles(
            List<ImportManifestRow> rows,
            List<ImportValidationIssue> issues
    ) {
        detectRepeatedPathInSameField(
                rows,
                true,
                "file",
                "El archivo principal está reutilizado; apareció primero en la fila ",
                issues
        );
    }

    private void detectRepeatedThumbnails(
            List<ImportManifestRow> rows,
            List<ImportValidationIssue> issues
    ) {
        detectRepeatedPathInSameField(
                rows,
                false,
                "thumbnail",
                "La miniatura está reutilizada; apareció primero en la fila ",
                issues
        );
    }

    private void detectRepeatedPathInSameField(
            List<ImportManifestRow> rows,
            boolean useMainFile,
            String field,
            String messagePrefix,
            List<ImportValidationIssue> issues
    ) {
        Map<String, Integer> firstRowsByPath = new HashMap<>();

        for (ImportManifestRow row : rows) {
            String value = useMainFile
                    ? row.file()
                    : row.thumbnail();

            String normalizedPath = normalizePath(value);

            if (normalizedPath == null) {
                continue;
            }

            Integer firstRow = firstRowsByPath.putIfAbsent(
                    normalizedPath,
                    row.rowNumber()
            );

            if (firstRow != null) {
                addIssue(
                        row.rowNumber(),
                        field,
                        messagePrefix + firstRow + ".",
                        issues
                );
            }
        }
    }

    private void detectIncompatiblePathUsage(
            List<ImportManifestRow> rows,
            List<ImportValidationIssue> issues
    ) {
        Map<String, Integer> firstMainFileRows =
                new HashMap<>();
        Map<String, Integer> firstThumbnailRows =
                new HashMap<>();

        for (ImportManifestRow row : rows) {
            String normalizedFile = normalizePath(row.file());
            String normalizedThumbnail =
                    normalizePath(row.thumbnail());

            if (normalizedFile != null) {
                Integer firstThumbnailRow =
                        firstThumbnailRows.get(normalizedFile);

                if (firstThumbnailRow != null
                        && firstThumbnailRow
                        != row.rowNumber()) {
                    addIssue(
                            row.rowNumber(),
                            "file",
                            "La ruta se utiliza de forma incompatible; apareció primero en la fila "
                                    + firstThumbnailRow
                                    + ".",
                            issues
                    );
                }

                firstMainFileRows.putIfAbsent(
                        normalizedFile,
                        row.rowNumber()
                );
            }

            if (normalizedThumbnail != null) {
                Integer firstMainFileRow =
                        firstMainFileRows.get(
                                normalizedThumbnail
                        );

                boolean samePhotoRowReuse =
                        firstMainFileRow != null
                                && firstMainFileRow
                                == row.rowNumber()
                                && "photo".equals(
                                        normalizeText(row.type())
                                );

                if (firstMainFileRow != null
                        && !samePhotoRowReuse) {
                    addIssue(
                            row.rowNumber(),
                            "thumbnail",
                            "La ruta se utiliza de forma incompatible; apareció primero en la fila "
                                    + firstMainFileRow
                                    + ".",
                            issues
                    );
                }

                firstThumbnailRows.putIfAbsent(
                        normalizedThumbnail,
                        row.rowNumber()
                );
            }
        }
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private String normalizePath(String value) {
        String normalizedText = normalizeText(value);

        if (normalizedText == null) {
            return null;
        }

        try {
            return Path.of(normalizedText)
                    .normalize()
                    .toString();
        } catch (InvalidPathException exception) {
            return normalizedText;
        }
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

    private record RowSignature(
            String id,
            String title,
            String type,
            String category,
            String date,
            String place,
            String file,
            String thumbnail,
            String description
    ) {
        private static RowSignature from(
                ImportManifestRow row
        ) {
            return new RowSignature(
                    normalize(row.id()),
                    normalize(row.title()),
                    normalize(row.type()),
                    normalize(row.category()),
                    normalize(row.date()),
                    normalize(row.place()),
                    normalize(row.file()),
                    normalize(row.thumbnail()),
                    normalize(row.description())
            );
        }

        private static String normalize(String value) {
            return value == null
                    ? null
                    : value.trim();
        }
    }
}

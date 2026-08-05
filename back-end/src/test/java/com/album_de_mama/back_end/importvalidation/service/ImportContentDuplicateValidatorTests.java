package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportContentDuplicateValidatorTests {

    @TempDir
    Path importRoot;

    @Test
    void shouldAcceptFilesWithDifferentContent()
            throws IOException {
        createFile(
                "photos/primera.jpg",
                "contenido primero"
        );
        createFile(
                "photos/segunda.jpg",
                "contenido segundo"
        );

        List<ImportManifestRow> rows = List.of(
                photoRow(2, "10", "photos/primera.jpg"),
                photoRow(3, "11", "photos/segunda.jpg")
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(rows);

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldReportMainFilesWithIdenticalContent()
            throws IOException {
        createFile(
                "photos/primera.jpg",
                "contenido compartido"
        );
        createFile(
                "photos/copia.jpg",
                "contenido compartido"
        );

        List<ImportManifestRow> rows = List.of(
                photoRow(2, "10", "photos/primera.jpg"),
                photoRow(5, "11", "photos/copia.jpg")
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(rows);

        assertIssue(
                issues,
                5,
                "file",
                "El archivo tiene contenido duplicado; apareció primero en el campo file de la fila 2."
        );
    }

    @Test
    void shouldReportThumbnailsWithIdenticalContent()
            throws IOException {
        createFile(
                "videos/primero.mp4",
                "video primero"
        );
        createFile(
                "videos/segundo.mp4",
                "video segundo"
        );
        createFile(
                "thumbnails/primera.jpg",
                "miniatura compartida"
        );
        createFile(
                "thumbnails/copia.jpg",
                "miniatura compartida"
        );

        List<ImportManifestRow> rows = List.of(
                videoRow(
                        2,
                        "10",
                        "videos/primero.mp4",
                        "thumbnails/primera.jpg"
                ),
                videoRow(
                        6,
                        "11",
                        "videos/segundo.mp4",
                        "thumbnails/copia.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(rows);

        assertIssue(
                issues,
                6,
                "thumbnail",
                "El archivo tiene contenido duplicado; apareció primero en el campo thumbnail de la fila 2."
        );
    }

    @Test
    void shouldReportContentReusedBetweenMainFileAndThumbnail()
            throws IOException {
        createFile(
                "photos/original.jpg",
                "contenido compartido"
        );
        createFile(
                "videos/recuerdo.mp4",
                "video diferente"
        );
        createFile(
                "thumbnails/copia.jpg",
                "contenido compartido"
        );

        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "photos/original.jpg"
                ),
                videoRow(
                        8,
                        "11",
                        "videos/recuerdo.mp4",
                        "thumbnails/copia.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(rows);

        assertIssue(
                issues,
                8,
                "thumbnail",
                "El archivo tiene contenido duplicado; apareció primero en el campo file de la fila 2."
        );
    }


    @Test
    void shouldIgnoreSamePhysicalFileRepeatedInAnotherRow()
            throws IOException {
        createFile(
                "photos/compartida.jpg",
                "contenido compartido"
        );

        List<ImportManifestRow> rows = List.of(
                photoRow(2, "10", "photos/compartida.jpg"),
                photoRow(3, "11", "photos/compartida.jpg")
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(rows);

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldAllowPhotoUsingOwnFileAsThumbnail()
            throws IOException {
        createFile(
                "photos/viaje.jpg",
                "contenido familiar"
        );

        ImportManifestRow row = photoRow(
                2,
                "10",
                "photos/viaje.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(List.of(row));

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldIgnoreInvalidOrMissingPaths() {
        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "../outside.jpg"
                ),
                photoRow(
                        3,
                        "11",
                        "photos/missing.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(rows);

        assertTrue(issues.isEmpty());
    }


    @Test
    void shouldReportHashCalculationFailure()
            throws IOException {
        createFile(
                "photos/ilegible.jpg",
                "contenido familiar"
        );

        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        ImportFileHashCalculator failingHashCalculator =
                new ImportFileHashCalculator() {
                    @Override
                    public String calculate(
                            Path realPath,
                            java.util.Map<Path, String> hashesByRealPath
                    ) {
                        throw new IllegalStateException(
                                "No fue posible calcular el hash SHA-256 del archivo: "
                                        + realPath,
                                new IOException("Lectura fallida")
                        );
                    }
                };

        ImportContentDuplicateValidator validator =
                new ImportContentDuplicateValidator(
                        new ImportMediaPathResolver(properties),
                        failingHashCalculator
                );

        List<ImportValidationIssue> issues =
                validator.validate(
                        List.of(
                                photoRow(
                                        2,
                                        "10",
                                        "photos/ilegible.jpg"
                                )
                        )
                );

        assertIssue(
                issues,
                2,
                "file",
                "No fue posible calcular el hash SHA-256 del archivo."
        );
    }

    private ImportContentDuplicateValidator createValidator() {
        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        return new ImportContentDuplicateValidator(
                new ImportMediaPathResolver(properties),
                new ImportFileHashCalculator()
        );
    }

    private ImportManifestRow photoRow(
            int rowNumber,
            String id,
            String file
    ) {
        return row(
                rowNumber,
                id,
                "photo",
                file,
                file
        );
    }

    private ImportManifestRow videoRow(
            int rowNumber,
            String id,
            String file,
            String thumbnail
    ) {
        return row(
                rowNumber,
                id,
                "video",
                file,
                thumbnail
        );
    }

    private ImportManifestRow row(
            int rowNumber,
            String id,
            String type,
            String file,
            String thumbnail
    ) {
        return new ImportManifestRow(
                rowNumber,
                id,
                "Recuerdo familiar " + id,
                type,
                "familia",
                "2024-03-15",
                "San José",
                file,
                thumbnail,
                "Un recuerdo especial."
        );
    }

    private void createFile(
            String relativePath,
            String content
    ) throws IOException {
        Path file = importRoot.resolve(relativePath);

        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
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

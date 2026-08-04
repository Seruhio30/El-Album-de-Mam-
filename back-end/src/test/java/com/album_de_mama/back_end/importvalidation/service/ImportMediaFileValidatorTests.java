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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportMediaFileValidatorTests {

    @TempDir
    Path importRoot;

    @Test
    void shouldAcceptPhotoUsingSameImageAsThumbnail()
            throws IOException {
        createFile("photos/viaje.jpg");

        ImportManifestRow row = row(
                "photo",
                "photos/viaje.jpg",
                "photos/viaje.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldAcceptVideoWithSeparateThumbnail()
            throws IOException {
        createFile("videos/cumpleanos.mp4");
        createFile("thumbnails/cumpleanos.jpg");

        ImportManifestRow row = row(
                "video",
                "videos/cumpleanos.mp4",
                "thumbnails/cumpleanos.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldRejectAbsolutePath() {
        ImportManifestRow row = row(
                "photo",
                importRoot.resolve("outside.jpg")
                        .toAbsolutePath()
                        .toString(),
                "photos/viaje.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "file",
                "La ruta debe ser relativa a IMPORT_ROOT."
        );
    }

    @Test
    void shouldRejectPathTraversal() {
        ImportManifestRow row = row(
                "photo",
                "../outside.jpg",
                "photos/viaje.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "file",
                "La ruta no puede salir de IMPORT_ROOT."
        );
    }

    @Test
    void shouldRejectMissingFile() {
        ImportManifestRow row = row(
                "photo",
                "photos/missing.jpg",
                "photos/missing.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "file",
                "El archivo no existe o no es un archivo regular."
        );
        assertIssue(
                issues,
                "thumbnail",
                "El archivo no existe o no es un archivo regular."
        );
    }

    @Test
    void shouldRejectDirectoryUsedAsFile()
            throws IOException {
        Files.createDirectories(
                importRoot.resolve("photos/directory.jpg")
        );

        ImportManifestRow row = row(
                "photo",
                "photos/directory.jpg",
                "photos/directory.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "file",
                "El archivo no existe o no es un archivo regular."
        );
    }

    @Test
    void shouldRejectInvalidPhotoExtension()
            throws IOException {
        createFile("photos/viaje.gif");

        ImportManifestRow row = row(
                "photo",
                "photos/viaje.gif",
                "photos/viaje.gif"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "file",
                "La fotografía debe usar un archivo JPG, JPEG o PNG."
        );
        assertIssue(
                issues,
                "thumbnail",
                "La miniatura debe usar un archivo JPG, JPEG o PNG."
        );
    }

    @Test
    void shouldRejectInvalidVideoExtension()
            throws IOException {
        createFile("videos/cumpleanos.avi");
        createFile("thumbnails/cumpleanos.jpg");

        ImportManifestRow row = row(
                "video",
                "videos/cumpleanos.avi",
                "thumbnails/cumpleanos.jpg"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "file",
                "El video debe usar un archivo MP4."
        );
    }

    @Test
    void shouldRejectInvalidThumbnailExtension()
            throws IOException {
        createFile("videos/cumpleanos.mp4");
        createFile("thumbnails/cumpleanos.gif");

        ImportManifestRow row = row(
                "video",
                "videos/cumpleanos.mp4",
                "thumbnails/cumpleanos.gif"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "thumbnail",
                "La miniatura debe usar un archivo JPG, JPEG o PNG."
        );
    }

    @Test
    void shouldRejectVideoUsingSameFileAsThumbnail()
            throws IOException {
        createFile("videos/cumpleanos.mp4");

        ImportManifestRow row = row(
                "video",
                "videos/cumpleanos.mp4",
                "videos/cumpleanos.mp4"
        );

        List<ImportValidationIssue> issues =
                createValidator().validate(row);

        assertIssue(
                issues,
                "thumbnail",
                "La miniatura debe usar un archivo JPG, JPEG o PNG."
        );
        assertIssue(
                issues,
                "thumbnail",
                "El video debe utilizar una miniatura separada."
        );
    }

    private ImportManifestRow row(
            String type,
            String file,
            String thumbnail
    ) {
        return new ImportManifestRow(
                2,
                "10",
                "Recuerdo familiar",
                type,
                "familia",
                "2024-03-15",
                "San José",
                file,
                thumbnail,
                "Un recuerdo especial."
        );
    }

    private void createFile(String relativePath)
            throws IOException {
        Path file = importRoot.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, "test content");
    }

    private ImportMediaFileValidator createValidator() {
        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        return new ImportMediaFileValidator(properties);
    }

    private void assertIssue(
            List<ImportValidationIssue> issues,
            String field,
            String message
    ) {
        assertTrue(
                issues.contains(
                        new ImportValidationIssue(
                                2,
                                field,
                                message
                        )
                )
        );
    }
}

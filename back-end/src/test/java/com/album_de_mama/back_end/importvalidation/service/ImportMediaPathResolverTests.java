package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportMediaPathResolverTests {

    @TempDir
    Path importRoot;

    @Test
    void shouldResolveRegularFileInsideImportRoot()
            throws IOException {
        Path expectedPath = createFile("photos/viaje.jpg");
        List<ImportValidationIssue> issues = new ArrayList<>();

        Path resolvedPath = createResolver().resolve(
                2,
                "file",
                "photos/viaje.jpg",
                issues
        );

        assertEquals(expectedPath.toRealPath(), resolvedPath);
        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldRejectAbsolutePath() {
        List<ImportValidationIssue> issues = new ArrayList<>();

        Path resolvedPath = createResolver().resolve(
                2,
                "file",
                importRoot.resolve("outside.jpg")
                        .toAbsolutePath()
                        .toString(),
                issues
        );

        assertNull(resolvedPath);
        assertIssue(
                issues,
                "file",
                "La ruta debe ser relativa a IMPORT_ROOT."
        );
    }

    @Test
    void shouldRejectPathTraversal() {
        List<ImportValidationIssue> issues = new ArrayList<>();

        Path resolvedPath = createResolver().resolve(
                2,
                "file",
                "../outside.jpg",
                issues
        );

        assertNull(resolvedPath);
        assertIssue(
                issues,
                "file",
                "La ruta no puede salir de IMPORT_ROOT."
        );
    }

    @Test
    void shouldRejectMissingFile() {
        List<ImportValidationIssue> issues = new ArrayList<>();

        Path resolvedPath = createResolver().resolve(
                2,
                "file",
                "photos/missing.jpg",
                issues
        );

        assertNull(resolvedPath);
        assertIssue(
                issues,
                "file",
                "El archivo no existe o no es un archivo regular."
        );
    }

    @Test
    void shouldRejectDirectoryUsedAsFile()
            throws IOException {
        Files.createDirectories(
                importRoot.resolve("photos/directory.jpg")
        );

        List<ImportValidationIssue> issues = new ArrayList<>();

        Path resolvedPath = createResolver().resolve(
                2,
                "file",
                "photos/directory.jpg",
                issues
        );

        assertNull(resolvedPath);
        assertIssue(
                issues,
                "file",
                "El archivo no existe o no es un archivo regular."
        );
    }

    @Test
    void shouldRejectSymlinkResolvingOutsideImportRoot()
            throws IOException {
        Path outsideRoot = Files.createTempDirectory(
                importRoot.getParent(),
                "outside-import-root"
        );

        Path outsideFile = outsideRoot.resolve("outside.jpg");
        Files.writeString(outsideFile, "outside content");

        Path symlink = importRoot.resolve("photos/link.jpg");
        Files.createDirectories(symlink.getParent());
        Files.createSymbolicLink(symlink, outsideFile);

        List<ImportValidationIssue> issues = new ArrayList<>();

        Path resolvedPath = createResolver().resolve(
                2,
                "file",
                "photos/link.jpg",
                issues
        );

        assertNull(resolvedPath);
        assertIssue(
                issues,
                "file",
                "La ruta resuelta no puede salir de IMPORT_ROOT."
        );
    }

    private Path createFile(String relativePath)
            throws IOException {
        Path file = importRoot.resolve(relativePath);

        Files.createDirectories(file.getParent());
        Files.writeString(file, "test content");

        return file;
    }

    private ImportMediaPathResolver createResolver() {
        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        return new ImportMediaPathResolver(properties);
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

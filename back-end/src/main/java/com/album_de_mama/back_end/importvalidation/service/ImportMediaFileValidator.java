package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class ImportMediaFileValidator {

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
            "jpg",
            "jpeg",
            "png"
    );

    private static final Set<String> VIDEO_EXTENSIONS = Set.of(
            "mp4"
    );

    private final Path importRoot;

    public ImportMediaFileValidator(
            ImportProperties importProperties
    ) {
        this.importRoot = importProperties.getRoot()
                .toAbsolutePath()
                .normalize();
    }

    public List<ImportValidationIssue> validate(
            ImportManifestRow row
    ) {
        List<ImportValidationIssue> issues = new ArrayList<>();

        validateMainFile(row, issues);
        validateThumbnail(row, issues);
        validateVideoUsesSeparateThumbnail(row, issues);

        return List.copyOf(issues);
    }

    private void validateMainFile(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(row.file())) {
            return;
        }

        Path resolvedFile = validatePath(
                row.rowNumber(),
                "file",
                row.file(),
                issues
        );

        if (resolvedFile == null || isBlank(row.type())) {
            return;
        }

        String normalizedType = row.type().trim();

        if ("photo".equals(normalizedType)) {
            validateExtension(
                    row.rowNumber(),
                    "file",
                    resolvedFile,
                    IMAGE_EXTENSIONS,
                    "La fotografía debe usar un archivo JPG, JPEG o PNG.",
                    issues
            );
        }

        if ("video".equals(normalizedType)) {
            validateExtension(
                    row.rowNumber(),
                    "file",
                    resolvedFile,
                    VIDEO_EXTENSIONS,
                    "El video debe usar un archivo MP4.",
                    issues
            );
        }
    }

    private void validateThumbnail(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(row.thumbnail())) {
            return;
        }

        Path resolvedThumbnail = validatePath(
                row.rowNumber(),
                "thumbnail",
                row.thumbnail(),
                issues
        );

        if (resolvedThumbnail == null) {
            return;
        }

        validateExtension(
                row.rowNumber(),
                "thumbnail",
                resolvedThumbnail,
                IMAGE_EXTENSIONS,
                "La miniatura debe usar un archivo JPG, JPEG o PNG.",
                issues
        );
    }

    private void validateVideoUsesSeparateThumbnail(
            ImportManifestRow row,
            List<ImportValidationIssue> issues
    ) {
        if (isBlank(row.type())
                || isBlank(row.file())
                || isBlank(row.thumbnail())) {
            return;
        }

        if (!"video".equals(row.type().trim())) {
            return;
        }

        Path filePath = Path.of(row.file().trim()).normalize();
        Path thumbnailPath = Path.of(row.thumbnail().trim()).normalize();

        if (filePath.equals(thumbnailPath)) {
            addIssue(
                    row.rowNumber(),
                    "thumbnail",
                    "El video debe utilizar una miniatura separada.",
                    issues
            );
        }
    }

    private Path validatePath(
            int rowNumber,
            String field,
            String value,
            List<ImportValidationIssue> issues
    ) {
        Path relativePath;

        try {
            relativePath = Path.of(value.trim());
        } catch (RuntimeException exception) {
            addIssue(
                    rowNumber,
                    field,
                    "La ruta del archivo no es válida.",
                    issues
            );
            return null;
        }

        if (relativePath.isAbsolute()) {
            addIssue(
                    rowNumber,
                    field,
                    "La ruta debe ser relativa a IMPORT_ROOT.",
                    issues
            );
            return null;
        }

        Path resolvedPath = importRoot
                .resolve(relativePath)
                .normalize();

        if (!resolvedPath.startsWith(importRoot)) {
            addIssue(
                    rowNumber,
                    field,
                    "La ruta no puede salir de IMPORT_ROOT.",
                    issues
            );
            return null;
        }

        if (!Files.exists(resolvedPath)
                || !Files.isRegularFile(resolvedPath)) {
            addIssue(
                    rowNumber,
                    field,
                    "El archivo no existe o no es un archivo regular.",
                    issues
            );
            return null;
        }

        try {
            Path realImportRoot = importRoot.toRealPath();
            Path realFilePath = resolvedPath.toRealPath();

            if (!realFilePath.startsWith(realImportRoot)) {
                addIssue(
                        rowNumber,
                        field,
                        "La ruta resuelta no puede salir de IMPORT_ROOT.",
                        issues
                );
                return null;
            }

            return realFilePath;
        } catch (IOException exception) {
            addIssue(
                    rowNumber,
                    field,
                    "No fue posible resolver la ruta real del archivo.",
                    issues
            );
            return null;
        }
    }

    private void validateExtension(
            int rowNumber,
            String field,
            Path file,
            Set<String> allowedExtensions,
            String message,
            List<ImportValidationIssue> issues
    ) {
        String fileName = file.getFileName().toString();
        int separatorIndex = fileName.lastIndexOf('.');

        if (separatorIndex < 0
                || separatorIndex == fileName.length() - 1) {
            addIssue(rowNumber, field, message, issues);
            return;
        }

        String extension = fileName
                .substring(separatorIndex + 1)
                .toLowerCase(Locale.ROOT);

        if (!allowedExtensions.contains(extension)) {
            addIssue(rowNumber, field, message, issues);
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

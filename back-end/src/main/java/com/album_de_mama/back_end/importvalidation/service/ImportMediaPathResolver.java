package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ImportMediaPathResolver {

    private final Path importRoot;

    public ImportMediaPathResolver(
            ImportProperties importProperties
    ) {
        this.importRoot = importProperties.getRoot()
                .toAbsolutePath()
                .normalize();
    }

    public Path resolve(
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

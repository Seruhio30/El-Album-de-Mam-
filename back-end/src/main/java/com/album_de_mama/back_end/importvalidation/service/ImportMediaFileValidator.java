package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.springframework.stereotype.Service;

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

    private final ImportMediaPathResolver pathResolver;

    public ImportMediaFileValidator(
            ImportMediaPathResolver pathResolver
    ) {
        this.pathResolver = pathResolver;
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

        Path resolvedFile = pathResolver.resolve(
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

        Path resolvedThumbnail = pathResolver.resolve(
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
        Path thumbnailPath = Path.of(
                row.thumbnail().trim()
        ).normalize();

        if (filePath.equals(thumbnailPath)) {
            addIssue(
                    row.rowNumber(),
                    "thumbnail",
                    "El video debe utilizar una miniatura separada.",
                    issues
            );
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

package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportManifestDuplicateValidatorTests {

    private final ImportManifestDuplicateValidator validator =
            new ImportManifestDuplicateValidator();

    @Test
    void shouldAcceptRowsWithoutDuplicates() {
        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "photos/viaje.jpg"
                ),
                videoRow(
                        3,
                        "11",
                        "videos/cumpleanos.mp4",
                        "thumbnails/cumpleanos.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                validator.validate(rows);

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldAllowPhotoUsingOwnFileAsThumbnail() {
        ImportManifestRow row = photoRow(
                2,
                "10",
                "photos/viaje.jpg"
        );

        List<ImportValidationIssue> issues =
                validator.validate(List.of(row));

        assertTrue(issues.isEmpty());
    }

    @Test
    void shouldReportRepeatedId() {
        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "photos/viaje.jpg"
                ),
                videoRow(
                        5,
                        "10",
                        "videos/cumpleanos.mp4",
                        "thumbnails/cumpleanos.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                validator.validate(rows);

        assertIssue(
                issues,
                5,
                "id",
                "El identificador está repetido; apareció primero en la fila 2."
        );
    }

    @Test
    void shouldReportCompletelyRepeatedRow() {
        ImportManifestRow firstRow = photoRow(
                2,
                "10",
                "photos/viaje.jpg"
        );

        ImportManifestRow repeatedRow =
                copyWithRowNumber(firstRow, 6);

        List<ImportValidationIssue> issues =
                validator.validate(
                        List.of(firstRow, repeatedRow)
                );

        assertIssue(
                issues,
                6,
                "row",
                "La fila está completamente repetida; apareció primero en la fila 2."
        );
    }

    @Test
    void shouldReportRepeatedMainFile() {
        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "photos/compartida.jpg"
                ),
                photoRow(
                        4,
                        "12",
                        "photos/compartida.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                validator.validate(rows);

        assertIssue(
                issues,
                4,
                "file",
                "El archivo principal está reutilizado; apareció primero en la fila 2."
        );
    }

    @Test
    void shouldReportRepeatedThumbnail() {
        List<ImportManifestRow> rows = List.of(
                videoRow(
                        2,
                        "10",
                        "videos/primero.mp4",
                        "thumbnails/compartida.jpg"
                ),
                videoRow(
                        7,
                        "11",
                        "videos/segundo.mp4",
                        "thumbnails/compartida.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                validator.validate(rows);

        assertIssue(
                issues,
                7,
                "thumbnail",
                "La miniatura está reutilizada; apareció primero en la fila 2."
        );
    }

    @Test
    void shouldNormalizePathsBeforeDetectingDuplicates() {
        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "photos/viaje.jpg"
                ),
                photoRow(
                        8,
                        "11",
                        "photos/familia/../viaje.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                validator.validate(rows);

        assertIssue(
                issues,
                8,
                "file",
                "El archivo principal está reutilizado; apareció primero en la fila 2."
        );
    }

    @Test
    void shouldReportPathUsedAsMainFileAndThumbnail()
    {
        List<ImportManifestRow> rows = List.of(
                photoRow(
                        2,
                        "10",
                        "photos/compartida.jpg"
                ),
                videoRow(
                        9,
                        "11",
                        "videos/recuerdo.mp4",
                        "photos/compartida.jpg"
                )
        );

        List<ImportValidationIssue> issues =
                validator.validate(rows);

        assertIssue(
                issues,
                9,
                "thumbnail",
                "La ruta se utiliza de forma incompatible; apareció primero en la fila 2."
        );
    }

    @Test
    void shouldIgnoreBlankValuesForDuplicateDetection() {
        ImportManifestRow firstRow = row(
                2,
                "",
                "Primer recuerdo",
                "photo",
                "",
                ""
        );

        ImportManifestRow secondRow = row(
                3,
                " ",
                "Segundo recuerdo",
                "photo",
                " ",
                " "
        );

        List<ImportValidationIssue> issues =
                validator.validate(
                        List.of(firstRow, secondRow)
                );

        assertTrue(issues.isEmpty());
    }

    private ImportManifestRow photoRow(
            int rowNumber,
            String id,
            String file
    ) {
        return row(
                rowNumber,
                id,
                "Fotografía familiar " + id,
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
                "Video familiar " + id,
                "video",
                file,
                thumbnail
        );
    }

    private ImportManifestRow row(
            int rowNumber,
            String id,
            String title,
            String type,
            String file,
            String thumbnail
    ) {
        return new ImportManifestRow(
                rowNumber,
                id,
                title,
                type,
                "familia",
                "2024-03-15",
                "San José",
                file,
                thumbnail,
                "Un recuerdo especial."
        );
    }

    private ImportManifestRow copyWithRowNumber(
            ImportManifestRow row,
            int rowNumber
    ) {
        return new ImportManifestRow(
                rowNumber,
                row.id(),
                row.title(),
                row.type(),
                row.category(),
                row.date(),
                row.place(),
                row.file(),
                row.thumbnail(),
                row.description()
        );
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

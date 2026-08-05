package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportDryRunReport;
import com.album_de_mama.back_end.importvalidation.model.ImportValidationIssue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImportDryRunServiceTests {

    @TempDir
    Path importRoot;

    @Test
    void shouldReturnValidReportForValidManifest()
            throws IOException {
        createFile(
                "photos/viaje.jpg",
                "contenido de la fotografía"
        );
        createFile(
                "videos/cumpleanos.mp4",
                "contenido del video"
        );
        createFile(
                "thumbnails/cumpleanos.jpg",
                "contenido de la miniatura"
        );

        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail,description
                10,Viaje familiar,photo,viajes,2024-03-15,Guanacaste,photos/viaje.jpg,photos/viaje.jpg,Un día especial en familia
                11,Cumpleaños familiar,video,celebraciones,2025-06-10,Cartago,videos/cumpleanos.mp4,thumbnails/cumpleanos.jpg,Una celebración especial
                """
        );

        ImportDryRunReport report =
                createService().validate("manifest.csv");

        assertTrue(report.valid());
        assertEquals(2, report.totalRows());
        assertEquals(2, report.validRows());
        assertEquals(0, report.invalidRows());
        assertTrue(report.issues().isEmpty());
    }

    @Test
    void shouldAccumulateIssuesAndCountInvalidRowsOnce()
            throws IOException {
        createFile("photos/compartida.gif");

        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail,description
                10,,photo,mascotas,2024-02-30,Guanacaste,photos/compartida.gif,photos/compartida.gif,
                10,Segundo recuerdo,audio,familia,2024-03-15,Cartago,photos/compartida.gif,photos/compartida.gif,Descripción válida
                """
        );

        ImportDryRunReport report =
                createService().validate("manifest.csv");

        assertFalse(report.valid());
        assertEquals(2, report.totalRows());
        assertEquals(0, report.validRows());
        assertEquals(2, report.invalidRows());

        assertIssue(report, 2, "title", "El campo es obligatorio.");
        assertIssue(
                report,
                2,
                "category",
                "La categoría debe ser viajes, familia o celebraciones."
        );
        assertIssue(
                report,
                2,
                "date",
                "La fecha debe usar el formato válido YYYY-MM-DD."
        );
        assertIssue(
                report,
                2,
                "file",
                "La fotografía debe usar un archivo JPG, JPEG o PNG."
        );
        assertIssue(
                report,
                3,
                "id",
                "El identificador está repetido; apareció primero en la fila 2."
        );
        assertIssue(
                report,
                3,
                "type",
                "El tipo debe ser photo o video."
        );
        assertIssue(
                report,
                3,
                "file",
                "El archivo principal está reutilizado; apareció primero en la fila 2."
        );
    }


    @Test
    void shouldReportFilesWithDuplicateContent()
            throws IOException {
        createFile(
                "photos/original.jpg",
                "contenido compartido"
        );
        createFile(
                "photos/copia.jpg",
                "contenido compartido"
        );

        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail,description
                10,Primer recuerdo,photo,familia,2024-03-15,San José,photos/original.jpg,photos/original.jpg,Primer recuerdo
                11,Segundo recuerdo,photo,familia,2024-03-16,Cartago,photos/copia.jpg,photos/copia.jpg,Segundo recuerdo
                """
        );

        ImportDryRunReport report =
                createService().validate("manifest.csv");

        assertFalse(report.valid());
        assertEquals(2, report.totalRows());
        assertEquals(1, report.validRows());
        assertEquals(1, report.invalidRows());

        assertIssue(
                report,
                3,
                "file",
                "El archivo tiene contenido duplicado; apareció primero en el campo file de la fila 2."
        );
    }

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

        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail,description
                10,Primer recuerdo,photo,familia,2024-03-15,San José,photos/primera.jpg,photos/primera.jpg,Primer recuerdo
                11,Segundo recuerdo,photo,familia,2024-03-16,Cartago,photos/segunda.jpg,photos/segunda.jpg,Segundo recuerdo
                """
        );

        ImportDryRunReport report =
                createService().validate("manifest.csv");

        assertTrue(report.valid());
        assertEquals(2, report.totalRows());
        assertEquals(2, report.validRows());
        assertEquals(0, report.invalidRows());
        assertTrue(report.issues().isEmpty());
    }

    private ImportDryRunService createService() {
        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        ImportMediaPathResolver pathResolver =
                new ImportMediaPathResolver(properties);

        return new ImportDryRunService(
                new ImportManifestReader(properties),
                new ImportManifestRowValidator(),
                new ImportMediaFileValidator(pathResolver),
                new ImportManifestDuplicateValidator(),
                new ImportContentDuplicateValidator(
                        pathResolver,
                        new ImportFileHashCalculator()
                )
        );
    }

    private void writeManifest(String content)
            throws IOException {
        Files.writeString(
                importRoot.resolve("manifest.csv"),
                content
        );
    }

    private void createFile(String relativePath)
            throws IOException {
        createFile(relativePath, "test content");
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
            ImportDryRunReport report,
            int rowNumber,
            String field,
            String message
    ) {
        assertTrue(
                report.issues().contains(
                        new ImportValidationIssue(
                                rowNumber,
                                field,
                                message
                        )
                )
        );
    }
}

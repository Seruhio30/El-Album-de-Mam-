package com.album_de_mama.back_end.importvalidation.service;

import com.album_de_mama.back_end.importvalidation.config.ImportProperties;
import com.album_de_mama.back_end.importvalidation.model.ImportManifestRow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImportManifestReaderTests {

    private static final String INVALID_HEADER_MESSAGE =
            "El encabezado del manifiesto no coincide con el contrato esperado.";

    @TempDir
    Path importRoot;

    @Test
    void shouldReadValidManifest() throws IOException {
        Path manifest = importRoot.resolve("manifest.csv");

        Files.writeString(
                manifest,
                """
                id,title,type,category,date,place,file,thumbnail,description
                10,Viaje a la playa,photo,viajes,2024-03-15,Guanacaste,photos/viaje.jpg,photos/viaje.jpg,Un día en familia
                11,Cumpleaños familiar,video,celebraciones,2025-06-10,Cartago,videos/cumpleanos.mp4,thumbnails/cumpleanos.jpg,Una celebración especial
                """
        );

        ImportManifestReader reader = createReader();

        List<ImportManifestRow> rows = reader.read("manifest.csv");

        assertEquals(2, rows.size());

        ImportManifestRow firstRow = rows.getFirst();

        assertEquals(2, firstRow.rowNumber());
        assertEquals("10", firstRow.id());
        assertEquals("Viaje a la playa", firstRow.title());
        assertEquals("photo", firstRow.type());
        assertEquals("viajes", firstRow.category());
        assertEquals("2024-03-15", firstRow.date());
        assertEquals("Guanacaste", firstRow.place());
        assertEquals("photos/viaje.jpg", firstRow.file());
        assertEquals("photos/viaje.jpg", firstRow.thumbnail());
        assertEquals("Un día en familia", firstRow.description());

        ImportManifestRow secondRow = rows.get(1);

        assertEquals(3, secondRow.rowNumber());
        assertEquals("11", secondRow.id());
        assertEquals("video", secondRow.type());
        assertEquals(
                "thumbnails/cumpleanos.jpg",
                secondRow.thumbnail()
        );
    }

    @Test
    void shouldRejectMissingManifest() {
        ImportManifestReader reader = createReader();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reader.read("missing.csv")
        );

        assertEquals(
                "El manifiesto solicitado no existe.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectManifestOutsideImportRoot() {
        ImportManifestReader reader = createReader();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reader.read("../outside.csv")
        );

        assertEquals(
                "La ruta del manifiesto no es válida.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectMissingHeaderColumn() throws IOException {
        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail
                10,Viaje,photo,viajes,2024-03-15,Guanacaste,photos/viaje.jpg,photos/viaje.jpg
                """
        );

        assertInvalidHeader();
    }

    @Test
    void shouldRejectAdditionalHeaderColumn() throws IOException {
        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail,description,notes
                10,Viaje,photo,viajes,2024-03-15,Guanacaste,photos/viaje.jpg,photos/viaje.jpg,Un viaje,Nota
                """
        );

        assertInvalidHeader();
    }

    @Test
    void shouldRejectIncorrectHeaderOrder() throws IOException {
        writeManifest(
                """
                title,id,type,category,date,place,file,thumbnail,description
                Viaje,10,photo,viajes,2024-03-15,Guanacaste,photos/viaje.jpg,photos/viaje.jpg,Un viaje
                """
        );

        assertInvalidHeader();
    }

    @Test
    void shouldRejectRepeatedHeader() throws IOException {
        writeManifest(
                """
                id,title,type,category,date,place,file,thumbnail,title
                10,Viaje,photo,viajes,2024-03-15,Guanacaste,photos/viaje.jpg,photos/viaje.jpg,Un viaje
                """
        );

        assertInvalidHeader();
    }

    private void assertInvalidHeader() {
        ImportManifestReader reader = createReader();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> reader.read("manifest.csv")
        );

        assertEquals(
                INVALID_HEADER_MESSAGE,
                exception.getMessage()
        );
    }

    private void writeManifest(String content) throws IOException {
        Files.writeString(
                importRoot.resolve("manifest.csv"),
                content
        );
    }

    private ImportManifestReader createReader() {
        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        return new ImportManifestReader(properties);
    }
}

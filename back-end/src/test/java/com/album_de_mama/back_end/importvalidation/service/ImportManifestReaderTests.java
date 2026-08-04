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

    private ImportManifestReader createReader() {
        ImportProperties properties = new ImportProperties();
        properties.setRoot(importRoot);

        return new ImportManifestReader(properties);
    }
}

package com.album_de_mama.back_end.storage.service;

import com.album_de_mama.back_end.storage.config.StorageProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalMediaStorageServiceTests {

    @TempDir
    Path storageRoot;

    @Test
    void shouldLoadExistingFile() throws IOException {
        Path file = storageRoot.resolve("memories/1/photo/test.jpg");
        Files.createDirectories(file.getParent());
        Files.writeString(file, "test content");

        LocalMediaStorageService storageService = createStorageService();

        Resource resource = storageService.load(
                "memories/1/photo/test.jpg"
        );

        assertTrue(resource.exists());
        assertEquals(file.toAbsolutePath(), resource.getFile().toPath());
    }

    @Test
    void shouldRejectStorageKeyOutsideStorageRoot() {
        LocalMediaStorageService storageService = createStorageService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storageService.load("../outside.jpg")
        );

        assertEquals(
                "La clave de almacenamiento no es válida.",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectMissingFile() {
        LocalMediaStorageService storageService = createStorageService();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> storageService.load(
                        "memories/99/photo/missing.jpg"
                )
        );

        assertEquals(
                "El archivo solicitado no existe.",
                exception.getMessage()
        );
    }

    private LocalMediaStorageService createStorageService() {
        StorageProperties storageProperties = new StorageProperties();
        storageProperties.setRoot(storageRoot);

        return new LocalMediaStorageService(storageProperties);
    }
}

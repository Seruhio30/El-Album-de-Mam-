package com.album_de_mama.back_end.importvalidation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ImportFileHashCalculatorTests {

    @TempDir
    Path tempDirectory;

    @Test
    void shouldCalculateSha256Hash()
            throws IOException {
        Path file = tempDirectory.resolve("recuerdo.jpg");
        Files.writeString(file, "contenido familiar");

        String hash = new ImportFileHashCalculator().calculate(
                file.toRealPath(),
                new HashMap<>()
        );

        assertEquals(
                "a0438e100adec67859fae73ea02969c0f6198bd66f65535c4d05e59473f0b0db",
                hash
        );
    }

    @Test
    void shouldReuseHashFromProvidedCache()
            throws IOException {
        Path file = tempDirectory.resolve("recuerdo.jpg");
        Files.writeString(file, "contenido original");

        ImportFileHashCalculator calculator =
                new ImportFileHashCalculator();

        Map<Path, String> hashesByRealPath = new HashMap<>();
        Path realPath = file.toRealPath();

        String firstHash = calculator.calculate(
                realPath,
                hashesByRealPath
        );

        Files.delete(file);

        String reusedHash = calculator.calculate(
                realPath,
                hashesByRealPath
        );

        assertEquals(firstHash, reusedHash);
        assertEquals(1, hashesByRealPath.size());
    }

    @Test
    void shouldCalculateAgainWhenUsingNewCache()
            throws IOException {
        Path file = tempDirectory.resolve("recuerdo.jpg");
        Files.writeString(file, "contenido original");

        ImportFileHashCalculator calculator =
                new ImportFileHashCalculator();

        String firstHash = calculator.calculate(
                file.toRealPath(),
                new HashMap<>()
        );

        Files.writeString(file, "contenido modificado");

        String secondHash = calculator.calculate(
                file.toRealPath(),
                new HashMap<>()
        );

        assertNotEquals(firstHash, secondHash);
    }

    @Test
    void shouldCalculateDifferentHashesForDifferentContent()
            throws IOException {
        Path firstFile = tempDirectory.resolve("primero.jpg");
        Path secondFile = tempDirectory.resolve("segundo.jpg");

        Files.writeString(firstFile, "primer contenido");
        Files.writeString(secondFile, "segundo contenido");

        ImportFileHashCalculator calculator =
                new ImportFileHashCalculator();

        Map<Path, String> hashesByRealPath = new HashMap<>();

        String firstHash = calculator.calculate(
                firstFile.toRealPath(),
                hashesByRealPath
        );
        String secondHash = calculator.calculate(
                secondFile.toRealPath(),
                hashesByRealPath
        );

        assertNotEquals(firstHash, secondHash);
        assertEquals(2, hashesByRealPath.size());
    }
}

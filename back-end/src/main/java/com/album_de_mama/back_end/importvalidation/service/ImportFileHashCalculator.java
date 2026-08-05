package com.album_de_mama.back_end.importvalidation.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;

@Service
public class ImportFileHashCalculator {

    public String calculate(
            Path realPath,
            Map<Path, String> hashesByRealPath
    ) {
        Path normalizedRealPath = realPath
                .toAbsolutePath()
                .normalize();

        return hashesByRealPath.computeIfAbsent(
                normalizedRealPath,
                this::calculateSha256
        );
    }

    private String calculateSha256(Path file) {
        MessageDigest digest = createSha256Digest();

        try (InputStream inputStream = Files.newInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            return HexFormat.of().formatHex(digest.digest());
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "No fue posible calcular el hash SHA-256 del archivo: "
                            + file,
                    exception
            );
        }
    }

    private MessageDigest createSha256Digest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException(
                    "SHA-256 no está disponible en la JVM.",
                    exception
            );
        }
    }
}

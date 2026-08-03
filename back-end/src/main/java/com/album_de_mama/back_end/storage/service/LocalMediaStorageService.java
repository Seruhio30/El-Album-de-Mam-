package com.album_de_mama.back_end.storage.service;

import com.album_de_mama.back_end.storage.config.StorageProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalMediaStorageService implements MediaStorageService {

    private final Path storageRoot;

    public LocalMediaStorageService(StorageProperties storageProperties) {
        this.storageRoot = storageProperties.getRoot()
                .toAbsolutePath()
                .normalize();
    }

    @Override
    public Resource load(String storageKey) {
        Path filePath = storageRoot
                .resolve(storageKey)
                .normalize();

        if (!filePath.startsWith(storageRoot)) {
            throw new IllegalArgumentException(
                    "La clave de almacenamiento no es válida."
            );
        }

        if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException(
                    "El archivo solicitado no existe."
            );
        }

        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException(
                    "No fue posible cargar el archivo solicitado.",
                    exception
            );
        }
    }
}

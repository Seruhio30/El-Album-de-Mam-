package com.album_de_mama.back_end.memory.service;

import com.album_de_mama.back_end.memory.repository.MemoryRepository;
import com.album_de_mama.back_end.storage.service.MediaStorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemoryMediaService {

    private final MemoryRepository memoryRepository;
    private final MediaStorageService mediaStorageService;

    public MemoryMediaService(
            MemoryRepository memoryRepository,
            MediaStorageService mediaStorageService
    ) {
        this.memoryRepository = memoryRepository;
        this.mediaStorageService = mediaStorageService;
    }

    public Optional<Resource> loadFile(Long memoryId) {
        return memoryRepository.findById(memoryId)
                .flatMap(memory -> load(memory.getFile()));
    }

    public Optional<Resource> loadThumbnail(Long memoryId) {
        return memoryRepository.findById(memoryId)
                .flatMap(memory -> load(memory.getThumbnail()));
    }

    private Optional<Resource> load(String storageKey) {
        try {
            return Optional.of(mediaStorageService.load(storageKey));
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}

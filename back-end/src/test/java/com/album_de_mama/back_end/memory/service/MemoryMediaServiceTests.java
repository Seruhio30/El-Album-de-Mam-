package com.album_de_mama.back_end.memory.service;

import com.album_de_mama.back_end.memory.entity.Memory;
import com.album_de_mama.back_end.memory.repository.MemoryRepository;
import com.album_de_mama.back_end.storage.service.MediaStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MemoryMediaServiceTests {

    private final MemoryRepository memoryRepository =
            mock(MemoryRepository.class);

    private final MediaStorageService mediaStorageService =
            mock(MediaStorageService.class);

    private final MemoryMediaService memoryMediaService =
            new MemoryMediaService(
                    memoryRepository,
                    mediaStorageService
            );

    @Test
    void shouldLoadMemoryFile() {
        Memory memory = mock(Memory.class);
        Resource resource = mock(Resource.class);

        when(memoryRepository.findById(1L))
                .thenReturn(Optional.of(memory));
        when(memory.getFile())
                .thenReturn("memories/1/photo/test.jpg");
        when(mediaStorageService.load(
                "memories/1/photo/test.jpg"
        )).thenReturn(resource);

        Optional<Resource> result =
                memoryMediaService.loadFile(1L);

        assertTrue(result.isPresent());
        assertSame(resource, result.get());
    }

    @Test
    void shouldLoadMemoryThumbnail() {
        Memory memory = mock(Memory.class);
        Resource resource = mock(Resource.class);

        when(memoryRepository.findById(2L))
                .thenReturn(Optional.of(memory));
        when(memory.getThumbnail())
                .thenReturn("memories/2/thumbnail/test.jpg");
        when(mediaStorageService.load(
                "memories/2/thumbnail/test.jpg"
        )).thenReturn(resource);

        Optional<Resource> result =
                memoryMediaService.loadThumbnail(2L);

        assertTrue(result.isPresent());
        assertSame(resource, result.get());
    }

    @Test
    void shouldReturnEmptyWhenMemoryDoesNotExist() {
        when(memoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        Optional<Resource> result =
                memoryMediaService.loadFile(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyWhenStoredFileDoesNotExist() {
        Memory memory = mock(Memory.class);

        when(memoryRepository.findById(1L))
                .thenReturn(Optional.of(memory));
        when(memory.getFile())
                .thenReturn("memories/1/photo/missing.jpg");
        when(mediaStorageService.load(
                "memories/1/photo/missing.jpg"
        )).thenThrow(new IllegalArgumentException());

        Optional<Resource> result =
                memoryMediaService.loadFile(1L);

        assertTrue(result.isEmpty());
    }
}
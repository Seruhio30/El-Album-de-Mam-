package com.album_de_mama.back_end.memory.controller;

import com.album_de_mama.back_end.memory.model.MemoryResponse;
import com.album_de_mama.back_end.memory.service.MemoryMediaService;
import com.album_de_mama.back_end.memory.service.MemoryService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;
    private final MemoryMediaService memoryMediaService;

    public MemoryController(
            MemoryService memoryService,
            MemoryMediaService memoryMediaService
    ) {
        this.memoryService = memoryService;
        this.memoryMediaService = memoryMediaService;
    }

    @GetMapping
    public List<MemoryResponse> findAll() {
        return memoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemoryResponse> findById(@PathVariable Long id) {
        return memoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<Resource> findFile(@PathVariable Long id) {
        return buildMediaResponse(memoryMediaService.loadFile(id));
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Resource> findThumbnail(@PathVariable Long id) {
        return buildMediaResponse(memoryMediaService.loadThumbnail(id));
    }

    private ResponseEntity<Resource> buildMediaResponse(
            Optional<Resource> resource
    ) {
        if (resource.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Resource media = resource.get();

        try {
            String contentType = Files.probeContentType(
                    media.getFile().toPath()
            );

            MediaType mediaType = contentType == null
                    ? MediaType.APPLICATION_OCTET_STREAM
                    : MediaType.parseMediaType(contentType);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(media);
        } catch (IOException exception) {
            return ResponseEntity.notFound().build();
        }
    }
}
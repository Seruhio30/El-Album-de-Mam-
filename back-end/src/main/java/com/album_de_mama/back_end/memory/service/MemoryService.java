package com.album_de_mama.back_end.memory.service;

import com.album_de_mama.back_end.memory.entity.Memory;
import com.album_de_mama.back_end.memory.model.MemoryResponse;
import com.album_de_mama.back_end.memory.repository.MemoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class MemoryService {

    private final MemoryRepository memoryRepository;

    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    public List<MemoryResponse> findAll() {
        return memoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<MemoryResponse> findById(Long id) {
        return memoryRepository.findById(id)
                .map(this::toResponse);
    }

    private MemoryResponse toResponse(Memory memory) {
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();

        String fileUrl = baseUrl
                + "/api/memories/"
                + memory.getId()
                + "/file";

        String thumbnailUrl = baseUrl
                + "/api/memories/"
                + memory.getId()
                + "/thumbnail";

        return new MemoryResponse(
                memory.getId(),
                memory.getTitle(),
                memory.getType(),
                memory.getCategory(),
                memory.getDate(),
                memory.getPlace(),
                fileUrl,
                thumbnailUrl,
                memory.getDescription()
        );
    }
}
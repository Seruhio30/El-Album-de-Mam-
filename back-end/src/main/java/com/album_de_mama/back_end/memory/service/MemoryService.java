package com.album_de_mama.back_end.memory.service;

import com.album_de_mama.back_end.memory.entity.Memory;
import com.album_de_mama.back_end.memory.model.MemoryResponse;
import com.album_de_mama.back_end.memory.model.PagedMemoryResponse;
import com.album_de_mama.back_end.memory.repository.MemoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class MemoryService {

    private static final Sort MEMORY_SORT = Sort.by(
            Sort.Order.desc("date"),
            Sort.Order.desc("id")
    );

    private final MemoryRepository memoryRepository;

    public MemoryService(MemoryRepository memoryRepository) {
        this.memoryRepository = memoryRepository;
    }

    public PagedMemoryResponse findAll(
            int page,
            int size,
            String category
    ) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                MEMORY_SORT
        );

        Page<Memory> memoryPage =
                category == null || category.isBlank()
                        ? memoryRepository.findAll(pageRequest)
                        : memoryRepository.findByCategoryIgnoreCase(
                                category.trim(),
                                pageRequest
                        );

        List<MemoryResponse> content = memoryPage
                .getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PagedMemoryResponse(
                content,
                memoryPage.getNumber(),
                memoryPage.getSize(),
                memoryPage.getTotalElements(),
                memoryPage.getTotalPages(),
                memoryPage.hasNext()
        );
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

package com.album_de_mama.back_end.memory.controller;

import com.album_de_mama.back_end.memory.model.MemoryResponse;
import com.album_de_mama.back_end.memory.service.MemoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/memories")
public class MemoryController {

    private final MemoryService memoryService;

    public MemoryController(MemoryService memoryService) {
        this.memoryService = memoryService;
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
}
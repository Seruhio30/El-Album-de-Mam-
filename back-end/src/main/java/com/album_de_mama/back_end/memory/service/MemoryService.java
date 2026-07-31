package com.album_de_mama.back_end.memory.service;

import com.album_de_mama.back_end.memory.model.MemoryResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public class MemoryService {

    private final List<MemoryResponse> memories = List.of(
            new MemoryResponse(
                    1L,
                    "Viaje familiar",
                    "photo",
                    "viajes",
                    LocalDate.of(2024, 3, 15),
                    "Guanacaste",
                    "assets/photos/viaje-familiar.png",
                    "assets/photos/viaje-familiar.png",
                    "Un día especial en familia durante el viaje."
            ),
            new MemoryResponse(
                    2L,
                    "Cumpleaños familiar",
                    "video",
                    "celebraciones",
                    LocalDate.of(2025, 6, 10),
                    "Cartago",
                    "assets/videos/cumpleanos-familiar.mp4",
                    "assets/thumbnails/cumpleanos-familiar.jpg",
                    "Celebración de cumpleaños con toda la familia."
            ),
            new MemoryResponse(
                    3L,
                    "Tarde en familia",
                    "photo",
                    "familia",
                    LocalDate.of(2025, 12, 20),
                    "San José",
                    "assets/photos/tarde-en-familia.jpg",
                    "assets/photos/tarde-en-familia.jpg",
                    "Una tarde tranquila compartiendo juntos."
            )
    );

    public List<MemoryResponse> findAll() {
        return memories;
    }
    
    public Optional<MemoryResponse> findById(Long id) {
        return memories.stream()
                .filter(memory -> memory.id().equals(id))
                .findFirst();
    }
}
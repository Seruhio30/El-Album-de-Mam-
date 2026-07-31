package com.album_de_mama.back_end.memory.model;

import java.time.LocalDate;

public record MemoryResponse(
        Long id,
        String title,
        String type,
        String category,
        LocalDate date,
        String place,
        String file,
        String thumbnail,
        String description
) {
}
package com.album_de_mama.back_end.memory.model;

import java.util.List;

public record PagedMemoryResponse(
        List<MemoryResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
}

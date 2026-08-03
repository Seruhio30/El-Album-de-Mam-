package com.album_de_mama.back_end.memory.repository;

import com.album_de_mama.back_end.memory.entity.Memory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    Page<Memory> findByCategoryIgnoreCase(
            String category,
            Pageable pageable
    );
}

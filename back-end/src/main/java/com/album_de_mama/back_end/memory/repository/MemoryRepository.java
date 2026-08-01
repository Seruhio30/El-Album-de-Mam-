package com.album_de_mama.back_end.memory.repository;

import com.album_de_mama.back_end.memory.entity.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {
}
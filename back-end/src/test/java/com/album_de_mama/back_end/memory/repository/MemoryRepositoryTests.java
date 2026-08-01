package com.album_de_mama.back_end.memory.repository;

import com.album_de_mama.back_end.memory.entity.Memory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemoryRepositoryTests {

    @Autowired
    private MemoryRepository memoryRepository;

    @Test
    void shouldLoadInitialMemoriesFromDatabase() {
        List<Memory> memories = memoryRepository.findAll();

        assertThat(memories).hasSize(3);
        assertThat(memories)
                .extracting(Memory::getId)
                .containsExactlyInAnyOrder(1L, 2L, 3L);
    }
}
package com.album_de_mama.back_end.memory.repository;

import com.album_de_mama.back_end.memory.entity.Memory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    @Test
    void shouldReturnMemoriesUsingStablePaginationOrder() {
        PageRequest pageRequest = PageRequest.of(
                0,
                2,
                Sort.by(
                        Sort.Order.desc("date"),
                        Sort.Order.desc("id")
                )
        );

        Page<Memory> page = memoryRepository.findAll(pageRequest);

        assertThat(page.getContent())
                .extracting(Memory::getId)
                .containsExactly(3L, 2L);

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    void shouldFilterMemoriesByCategoryIgnoringCase() {
        PageRequest pageRequest = PageRequest.of(
                0,
                6,
                Sort.by(
                        Sort.Order.desc("date"),
                        Sort.Order.desc("id")
                )
        );

        Page<Memory> page =
                memoryRepository.findByCategoryIgnoreCase(
                        "VIAJES",
                        pageRequest
                );

        assertThat(page.getContent())
                .extracting(Memory::getId)
                .containsExactly(1L);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.hasNext()).isFalse();
    }
}

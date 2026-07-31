package com.album_de_mama.back_end.memory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnAllMemories() throws Exception {
        mockMvc.perform(get("/api/memories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Viaje familiar"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));
    }

    @Test
    void shouldReturnMemoryById() throws Exception {
        mockMvc.perform(get("/api/memories/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Cumpleaños familiar"))
                .andExpect(jsonPath("$.type").value("video"))
                .andExpect(jsonPath("$.category").value("celebraciones"))
                .andExpect(jsonPath("$.date").value("2025-06-10"))
                .andExpect(jsonPath("$.place").value("Cartago"));
    }

    @Test
    void shouldReturnNotFoundWhenMemoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/memories/999"))
                .andExpect(status().isNotFound());
    }
}
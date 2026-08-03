package com.album_de_mama.back_end.memory.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

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
                .andExpect(jsonPath("$[0].file").value("http://localhost/api/memories/1/file"))
                .andExpect(jsonPath("$[0].thumbnail").value("http://localhost/api/memories/1/thumbnail"))
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
                .andExpect(jsonPath("$.place").value("Cartago"))
                .andExpect(jsonPath("$.file")
                .value("http://localhost/api/memories/2/file"))
                .andExpect(jsonPath("$.thumbnail").value("http://localhost/api/memories/2/thumbnail"));
    }

    @Test
    void shouldReturnNotFoundWhenMemoryDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/memories/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAllowLocalhostFrontendOrigin() throws Exception {
        mockMvc.perform(get("/api/memories")
                        .header(ORIGIN, "http://localhost:5500"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://localhost:5500"
                ));
    }

    @Test
    void shouldAllowLoopbackFrontendOrigin() throws Exception {
        mockMvc.perform(get("/api/memories")
                        .header(ORIGIN, "http://127.0.0.1:5500"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        ACCESS_CONTROL_ALLOW_ORIGIN,
                        "http://127.0.0.1:5500"
                ));
    }

    @Test
    void shouldNotAllowUnknownOrigin() throws Exception {
        mockMvc.perform(get("/api/memories")
                        .header(ORIGIN, "http://example.com"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(
                        ACCESS_CONTROL_ALLOW_ORIGIN
                ));
    }
    @Test
    void shouldReturnMemoryFile() throws Exception {
        mockMvc.perform(get("/api/memories/1/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/png"));
    }

    @Test
    void shouldReturnMemoryThumbnail() throws Exception {
        mockMvc.perform(get("/api/memories/2/thumbnail"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("image/jpeg"));
    }

    @Test
    void shouldReturnNotFoundWhenMemoryFileDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/memories/999/file"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenMemoryThumbnailDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/memories/999/thumbnail"))
                .andExpect(status().isNotFound());
    }
}

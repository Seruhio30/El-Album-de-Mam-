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
}

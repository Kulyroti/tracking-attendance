package ru.java.kursach.tracking_attendance.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class DisciplineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllDisciplines() throws Exception {
        mockMvc.perform(get("/disciplines?page=0&sortByName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetDisciplineByIdNotFound() throws Exception {
        mockMvc.perform(get("/disciplines/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetDiscipline() throws Exception {
        // Создание Discipline
        String disciplineJson = "{ \"name\": \"Test Discipline\" }";
        mockMvc.perform(post("/disciplines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(disciplineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Discipline"));

        // Получение всех дисциплин (должна быть 1)
        mockMvc.perform(get("/disciplines?page=0&sortByName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Получение созданной Discipline по id
        mockMvc.perform(get("/disciplines/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Test Discipline"));
    }

    @Test
    void testUpdateDiscipline() throws Exception {
        // Создаем Discipline для обновления
        String disciplineJson = "{ \"name\": \"Test Discipline\" }";
        mockMvc.perform(post("/disciplines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(disciplineJson))
                .andExpect(status().isOk());

        // Обновление Discipline
        String updatedDisciplineJson = "{ \"name\": \"Updated Discipline\" }";
        mockMvc.perform(put("/disciplines/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedDisciplineJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Discipline"));
    }

    @Test
    void testDeleteDiscipline() throws Exception {
        // Создаем Discipline для удаления
        String disciplineJson = "{ \"name\": \"Test Discipline\" }";
        mockMvc.perform(post("/disciplines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(disciplineJson))
                .andExpect(status().isOk());

        // Удаление Discipline
        mockMvc.perform(delete("/disciplines/1"))
                .andExpect(status().isNoContent());

        // Проверка, что Discipline удалена
        mockMvc.perform(get("/disciplines/1"))
                .andExpect(status().isNotFound());
    }
}

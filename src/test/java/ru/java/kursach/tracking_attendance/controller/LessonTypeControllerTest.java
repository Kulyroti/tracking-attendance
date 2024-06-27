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
class LessonTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllLessonTypes() throws Exception {
        mockMvc.perform(get("/lessons/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetLessonTypeByIdNotFound() throws Exception {
        mockMvc.perform(get("/lessons/types/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetLessonType() throws Exception {
        // Создание LessonType
        String lessonTypeJson = "{ \"name\": \"Lecture\" }";
        mockMvc.perform(post("/lessons/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonTypeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lecture"));

        // Получение всех типов уроков (должен быть 1)
        mockMvc.perform(get("/lessons/types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));

        // Получение созданного LessonType по id
        mockMvc.perform(get("/lessons/types/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Lecture"));
    }

    @Test
    void testUpdateLessonType() throws Exception {
        // Создаем LessonType для обновления
        String lessonTypeJson = "{ \"name\": \"Lecture\" }";
        mockMvc.perform(post("/lessons/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonTypeJson))
                .andExpect(status().isOk());

        // Обновление LessonType
        String updatedLessonTypeJson = "{ \"name\": \"Practice\" }";
        mockMvc.perform(put("/lessons/types/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLessonTypeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Practice"));
    }

    @Test
    void testDeleteLessonType() throws Exception {
        // Создаем LessonType для удаления
        String lessonTypeJson = "{ \"name\": \"Lecture\" }";
        mockMvc.perform(post("/lessons/types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonTypeJson))
                .andExpect(status().isOk());

        // Удаление LessonType
        mockMvc.perform(delete("/lessons/types/1"))
                .andExpect(status().isNoContent());

        // Проверка, что LessonType удален
        mockMvc.perform(get("/lessons/types/1"))
                .andExpect(status().isNotFound());
    }
}

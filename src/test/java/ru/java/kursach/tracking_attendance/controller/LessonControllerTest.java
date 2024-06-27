package ru.java.kursach.tracking_attendance.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testGetAllLessons() throws Exception {
        mockMvc.perform(get("/lessons?page=0&sortByDate=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetLessonByIdNotFound() throws Exception {
        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetLesson() throws Exception {
        // Создание Lesson
        String lessonJson = "{ \"topic\": \"Math\", \"dateTime\": \"2024-01-10T10:00:00\", \"groupId\": 1, \"disciplineId\": 1 }";
        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("Math"))
                .andExpect(jsonPath("$.dateTime").value("2024-01-10T10:00:00"));

        // Получение всех уроков (должен быть 1)
        mockMvc.perform(get("/lessons?page=0&sortByDate=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Получение созданного Lesson по id
        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.topic").value("Math"));
    }

    @Test
    void testUpdateLesson() throws Exception {
        // Создаем Lesson для обновления
        String lessonJson = "{ \"topic\": \"Math\", \"dateTime\": \"2024-01-10T10:00:00\", \"groupId\": 1, \"disciplineId\": 1 }";
        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isOk());

        // Обновление Lesson
        String updatedLessonJson = "{ \"topic\": \"Physics\", \"dateTime\": \"2024-01-15T12:00:00\", \"groupId\": 2, \"disciplineId\": 2 }";
        mockMvc.perform(put("/lessons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedLessonJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("Physics"))
                .andExpect(jsonPath("$.dateTime").value("2024-01-15T12:00:00"));
    }

    @Test
    void testDeleteLesson() throws Exception {
        // Создаем Lesson для удаления
        String lessonJson = "{ \"topic\": \"Math\", \"dateTime\": \"2024-01-10T10:00:00\", \"groupId\": 1, \"disciplineId\": 1 }";
        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lessonJson))
                .andExpect(status().isOk());

        // Удаление Lesson
        mockMvc.perform(delete("/lessons/1"))
                .andExpect(status().isNoContent());

        // Проверка, что Lesson удален
        mockMvc.perform(get("/lessons/1"))
                .andExpect(status().isNotFound());
    }
}

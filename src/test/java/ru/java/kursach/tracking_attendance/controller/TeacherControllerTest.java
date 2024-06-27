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
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllTeachers() throws Exception {
        mockMvc.perform(get("/teachers?page=0&sortByLastName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetTeacherByIdNotFound() throws Exception {
        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetTeacher() throws Exception {
        // Создание Teacher
        String teacherJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\" }";
        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Petrov"));

        // Получение всех учителей (должен быть 1)
        mockMvc.perform(get("/teachers?page=0&sortByLastName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Получение созданного Teacher по id
        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Ivan"));
    }

    @Test
    void testUpdateTeacher() throws Exception {
        // Создаем Teacher для обновления
        String teacherJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\" }";
        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk());

        // Обновление Teacher
        String updatedTeacherJson = "{ \"firstName\": \"Petr\", \"lastName\": \"Sidorov\" }";
        mockMvc.perform(put("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedTeacherJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Petr"))
                .andExpect(jsonPath("$.lastName").value("Sidorov"));
    }

    @Test
    void testDeleteTeacher() throws Exception {
        // Создаем Teacher для удаления
        String teacherJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\" }";
        mockMvc.perform(post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(teacherJson))
                .andExpect(status().isOk());

        // Удаление Teacher
        mockMvc.perform(delete("/teachers/1"))
                .andExpect(status().isNoContent());

        // Проверка, что Teacher удален
        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isNotFound());
    }
}

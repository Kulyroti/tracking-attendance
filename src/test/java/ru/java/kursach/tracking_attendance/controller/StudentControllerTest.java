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
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllStudents() throws Exception {
        mockMvc.perform(get("/students?page=0&sortByLastName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetStudentByIdNotFound() throws Exception {
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetStudent() throws Exception {
        // Создание Student
        String studentJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\", \"groupId\": 1 }";
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ivan"))
                .andExpect(jsonPath("$.lastName").value("Petrov"));

        // Получение всех студентов (должен быть 1)
        mockMvc.perform(get("/students?page=0&sortByLastName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Получение созданного Student по id
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Ivan"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // Создаем Student для обновления
        String studentJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\", \"groupId\": 1 }";
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk());

        // Обновление Student
        String updatedStudentJson = "{ \"firstName\": \"Petr\", \"lastName\": \"Sidorov\", \"groupId\": 2 }";
        mockMvc.perform(put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedStudentJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Petr"))
                .andExpect(jsonPath("$.lastName").value("Sidorov"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        // Создаем Student для удаления
        String studentJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\", \"groupId\": 1 }";
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk());

        // Удаление Student
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().isNoContent());

        // Проверка, что Student удален
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAssignAndRemoveStudentFromGroup() throws Exception {
        // Создаем Student
        String studentJson = "{ \"firstName\": \"Ivan\", \"lastName\": \"Petrov\" }";
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentJson))
                .andExpect(status().isOk());

        // Назначаем Student в группу
        mockMvc.perform(put("/students/1/groups/1"))
                .andExpect(status().isOk());

        // Проверяем, что Student назначен в группу
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value(1));

        // Удаляем Student из группы
        mockMvc.perform(delete("/students/1/groups/1"))
                .andExpect(status().isNoContent());

        // Проверяем, что Student удален из группы (groupId должен быть null)
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupId").value((Object) null));
    }
}

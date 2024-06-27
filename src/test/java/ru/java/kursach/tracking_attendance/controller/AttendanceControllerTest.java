package ru.java.kursach.tracking_attendance.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AttendanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllAttendances() throws Exception {
        mockMvc.perform(get("/attendances?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAttendanceByIdNotFound() throws Exception {
        mockMvc.perform(get("/attendances/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetAttendance() throws Exception {
        // Создание Attendance
        String attendanceJson = "{ \"studentId\": 1, \"lessonId\": 1, \"groupId\": 1, \"date\": \"2023-10-26\", \"present\": true }";
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(1))
                .andExpect(jsonPath("$.lessonId").value(1));

        // Получение всех Attendance (должен быть 1)
        mockMvc.perform(get("/attendances?page=0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Получение созданного Attendance по id
        mockMvc.perform(get("/attendances/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentId").value(1));

    }

    @Test
    void testUpdateAttendance() throws Exception {
        // Создаем Attendance для обновления
        String attendanceJson = "{ \"studentId\": 1, \"lessonId\": 1, \"groupId\": 1, \"date\": \"2023-10-26\", \"present\": true }";
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson))
                .andExpect(status().isOk());

        // Обновление Attendance
        String updatedAttendanceJson = "{ \"studentId\": 2, \"lessonId\": 2, \"groupId\": 2, \"date\": \"2023-10-27\", \"present\": false }";
        mockMvc.perform(put("/attendances/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAttendanceJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(2))
                .andExpect(jsonPath("$.lessonId").value(2));
    }

    @Test
    void testDeleteAttendance() throws Exception {
        // Создание Attendance для удаления
        String attendanceJson = "{ \"studentId\": 1, \"lessonId\": 1, \"groupId\": 1, \"date\": \"2023-10-26\", \"present\": true }";
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson))
                .andExpect(status().isOk());

        // Удаление Attendance
        mockMvc.perform(delete("/attendances/1"))
                .andExpect(status().isNoContent());

        // Проверка, что Attendance удален
        mockMvc.perform(get("/attendances/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdatePresentAttendance() throws Exception {
        // Создание Attendance для обновления статуса присутствия
        String attendanceJson = "{ \"studentId\": 1, \"lessonId\": 1, \"groupId\": 1, \"date\": \"2023-10-26\", \"present\": false }";
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson))
                .andExpect(status().isOk());

        // Обновление статуса присутствия
        mockMvc.perform(put("/attendances/mark/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.present").value(true));
    }

    @Test
    void testGetAttendanceForPeriod() throws Exception {
        // Создание Attendance для проверки по периоду
        String attendanceJson1 = "{ \"studentId\": 1, \"lessonId\": 1, \"groupId\": 1, \"date\": \"2023-10-25\", \"present\": true }";
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson1))
                .andExpect(status().isOk());

        String attendanceJson2 = "{ \"studentId\": 2, \"lessonId\": 2, \"groupId\": 1, \"date\": \"2023-10-26\", \"present\": false }";
        mockMvc.perform(post("/attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(attendanceJson2))
                .andExpect(status().isOk());

        // Получение Attendance за период
        LocalDate startDate = LocalDate.of(2023, 10, 25);
        LocalDate endDate = LocalDate.of(2023, 10, 26);
        mockMvc.perform(get("/attendances/period?startDate=" + startDate + "&endDate=" + endDate))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}

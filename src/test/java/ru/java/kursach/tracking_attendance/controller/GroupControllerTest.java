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
class GroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllGroups() throws Exception {
        mockMvc.perform(get("/groups?page=0&sortByName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetGroupByIdNotFound() throws Exception {
        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateAndGetGroup() throws Exception {
        // Создание Group
        String groupJson = "{ \"name\": \"Group A\" }";
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Group A"));

        // Получение всех групп (должна быть 1)
        mockMvc.perform(get("/groups?page=0&sortByName=false"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)));

        // Получение созданной Group по id
        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Group A"));
    }

    @Test
    void testUpdateGroup() throws Exception {
        // Создаем Group для обновления
        String groupJson = "{ \"name\": \"Group A\" }";
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupJson))
                .andExpect(status().isOk());

        // Обновление Group
        String updatedGroupJson = "{ \"name\": \"Group B\" }";
        mockMvc.perform(put("/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedGroupJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Group B"));
    }

    @Test
    void testDeleteGroup() throws Exception {
        // Создаем Group для удаления
        String groupJson = "{ \"name\": \"Group A\" }";
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(groupJson))
                .andExpect(status().isOk());

        // Удаление Group
        mockMvc.perform(delete("/groups/1"))
                .andExpect(status().isNoContent());

        // Проверка, что Group удалена
        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isNotFound());
    }
}

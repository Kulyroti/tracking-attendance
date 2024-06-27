package ru.java.kursach.tracking_attendance.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSignUpAndSignIn() throws Exception {
        // Регистрация
        String registrationRequest = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "email": "john.doe@example.com",
                    "password": "password"
                }
                """;

        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // Аутентификация
        String signInRequest = """
                {
                    "email": "john.doe@example.com",
                    "password": "password"
                }
                """;

        ResultActions resultActions = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // Получение токена из ответа
        String token = resultActions.andReturn().getResponse().getContentAsString();
        // Можно добавить дополнительные проверки токена, если необходимо
    }

    @Test
    void testSignInWithInvalidCredentials() throws Exception {
        // Попытка аутентификации с неверными данными
        String signInRequest = """
                {
                    "email": "nonexistent@example.com",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInRequest))
                .andExpect(status().isUnauthorized()); // Ожидаем код 401 Unauthorized
    }
}


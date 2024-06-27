package ru.java.kursach.tracking_attendance.model.request;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;

    private String email;

    private String password;
}

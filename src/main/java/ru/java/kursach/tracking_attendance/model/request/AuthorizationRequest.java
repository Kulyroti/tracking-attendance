package ru.java.kursach.tracking_attendance.model.request;

import lombok.Data;

@Data
public class AuthorizationRequest {

    private String username;

    private String password;
}

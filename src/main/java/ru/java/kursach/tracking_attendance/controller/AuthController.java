package ru.java.kursach.tracking_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.java.kursach.tracking_attendance.model.request.AuthorizationRequest;
import ru.java.kursach.tracking_attendance.model.request.JwtAuthenticationResponse;
import ru.java.kursach.tracking_attendance.model.request.RegistrationRequest;
import ru.java.kursach.tracking_attendance.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public JwtAuthenticationResponse signUp(@RequestBody RegistrationRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/login")
    public JwtAuthenticationResponse signIn(@RequestBody AuthorizationRequest request) {
        return authenticationService.signIn(request);
    }
}

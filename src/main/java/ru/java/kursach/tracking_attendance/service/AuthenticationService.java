package ru.java.kursach.tracking_attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.java.kursach.tracking_attendance.model.User;
import ru.java.kursach.tracking_attendance.model.UserAuthority;
import ru.java.kursach.tracking_attendance.model.UserRole;
import ru.java.kursach.tracking_attendance.model.request.AuthorizationRequest;
import ru.java.kursach.tracking_attendance.model.request.JwtAuthenticationResponse;
import ru.java.kursach.tracking_attendance.model.request.RegistrationRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(RegistrationRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build(); // Создаем пользователя без ролей

        // Создаем новую роль UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user); // Устанавливаем связь с пользователем
        userRole.setUserAuthority(UserAuthority.USER); // Назначаем роль USER

        // Добавляем роль в список ролей пользователя
        user.setUserRoles(List.of(userRole)); // Используем List.of() для создания неизменяемого списка

        userService.create(user); // Сохраняем пользователя с ролью

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(AuthorizationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}

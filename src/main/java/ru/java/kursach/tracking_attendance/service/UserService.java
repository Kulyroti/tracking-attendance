package ru.java.kursach.tracking_attendance.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.java.kursach.tracking_attendance.model.User;
import ru.java.kursach.tracking_attendance.model.UserAuthority;
import ru.java.kursach.tracking_attendance.model.UserRole;
import ru.java.kursach.tracking_attendance.repository.UserRepository;
import ru.java.kursach.tracking_attendance.repository.UserRoleRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserRoleRepository userRoleRepository;

    public User save(User user) {
        return repository.save(user);
    }

    @Transactional
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }



    @Transactional
    public void addRoleToUser(String username, UserAuthority authority) {
        User user = getByUsername(username);
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setUserAuthority(authority);

        userRoleRepository.save(userRole);

        user.getUserRoles().add(userRole);
        save(user);
    }

    @Transactional
    public void removeRoleFromUser(String username, UserAuthority authority) {
        User user = getByUsername(username);
        UserRole userRole = user.getUserRoles().stream()
                .filter(role -> role.getUserAuthority().equals(authority))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не имеет такой роли: " + authority));
        user.getUserRoles().remove(userRole);
        save(user);
    }
}

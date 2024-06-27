package ru.java.kursach.tracking_attendance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.java.kursach.tracking_attendance.model.UserAuthority;
import ru.java.kursach.tracking_attendance.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/{username}/role")
    public ResponseEntity<?> addRoleToUser(@PathVariable String username, @RequestBody UserAuthority authority) {
        try {
            userService.addRoleToUser(username, authority);
            return ResponseEntity.ok("Роль успешно добавлена.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Не удалось добавить роль пользователю: " + e.getMessage());
        }
    }

    @DeleteMapping("/{username}/role")
    public ResponseEntity<?> removeRoleFromUser(@PathVariable String username, @RequestBody UserAuthority authority) {
        try {
            userService.removeRoleFromUser(username, authority);
            return ResponseEntity.ok("Роль успешно удалена.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Не удалось удалить роль пользователя: " + e.getMessage());
        }
    }
}

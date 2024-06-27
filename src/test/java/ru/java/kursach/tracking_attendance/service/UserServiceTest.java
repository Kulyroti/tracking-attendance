package ru.java.kursach.tracking_attendance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.java.kursach.tracking_attendance.model.User;
import ru.java.kursach.tracking_attendance.model.UserAuthority;
import ru.java.kursach.tracking_attendance.model.UserRole;
import ru.java.kursach.tracking_attendance.repository.UserRepository;
import ru.java.kursach.tracking_attendance.repository.UserRoleRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testCreateUserUsernameExists() {
        User user = new User();
        user.setUsername("existinguser");
        user.setEmail("test@example.com");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserEmailExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("existing@example.com");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetByUsername() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername("testuser");

        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testGetByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class,
                () -> userService.getByUsername("nonexistentuser"));
    }

    @Test
    void testGetCurrentUser() {
        User user = new User();
        user.setUsername("testuser");

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser();

        assertEquals("testuser", currentUser.getUsername());
    }

    @Test
    void testAddRoleToUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setUserRoles(Collections.emptySet());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRoleRepository.save(any(UserRole.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addRoleToUser("testuser", UserAuthority.USER);

        assertEquals(1, user.getUserRoles().size());
        assertTrue(user.getUserRoles().stream()
                .anyMatch(role -> role.getUserAuthority() == UserAuthority.USER));
    }

    @Test
    void testRemoveRoleFromUser() {
        UserRole userRole = new UserRole();
        userRole.setUserAuthority(UserAuthority.USER);

        User user = new User();
        user.setUsername("testuser");
        user.setUserRoles(Collections.singleton(userRole));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.removeRoleFromUser("testuser", UserAuthority.USER);

        assertTrue(user.getUserRoles().isEmpty());
    }

    @Test
    void testRemoveRoleFromUserNotExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setUserRoles(Collections.emptySet());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () -> userService.removeRoleFromUser("testuser", UserAuthority.ADMIN));
    }
}

package ru.java.kursach.tracking_attendance.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.java.kursach.tracking_attendance.model.Teacher;
import ru.java.kursach.tracking_attendance.repository.TeacherRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    void testFindAllTeachers() {
        // Arrange
        int page = 0;
        boolean sortByLastName = true;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("lastName"));
        Page<Teacher> expectedTeachers = new PageImpl<>(new ArrayList<>());
        when(teacherRepository.findAll(pageRequest)).thenReturn(expectedTeachers);

        // Act
        Page<Teacher> actualTeachers = teacherService.findAllTeachers(page, sortByLastName);

        // Assert
        assertEquals(expectedTeachers, actualTeachers);
        verify(teacherRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void testFindTeacherById() {
        // Arrange
        Long id = 1L;
        Teacher expectedTeacher = new Teacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(expectedTeacher));

        // Act
        Optional<Teacher> actualTeacher = teacherService.findTeacherById(id);

        // Assert
        assertEquals(Optional.of(expectedTeacher), actualTeacher);
        verify(teacherRepository, times(1)).findById(id);
    }

    @Test
    void testCreateTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        // Act
        Teacher createdTeacher = teacherService.createTeacher(teacher);

        // Assert
        assertEquals(teacher, createdTeacher);
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testUpdateTeacher() {
        // Arrange
        Long id = 1L;
        Teacher updatedTeacher = new Teacher();
        Teacher existingTeacher = new Teacher();
        when(teacherRepository.findById(id)).thenReturn(Optional.of(existingTeacher));
        when(teacherRepository.save(existingTeacher)).thenReturn(existingTeacher);

        // Act
        Optional<Teacher> actualTeacher = teacherService.updateTeacher(id, updatedTeacher);

        // Assert
        assertEquals(Optional.of(existingTeacher), actualTeacher);
        assertEquals(updatedTeacher.getFirstName(), existingTeacher.getFirstName());
        assertEquals(updatedTeacher.getMiddleName(), existingTeacher.getMiddleName());
        assertEquals(updatedTeacher.getLastName(), existingTeacher.getLastName());
        assertEquals(updatedTeacher.getEmail(), existingTeacher.getEmail());
        verify(teacherRepository, times(1)).findById(id);
        verify(teacherRepository, times(1)).save(existingTeacher);
    }

    @Test
    void testDeleteTeacher() {
        // Arrange
        Long id = 1L;

        // Act
        teacherService.deleteTeacher(id);

        // Assert
        verify(teacherRepository, times(1)).deleteById(id);
    }
}

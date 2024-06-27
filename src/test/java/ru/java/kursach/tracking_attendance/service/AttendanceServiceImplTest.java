package ru.java.kursach.tracking_attendance.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.java.kursach.tracking_attendance.model.Attendance;
import ru.java.kursach.tracking_attendance.model.Lesson;
import ru.java.kursach.tracking_attendance.model.Student;
import ru.java.kursach.tracking_attendance.repository.AttendanceRepository;
import ru.java.kursach.tracking_attendance.repository.LessonRepository;
import ru.java.kursach.tracking_attendance.repository.StudentRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceImplTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Test
    void testFindAllAttendances() {
        // Подготовка тестовых данных
        Page<Attendance> expectedAttendances = new PageImpl<>(Collections.emptyList());
        when(attendanceRepository.findAll(PageRequest.of(0, 10))).thenReturn(expectedAttendances);

        // Вызов тестируемого метода
        Page<Attendance> actualAttendances = attendanceService.findAllAttendances(0);

        // Проверка результатов
        assertEquals(expectedAttendances, actualAttendances);
    }

    @Test
    void testFindAttendanceById() {
        // Подготовка тестовых данных
        Long attendanceId = 1L;
        Attendance attendance = new Attendance();
        attendance.setId(attendanceId);
        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(attendance));

        // Вызов тестируемого метода
        Optional<Attendance> actualAttendance = attendanceService.findAttendanceById(attendanceId);

        // Проверка результатов
        assertTrue(actualAttendance.isPresent());
        assertEquals(attendanceId, actualAttendance.get().getId());
    }

    @Test
    void testCreateAttendance() {
        // Подготовка тестовых данных
        Long lessonId = 1L;
        Long studentId = 1L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        Student student = new Student();
        student.setId(studentId);
        Attendance attendance = new Attendance();
        attendance.setLesson(lesson);
        attendance.setStudent(student);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(attendanceRepository.save(attendance)).thenReturn(attendance);

        // Вызов тестируемого метода
        Attendance createdAttendance = attendanceService.createAttendance(attendance);

        // Проверка результатов
        assertNotNull(createdAttendance);
        assertEquals(lesson, createdAttendance.getLesson());
        assertEquals(student, createdAttendance.getStudent());
    }

    @Test
    void testUpdateAttendance() {
        // Подготовка тестовых данных
        Long attendanceId = 1L;
        Attendance existingAttendance = new Attendance();
        existingAttendance.setId(attendanceId);
        Lesson newLesson = new Lesson();
        Student newStudent = new Student();
        Attendance updatedAttendance = new Attendance();
        updatedAttendance.setId(attendanceId);
        updatedAttendance.setLesson(newLesson);
        updatedAttendance.setStudent(newStudent);

        when(attendanceRepository.findById(attendanceId)).thenReturn(Optional.of(existingAttendance));
        when(attendanceRepository.save(existingAttendance)).thenReturn(updatedAttendance);

        // Вызов тестируемого метода
        Optional<Attendance> result = attendanceService.updateAttendance(attendanceId, updatedAttendance);

        // Проверка результатов
        assertTrue(result.isPresent());
        assertEquals(newLesson, result.get().getLesson());
        assertEquals(newStudent, result.get().getStudent());
    }

    @Test
    void testDeleteAttendance() {
        // Подготовка тестовых данных
        Long attendanceId = 1L;

        // Вызов тестируемого метода
        attendanceService.deleteAttendance(attendanceId);

        // Проверка результатов
        verify(attendanceRepository, times(1)).deleteById(attendanceId);
    }

}

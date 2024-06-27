package ru.java.kursach.tracking_attendance.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.java.kursach.tracking_attendance.model.Group;
import ru.java.kursach.tracking_attendance.model.Student;
import ru.java.kursach.tracking_attendance.repository.GroupRepository;
import ru.java.kursach.tracking_attendance.repository.StudentRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;
    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllStudents() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("lastName"));
        Page<Student> expectedPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(studentRepository.findAll(pageRequest)).thenReturn(expectedPage);

        Page<Student> actualPage = studentService.findAllStudents(0, true);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testFindStudentById() {
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        Optional<Student> actualStudent = studentService.findStudentById(studentId);

        assertTrue(actualStudent.isPresent());
        assertEquals(studentId, actualStudent.get().getId());
    }

    @Test
    void testCreateStudent() {
        Student student = new Student();
        student.setFirstName("John");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Student createdStudent = studentService.createStudent(student);

        assertNotNull(createdStudent);
        assertEquals("John", createdStudent.getFirstName());
    }

    @Test
    void testUpdateStudent() {
        Long studentId = 1L;
        Student existingStudent = new Student();
        existingStudent.setId(studentId);
        existingStudent.setFirstName("John");

        Student updatedStudent = new Student();
        updatedStudent.setFirstName("Jane");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        Optional<Student> actualStudent = studentService.updateStudent(studentId, updatedStudent);

        assertTrue(actualStudent.isPresent());
        assertEquals("Jane", actualStudent.get().getFirstName());
    }

    @Test
    void testDeleteStudent() {
        Long studentId = 1L;
        studentService.deleteStudent(studentId);
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void testAssignStudentToGroup() {
        Long studentId = 1L;
        Long groupId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Group group = new Group();
        group.setId(groupId);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(studentRepository.save(student)).thenReturn(student);

        Optional<Student> updatedStudent = studentService.assignStudentToGroup(studentId, groupId);

        assertTrue(updatedStudent.isPresent());
        assertEquals(groupId, updatedStudent.get().getGroup().getId());
    }

    @Test
    void testRemoveStudentFromGroup() {
        Long studentId = 1L;
        Long groupId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Group group = new Group();
        group.setId(groupId);
        student.setGroup(group);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        studentService.removeStudentFromGroup(studentId, groupId);

        assertNull(student.getGroup());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testRemoveStudentFromGroupThrowsExceptionWhenStudentNotInGroup() {
        Long studentId = 1L;
        Long groupId = 1L;
        Student student = new Student();
        student.setId(studentId);
        Group group = new Group();
        group.setId(2L); // другая группа
        student.setGroup(group);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        assertThrows(IllegalArgumentException.class, () ->
                studentService.removeStudentFromGroup(studentId, groupId));
    }
}

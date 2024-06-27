package ru.java.kursach.tracking_attendance.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.java.kursach.tracking_attendance.model.*;
import ru.java.kursach.tracking_attendance.repository.*;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LessonServiceImplTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private LessonTypeRepository lessonTypeRepository;

    @InjectMocks
    private LessonServiceImpl lessonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllLessons() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("date"));
        Page<Lesson> expectedPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(lessonRepository.findAll(pageRequest)).thenReturn(expectedPage);

        Page<Lesson> actualPage = lessonService.findAllLessons(0, true);

        assertEquals(expectedPage, actualPage);
    }

    @Test
    void testFindLessonById() {
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);
        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));

        Optional<Lesson> actualLesson = lessonService.findLessonById(lessonId);

        assertTrue(actualLesson.isPresent());
        assertEquals(lessonId, actualLesson.get().getId());
    }

    @Test
    void testCreateLesson() {
        Lesson lesson = new Lesson();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Group group = new Group();
        group.setId(1L);
        LessonType lessonType = new LessonType();
        lessonType.setId(1L);
        lesson.setTeacher(teacher);
        lesson.setGroup(group);
        lesson.setLessonType(lessonType);

        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(lessonTypeRepository.findById(lessonType.getId())).thenReturn(Optional.of(lessonType));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson createdLesson = lessonService.createLesson(lesson);

        assertNotNull(createdLesson);
        assertEquals(teacher, createdLesson.getTeacher());
        assertEquals(group, createdLesson.getGroup());
        assertEquals(lessonType, createdLesson.getLessonType());
    }

    @Test
    void testCreateLessonTeacherNotFound() {
        Lesson lesson = new Lesson();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        lesson.setTeacher(teacher);

        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonService.createLesson(lesson));
    }

    @Test
    void testCreateLessonGroupNotFound() {
        Lesson lesson = new Lesson();
        Group group = new Group();
        group.setId(1L);
        lesson.setGroup(group);

        when(groupRepository.findById(group.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonService.createLesson(lesson));
    }
    @Test
    void testCreateLessonLessonTypeNotFound() {
        Lesson lesson = new Lesson();
        LessonType lessonType = new LessonType();
        lessonType.setId(1L);
        lesson.setLessonType(lessonType);

        when(lessonTypeRepository.findById(lessonType.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> lessonService.createLesson(lesson));
    }

    @Test
    void testUpdateLesson() {
        Long lessonId = 1L;
        Lesson existingLesson = new Lesson();
        existingLesson.setId(lessonId);
        Lesson updatedLesson = new Lesson();
        updatedLesson.setId(lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(existingLesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(updatedLesson);

        Optional<Lesson> actualLesson = lessonService.updateLesson(lessonId, updatedLesson);

        assertTrue(actualLesson.isPresent());
        assertEquals(lessonId, actualLesson.get().getId());
    }

    @Test
    void testUpdateLessonNotFound() {
        Long lessonId = 1L;
        Lesson updatedLesson = new Lesson();
        updatedLesson.setId(lessonId);

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        Optional<Lesson> actualLesson = lessonService.updateLesson(lessonId, updatedLesson);

        assertTrue(actualLesson.isEmpty());
    }

    @Test
    void testDeleteLesson() {
        Long lessonId = 1L;

        lessonService.deleteLesson(lessonId);

        verify(lessonRepository, times(1)).deleteById(lessonId);
    }
}

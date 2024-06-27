package ru.java.kursach.tracking_attendance.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.java.kursach.tracking_attendance.model.LessonType;
import ru.java.kursach.tracking_attendance.repository.LessonTypeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonTypeServiceImplTest {

    @Mock
    private LessonTypeRepository lessonTypeRepository;

    @InjectMocks
    private LessonTypeServiceImpl lessonTypeService;

    @Test
    void testFindAllLessonTypes() {
        // Arrange
        List<LessonType> expectedLessonTypes = new ArrayList<>();
        when(lessonTypeRepository.findAll()).thenReturn(expectedLessonTypes);

        // Act
        List<LessonType> actualLessonTypes = lessonTypeService.findAllLessonTypes();

        // Assert
        assertEquals(expectedLessonTypes, actualLessonTypes);
        verify(lessonTypeRepository, times(1)).findAll();
    }

    @Test
    void testFindLessonTypeById() {
        // Arrange
        Long id = 1L;
        LessonType expectedLessonType = new LessonType();
        when(lessonTypeRepository.findById(id)).thenReturn(Optional.of(expectedLessonType));

        // Act
        Optional<LessonType> actualLessonType = lessonTypeService.findLessonTypeById(id);

        // Assert
        assertEquals(Optional.of(expectedLessonType), actualLessonType);
        verify(lessonTypeRepository, times(1)).findById(id);
    }

    @Test
    void testCreateLessonType() {
        // Arrange
        LessonType lessonType = new LessonType();
        when(lessonTypeRepository.save(lessonType)).thenReturn(lessonType);

        // Act
        LessonType createdLessonType = lessonTypeService.createLessonType(lessonType);

        // Assert
        assertEquals(lessonType, createdLessonType);
        verify(lessonTypeRepository, times(1)).save(lessonType);
    }

    @Test
    void testUpdateLessonType() {
        // Arrange
        Long id = 1L;
        LessonType updatedLessonType = new LessonType();
        LessonType existingLessonType = new LessonType();
        when(lessonTypeRepository.findById(id)).thenReturn(Optional.of(existingLessonType));
        when(lessonTypeRepository.save(existingLessonType)).thenReturn(existingLessonType);

        // Act
        Optional<LessonType> actualLessonType = lessonTypeService.updateLessonType(id, updatedLessonType);

        // Assert
        assertEquals(Optional.of(existingLessonType), actualLessonType);
        assertEquals(updatedLessonType.getName(), existingLessonType.getName());
        verify(lessonTypeRepository, times(1)).findById(id);
        verify(lessonTypeRepository, times(1)).save(existingLessonType);
    }

    @Test
    void testDeleteLessonType() {
        // Arrange
        Long id = 1L;

        // Act
        lessonTypeService.deleteLessonType(id);

        // Assert
        verify(lessonTypeRepository, times(1)).deleteById(id);
    }
}

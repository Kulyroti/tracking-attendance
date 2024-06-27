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
import ru.java.kursach.tracking_attendance.model.Discipline;
import ru.java.kursach.tracking_attendance.repository.DisciplineRepository;
import ru.java.kursach.tracking_attendance.service.DisciplineServiceImpl;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DisciplineServiceImplTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineServiceImpl disciplineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllDisciplines() {
        // Arrange
        int page = 0;
        boolean sortByName = true;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by("name"));
        Page<Discipline> expectedPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(disciplineRepository.findAll(pageRequest)).thenReturn(expectedPage);

        // Act
        Page<Discipline> actualPage = disciplineService.findAllDisciplines(page, sortByName);

        // Assert
        assertEquals(expectedPage, actualPage);
    }

    @Test
    void findDisciplineById() {
        // Arrange
        Long id = 1L;
        Discipline discipline = new Discipline();
        discipline.setId(id);
        when(disciplineRepository.findById(id)).thenReturn(Optional.of(discipline));

        // Act
        Optional<Discipline> result = disciplineService.findDisciplineById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(discipline, result.get());
    }

    @Test
    void createDiscipline() {
        // Arrange
        Discipline discipline = new Discipline();
        discipline.setName("Test Discipline");
        when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);

        // Act
        Discipline createdDiscipline = disciplineService.createDiscipline(discipline);

        // Assert
        assertNotNull(createdDiscipline);
        assertEquals("Test Discipline", createdDiscipline.getName());
    }

    @Test
    void updateDiscipline() {
        // Arrange
        Long id = 1L;
        Discipline existingDiscipline = new Discipline();
        existingDiscipline.setId(id);
        existingDiscipline.setName("Existing Discipline");

        Discipline updatedDiscipline = new Discipline();
        updatedDiscipline.setName("Updated Discipline");

        when(disciplineRepository.findById(id)).thenReturn(Optional.of(existingDiscipline));
        when(disciplineRepository.save(any(Discipline.class))).thenReturn(updatedDiscipline);

        // Act
        Optional<Discipline> result = disciplineService.updateDiscipline(id, updatedDiscipline);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Updated Discipline", result.get().getName());
    }

    @Test
    void deleteDiscipline() {
        // Arrange
        Long id = 1L;

        // Act
        disciplineService.deleteDiscipline(id);

        // Assert
        verify(disciplineRepository, times(1)).deleteById(id);
    }
}

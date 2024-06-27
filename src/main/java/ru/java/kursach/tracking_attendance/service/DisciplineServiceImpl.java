package ru.java.kursach.tracking_attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.kursach.tracking_attendance.model.Discipline;
import ru.java.kursach.tracking_attendance.repository.DisciplineRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService{
    private final DisciplineRepository disciplineRepository;

    @Override
    public Page<Discipline> findAllDisciplines(int page, boolean sortByName) {
        int size = 5;
        return disciplineRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    @Override
    public Optional<Discipline> findDisciplineById(Long id) {
        return disciplineRepository.findById(id);
    }

    @Transactional
    @Override
    public Discipline createDiscipline(Discipline discipline) {
        return disciplineRepository.save(discipline);
    }

    @Transactional
    @Override
    public Optional<Discipline> updateDiscipline(Long id, Discipline updatedDiscipline) {
        return disciplineRepository.findById(id)
                .map(discipline -> {
                    discipline.setName(updatedDiscipline.getName());
                    return disciplineRepository.save(discipline);
                });
    }

    @Transactional
    @Override
    public void deleteDiscipline(Long id) {
        disciplineRepository.deleteById(id);
    }
}

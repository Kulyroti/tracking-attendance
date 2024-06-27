package ru.java.kursach.tracking_attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.kursach.tracking_attendance.model.LessonType;
import ru.java.kursach.tracking_attendance.repository.LessonTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LessonTypeServiceImpl implements LessonTypeService{
    private final LessonTypeRepository lessonTypeRepository;

    @Override
    public List<LessonType> findAllLessonTypes() {
        return lessonTypeRepository.findAll();
    }

    @Override
    public Optional<LessonType> findLessonTypeById(Long id) {
        return lessonTypeRepository.findById(id);
    }

    @Transactional
    @Override
    public LessonType createLessonType(LessonType lessonType) {
        return lessonTypeRepository.save(lessonType);
    }

    @Transactional
    @Override
    public Optional<LessonType> updateLessonType(Long id, LessonType updatedLessonType) {
        return lessonTypeRepository.findById(id)
                .map(lessonType -> {
                    lessonType.setName(updatedLessonType.getName());
                    return lessonTypeRepository.save(lessonType);
                });
    }

    @Transactional
    @Override
    public void deleteLessonType(Long id) {
        lessonTypeRepository.deleteById(id);
    }
}

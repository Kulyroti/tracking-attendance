package ru.java.kursach.tracking_attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.java.kursach.tracking_attendance.model.Group;
import ru.java.kursach.tracking_attendance.repository.GroupRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
    private final GroupRepository groupRepository;

    @Override
    public Page<Group> findAllGroups(int page, boolean sortByName) {
        int size = 10;
        return groupRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    @Override
    public Optional<Group> findGroupById(Long id) {
        return groupRepository.findById(id);
    }

    @Transactional
    @Override
    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    @Transactional
    @Override
    public Optional<Group> updateGroup(Long id, Group updatedGroup) {
        return groupRepository.findById(id)
                .map(group -> {
                    group.setName(updatedGroup.getName());
                    group.setYear(updatedGroup.getYear());
                    return groupRepository.save(group);
                });
    }

    @Transactional
    @Override
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}

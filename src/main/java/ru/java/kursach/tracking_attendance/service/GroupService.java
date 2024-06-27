package ru.java.kursach.tracking_attendance.service;

import org.springframework.data.domain.Page;
import ru.java.kursach.tracking_attendance.model.Group;

import java.util.Optional;

public interface GroupService {
    Page<Group> findAllGroups(int page, boolean sortByName);

    Optional<Group> findGroupById(Long id);

    Group createGroup(Group group);

    Optional<Group> updateGroup(Long id, Group updatedGroup);

    void deleteGroup(Long id);
}

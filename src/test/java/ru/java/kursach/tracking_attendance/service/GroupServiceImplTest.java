package ru.java.kursach.tracking_attendance.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.java.kursach.tracking_attendance.model.Group;
import ru.java.kursach.tracking_attendance.repository.GroupRepository;

import java.util.Collections;
import java.util.Optional;

public class GroupServiceImplTest {

    @Mock
    private GroupRepository groupRepository;

    private GroupServiceImpl groupService;

    @BeforeEach
    public void setUp() {
        groupService = new GroupServiceImpl(groupRepository);
    }

    @Test
    public void testFindAllGroups() {
        int page = 0;
        boolean sortByName = true;
        Sort sort = Sort.by("name");

        when(groupRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<Group> result = groupService.findAllGroups(page, sortByName);

        verify(groupRepository).findAll(PageRequest.of(page, 10, sort));
        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void testFindGroupById() {
        long id = 1L;
        Group group = new Group(1L, "Group", 2022);
        Optional<Group> optionalGroup = Optional.of(group);

        when(groupRepository.findById(id)).thenReturn(optionalGroup);

        Optional<Group> result = groupService.findGroupById(id);

        verify(groupRepository).findById(id);
        assertTrue(result.isPresent());
        assertEquals(group, result.get());
    }

    @Test
    public void testCreateGroup() {
        Group group = new Group(1L, "Group", 2022);

        when(groupRepository.save(group)).thenReturn(group);

        Group result = groupService.createGroup(group);

        verify(groupRepository).save(group);
        assertEquals(group, result);
    }

    @Test
    public void testUpdateGroup() {
        long id = 1L;
        Group existingGroup = new Group(1L, "Group", 2022);
        Group updatedGroup = new Group(1L, "Updated Group", 2023);

        when(groupRepository.findById(id)).thenReturn(Optional.of(existingGroup));
        when(groupRepository.save(existingGroup)).thenReturn(updatedGroup);

        Optional<Group> result = groupService.updateGroup(id, updatedGroup);

        verify(groupRepository).findById(id);
        verify(groupRepository).save(existingGroup);
        assertTrue(result.isPresent());
        assertEquals(updatedGroup, result.get());
    }

    @Test
    public void testDeleteGroup() {
        long id = 1L;

        groupService.deleteGroup(id);

        verify(groupRepository).deleteById(id);
    }
}

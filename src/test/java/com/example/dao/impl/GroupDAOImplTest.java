package com.example.dao.impl;

import com.example.dao.impl.mapper.GroupMapper;
import com.example.dao.impl.mapper.GroupStudentCountMapper;
import com.example.model.Group;
import com.example.model.dto.GroupStudentCount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GroupMapper.class, GroupStudentCountMapper.class, GroupDAOImpl.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/sql/clean_data.sql", "/sql/insert_test_data.sql"})
public class GroupDAOImplTest {
    @Autowired
    private GroupDAOImpl groupDAO;

    @Test
    void saveAll_returnTrue_whenWeAddList() {
        List<Group> groups = new ArrayList<>();
        groups.add(new Group("test"));
        groups.add(new Group("test2"));
        List<Group> saved = groupDAO.saveAll(groups);
        assertEquals(groups.size(), saved.size());
        for (int i = 0; i < groups.size(); i++) {
            Group expected = groups.get(i);
            Group actual = saved.get(i);
            expected.setId(actual.getId());
            assertEquals(expected, actual);
        }
    }

    @Test
    void saveAll_returnTrue_whenWeAddEmptyList() {
        List<Group> groups = new ArrayList<>();
        assertTrue(groupDAO.saveAll(groups).isEmpty());
    }

    @Test
    void saveAll_returnFalse_whenWeAddEmptyList() {
        Throwable exception = assertThrows(NullPointerException.class, () -> groupDAO.saveAll(null));
        assertEquals("Cannot invoke \"java.util.List.size()\" because \"entities\" is null", exception.getMessage());
    }

    @Test
    void getGroupsByStudentCount_returnList_whenSearchWithRealId() {
        List<GroupStudentCount> actual = groupDAO.getGroupsByStudentCount(1000);
        assertFalse(actual.isEmpty());
    }

    @Test
    void getGroupsByStudentCount_returnEmptyList_whenSearchWithNotRealId() {
        List<GroupStudentCount> actual = groupDAO.getGroupsByStudentCount(2);
        System.out.println(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_returnGroup_whenSearchWithRealId() {
        Group expected = new Group("1");
        Optional<Group> actual = groupDAO.findById(1000);
        assertTrue(actual.isPresent());
        expected.setId(actual.get().getId());
        assertEquals(expected, actual.get());
    }

    @Test
    void findById_returnNull_whenSearchWithNotRealId() {
        Optional<Group> actual = groupDAO.findById(999);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_returnList() {
        List<Group> actual = groupDAO.findAll();
        assertFalse(actual.isEmpty());
    }

    @Test
    void deleteById_returnTrue_whenSearchWithRealId() {
        assertTrue(groupDAO.findById(4000).isPresent());
        groupDAO.deleteById(4000);
        assertFalse(groupDAO.findById(4000).isPresent());
    }

    @Test
    void save_returnObject_whenInputIsNotGroupId() {
        Group expected = new Group("test");
        Group actual = groupDAO.save(expected);
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    void save_returnUpdatedObject_whenInputIsNotGroupId() {
        Optional<Group> expected = groupDAO.findById(1000);
        Group actual = null;
        if (expected.isPresent()) {
            expected.get().setName("New");
            actual = groupDAO.save(expected.get());
            expected.get().setId(actual.getId());
        }
        assertEquals(expected.get(), actual);
    }
}

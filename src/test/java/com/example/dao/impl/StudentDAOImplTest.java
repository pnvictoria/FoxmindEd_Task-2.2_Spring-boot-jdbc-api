package com.example.dao.impl;

import com.example.dao.impl.mapper.CourseMapper;
import com.example.dao.impl.mapper.StudentMapper;
import com.example.model.Group;
import com.example.model.Student;
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

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {StudentMapper.class, StudentDAOImpl.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/sql/clean_data.sql", "/sql/insert_test_data.sql"})
public class StudentDAOImplTest {
    @Autowired
    private StudentDAOImpl studentDAO;

    @Test
    void saveAll_returnTrue_whenWeAddList() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1000, "Test", "Test"));
        students.add(new Student(1000, "Test2", "Test2"));
        List<Student> saved = studentDAO.saveAll(students);
        assertEquals(students.size(), saved.size());
        for (int i = 0; i < students.size(); i++) {
            Student expected = students.get(i);
            Student actual = saved.get(i);
            expected.setId(actual.getId());
            assertEquals(expected, actual);
        }
    }

    @Test
    void saveAll_returnTrue_whenWeAddEmptyList() {
        List<Student> courses = new ArrayList<>();
        assertTrue(studentDAO.saveAll(courses).isEmpty());
    }

    @Test
    void saveAll_returnFalse_whenWeAddEmptyList() {
        Throwable exception = assertThrows(NullPointerException.class, () -> studentDAO.saveAll(null));
        assertEquals("Cannot invoke \"java.util.List.size()\" because \"entities\" is null", exception.getMessage());
    }

    @Test
    void getStudentsByCourseName_returnList_whenSearchWithRealId() {
        List<Student> actual = studentDAO.getStudentsByCourseName("Math");
        assertFalse(actual.isEmpty());
    }

    @Test
    void getStudentsByCourseName_returnEmptyList_whenSearchWithNotRealId() {
        List<Student> actual = studentDAO.getStudentsByCourseName("999");
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_returnCourse_whenSearchWithRealId() {
        Student expected = new Student("Name1.0", "Surname1.0");
        Optional<Student> actual = studentDAO.findById(1000);
        assertTrue(actual.isPresent());
        expected.setId(actual.get().getId());
        expected.setGroupId(actual.get().getGroupId());
        assertEquals(expected, actual.get());
    }

    @Test
    void findById_returnNull_whenSearchWithNotRealId() {
        Optional<Student> actual = studentDAO.findById(999);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_returnList() {
        List<Student> actual = studentDAO.findAll();
        assertFalse(actual.isEmpty());
    }

    @Test
    void deleteById_returnFalse_whenSearchWithNotRealId() {
        assertTrue(studentDAO.findById(4000).isPresent());
        studentDAO.deleteById(4000);
        assertFalse(studentDAO.findById(4000).isPresent());
    }

    @Test
    void save_returnObject_whenInputIsNotStudentId() {
        Student expected = new Student(1000, "test", "test");
        Student actual = studentDAO.save(expected);
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    void save_returnUpdatedObject_whenInputIsNotStudentId() {
        Optional<Student> expected = studentDAO.findById(1000);
        Student actual = null;
        if (expected.isPresent()) {
            expected.get().setFirstName("NewName");
            actual = studentDAO.save(expected.get());
            expected.get().setId(actual.getId());
        }
        assertEquals(expected.get(), actual);
    }
}


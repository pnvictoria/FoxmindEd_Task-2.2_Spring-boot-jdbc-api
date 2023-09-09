package com.example.dao.impl;

import com.example.dao.impl.mapper.CourseMapper;
import com.example.model.Course;
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

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {CourseMapper.class, CourseDAOImpl.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql({"/sql/clean_data.sql", "/sql/insert_test_data.sql"})
public class CourseDAOImplTest {
    @Autowired
    private CourseDAOImpl courseDAO;

    @Test
    void saveAll_returnTrue_whenWeAddList() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("test","test desc", null));
        courses.add(new Course("test2","test2 desc", null));
        List<Course> saved = courseDAO.saveAll(courses);
        assertEquals(courses.size(), saved.size());
        for (int i = 0; i < courses.size(); i++) {
            Course expected = courses.get(i);
            Course actual = saved.get(i);
            expected.setId(actual.getId());
            assertEquals(expected, actual);
        }
    }

    @Test
    void saveAll_returnTrue_whenWeAddEmptyList() {
        List<Course> courses = new ArrayList<>();
        assertTrue(courseDAO.saveAll(courses).isEmpty());
    }

    @Test
    void saveAll_returnFalse_whenWeAddEmptyList() {
        Throwable exception = assertThrows(NullPointerException.class, () -> courseDAO.saveAll(null));
        assertEquals("Cannot invoke \"java.util.List.size()\" because \"entities\" is null", exception.getMessage());
    }

    @Test
    void getCoursesByStudentId_returnList_whenSearchWithRealId() {
        List<Course> actual = courseDAO.getCoursesByStudentId(1000);
        assertFalse(actual.isEmpty());
    }

    @Test
    void getCoursesByStudentId_returnEmptyList_whenSearchWithNotRealId() {
        List<Course> actual = courseDAO.getCoursesByStudentId(999);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findById_returnCourse_whenSearchWithRealId() {
        Course expected = new Course(1000, "Math", "Desc math");
        Optional<Course> actual = courseDAO.findById(1000);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void findById_returnNull_whenSearchWithNotRealId() {
        Optional<Course> actual = courseDAO.findById(999);
        assertTrue(actual.isEmpty());
    }

    @Test
    void findAll_returnList() {
        List<Course> actual = courseDAO.findAll();
        assertFalse(actual.isEmpty());
    }

    @Test
    void deleteById_returnTrue_whenSearchWithRealId() {
        assertTrue(courseDAO.findById(4000).isPresent());
        courseDAO.deleteById(4000);
        assertFalse(courseDAO.findById(4000).isPresent());
    }

    @Test
    void save_returnObject_whenInputIsNotStudentId() {
        Course expected = new Course("test","test desc", null);
        Course actual = courseDAO.save(expected);
        expected.setId(actual.getId());
        assertEquals(expected, actual);
    }

    @Test
    void save_returnUpdatedObject_whenInputIsNotStudentId() {
        Optional<Course> course = courseDAO.findById(1000);
        Course actual = null;
        Course expecteds = null;
        if (course.isPresent()) {
            expecteds = course.get();
            course.get().setName("NewName");
            actual = courseDAO.save(course.get());
            course.get().setId(actual.getId());
        }
        assertEquals(expecteds, actual);
    }
}

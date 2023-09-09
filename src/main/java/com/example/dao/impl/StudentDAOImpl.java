package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.dao.impl.mapper.StudentMapper;
import com.example.exception.SchoolDAOException;
import com.example.model.Course;
import com.example.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.constant.QueryConstants.*;

@Component
public class StudentDAOImpl implements StudentDAO {
    private final Logger LOGGER = LoggerFactory.getLogger(StudentDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final StudentMapper mapper;

    public StudentDAOImpl(JdbcTemplate jdbcTemplate, StudentMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<Student> findById(Integer id) throws SchoolDAOException {
        try {
            return Optional.of(jdbcTemplate.queryForObject(STUDENT_GET_OBJECT_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("Search return null : {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Student> findAll() throws SchoolDAOException {
        return jdbcTemplate.query(STUDENT_GET_ALL_OBJECTS, mapper);
    }

    @Override
    public void deleteById(Integer id) throws SchoolDAOException {
        LOGGER.info("Student ID: {}", id);
        jdbcTemplate.query(STUDENT_REMOVE_OBJECT, mapper, id);
    }

    @Override
    public List<Student> saveAll(List<Student> entities) throws SchoolDAOException {
        LOGGER.info("Save list with students count: {}", entities.size());
        return entities.stream().map(this::save).toList();
    }

    @Override
    public List<Student> getStudentsByCourseName(String courseName) throws SchoolDAOException {
        LOGGER.info("courseName: {}", courseName);
        return jdbcTemplate.query(STUDENT_BY_COURSE_NAME, mapper, courseName);
    }

    @Override
    public void assignStudentsToCourse(int studentId, int courseId) throws SchoolDAOException {
        LOGGER.info("studentId: {}, courseId: {}", studentId, courseId);
        jdbcTemplate.update(STUDENT_ASSIGN_TO_COURSE, mapper, studentId, courseId);
    }

    @Override
    public void deleteStudentFromCourse(int studentId, int courseId) throws SchoolDAOException {
        LOGGER.info("studentId: {}, courseId: {}", studentId, courseId);
        jdbcTemplate.query(STUDENT_DELETE_FROM_COURSE, mapper, studentId, courseId);
    }

    @Override
    public void assignStudentsToCourse(List<Student> students) throws SchoolDAOException {
        LOGGER.info("Count of students assigned to course: {}", students.size());
        for (Student student : students) {
            for (Course course : student.getCourses()) {
                LOGGER.info("student.getId(): {}", student.getId());
                LOGGER.info("course.getId(): {}", course.getId());
                jdbcTemplate.query(STUDENT_ASSIGN_TO_COURSE, mapper, student.getId(), course.getId());
            }
        }
    }

    @Override
    public Student create(Student entity) throws SchoolDAOException {
        LOGGER.info("Student: {}", entity);
        return jdbcTemplate.queryForObject(STUDENT_ADD_OBJECT, mapper, entity.getGroupId(), entity.getFirstName(), entity.getLastName());
    }

    @Override
    public Student update(Student entity) throws SchoolDAOException {
        LOGGER.info("Student: {}", entity);
        return jdbcTemplate.queryForObject(STUDENT_UPDATE_OBJECT, mapper, entity.getGroupId(), entity.getFirstName(), entity.getLastName(), entity.getId());
    }
}

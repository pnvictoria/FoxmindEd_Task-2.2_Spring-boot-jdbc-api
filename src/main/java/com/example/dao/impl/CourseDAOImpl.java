package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.dao.impl.mapper.CourseMapper;
import com.example.exception.SchoolDAOException;
import com.example.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.constant.QueryConstants.*;

@Component
public class CourseDAOImpl implements CourseDAO {
    private final Logger LOGGER = LoggerFactory.getLogger(CourseDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final CourseMapper mapper;

    public CourseDAOImpl(JdbcTemplate jdbcTemplate, CourseMapper mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Course> saveAll(List<Course> entities) throws SchoolDAOException {
        LOGGER.info("Save list with courses count: {}", entities.size());
        return entities.stream().map(this::save).toList();
    }

    @Override
    public List<Course> getCoursesByStudentId(int id) throws SchoolDAOException {
        LOGGER.info("Course ID: {}", id);
        return jdbcTemplate.query(COURSE_GET_BY_STUDENT_ID, mapper, id);
    }

    @Override
    public Optional<Course> findById(Integer id) throws SchoolDAOException {
        try {
            return Optional.of(jdbcTemplate.queryForObject(COURSE_GET_OBJECT_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("Search return null : {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAll() throws SchoolDAOException {
        return jdbcTemplate.query(COURSE_GET_ALL_OBJECTS, mapper);
    }

    @Override
    public void deleteById(Integer id) throws SchoolDAOException {
        LOGGER.info("Course ID: {}", id);
        jdbcTemplate.query(COURSE_REMOVE_OBJECT, mapper, id);
    }

    @Override
    public Course create(Course entity) throws SchoolDAOException {
        LOGGER.info("Course: {}", entity);
        return jdbcTemplate.queryForObject(COURSE_ADD_OBJECT, mapper, entity.getName(), entity.getDescription());
    }

    @Override
    public Course update(Course entity) throws SchoolDAOException {
        LOGGER.info("Course: {}", entity);
        return jdbcTemplate.queryForObject(COURSE_UPDATE_OBJECT, mapper, entity.getName(), entity.getDescription(), entity.getId());
    }
}

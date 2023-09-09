package com.example.dao.impl;

import com.example.dao.GroupDAO;
import com.example.dao.impl.mapper.GroupMapper;
import com.example.dao.impl.mapper.GroupStudentCountMapper;
import com.example.exception.SchoolDAOException;
import com.example.model.Group;
import com.example.model.dto.GroupStudentCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.constant.QueryConstants.*;

@Component
public class GroupDAOImpl implements GroupDAO {
    private final Logger LOGGER = LoggerFactory.getLogger(GroupDAOImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final GroupMapper mapper;
    private final GroupStudentCountMapper groupStudentCountMapper;

    public GroupDAOImpl(JdbcTemplate jdbcTemplate, GroupMapper mapper, GroupStudentCountMapper groupStudentCountMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.groupStudentCountMapper = groupStudentCountMapper;
    }

    @Override
    public Optional<Group> findById(Integer id) throws SchoolDAOException {
        try {
            return Optional.of(jdbcTemplate.queryForObject(GROUP_GET_OBJECT_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.error("Search return null : {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Group> findAll() throws SchoolDAOException {
        return jdbcTemplate.query(GROUP_GET_ALL_OBJECTS, mapper);
    }

    @Override
    public void deleteById(Integer id) throws SchoolDAOException {
        LOGGER.info("Group ID: {}", id);
        jdbcTemplate.query(GROUP_REMOVE_OBJECT, mapper, id);
    }

    @Override
    public List<Group> saveAll(List<Group> entities) throws SchoolDAOException {
        LOGGER.info("Save list with groups count: {}", entities.size());
        return entities.stream().map(this::save).toList();
    }

    @Override
    public List<GroupStudentCount> getGroupsByStudentCount(int count) throws SchoolDAOException {
        LOGGER.info("Student count: {}", count);
        return jdbcTemplate.query(GROUP_GET_BY_STUDENT_COUNT, groupStudentCountMapper, count);
    }

    @Override
    public Group create(Group entity) throws SchoolDAOException {
        LOGGER.info("Group: {}", entity);
        return jdbcTemplate.queryForObject(GROUP_ADD_OBJECT, mapper, entity.getName());
    }

    @Override
    public Group update(Group entity) throws SchoolDAOException {
        LOGGER.info("Group: {}", entity);
        return jdbcTemplate.queryForObject(GROUP_UPDATE_OBJECT, mapper, entity.getName(), entity.getId());
    }
}

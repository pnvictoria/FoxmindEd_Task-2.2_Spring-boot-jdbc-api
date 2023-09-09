package com.example.dao.impl.mapper;

import com.example.model.dto.GroupStudentCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupStudentCountMapper implements RowMapper<GroupStudentCount> {
    private final GroupMapper groupMapper;

    @Autowired
    public GroupStudentCountMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Override
    public GroupStudentCount mapRow(ResultSet rs, int rowNum) throws SQLException {
        GroupStudentCount groupStudentCount = new GroupStudentCount();
        groupStudentCount.setGroup(groupMapper.mapRow(rs, rowNum));
        groupStudentCount.setCount(rs.getInt("students"));
        return groupStudentCount;
    }
}
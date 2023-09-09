package com.example.dao;

import com.example.dao.impl.AbstractCrudDAOImpl;
import com.example.exception.SchoolDAOException;
import com.example.model.Group;
import com.example.model.dto.GroupStudentCount;

import java.util.List;

public interface GroupDAO extends AbstractCrudDAOImpl<Group, Integer> {
    List<GroupStudentCount> getGroupsByStudentCount(int count) throws SchoolDAOException;
}

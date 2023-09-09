package com.example.dao;

import com.example.dao.impl.AbstractCrudDAOImpl;
import com.example.exception.SchoolDAOException;
import com.example.model.Course;

import java.util.List;

public interface CourseDAO extends AbstractCrudDAOImpl<Course, Integer> {
    List<Course> getCoursesByStudentId(int id) throws SchoolDAOException;
}

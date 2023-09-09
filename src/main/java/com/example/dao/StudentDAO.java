package com.example.dao;

import com.example.dao.impl.AbstractCrudDAOImpl;
import com.example.exception.SchoolDAOException;
import com.example.model.Student;

import java.util.List;

public interface StudentDAO extends AbstractCrudDAOImpl<Student, Integer> {
    List<Student> getStudentsByCourseName(String courseName) throws SchoolDAOException;

    void assignStudentsToCourse(int studentId, int courseId) throws SchoolDAOException;

    void deleteStudentFromCourse(int studentId, int courseId) throws SchoolDAOException;

    void assignStudentsToCourse(List<Student> students) throws SchoolDAOException;
}

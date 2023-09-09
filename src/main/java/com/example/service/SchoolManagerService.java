package com.example.service;

import com.example.dao.CourseDAO;
import com.example.dao.GroupDAO;
import com.example.dao.StudentDAO;
import com.example.exception.SchoolDAOException;
import com.example.model.Course;
import com.example.model.Group;
import com.example.model.Student;
import com.example.model.dto.GroupStudentCount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class SchoolManagerService {
    private final Logger LOGGER = LoggerFactory.getLogger(SchoolManagerService.class);

    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public SchoolManagerService(GroupDAO groupDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    @Transactional
    public List<Group> createGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            LOGGER.error("{} cannot be null", groups);
            throw new NullPointerException("List cannot be null");
        }
        for (Group group : groups) {
            if (group.getName().length() > 8) {
                LOGGER.error("{} cannot be bigger than 8", group.getName());
                throw new RuntimeException("Incorrect group name.");
            }
        }
        LOGGER.info("List of groups: {}", groups);
        return groupDAO.saveAll(groups);
    }

    @Transactional
    public List<GroupStudentCount> getGroupsByStudentCount(int studentCount) {
        if (studentCount < 0) {
            LOGGER.error("{} cannot be negative", studentCount);
            throw new RuntimeException("StudentCount cannot be negative");
        }
        LOGGER.info("studentCount: {}", studentCount);
        return groupDAO.getGroupsByStudentCount(studentCount);
    }

    @Transactional
    public List<Group> getGroups() {
        return groupDAO.findAll();
    }

    @Transactional
    public List<Course> createCourses(List<Course> courses) {
        if (courses.isEmpty()) {
            LOGGER.error("{} cannot be null", courses);
            throw new NullPointerException("List cannot be null");
        }
        for (Course course : courses) {
            if (course.getName().length() > 20) {
                LOGGER.error("{} cannot be bigger than 20", course.getName());
                throw new RuntimeException("Incorrect course name.");
            }
        }
        LOGGER.info("List of courses: {}", courses);
        return courseDAO.saveAll(courses);
    }

    @Transactional
    public List<Course> getCourses() {
        return courseDAO.findAll();
    }

    @Transactional
    public List<Course> getCoursesByStudentId(int studentId) {
        if (studentId < 0) {
            throw new RuntimeException("StudentId cannot be negative");
        }
        LOGGER.info("studentId: {}", studentId);
        return courseDAO.getCoursesByStudentId(studentId);
    }

    @Transactional
    public List<Student> createStudents(List<Student> students) {
        if (students.isEmpty()) {
            LOGGER.error("{} cannot be null", students);
            throw new NullPointerException("List cannot be null");
        }
        for (Student student : students) {
            checkStudent(student);
        }
        LOGGER.info("List of students: {}", students);
        return studentDAO.saveAll(students);
    }

    @Transactional
    public List<Student> getStudentsByCourseName(String courseName) {
        if (courseName.isEmpty()) {
            LOGGER.error("{} cannot be null", courseName);
            throw new NullPointerException("CourseName cannot be null");
        }
        LOGGER.info("courseName: {}", courseName);
        return studentDAO.getStudentsByCourseName(courseName);
    }

    @Transactional
    public void assignStudentsToCourse(int studentId, int courseId) {
        checkStudentIdAndCourseId(studentId, courseId);
        LOGGER.info("studentId: {}, courseId: {}", studentId, courseId);
        studentDAO.assignStudentsToCourse(studentId, courseId);
    }

    @Transactional
    public void assignStudentsToCourse(List<Student> students) {
        if (students.isEmpty()) {
            LOGGER.error("{} cannot be null", students);
            throw new NullPointerException("List cannot be null");
        }
        for (Student student : students) {
            checkStudent(student);
        }
        LOGGER.info("List of students: {}", students);
        studentDAO.assignStudentsToCourse(students);
    }

    @Transactional
    public void deleteStudentFromCourse(int studentId, int courseId) {
        checkStudentIdAndCourseId(studentId, courseId);
        LOGGER.info("studentId: {}, courseId: {}", studentId, courseId);
        studentDAO.deleteStudentFromCourse(studentId, courseId);
    }

    @Transactional
    public List<Student> getStudents() {
        return studentDAO.findAll();
    }

    @Transactional
    public boolean addNewStudent(Student student) {
        checkStudent(student);
        LOGGER.info("Student: {}", student);
        return studentDAO.create(student) != null;
    }

    @Transactional
    public boolean deleteStudent(int studentId) {
        LOGGER.info("Student is present: {}", studentDAO.findById(studentId).isPresent());
        if (studentDAO.findById(studentId).isPresent()) {
            studentDAO.deleteById(studentId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean addStudentToCourse(int studentId, int courseId) {
        checkStudentIdAndCourseId(studentId, courseId);
        List<Course> studentCourses = getCoursesByStudentId(studentId);
        LOGGER.info("Course is present: {}", getCourseIdList(studentCourses).contains(courseId));
        if (!getCourseIdList(studentCourses).contains(courseId)) {
            assignStudentsToCourse(studentId, courseId);
            return true;
        } else {
            LOGGER.error("Student {} already on this course: {}", studentId, courseId);
            throw new SchoolDAOException("Student already on this course.");
        }
    }

    @Transactional
    public boolean removeStudentFromCourse(int studentId, int courseId) {
        checkStudentIdAndCourseId(studentId, courseId);
        List<Course> studentCourses = getCoursesByStudentId(studentId);
        LOGGER.info("Course is present: {}", getCourseIdList(studentCourses).contains(courseId));
        if (getCourseIdList(studentCourses).contains(courseId)) {
            deleteStudentFromCourse(studentId, courseId);
            return true;
        } else {
            LOGGER.error("Student {} does not have this course: {}", studentId, courseId);
            throw new SchoolDAOException("Student does not have this course.");
        }
    }

    private List<Integer> getCourseIdList(List<Course> course) {
        return course.stream().map(Course::getId).collect(Collectors.toList());
    }

    private void checkStudent(Student student) {
        if (student.getFirstName().length() > 20) {
            LOGGER.error("{} cannot be bigger than 20", student.getFirstName());
            throw new RuntimeException("FirstName cannot be bigger than 20");
        }
        if (student.getLastName().length() > 20) {
            LOGGER.error("{} cannot be bigger than 20", student.getLastName());
            throw new RuntimeException("LastName cannot be bigger than 20");
        }
        if (student.getGroupId() < 0) {
            LOGGER.error("{} cannot be bigger than 20", student.getGroupId());
            throw new RuntimeException("GroupId cannot be negative");
        }
    }

    private void checkStudentIdAndCourseId(int studentId, int courseId) {
        if (studentId < 0) {
            LOGGER.error("{} cannot be negative", studentId);
            throw new RuntimeException("StudentId cannot be negative");
        }
        if (courseId < 0) {
            LOGGER.error("{} cannot be negative", courseId);
            throw new RuntimeException("CourseId cannot be negative");
        }
    }
}


package com.example.service;

import com.example.dao.CourseDAO;
import com.example.dao.GroupDAO;
import com.example.dao.StudentDAO;
import com.example.exception.SchoolDAOException;
import com.example.model.Course;
import com.example.model.Group;
import com.example.model.Student;
import com.example.model.dto.GroupStudentCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {SchoolManagerService.class})
public class SchoolManagerServiceTest {
    @MockBean
    private GroupDAO groupDAO;
    @MockBean
    private StudentDAO studentDAO;
    @MockBean
    private CourseDAO courseDAO;
    @Autowired
    private SchoolManagerService service;

    private List<Group> groups;
    private List<Course> courses;
    private List<Student> students;

    @BeforeEach
    public void preInit() {
        groups = new ArrayList<>();
        groups.add(new Group("UT-62"));
        groups.add(new Group("ER-81"));
        groups.add(new Group("IN-36"));

        courses = new ArrayList<>();
        courses.add(new Course("Math", "Math desc", null));
        courses.add(new Course("Biology", "Biology desc", null));

        students = new ArrayList<>();
        students.add(new Student(1000, "Test", "Test"));
        students.add(new Student(1000, "Test", "Test2"));
    }

    @Test
    public void createGroups_returnListOfGroups_whenSentCorrectData() {
        when(groupDAO.saveAll(groups)).thenReturn(groups);
        List<Group> actual = service.createGroups(groups);
        assertEquals(groups, actual, "Should return list with three objects.");
        verify(groupDAO, times(1)).saveAll(groups);
    }

    @Test
    public void createGroups_returnError_whenSentNull() {
        assertThrows(NullPointerException.class, () -> service.createGroups(null), "List cannot be null");
    }

    @Test
    public void createGroups_returnError_whenGroupNameIncorrectData() {
        List<Group> group = new ArrayList<>();
        group.add(new Group("TestData-62"));
        assertThrows(RuntimeException.class, () -> service.createGroups(group), "Name cannot be bigger than 10");
    }

    @Test
    public void getGroups_returnListOfAllGroups() {
        when(groupDAO.findAll()).thenReturn(groups);
        List<Group> actual = service.getGroups();
        assertEquals(groups, actual, "Should return list with three objects.");
    }

    @Test
    public void getGroupsByStudentCount_returnList() {
        List<GroupStudentCount> expected = new ArrayList<>();
        expected.add(new GroupStudentCount(groups.get(0), 15));
        expected.add(new GroupStudentCount(groups.get(1), 15));

        when(groupDAO.getGroupsByStudentCount(15)).thenReturn(expected);
        List<GroupStudentCount> actual = service.getGroupsByStudentCount(15);
        assertEquals(expected, actual, "Should return list with two objects.");
    }

    @Test
    public void getGroupsByStudentCount_returnEmptyListOfGroups_whenSentNull() {
        List<GroupStudentCount> expected = new ArrayList<>();
        when(groupDAO.getGroupsByStudentCount(15)).thenReturn(expected);
        List<GroupStudentCount> actual = service.getGroupsByStudentCount(15);
        assertEquals(expected, actual, "Should return empty list.");
    }

    @Test
    public void getGroupsByStudentCount_returnError_whenSentNegativeStudentCount() {
        assertThrows(RuntimeException.class, () -> service.getGroupsByStudentCount(-15), "StudentCount cannot be negative");
    }

    @Test
    public void createCourses_returnListOfCourses() {
        when(courseDAO.saveAll(courses)).thenReturn(courses);
        List<Course> actual = service.createCourses(courses);
        assertEquals(courses, actual, "Should return list with three objects.");
        verify(courseDAO, times(1)).saveAll(courses);
    }

    @Test
    public void createCourses_returnError_whenSentNull() {
        assertThrows(NullPointerException.class, () -> service.createCourses(null), "List cannot be null");
    }

    @Test
    public void createCourses_returnError_whenSentIncorrectData() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("TestDataBiggerThen20Letters", "Test desc", null));
        assertThrows(RuntimeException.class, () -> service.createCourses(courses), "Name cannot be bigger than 20");
    }

    @Test
    public void getCourses_returnListOfAllCourses() {
        when(courseDAO.findAll()).thenReturn(courses);
        List<Course> actual = service.getCourses();
        assertEquals(courses, actual, "Should return list with three objects.");
    }

    @Test
    public void getCoursesByStudentId_returnListOfAllCoursesByStudentId() {
        when(courseDAO.getCoursesByStudentId(10)).thenReturn(courses);
        List<Course> actual = service.getCoursesByStudentId(10);
        assertEquals(courses, actual, "Should return list with three objects.");
    }

    @Test
    public void getCoursesByStudentId_returnError_whenSentNegativeStudentCount() {
        assertThrows(RuntimeException.class, () -> service.getGroupsByStudentCount(-15), "StudentId cannot be negative");
    }

    @Test
    public void createStudents_returnListOfStudents() {
        when(studentDAO.saveAll(students)).thenReturn(students);
        List<Student> actual = service.createStudents(students);
        assertEquals(students, actual, "Should return list with two objects.");
        verify(studentDAO, times(1)).saveAll(students);
    }

    @Test
    public void createStudents_returnError_whenSentNull() {
        assertThrows(NullPointerException.class, () -> service.createStudents(null), "List cannot be null");
    }

    @Test
    public void createStudents_returnError_whenSentIncorrectFirstNameData() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1000, "TestDataBiggerThen20Letters", "Test"));
        assertThrows(RuntimeException.class, () -> service.createStudents(students), "FirstName cannot be bigger than 20");
    }

    @Test
    public void createStudents_returnError_whenSentIncorrectLastNameData() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1000, "Test", "TestDataBiggerThen20Letters"));
        assertThrows(RuntimeException.class, () -> service.createStudents(students), "LastName cannot be bigger than 20");
    }

    @Test
    public void createStudents_returnError_whenSentIncorrectGroupIdData() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(-1000, "Test", "Test"));
        assertThrows(NullPointerException.class, () -> service.createStudents(null), "GroupId cannot be negative");
    }

    @Test
    public void getStudents_returnListOfAllStudents() {
        when(studentDAO.findAll()).thenReturn(students);
        List<Student> actual = service.getStudents();
        assertEquals(students, actual, "Should return list with two objects.");
    }

    @Test
    public void getStudentsByCourseName_returnListOfStudents() {
        when(studentDAO.getStudentsByCourseName("Test")).thenReturn(students);
        List<Student> actual = service.getStudentsByCourseName("Test");
        assertEquals(students, actual, "Should return list with two objects.");
    }

    @Test
    public void getStudentsByCourseName_returnError_whenSentEmptyString() {
        assertThrows(NullPointerException.class, () -> service.getStudentsByCourseName(""), "CourseName cannot be null");
    }

    @Test
    public void addNewStudent_returnTrue_whenAddNewStudent() {
        Student student = new Student("Nick", "Search");
        when(studentDAO.create(student)).thenReturn(student);
        assertTrue(service.addNewStudent(student), "Return true if student is added.");
        verify(studentDAO, times(1)).create(student);
    }

    @Test
    public void addNewStudent_returnError_whenAddNewStudentWithIncorrectGroupId() {
        Student student = new Student(10000, "Nick", "Search");
        when(studentDAO.create(student)).thenThrow(new SchoolDAOException());
        assertThrows(SchoolDAOException.class, () -> service.addNewStudent(student), "Throw the error cause groupId is not relevant.");
        verify(studentDAO, times(1)).create(student);
    }

    @Test
    public void assignStudentsToCourse_returnOneTimeUsing_whenSentIds() {
        doNothing().when(studentDAO).assignStudentsToCourse(isA(Integer.class), isA(Integer.class));
        service.assignStudentsToCourse(10, 20);
        verify(studentDAO, times(1)).assignStudentsToCourse(10, 20);
    }

    @Test
    public void assignStudentsToCourse_returnError_whenSentIncorrectStudentIdData() {
        assertThrows(RuntimeException.class, () -> service.assignStudentsToCourse(-10, 10), "StudentId cannot be negative");
    }

    @Test
    public void assignStudentsToCourse_returnError_whenSentIncorrectCourseIdData() {
        assertThrows(RuntimeException.class, () -> service.assignStudentsToCourse(10, -10), "CourseId cannot be negative");
    }

    @Test
    public void assignStudentsToCourse_returnOneTimeUsing_whenSentListOfStudents() {
        doNothing().when(studentDAO).assignStudentsToCourse(students);
        service.assignStudentsToCourse(students);
        verify(studentDAO, times(1)).assignStudentsToCourse(students);
    }

    @Test
    public void assignStudentsToCourse_returnError_whenSentNull() {
        assertThrows(NullPointerException.class, () -> service.createStudents(null), "List cannot be null");
    }

    @Test
    public void assignStudentsToCourse_returnError_whenSentIncorrectFirstNameData() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1000, "TestDataBiggerThen20Letters", "Test"));
        assertThrows(RuntimeException.class, () -> service.assignStudentsToCourse(students), "FirstName cannot be bigger than 20");
    }

    @Test
    public void assignStudentsToCourse_returnError_whenSentIncorrectLastNameData() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1000, "Test", "TestDataBiggerThen20Letters"));
        assertThrows(RuntimeException.class, () -> service.assignStudentsToCourse(students), "LastName cannot be bigger than 20");
    }

    @Test
    public void deleteStudentFromCourse_returnOneTimeUsing_whenSentListOfStudents() {
        doNothing().when(studentDAO).deleteStudentFromCourse(isA(Integer.class), isA(Integer.class));
        service.deleteStudentFromCourse(10, 10);
        verify(studentDAO, times(1)).deleteStudentFromCourse(10, 10);
    }

    @Test
    public void deleteStudentFromCourse_returnError_whenSentIncorrectStudentIdData() {
        assertThrows(RuntimeException.class, () -> service.assignStudentsToCourse(-10, 10), "StudentId cannot be negative");
    }

    @Test
    public void deleteStudentFromCourse_returnError_whenSentIncorrectCourseIdData() {
        assertThrows(RuntimeException.class, () -> service.assignStudentsToCourse(10, -10), "CourseId cannot be negative");
    }

    @Test
    public void deleteStudent_returnTrue() {
        Student student = new Student(100, "Test", "Test");
        when(studentDAO.findById(10)).thenReturn(Optional.of(student));
        doNothing().when(studentDAO).deleteById(10);
        assertTrue(service.deleteStudent(10));
        verify(studentDAO, times(2)).findById(10);
        verify(studentDAO, times(1)).deleteById(10);
    }
}

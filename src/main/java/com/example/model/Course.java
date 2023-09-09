package com.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course implements HasId<Integer> {
    private Integer id;
    private String name;
    private String description;
    private List<Student> students;

    public Course() {
    }

    public Course(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        students = new ArrayList<>();
    }

    public Course(String name, String description, List<Student> students) {
        this.name = name;
        this.description = description;
        this.students = students;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(Student student) {
        students.add(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(name, course.name) && Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}


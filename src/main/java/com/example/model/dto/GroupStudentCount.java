package com.example.model.dto;

import com.example.model.Group;

import java.util.Objects;

public class GroupStudentCount {
    private Group group;
    private Integer count;

    public GroupStudentCount() {
    }

    public GroupStudentCount(Group group, Integer count) {
        this.group = group;
        this.count = count;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupStudentCount that = (GroupStudentCount) o;
        return Objects.equals(group, that.group) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, count);
    }

    @Override
    public String toString() {
        return "GroupStudentCount{" +
                "group=" + group +
                ", count=" + count +
                '}';
    }
}

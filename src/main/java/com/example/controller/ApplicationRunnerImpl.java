package com.example.controller;

import com.example.dao.CourseDAO;
import com.example.dao.GroupDAO;
import com.example.dao.StudentDAO;
import com.example.exception.SchoolDAOException;
import com.example.service.SchoolManagerService;
import com.example.view.ConsoleIO;
import com.example.view.UserInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(SchoolManagerService.class);
    private final GroupDAO groupDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public ApplicationRunnerImpl(GroupDAO groupDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    @Override
    public void run(ApplicationArguments args) {
        SchoolManagerService manager = new SchoolManagerService(groupDAO, studentDAO, courseDAO);
        ConsoleIO consoleIO = new ConsoleIO();
        UserInterface userInterface = new UserInterface(manager, consoleIO);
        try {
            userInterface.runMenu();
        } catch (SchoolDAOException e) {
            LOGGER.error("Error: {}", e.getMessage());
            throw new SchoolDAOException();
        }
    }
}

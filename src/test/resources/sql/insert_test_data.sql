INSERT INTO groups(group_id, group_name) VALUES
    (1000, '1'),
	(2000, '2'),
	(3000, '3'),
	(4000, '4');

INSERT INTO courses(course_id, course_name, course_description) VALUES
    (1000, 'Math', 'Desc math'),
    (2000, 'Biology', 'Desc biology'),
    (3000, 'History', 'Desc history'),
    (4000, 'History', 'Desc history');

INSERT INTO students(student_id, group_id, first_name, last_name) VALUES
    (1000, 1000, 'Name1.0', 'Surname1.0'),
    (1001, 1000, 'Name1.1', 'Surname1.1'),
    (1002, 1000, 'Name1.2', 'Surname1.2'),
    (1003, 1000, 'Name1.3', 'Surname1.3'),

    (2000, 2000, 'Name2.0', 'Surname2.0'),
    (2001, 2000, 'Name2.1', 'Surname2.1'),
    (2002, 2000, 'Name2.2', 'Surname2.2'),

    (3000, 3000, 'Name3.0', 'Surname3.0'),
    (3001, 3000, 'Name3.1', 'Surname3.1'),
    (3002, 3000, 'Name3.2', 'Surname3.2'),
    (3003, 3000, 'Name3.3', 'Surname3.3'),

    (4000, 1000, 'Name4.0', 'Surname1.0');

INSERT INTO students_courses(student_id, course_id) VALUES
    (1000, 1000),
    (1001, 1000),
    (1002, 1000),
    (1003, 1000),

    (2000, 2000),
    (2001, 2000),
    (2002, 2000),

    (3000, 3000),
    (3001, 3000),
    (3002, 3000),
    (3003, 3000);
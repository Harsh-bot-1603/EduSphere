CREATE TABLE roles (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT uk_roles_name UNIQUE (name)
);

CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),
                       role_id BIGINT,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_users_role
                           FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE courses (
                         id BIGINT NOT NULL AUTO_INCREMENT,
                         title VARCHAR(255),
                         description VARCHAR(2000),
                         price DECIMAL(19,2),
                         teacher_id BIGINT NOT NULL,
                         status VARCHAR(255),
                         created_at DATETIME NOT NULL,
                         updated_at DATETIME,
                         PRIMARY KEY (id),
                         CONSTRAINT fk_courses_teacher
                             FOREIGN KEY (teacher_id) REFERENCES users(id)
);

CREATE TABLE lesson (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        lesson_order INT,
                        title VARCHAR(255),
                        video_url VARCHAR(255),
                        duration_in_minutes INT,
                        description VARCHAR(2000),
                        course_id BIGINT,
                        PRIMARY KEY (id),
                        CONSTRAINT fk_lesson_course
                            FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE enrollment (
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            course_id BIGINT,
                            student_id BIGINT,
                            enrollment_date DATETIME,
                            status VARCHAR(255),
                            progress INT,
                            instructor_name VARCHAR(255),
                            PRIMARY KEY (id),
                            CONSTRAINT uk_enrollment_course_student
                                UNIQUE (course_id, student_id),
                            CONSTRAINT fk_enrollment_course
                                FOREIGN KEY (course_id) REFERENCES courses(id),
                            CONSTRAINT fk_enrollment_student
                                FOREIGN KEY (student_id) REFERENCES users(id)
);

CREATE TABLE lesson_progress (
                                 id BIGINT NOT NULL AUTO_INCREMENT,
                                 enrollment_id BIGINT NOT NULL,
                                 lesson_id BIGINT NOT NULL,
                                 completed BOOLEAN NOT NULL DEFAULT FALSE,
                                 completed_at DATETIME,
                                 PRIMARY KEY (id),
                                 CONSTRAINT uk_lesson_progress_enrollment_lesson
                                     UNIQUE (enrollment_id, lesson_id),
                                 CONSTRAINT fk_lesson_progress_enrollment
                                     FOREIGN KEY (enrollment_id) REFERENCES enrollment(id),
                                 CONSTRAINT fk_lesson_progress_lesson
                                     FOREIGN KEY (lesson_id) REFERENCES lesson(id)
);

CREATE TABLE review (
                        id BIGINT NOT NULL AUTO_INCREMENT,
                        rating INT NOT NULL,
                        comment VARCHAR(2000),
                        created_at DATETIME,
                        updated_at DATETIME,
                        student_id BIGINT NOT NULL,
                        course_id BIGINT NOT NULL,
                        PRIMARY KEY (id),
                        CONSTRAINT uk_review_student_course
                            UNIQUE (student_id, course_id),
                        CONSTRAINT fk_review_student
                            FOREIGN KEY (student_id) REFERENCES users(id),
                        CONSTRAINT fk_review_course
                            FOREIGN KEY (course_id) REFERENCES courses(id)
);
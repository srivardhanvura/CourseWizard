DROP SCHEMA IF EXISTS course_wizard;

CREATE SCHEMA "course_wizard";

SET search_path TO course_wizard, public;

CREATE TABLE course_wizard.users (
  username varchar(50) NOT NULL,
  pwd char(68) NOT NULL,
  enabled smallint NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE course_wizard.roles (
  user_id varchar(50) NOT NULL,
  role varchar(50) NOT NULL,
  UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES course_wizard.users (username) ON DELETE CASCADE
);

CREATE TABLE course_wizard.departments (
  code varchar(5) NOT NULL,
  name varchar(50) NOT NULL,
  PRIMARY KEY (code)
);

CREATE TABLE course_wizard.instructor (
  id SERIAL PRIMARY KEY,
  first_name varchar(45),
  last_name varchar(45),
  user_name varchar(45) UNIQUE,
  email varchar(45),
  office varchar(128),
  department varchar(5) not null,
  active boolean default true,
  FOREIGN KEY (department) REFERENCES course_wizard.departments (code)
);

CREATE TABLE course_wizard.course (
  id SERIAL PRIMARY KEY,
  title varchar(128),
  description varchar(228),
  location varchar(128),
  timings varchar(128),
  instructor_id int,
  status varchar(128),
  department varchar(5) not NULL,
  code varchar(10) unique,
  credits int not null,
  total_strength int not null,
  current_strength int not null,
  
  UNIQUE (title),
  
  CONSTRAINT FK_INSTRUCTOR
  FOREIGN KEY (instructor_id)
  REFERENCES course_wizard.instructor (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  FOREIGN KEY (department)
  REFERENCES course_wizard.departments (code)
);

CREATE TABLE course_wizard.review (
  id SERIAL PRIMARY KEY,
  comment varchar(256),
  rating int,
  course_id int,

  CONSTRAINT FK_COURSE
  FOREIGN KEY (course_id)
  REFERENCES course_wizard.course (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE course_wizard.student (
  id SERIAL PRIMARY KEY,
  first_name varchar(45),
  last_name varchar(45),
  user_name varchar(45) UNIQUE,
  email varchar(45),
  department varchar(5) not null,
  
  FOREIGN KEY (department)
  REFERENCES course_wizard.departments (code)
);

CREATE TABLE course_wizard.course_student (
  course_id int NOT NULL,
  student_id int NOT NULL,
  
  PRIMARY KEY (course_id, student_id),
  
  CONSTRAINT FK_COURSE_05 FOREIGN KEY (course_id)
  REFERENCES course_wizard.course (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT FK_STUDENT FOREIGN KEY (student_id)
  REFERENCES course_wizard.student (id)
  ON DELETE NO ACTION ON UPDATE NO ACTION
);
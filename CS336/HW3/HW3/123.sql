CREATE DATABASE rutgers;
USE rutgers;


CREATE TABLE Departments (
name VARCHAR(4) PRIMARY KEY CHECK(name IN('Bio','Chem','CS','Eng','Math','Phys')) ,
campus VARCHAR(5) CHECK(campus IN('Busch','CAC','Livi','CD'))
);

CREATE TABLE Students(
first_name VARCHAR(20),
last_name VARCHAR(20),
id CHAR(9) PRIMARY KEY
);

CREATE TABLE Classes(
name VARCHAR(50) PRIMARY KEY,
credits INT CHECK(credits IN(3,4))
);

CREATE TABLE Majors(
sid CHAR(9),
dname VARCHAR(4),
FOREIGN KEY (sid) REFERENCES Students(id),
FOREIGN KEY (dname) REFERENCES Departments(name),
PRIMARY KEY (sid,dname)
);

CREATE TABLE Minors(
sid CHAR(9) NOT NULL,
dname VARCHAR(4),
FOREIGN KEY (sid) REFERENCES Students(id),
FOREIGN KEY (dname) REFERENCES Departments(name),
PRIMARY KEY (sid,dname)
);

CREATE TABLE IsTaking(
sid CHAR(9),
name VARCHAR(50),
FOREIGN KEY (sid) REFERENCES Students(id),
FOREIGN KEY (name) REFERENCES Classes(name),
PRIMARY KEY (sid,name)
);

CREATE TABLE HasTaken(
sid CHAR(9),
name VARCHAR(50),
grade CHAR(1) CHECK(grade IN('A','B','C','D','F')),
FOREIGN KEY (sid) REFERENCES Students(id),
FOREIGN KEY (name) REFERENCES Classes(name),
PRIMARY KEY (sid,name)
);

INSERT INTO Departments (name, campus)
SELECT name, (SELECT campus FROM (SELECT 'Busch' AS campus UNION SELECT 'CAC' UNION SELECT 'Livi' UNION SELECT 'CD') AS c ORDER BY RAND() LIMIT 1)
FROM (SELECT 'Bio' AS name UNION SELECT 'Chem' UNION SELECT 'CS' UNION SELECT 'Eng' UNION SELECT 'Math' UNION SELECT 'Phys') AS d;
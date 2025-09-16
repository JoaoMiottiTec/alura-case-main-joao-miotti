-- =========================================================
-- USERS (1 instrutor + 5 alunos) | senha MD5 de 'secret'
-- =========================================================
-- INSTRUCTOR
INSERT INTO `User` (createdAt, email, name, password, role)
SELECT NOW(), 'ana@alura.com', 'Ana Silva', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'INSTRUCTOR'
WHERE NOT EXISTS (SELECT 1 FROM `User` WHERE email='ana@alura.com');
SET @instructor_id := (SELECT id FROM `User` WHERE email='ana@alura.com');

-- STUDENTS
INSERT INTO `User` (createdAt, email, name, password, role)
SELECT NOW(), 'tito@alura.com', 'Tito', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM `User` WHERE email='tito@alura.com');
SET @s1 := (SELECT id FROM `User` WHERE email='tito@alura.com');

INSERT INTO `User` (createdAt, email, name, password, role)
SELECT NOW(), 'alice@alura.com', 'Alice', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM `User` WHERE email='alice@alura.com');
SET @s2 := (SELECT id FROM `User` WHERE email='alice@alura.com');

INSERT INTO `User` (createdAt, email, name, password, role)
SELECT NOW(), 'bob@alura.com', 'Bob', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM `User` WHERE email='bob@alura.com');
SET @s3 := (SELECT id FROM `User` WHERE email='bob@alura.com');

INSERT INTO `User` (createdAt, email, name, password, role)
SELECT NOW(), 'carol@alura.com', 'Carol', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM `User` WHERE email='carol@alura.com');
SET @s4 := (SELECT id FROM `User` WHERE email='carol@alura.com');

INSERT INTO `User` (createdAt, email, name, password, role)
SELECT NOW(), 'diego@alura.com', 'Diego', '5ebe2294ecd0e0f08eab7690d2a6ee69', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM `User` WHERE email='diego@alura.com');
SET @s5 := (SELECT id FROM `User` WHERE email='diego@alura.com');

-- =========================================================
-- CATEGORIES (2)
-- =========================================================
INSERT INTO Category (code, color, createdAt, `name`, `order`)
SELECT 'CAT-PROG', '#0A84FF', NOW(), 'Programming', 1
WHERE NOT EXISTS (SELECT 1 FROM Category WHERE code='CAT-PROG');
SET @cat_prog := (SELECT id FROM Category WHERE code='CAT-PROG');

INSERT INTO Category (code, color, createdAt, `name`, `order`)
SELECT 'CAT-DATA', '#34D399', NOW(), 'Data', 2
WHERE NOT EXISTS (SELECT 1 FROM Category WHERE code='CAT-DATA');
SET @cat_data := (SELECT id FROM Category WHERE code='CAT-DATA');

-- =========================================================
-- COURSES (3) - todos ACTIVE
-- =========================================================
INSERT INTO Course (category_id, code, description, inactive_at, instructor_id, `name`, status)
SELECT @cat_prog, 'JAVA01', NULL, NULL, @instructor_id, 'Java Fundamentals', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM Course WHERE code='JAVA01');
SET @c_java := (SELECT id FROM Course WHERE code='JAVA01');

INSERT INTO Course (category_id, code, description, inactive_at, instructor_id, `name`, status)
SELECT @cat_prog, 'SPR02', NULL, NULL, @instructor_id, 'Spring Boot API', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM Course WHERE code='SPR02');
SET @c_spr := (SELECT id FROM Course WHERE code='SPR02');

INSERT INTO Course (category_id, code, description, inactive_at, instructor_id, `name`, status)
SELECT @cat_data, 'SQL01', NULL, NULL, @instructor_id, 'SQL for Data Analysts', 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM Course WHERE code='SQL01');
SET @c_sql := (SELECT id FROM Course WHERE code='SQL01');

-- =========================================================
-- REGISTRATIONS (6 matrículas / 5 alunos únicos)
--  - JAVA01: 3 alunos (s1, s2, s3)
--  - SPR02 : 2 alunos (s4, s5)
--  - SQL01 : 1 aluno  (s1 novamente)
-- =========================================================
INSERT INTO Registration (course_id, registered_at, user_id)
SELECT @c_java, NOW(), @s1
WHERE NOT EXISTS (SELECT 1 FROM Registration WHERE course_id=@c_java AND user_id=@s1);

INSERT INTO Registration (course_id, registered_at, user_id)
SELECT @c_java, NOW(), @s2
WHERE NOT EXISTS (SELECT 1 FROM Registration WHERE course_id=@c_java AND user_id=@s2);

INSERT INTO Registration (course_id, registered_at, user_id)
SELECT @c_java, NOW(), @s3
WHERE NOT EXISTS (SELECT 1 FROM Registration WHERE course_id=@c_java AND user_id=@s3);

INSERT INTO Registration (course_id, registered_at, user_id)
SELECT @c_spr, NOW(), @s4
WHERE NOT EXISTS (SELECT 1 FROM Registration WHERE course_id=@c_spr AND user_id=@s4);

INSERT INTO Registration (course_id, registered_at, user_id)
SELECT @c_spr, NOW(), @s5
WHERE NOT EXISTS (SELECT 1 FROM Registration WHERE course_id=@c_spr AND user_id=@s5);

INSERT INTO Registration (course_id, registered_at, user_id)
SELECT @c_sql, NOW(), @s1
WHERE NOT EXISTS (SELECT 1 FROM Registration WHERE course_id=@c_sql AND user_id=@s1);

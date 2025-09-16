CREATE TABLE Course (
    id                  bigint  AUTO_INCREMENT PRIMARY KEY,
    name                varchar(100) NOT NULL,
    code                varchar(10) NOT NULL UNIQUE,
    instructor_id       bigint(20) NOT NULL,
    category_id         bigint(20) NOT NULL,
    description         text,
    status              enum('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    inactive_at         datetime NULL,

    CONSTRAINT fk_course_instructor
     FOREIGN KEY (instructor_id) REFERENCES `User`(id)
        ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_course_category
     FOREIGN KEY (category_id) REFERENCES `Category`(id)
        ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
CREATE TABLE Registration (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    course_id      BIGINT NOT NULL,
    registered_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_registration_user_course UNIQUE (user_id, course_id),

    CONSTRAINT fk_registration_user
      FOREIGN KEY (user_id) REFERENCES `User`(id)
      ON UPDATE CASCADE ON DELETE RESTRICT,

    CONSTRAINT fk_registration_course
      FOREIGN KEY (course_id) REFERENCES `Course`(id)
      ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

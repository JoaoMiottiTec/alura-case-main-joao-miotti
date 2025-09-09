CREATE TABLE Category (
    id        bigint(20)  NOT NULL AUTO_INCREMENT,
    createdAt datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name      varchar(50) NOT NULL,
    code      varchar(50) NOT NULL,
    color     varchar(50) NOT NULL,
    `order`   int(11) NOT NULL,
    PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
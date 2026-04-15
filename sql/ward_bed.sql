CREATE TABLE ward_info (
    ward_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '病房ID',
    department VARCHAR(50) NOT NULL COMMENT '科室',
    ward_no VARCHAR(20) NOT NULL COMMENT '病房号',
    nurse_name VARCHAR(20) NOT NULL COMMENT '责任护士',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_dept_ward (department, ward_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '病房信息表';

CREATE TABLE bed_info (
                          bed_id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '床位ID',
                          ward_id BIGINT NOT NULL COMMENT '关联病房ID',
                          bed_no VARCHAR(10) NOT NULL COMMENT '床位编号',
                          status TINYINT NOT NULL DEFAULT 0 COMMENT '床位状态：0-空床，1-已占用',
                          patient_id BIGINT COMMENT '关联患者ID（空床为NULL）',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (ward_id) REFERENCES ward_info(ward_id),
                          UNIQUE KEY uk_ward_bed (ward_id, bed_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '床位明细表';

INSERT INTO ward_info (department, ward_no, nurse_name)
VALUES
    ('呼吸道科', '普通病房401', '刘爽'),
    ('呼吸道科', '普通病房402', '刘爽'),
    ('呼吸道科', '普通病房403', '刘爽'),
    ('呼吸道科', '普通病房404', '刘爽'),
    ('呼吸道科', '普通病房405', '刘爽'),
    ('呼吸道科', '普通病房406', '刘爽'),
    ('呼吸道科', '普通病房407', '刘爽');

-- 普通病房401：2个占用，3个空床
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (1, '401-01', 1, 1001),
    (1, '401-02', 1, 1002),
    (1, '401-03', 0, NULL),
    (1, '401-04', 0, NULL),
    (1, '401-05', 0, NULL);

-- 普通病房402：4个占用，1个空床
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (2, '402-01', 1, 1003),
    (2, '402-02', 1, 1004),
    (2, '402-03', 1, 1005),
    (2, '402-04', 1, 1006),
    (2, '402-05', 0, NULL);

-- 普通病房403：4个占用，1个空床
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (3, '403-01', 1, 1007),
    (3, '403-02', 1, 1008),
    (3, '403-03', 1, 1009),
    (3, '403-04', 1, 1010),
    (3, '403-05', 0, NULL);

-- 普通病房404：4个占用，1个空床
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (4, '404-01', 1, 1011),
    (4, '404-02', 1, 1012),
    (4, '404-03', 1, 1013),
    (4, '404-04', 1, 1014),
    (4, '404-05', 0, NULL);

-- 普通病房405：5个全占用
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (5, '405-01', 1, 1015),
    (5, '405-02', 1, 1016),
    (5, '405-03', 1, 1017),
    (5, '405-04', 1, 1018),
    (5, '405-05', 1, 1019);

-- 普通病房406：5个全占用
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (6, '406-01', 1, 1020),
    (6, '406-02', 1, 1021),
    (6, '406-03', 1, 1022),
    (6, '406-04', 1, 1023),
    (6, '406-05', 1, 1024);

-- 普通病房407：4个占用，1个空床
INSERT INTO bed_info (ward_id, bed_no, status, patient_id)
VALUES
    (7, '407-01', 1, 1025),
    (7, '407-02', 1, 1026),
    (7, '407-03', 1, 1027),
    (7, '407-04', 1, 1028),
    (7, '407-05', 0, NULL);
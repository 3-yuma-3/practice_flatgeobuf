
CREATE TABLE schools (
    ulid varchar(255) NOT NULL
    , administrative_area_code varchar(255) NOT NULL comment '行政区域コード'
    , established_by varchar(255) NOT NULL comment '設置主体'
    , code varchar(255) comment '学校コード'
    , name varchar(255) NOT NULL comment '名称'
    , address varchar(255) NOT NULL comment '所在地'
    , PRIMARY KEY (ulid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE school_polygon (
    school_ulid varchar(255) NOT NULL
    , coordinate_list json NOT NULL comment 'POLYGONの座標一覧'
    , PRIMARY KEY (school_ulid)
    , FOREIGN KEY (school_ulid) REFERENCES schools(ulid) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

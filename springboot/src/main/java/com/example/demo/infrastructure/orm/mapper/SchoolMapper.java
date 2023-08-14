package com.example.demo.infrastructure.orm.mapper;

import com.example.demo.infrastructure.orm.entity.SchoolOrmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SchoolMapper {
    void insertList(@Param("schoolList") List<SchoolOrmEntity> schoolList);

    List<SchoolOrmEntity> selectListByUlidList(
        @Param("ulidList") List<String> ulidList
    );
}

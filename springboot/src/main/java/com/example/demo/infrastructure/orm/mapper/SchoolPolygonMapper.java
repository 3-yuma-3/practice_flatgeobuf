package com.example.demo.infrastructure.orm.mapper;

import com.example.demo.infrastructure.orm.entity.SchoolPolygonOrmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SchoolPolygonMapper {
    void insertList(@Param("polygonList") List<SchoolPolygonOrmEntity> polygonList);

    List<SchoolPolygonOrmEntity> selectListByUlidList(@Param("ulidList") List<String> ulidList);

    List<SchoolPolygonOrmEntity> selectList();
}

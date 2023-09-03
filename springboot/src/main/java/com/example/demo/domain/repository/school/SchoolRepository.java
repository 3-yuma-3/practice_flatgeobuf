package com.example.demo.domain.repository.school;

import com.example.demo.domain.entity.school.School;
import com.example.demo.domain.value_object.Ulid;

import java.util.List;

public interface SchoolRepository {
    void insertList(List<School> schooleList);

    List<School> selectListByUlidList(List<Ulid> ulidList);

    List<School> selectList();
}

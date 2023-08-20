package com.example.demo.usecase.geojson.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.domain.repository.school.SchoolRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ListGeojsonPolygonUsecase {
    private final SchoolRepository schoolRepository;

    @Transactional
    public List<School> getList() {
        return schoolRepository.selectList();
    }
}

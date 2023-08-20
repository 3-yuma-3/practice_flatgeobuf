package com.example.demo.presentation.controller.geojson.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.geojson.polygon.list.ListGeojsonPolygonUsecase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/geojson/polygon")
public class ListGeojsonPolygonController {
    private final ListGeojsonPolygonUsecase listGeojsonPolygonUsecase;

    @GetMapping()
    public List<School> listGeojsonPolygon(
    ) {
        List<School> geojsonPolygonList = listGeojsonPolygonUsecase.getList();

        return geojsonPolygonList;
    }
}

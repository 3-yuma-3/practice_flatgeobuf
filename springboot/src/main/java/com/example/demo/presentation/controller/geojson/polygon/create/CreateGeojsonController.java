package com.example.demo.presentation.controller.geojson.polygon.create;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.geojson.polygon.create.CreateGeojsonInput;
import com.example.demo.usecase.geojson.polygon.create.CreateGeojsonUsecase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/geojson/polygon")
public class CreateGeojsonController {
    private final CreateGeojsonUsecase createGeojsonUsecase;

    @PostMapping()
    public List<School> createGeojson(
        @RequestBody CreateGeojsonRequest createGeojsonRequest
    ) {
        List<CreateGeojsonInput> createGeojsonInputList = createGeojsonRequest.convertToInput();
        List<School> savedSchoolList = createGeojsonUsecase.create(createGeojsonInputList);

        return savedSchoolList;
    }
}


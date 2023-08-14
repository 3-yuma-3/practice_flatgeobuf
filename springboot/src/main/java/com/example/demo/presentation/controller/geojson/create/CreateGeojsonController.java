package com.example.demo.presentation.controller.geojson.create;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.geojson.create.CreateGeojsonInput;
import com.example.demo.usecase.geojson.create.CreateGeojsonUsecase;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CreateGeojsonController {
    private final CreateGeojsonUsecase createGeojsonUsecase;

    @PostMapping("/geojson")
    public List<School> createGeojson(
        @RequestBody CreateGeojsonRequest createGeojsonRequest
    ) {
        List<CreateGeojsonInput> createGeojsonInputList = createGeojsonRequest.convertToInput();
        List<School> savedSchoolList = createGeojsonUsecase.create(createGeojsonInputList);

        return savedSchoolList;
    }
}


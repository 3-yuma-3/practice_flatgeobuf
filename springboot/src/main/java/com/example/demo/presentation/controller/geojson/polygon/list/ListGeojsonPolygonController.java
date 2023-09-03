package com.example.demo.presentation.controller.geojson.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.geojson.polygon.list.ListGeojsonPolygonUsecase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.geotools.data.geojson.GeoJSONWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
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

    @GetMapping("/geojson")
    public void convertedToGeojson(HttpServletResponse response) throws IOException {
        List<School> schoolList = listGeojsonPolygonUsecase.getList();
        List<SimpleFeature> simpleFeatureList = new ArrayList<>(
                schoolList.stream().map(school -> {
                    SimpleFeature simpleFeature;
                    try {
                        simpleFeature = school.convertToSimpleFeature();
                    } catch (FactoryException e) {
                        throw new RuntimeException(e);
                    }
                    return simpleFeature;
                }).toList()
        );
        GeoJSONWriter writer = new GeoJSONWriter(response.getOutputStream());

        simpleFeatureList.forEach(simpleFeature -> {
            try {
                writer.write(simpleFeature);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        response.flushBuffer();
    }
}

package com.example.demo.presentation.controller.geojson.create.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Geometry {
    /**
     * e.g. Polygon
     */
    private String type;
//    private List<CreateGeojsonRequestCoordinate> coordinates;
    private List<List<List<Double>>> coordinates;
}

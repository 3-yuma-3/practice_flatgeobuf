package com.example.demo.presentation.controller.geojson.polygon.create.request;

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
    /**
     * request は CRS84 で来るので、 lng, lat の順番
     * entity は EPSG:4326 なので、 lat, lng の順番に入れ替える
     */
//    private List<CreateGeojsonRequestCoordinate> coordinates;
    private List<List<List<Double>>> coordinates;
}

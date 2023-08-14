package com.example.demo.usecase.geojson.create;

import com.example.demo.domain.entity.Coordinate;
import com.example.demo.domain.entity.Polygon;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CreateGeojsonInput {
    private String administrativeAreaCode;
    private String establishedBy;
    private String code;
    private String name;
    private String address;
    private List<CreateGeojsonInputCoordinate> coordinateList;

    public Polygon toPolygon() {
        List<Coordinate> entityCoordinateList = this.coordinateList.stream()
            .map(CreateGeojsonInputCoordinate::toCoordinate).toList();

        return new Polygon(entityCoordinateList);
    }
}

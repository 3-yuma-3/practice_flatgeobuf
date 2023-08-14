package com.example.demo.usecase.geojson.create;

import com.example.demo.domain.entity.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 緯度(latitude), 経度(longitude) の並び
 */
@AllArgsConstructor
@Data
public class CreateGeojsonInputCoordinate {
    private double lat;
    private double lng;

    public Coordinate toCoordinate() {
        return new Coordinate(
            this.lat,
            this.lng
        );
    }
}

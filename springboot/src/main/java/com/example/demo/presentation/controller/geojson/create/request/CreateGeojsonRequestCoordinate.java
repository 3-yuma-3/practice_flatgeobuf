package com.example.demo.presentation.controller.geojson.create.request;

import com.example.demo.usecase.geojson.create.CreateGeojsonInputCoordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CRS84: 経度(longitude), 緯度(latitude の並び
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGeojsonRequestCoordinate {
    /**
     * 経度
     */
    private double lng;
    /**
     * 緯度
     */
    private double lat;

    /**
     * request は CRS84 で来るので、 lng, lat の順番
     * entity は EPSG:4326 なので、 lat, lng の順番に入れ替える
     *
     * @return
     */
    public CreateGeojsonInputCoordinate convertToInputCoordinate() {
        return new CreateGeojsonInputCoordinate(
            this.lat,
            this.lng
        );
    }
}

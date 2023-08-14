package com.example.demo.presentation.controller.geojson.create;

import com.example.demo.presentation.controller.geojson.create.request.CRS;
import com.example.demo.presentation.controller.geojson.create.request.Feature;
import com.example.demo.usecase.geojson.create.CreateGeojsonInput;
import com.example.demo.usecase.geojson.create.CreateGeojsonInputCoordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateGeojsonRequest {
    /**
     * e.g. FeatureCollection
     */
    private String type;
    /**
     * e.g. A27-21_08_4326
     */
    private String name;
    private CRS crs;
    private List<Feature> features;

    public List<CreateGeojsonInput> convertToInput() {
        return this.features.stream().map(feature -> {
//            List<CreateGeojsonInputCoordinate> inputCoordinateList =
//                feature.getGeometry().getCoordinates().get(0)
//                    .stream().map(CreateGeojsonRequestCoordinate::convertToInputCoordinate)
//                    .toList();
            List<CreateGeojsonInputCoordinate> inputCoordinateList =
                feature.getGeometry().getCoordinates().get(0)
                    .stream().map(list -> new CreateGeojsonInputCoordinate(
                        list.get(1),
                        list.get(0)
                    ))
                    .toList();
//            List<CreateGeojsonInputCoordinate> inputCoordinateList =
//                feature.getGeometry().getCoordinates()
//                    .stream().map(CreateGeojsonRequestCoordinate::convertToInputCoordinate)
//                    .toList();

            return new CreateGeojsonInput(
                feature.getProperties().getA27_001(),
                feature.getProperties().getA27_002(),
                feature.getProperties().getA27_003(),
                feature.getProperties().getA27_004(),
                feature.getProperties().getA27_005(),
                inputCoordinateList
            );
        }).toList();
    }
}

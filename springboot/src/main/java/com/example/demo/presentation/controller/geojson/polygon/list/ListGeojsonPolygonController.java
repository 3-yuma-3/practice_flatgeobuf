package com.example.demo.presentation.controller.geojson.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.geojson.polygon.list.ListGeojsonPolygonUsecase;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
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
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        response.flushBuffer();
    }

    /**
     * NOTE: feature.properties 有りの geojson
     *
     * @param response
     * @throws IOException
     * @throws FactoryException
     */
    @GetMapping("original_geojson_with_properties_java.json")
    public void originalGeojsonWithPropertiesJava(HttpServletResponse response) throws IOException, FactoryException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(139.7147, 36.1251),
            new Coordinate(139.7146, 36.1252),
            new Coordinate(139.7138, 36.126),
            new Coordinate(139.7147, 36.1251),
        };
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("school");
        simpleFeatureTypeBuilder.setCRS(CRS.decode("EPSG:4326"));
        simpleFeatureTypeBuilder.add("A27_001", String.class);
        simpleFeatureTypeBuilder.add("location", org.locationtech.jts.geom.Polygon.class);
        SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder simpleFeatureBuilder1 = new SimpleFeatureBuilder(simpleFeatureType);
        simpleFeatureBuilder1.set("A27_001", "08542");
        SimpleFeature simpleFeature1 = simpleFeatureBuilder1.buildFeature(null);
        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));

        GeoJSONWriter writer = new GeoJSONWriter(response.getOutputStream());

        try {
            writer.write(simpleFeature1);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.flushBuffer();
    }

    /**
     * NOTE: feature.properties 無しの geojson
     *
     * @param response
     * @throws IOException
     * @throws FactoryException
     */
    @GetMapping("original_geojson_no_properties_java.json")
    public void originalGeojsonNoPropertiesJava(HttpServletResponse response) throws IOException, FactoryException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(139.7147, 36.1251),
            new Coordinate(139.7146, 36.1252),
            new Coordinate(139.7138, 36.126),
            new Coordinate(139.7147, 36.1251),
        };
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("school");
        simpleFeatureTypeBuilder.setCRS(CRS.decode("EPSG:4326"));
        simpleFeatureTypeBuilder.add("location", org.locationtech.jts.geom.Polygon.class);
        SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder simpleFeatureBuilder1 = new SimpleFeatureBuilder(simpleFeatureType);
        SimpleFeature simpleFeature1 = simpleFeatureBuilder1.buildFeature(null);
        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));

        GeoJSONWriter writer = new GeoJSONWriter(response.getOutputStream());

        try {
            writer.write(simpleFeature1);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        response.flushBuffer();
    }
}

package com.example.demo.domain.entity.school;

import com.example.demo.domain.entity.Polygon;
import com.example.demo.domain.value_object.Ulid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
@Slf4j
public class School {
    private Ulid ulid;
    /* 行政区域コード: A27_001 */
    private String administrativeAreaCode;
    /* 設置主体: A27_002 */
    private String establishedBy;
    /* 学校コード: A27_003 */
    private String code;
    /* 名称: A27_004 */
    private String name;
    /* 所在地: A27_005 */
    private String address;
    /* */
    private Polygon polygon;

    /**
     * @return
     * @throws FactoryException
     * @see https://gis.stackexchange.com/a/158741
     * @see https://docs.geotools.org/stable/userguide/faq.html#:~:text=Q%3A%20How%20do%20I%20make%20a%20FeatureType%3F
     */
    public SimpleFeature convertToSimpleFeature() throws FactoryException {
        final SimpleFeatureType simpleFeatureType = generateSimpleFeatureType();

        SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
        simpleFeatureBuilder.set("A27_001", this.administrativeAreaCode);
        simpleFeatureBuilder.set("A27_002", this.establishedBy);
        simpleFeatureBuilder.set("A27_003", this.code);
        simpleFeatureBuilder.set("A27_004", this.name);
        simpleFeatureBuilder.set("A27_005", this.address);
//        simpleFeatureBuilder.add(new GeometryFactory().createPolygon(getCoordinateList()));

        SimpleFeature simpleFeature = simpleFeatureBuilder.buildFeature("fid");
        simpleFeature.setDefaultGeometry(new GeometryFactory().createPolygon(getCoordinateList()));

        return simpleFeature;
    }

    public static SimpleFeatureType generateSimpleFeatureType() throws FactoryException {
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();

        simpleFeatureTypeBuilder.setName("school");
        simpleFeatureTypeBuilder.setCRS(CRS.decode("EPSG:4326"));
        simpleFeatureTypeBuilder.add("A27_001", String.class);
        simpleFeatureTypeBuilder.add("A27_002", String.class);
        simpleFeatureTypeBuilder.add("A27_003", String.class);
        simpleFeatureTypeBuilder.add("A27_004", String.class);
        simpleFeatureTypeBuilder.add("A27_005", String.class);
        simpleFeatureTypeBuilder.add("location", org.locationtech.jts.geom.Polygon.class);

        return simpleFeatureTypeBuilder.buildFeatureType();
    }

    public SimpleFeature convertToSimpleFeatureWithoutProperty() throws SchemaException {
        // todo: A27_001 を追加するとdeserializeできない
//        SimpleFeatureType simpleFeatureType = DataUtilities.createType(
//                "Polygon", "geometry:Polygon,A27_001:String"
//        );
        SimpleFeatureType simpleFeatureType = DataUtilities.createType(
                "Polygon", "geometry:Polygon,A27_001:String"
        );
        SimpleFeature simpleFeature = DataUtilities.template(simpleFeatureType);
        simpleFeature.setDefaultGeometry(new GeometryFactory().createPolygon(getCoordinateList()));
        // todo: 追加するとdeserializeできない
//        simpleFeature.setAttribute("A27_001", this.administrativeAreaCode);

        return simpleFeature;
    }

    private Coordinate[] getCoordinateList() {
        List<Coordinate> coordinateList = new ArrayList<>();
        this.polygon.getCoordinateList().forEach(coordinate ->
                coordinateList.add(new Coordinate(coordinate.getLat(), coordinate.getLng()))
        );

        return coordinateList.toArray(new Coordinate[0]);
    }
}

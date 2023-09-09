package com.example.demo.presentation.controller.flatgeobuf.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.usecase.flatgeobuf.polygon.list.ListFlatGeobufPolygonUsecase;
import com.google.flatbuffers.FlatBufferBuilder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.geotools.data.DataUtilities;
import org.geotools.data.flatgeobuf.FeatureCollectionConversions;
import org.geotools.data.flatgeobuf.FlatGeobufWriter;
import org.geotools.data.geojson.GeoJSONWriter;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.FactoryException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/flatgeobuf/polygon")
@Slf4j
public class ListFlatgeobufPolygonController {
    private final ListFlatGeobufPolygonUsecase listFlatGeobufPolygonUsecase;

    @GetMapping()
    public List<School> listFlatGeobufPolygon(
    ) {
        List<School> file = listFlatGeobufPolygonUsecase.getList();

        return file;
    }

    /**
     * @see https://itsakura.com/sb-filedownload
     * @see https://nainaistar.hatenablog.com/entry/2021/10/21/120000
     * @see geotools/modules/unsupported/flatgeobuf/src/test/java/org/geotools/data/flatgeobuf/AttributeRoundtripTest.java
     * @see https://stackoverflow.com/a/27741435
     */
    @GetMapping("/SchoolList.fgb")
    public void schoolList(HttpServletResponse response) throws IOException {
        DefaultFeatureCollection featureCollection = listFlatGeobufPolygonUsecase.getDefaultFeatureCollection();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();

        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * todo: deserializeに失敗する: byteのエラーが出る
     *
     * @param response
     * @throws FactoryException
     * @throws IOException
     * @see https://github.com/google/flatbuffers/issues/5325#issue-440910060
     */
    @GetMapping("/sample_list_3.fgb")
    public void sampleList3(HttpServletResponse response) throws FactoryException, IOException, URISyntaxException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(59.0625, 57.704147),
            new Coordinate(37.617187, 24.527135),
            new Coordinate(98.789062, 36.031332),
            new Coordinate(59.062499, 57.704147),
            new Coordinate(59.0625, 57.704147)
        };
        final SimpleFeatureType simpleFeatureType = School.generateSimpleFeatureType();
        SimpleFeatureBuilder simpleFeatureBuilder1 = new SimpleFeatureBuilder(simpleFeatureType);

        SimpleFeature simpleFeature1 = simpleFeatureBuilder1.buildFeature(null);
        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));

        SimpleFeatureBuilder simpleFeatureBuilder2 = new SimpleFeatureBuilder(simpleFeatureType);
        SimpleFeature simpleFeature2 = simpleFeatureBuilder2.buildFeature(null);

        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));
        List<SimpleFeature> simpleFeatureList = List.of(simpleFeature1, simpleFeature2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        simpleFeatureList.forEach(simpleFeature -> {
            try {
                DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
                featureCollection.add(simpleFeature);
                FeatureCollectionConversions.serialize(
                    featureCollection, 0, byteArrayOutputStream
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
//        sample_list_3.fgb
        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());

        // sample_list_3_1.fgb
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
//        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
//        IOUtils.copy(new ByteArrayInputStream(byteBuffer.array()), response.getOutputStream());

        // deserializeして中身を見てみる
        // todo: polygon1つしか読み出せない
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
//        Iterable<SimpleFeature> simpleFeatureIterable =
//                FeatureCollectionConversions.deserialize(byteArrayInputStream);
//        List<SimpleFeature> simpleFeatureList1 = new ArrayList<>();
//        simpleFeatureIterable.forEach(
//                simpleFeatureList1::add
//        );
//        log.info(String.valueOf(simpleFeatureList1));

        response.flushBuffer();
    }

    /**
     * @param response
     * @throws IOException
     * @throws SchemaException
     * @throws FactoryException
     * @see https://www.programcreek.com/java-api-examples/?api=org.geotools.data.DataUtilities#:~:text=)%3B%0A%20%20%7D%0A%7D-,Example%20%236,-Source%20File%3A
     */
    @GetMapping("/sample_list_2.fgb")
    public void sampleList2(HttpServletResponse response) throws IOException, SchemaException, FactoryException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(59.0625, 57.704147),
            new Coordinate(37.617187, 24.527135),
            new Coordinate(98.789062, 36.031332),
            new Coordinate(59.062499, 57.704147),
            new Coordinate(59.0625, 57.704147)
        };
        // todo: propertyをspecに指定するとdeserializeできない
//        SimpleFeatureType simpleFeatureType = DataUtilities.createType(
//                "Polygon", "geometry:Polygon,hoge:String"
//        );
        // todo: featureTypeもbuilderを使うとdeserializeできない
        final SimpleFeatureType simpleFeatureType = School.generateSimpleFeatureType();
//        // todo: builderを使うとdeserializeできない
        SimpleFeatureBuilder simpleFeatureBuilder1 = new SimpleFeatureBuilder(simpleFeatureType);
        SimpleFeature simpleFeature1 = simpleFeatureBuilder1.buildFeature(null);
        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));

        SimpleFeatureBuilder simpleFeatureBuilder2 = new SimpleFeatureBuilder(simpleFeatureType);
        SimpleFeature simpleFeature2 = simpleFeatureBuilder2.buildFeature(null);
        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));

//        SimpleFeature simpleFeature1 = DataUtilities.template(simpleFeatureType);
//        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));
//        SimpleFeature simpleFeature2 = DataUtilities.template(simpleFeatureType);
//        simpleFeature2.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));

        List<SimpleFeature> simpleFeatureList = List.of(simpleFeature1, simpleFeature2);

        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        featureCollection.addAll(simpleFeatureList);

        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * todo: deserializeできない
     *
     * @param response
     * @throws SchemaException
     * @throws IOException
     */
    @GetMapping("/sample_3.fgb")
    public void sample3(HttpServletResponse response) throws SchemaException, IOException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(59.0625, 57.704147),
            new Coordinate(37.617187, 24.527135),
            new Coordinate(98.789062, 36.031332),
            new Coordinate(59.062499, 57.704147),
            new Coordinate(59.0625, 57.704147)
        };
        SimpleFeatureType simpleFeatureType = DataUtilities.createType(
            "Polygon",
            "geometry:Geometry:srid=4326,hoge:java.lang.String"
        );
        List<org.opengis.feature.type.AttributeDescriptor> attributeDescriptorList =
            simpleFeatureType.getAttributeDescriptors();
        Object[] defaults = new Object[attributeDescriptorList.size()];
        int p = 0;
        for (final AttributeDescriptor descriptor : attributeDescriptorList) {
            defaults[p++] = descriptor.getDefaultValue();
        }

        SimpleFeature simpleFeature = SimpleFeatureBuilder.build(
            simpleFeatureType, defaults, UUID.randomUUID().toString()
        );
        simpleFeature.setAttribute("hoge", "fuga");
        GeometryFactory geometryFactory = new GeometryFactory();

        simpleFeature.setDefaultGeometry(geometryFactory.createPolygon(coordinates));
        List<SimpleFeature> simpleFeatureList = List.of(simpleFeature);

        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.addAll(simpleFeatureList);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * todo: propertyなしで、DataUtilitiesを使ってSimpleFeatureを生成すれば動く
     *
     * @param response
     * @throws IOException
     */
    @GetMapping("/sample_2.fgb")
    public void sample2(HttpServletResponse response) throws IOException {
        List<SimpleFeature> simpleFeatureList =
            listFlatGeobufPolygonUsecase.getSimpleFeatureListWithoutProperty();
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.addAll(simpleFeatureList);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();

        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * TODO: feature.properties 有りなので、javascriptでdeserializeできない
     * @param response
     * @throws IOException
     * @throws FactoryException
     */
    @GetMapping("original_geojson_with_properties_java.fgb")
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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.add(simpleFeature1);
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * NOTE: feature.properties 無しなので、javascriptでdeserializeできる
     * @param response
     * @throws IOException
     * @throws FactoryException
     */
    @GetMapping("original_geojson_no_properties_java.fgb")
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

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.add(simpleFeature1);
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }


    @GetMapping("/sample_1.fgb")
    public void sample1(HttpServletResponse response) throws IOException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(59.0625, 57.704147),
            new Coordinate(37.617187, 24.527135),
            new Coordinate(98.789062, 36.031332),
            new Coordinate(59.062499, 57.704147),
            new Coordinate(59.0625, 57.704147)
        };
        // todo: builderを使うとdeserializeできない
        // todo: geometryがnullになる
        SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        simpleFeatureTypeBuilder.setName("sample_1");
        SimpleFeatureType simpleFeatureType = simpleFeatureTypeBuilder.buildFeatureType();

        SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(simpleFeatureType);
        SimpleFeature simpleFeature = simpleFeatureBuilder.buildFeature("fid");
        simpleFeature.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));
        List<SimpleFeature> simpleFeatureList = List.of(simpleFeature);

        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.addAll(simpleFeatureList);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    /**
     * todo: これは動く
     *
     * @param response
     * @throws IOException
     * @throws SchemaException
     */
    @GetMapping("/sample_list_1.fgb")
    public void sampleList1(HttpServletResponse response) throws IOException, SchemaException {
        Coordinate[] coordinates = new Coordinate[]{
            new Coordinate(59.0625, 57.704147),
            new Coordinate(37.617187, 24.527135),
            new Coordinate(98.789062, 36.031332),
            new Coordinate(59.062499, 57.704147),
            new Coordinate(59.0625, 57.704147)
        };
        // feature.propertiesなしならいけた
        SimpleFeatureType simpleFeatureType = DataUtilities.createType(
            "Polygon", "geometry:Polygon"
        );
        SimpleFeature simpleFeature1 = DataUtilities.template(simpleFeatureType);
        simpleFeature1.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));
        SimpleFeature simpleFeature2 = DataUtilities.template(simpleFeatureType);
        simpleFeature2.setDefaultGeometry(new GeometryFactory().createPolygon(coordinates));
        List<SimpleFeature> simpleFeatureList = List.of(simpleFeature1, simpleFeature2);

        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.addAll(simpleFeatureList);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FeatureCollectionConversions.serialize(featureCollection, 0, byteArrayOutputStream);

        byte[] bytes = byteArrayOutputStream.toByteArray();
        IOUtils.copy(new ByteArrayInputStream(bytes), response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping("/write_file.fgb")
    public void writeFile() throws IOException {
        DefaultFeatureCollection defaultFeatureCollection =
            listFlatGeobufPolygonUsecase.getDefaultFeatureCollection();
        // todo: fileに書き込めてない
        listFlatGeobufPolygonUsecase.writeFile(defaultFeatureCollection);
    }

    @GetMapping("/use_writer.fgb")
    public void useWriter(HttpServletResponse response) throws IOException, SchemaException {
        List<SimpleFeature> simpleFeatureList =
            listFlatGeobufPolygonUsecase.getSimpleFeatureListWithoutProperty();

        OutputStream outputStream = response.getOutputStream();
        FlatGeobufWriter writer = new FlatGeobufWriter(outputStream, new FlatBufferBuilder());
        SimpleFeatureType simpleFeatureType =
            DataUtilities.createType("Polygon", "geometry:Polygon");
        writer.writeFeatureType(simpleFeatureType);

        // todo: IOException が発生する
        simpleFeatureList.forEach(simpleFeature -> {
            try {
                writer.writeFeature(simpleFeature);
            } catch (IOException e) {
                log.error("IOException が発生", e);
            }
        });
        outputStream.flush();
    }
}

package com.example.demo.usecase.flatgeobuf.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.domain.repository.school.SchoolRepository;
import com.google.flatbuffers.FlatBufferBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wololo.flatgeobuf.GeometryConversions;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class ListFlatGeobufPolygonUsecase {
    private final SchoolRepository schoolRepository;

    @Transactional
    public List<School> getList() {
        List<School> geojsonPolygonList = schoolRepository.selectList();
        return geojsonPolygonList;
    }

    public ByteBuffer write() {
        String wkt = """
                POLYGON ((
                    35.68267129314724 139.76617346908193, 35.68084840242705 139.7653295722163,
                    35.68029849666601 139.76797248631905, 35.68197072504614 139.76855677343286,
                    35.68267129314724 139.76617346908193
                ))
            """;
        WKTReader reader = new WKTReader();
        Geometry geometry;
        try {
            geometry = reader.read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        byte geometryType = GeometryConversions.toGeometryType(geometry.getClass());
        FlatBufferBuilder builder = new FlatBufferBuilder(1024);
        int gometryOffset;
        try {
            gometryOffset = GeometryConversions.serialize(builder, geometry, geometryType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        builder.finish(gometryOffset);
        byte[] bytes = builder.sizedByteArray();
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return bb;
//
//        SimpleFeatureCollection fc =
//            (SimpleFeatureCollection) featureCollection.getFeature().get(0);
//        FeatureCollectionConversions.serialize(fc, 0, output);
    }

    private File sample() {
        File tmpFile = new File("springboot/tmp/polygons.fgb");

        try {
            File file = File.createTempFile("hoge", "fuga", tmpFile);

            Map<String, Serializable> params = new HashMap<>();
//            URL url = tmpFile.toURI().toURL();
            URL url = file.toURI().toURL();
            params.put("url", url);
            DataStore store = DataStoreFinder.getDataStore(params);

            SimpleFeatureType featureType =
                DataUtilities.createType("lines", "geom:Polygon,name:String,id:int");
            store.createSchema(featureType);
            SimpleFeatureStore featureStore =
                (SimpleFeatureStore) store.getFeatureSource("polygons");
            WKTReader reader = new WKTReader();

            SimpleFeature feature1 = SimpleFeatureBuilder.build(
                featureType,
                new Object[]{
                    reader.read(
                        """
                            POLYGON (
                                (
                                    59.0625 57.704147, 37.617187 24.527135, 
                                    98.789062 36.031332, 59.062499 57.704147, 59.0625 57.704147
                                )
                            )
                            """
                    ),
                    "ABC",
                    1
                },
                "location.1"
            );

            SimpleFeatureCollection collection = DataUtilities.collection(feature1);
            featureStore.addFeatures(collection);
        } catch (IOException | SchemaException e) {
            log.error("IOException | SchemaException エラー発生", e);
        } catch (ParseException e) {
            log.error("ParseException エラー発生", e);
        }

        // TODO: 一時ファイルにfgb形式で出力して、そのバイナリファイルをresponseで返す？

        return tmpFile;
    }
}

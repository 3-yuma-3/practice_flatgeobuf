package com.example.demo.usecase.flatgeobuf.polygon.list;

import com.example.demo.domain.entity.school.School;
import com.example.demo.domain.repository.school.SchoolRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.data.flatgeobuf.FeatureCollectionConversions;
import org.geotools.data.flatgeobuf.FlatGeobufDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.SchemaException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<School> schoolList = schoolRepository.selectList();

        return schoolList;
    }

    public List<SimpleFeature> getSimpleFeatureListWithoutProperty() {
        List<School> schoolList = schoolRepository.selectList();

        return new ArrayList<>(
                schoolList.stream().map(school -> {
                    SimpleFeature simpleFeature;
                    try {
                        simpleFeature = school.convertToSimpleFeatureWithoutProperty();
                    } catch (SchemaException e) {
                        throw new RuntimeException(e);
                    }
                    return simpleFeature;
                }).toList()
        );
    }

    public List<SimpleFeature> getSimpleFeatureList() {
        List<School> schoolList = schoolRepository.selectList();

        return fromSchoolListToSimpleFeatureCollection(schoolList);
    }

    /**
     * TODO: controllerで実装して動作確認までできたのは、地物1つをfgbにする実装
     * 複数の地物を1つのfgbにするにはdataStoreを使って、fileを返す？
     *
     * @see https://docs.geotools.org/stable/userguide/tutorial/feature/csv2shp.html#:~:text=Write%20the%20feature%20data%20to%20the%20shapefile
     */
    public DefaultFeatureCollection getDefaultFeatureCollection() {
        List<School> schoolList = schoolRepository.selectList();
        List<SimpleFeature> simpleFeatureList =
                fromSchoolListToSimpleFeatureCollection(schoolList);
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        featureCollection.addAll(simpleFeatureList);

        return featureCollection;
    }

    private List<SimpleFeature> fromSchoolListToSimpleFeatureCollection(List<School> schoolList) {
        return new ArrayList<>(
                schoolList.stream().map(school -> {
                    SimpleFeature simpleFeature = null;
                    try {
                        simpleFeature = school.convertToSimpleFeature();
                    } catch (FactoryException e) {
                        log.error("FactoryException が発生: ", e);
                    }
                    return simpleFeature;
                }).toList()
        );
    }

    public void writeFile(DefaultFeatureCollection featureCollection) throws IOException {
        File file = new File("SchoolList" + LocalDateTime.now() + ".fgb");
        Map<String, URL> map = new HashMap<>();
        map.put("url", file.toURL());
        FlatGeobufDataStoreFactory factory = new FlatGeobufDataStoreFactory();
        DataStore dataStore = factory.createDataStore(map);
        try {
            dataStore.createSchema(School.generateSimpleFeatureType());
        } catch (FactoryException e) {
            log.error("FactoryException が発生: ", e);
        }

        String typeName = dataStore.getTypeNames()[0];
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(typeName);
        if (featureSource instanceof FeatureStore) {
            FeatureStore featureStore = (FeatureStore) featureSource;
            Transaction transaction = new DefaultTransaction("create");
            featureStore.setTransaction(transaction);
            try {
                featureStore.addFeatures(featureCollection);
                transaction.commit();
            } catch (Exception ex) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", ex);
                transaction.rollback();
            } finally {
                transaction.close();
            }
        }
        FileOutputStream fos = new FileOutputStream(file);
//        FeatureCollectionConversions.serialize(featureCollection, 0, fos);
        fos.flush();
        fos.close();
    }
}

package com.example.demo.infrastructure.repository_impl.school;

import com.example.demo.domain.entity.Polygon;
import com.example.demo.domain.entity.school.School;
import com.example.demo.domain.repository.school.SchoolRepository;
import com.example.demo.domain.value_object.Ulid;
import com.example.demo.infrastructure.orm.entity.SchoolOrmEntity;
import com.example.demo.infrastructure.orm.entity.SchoolPolygonOrmEntity;
import com.example.demo.infrastructure.orm.mapper.SchoolMapper;
import com.example.demo.infrastructure.orm.mapper.SchoolPolygonMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class SchoolRepositoryImpl implements SchoolRepository {
    private final SchoolMapper schoolMapper;
    private final SchoolPolygonMapper schoolPolygonMapper;

    @Override
    public void insertList(List<School> schooleList) {
        List<SchoolOrmEntity> schoolOrmEntityList =
            SchoolOrmEntity.fromEntity(schooleList);
        List<SchoolPolygonOrmEntity> schoolPolygonOrmEntityList =
            SchoolPolygonOrmEntity.fromEntity(schooleList);

        schoolMapper.insertList(schoolOrmEntityList);
        schoolPolygonMapper.insertList(schoolPolygonOrmEntityList);
    }

    @Override
    public List<School> selectListByUlidList(List<Ulid> ulidList) {
        List<String> ulidStringList = ulidList.stream().map(Ulid::toString).toList();

        List<SchoolOrmEntity> schoolOrmEntityList =
            schoolMapper.selectListByUlidList(ulidStringList);
        List<SchoolPolygonOrmEntity> schoolPolygonOrmEntities =
            schoolPolygonMapper.selectListByUlidList(ulidStringList);

        return generateEntityListFromOrmEntity(schoolOrmEntityList, schoolPolygonOrmEntities);
    }

    private List<School> generateEntityListFromOrmEntity(
        List<SchoolOrmEntity> schoolOrmEntityList,
        List<SchoolPolygonOrmEntity> schoolPolygonOrmEntityList
    ) {
        Map<String, SchoolPolygonOrmEntity> schoolUlidAndSchoolPolygonOrmEntityMap =
            schoolPolygonOrmEntityList.stream().collect(Collectors.toMap(
                SchoolPolygonOrmEntity::getSchoolUlid,
                schoolPolygon -> schoolPolygon
            ));

        return schoolOrmEntityList.stream().map(
            schoolOrmEntity -> {
                Polygon targetPolygon = schoolUlidAndSchoolPolygonOrmEntityMap
                    .get(schoolOrmEntity.getUlid())
                    .fromStringCoordinateListToPolygon();

                return new School(
                    Ulid.fromString(schoolOrmEntity.getUlid()),
                    schoolOrmEntity.getAdministrativeAreaCode(),
                    schoolOrmEntity.getEstablishedBy(),
                    schoolOrmEntity.getCode(),
                    schoolOrmEntity.getName(),
                    schoolOrmEntity.getAddress(),
                    targetPolygon
                );
            }
        ).toList();
    }
}

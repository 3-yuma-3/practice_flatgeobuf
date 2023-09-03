package com.example.demo.infrastructure.orm.entity;

import com.example.demo.domain.entity.Coordinate;
import com.example.demo.domain.entity.Polygon;
import com.example.demo.domain.entity.school.School;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchoolPolygonOrmEntity {
    private String schoolUlid;
    private String coordinateList;

    public static List<SchoolPolygonOrmEntity> fromEntity(List<School> schoolList) {
        ObjectMapper mapper = new ObjectMapper();

        return schoolList.stream().map(
            school -> {
                var coordinateList = school.getPolygon().getCoordinateList();
                String stringCoordinateList;
                try {
                    stringCoordinateList = mapper.writeValueAsString(coordinateList);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException();
                }

                return new SchoolPolygonOrmEntity(
                    school.getUlid().getValue(),
                    stringCoordinateList
                );
            }
        ).toList();
    }

    public Polygon fromStringCoordinateListToPolygon() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Coordinate> coordinateList;
        try {
            JsonNode jsonNode = objectMapper.readTree(this.coordinateList);
            ObjectReader objectReader = objectMapper.readerFor(new TypeReference<List<Coordinate>>() {
            });
            coordinateList = objectReader.readValue(jsonNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new Polygon(coordinateList);
    }
}

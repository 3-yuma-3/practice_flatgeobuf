package com.example.demo.infrastructure.orm.entity;

import com.example.demo.domain.entity.school.School;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SchoolOrmEntity {
    private String ulid;
    private String administrativeAreaCode;
    private String establishedBy;
    private String code;
    private String name;
    private String address;

    public static List<SchoolOrmEntity> fromEntity(List<School> schoolList) {
        return schoolList.stream().map(
            school -> new SchoolOrmEntity(
                school.getUlid().getValue(),
                school.getAdministrativeAreaCode(),
                school.getEstablishedBy(),
                school.getCode(),
                school.getName(),
                school.getAddress()
            )
        ).toList();
    }
}

package com.example.demo.usecase.geojson.polygon.create;

import com.example.demo.domain.entity.school.School;
import com.example.demo.domain.repository.school.SchoolRepository;
import com.example.demo.domain.value_object.Ulid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CreateGeojsonUsecase {
    private final SchoolRepository schoolRepository;

    @Transactional
    public List<School> create(
        List<CreateGeojsonInput> createGeojsonInputList
    ) {
        List<School> schoolList = createGeojsonInputList.stream().map(
            input -> new School(
                new Ulid().generate(),
                input.getAdministrativeAreaCode(),
                input.getEstablishedBy(),
                input.getCode(),
                input.getName(),
                input.getAddress(),
                input.toPolygon()
            )
        ).toList();

        schoolRepository.insertList(schoolList);

        List<Ulid> ulidList = schoolList.stream().map(School::getUlid).toList();
        // TODO: 空配列で返ってくる
        List<School> savedSchoolList = schoolRepository.selectListByUlidList(ulidList);

        return savedSchoolList;
    }
}

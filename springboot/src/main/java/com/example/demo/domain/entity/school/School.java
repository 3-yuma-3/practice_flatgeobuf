package com.example.demo.domain.entity.school;

import com.example.demo.domain.value_object.Ulid;
import com.example.demo.domain.entity.Polygon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class School {
    private Ulid ulid;
    private String administrativeAreaCode;
    private String establishedBy;
    private String code;
    private String name;
    private String address;
    private Polygon polygon;
}

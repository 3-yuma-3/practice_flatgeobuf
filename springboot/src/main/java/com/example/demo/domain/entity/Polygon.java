package com.example.demo.domain.entity;

import com.example.demo.domain.entity.Coordinate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class Polygon {
    private List<Coordinate> coordinateList;
}

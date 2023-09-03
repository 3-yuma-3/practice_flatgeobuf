package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 緯度(latitude), 経度(longitude) の並び
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Coordinate {
    private double lat;
    private double lng;
}

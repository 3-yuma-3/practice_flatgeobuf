package com.example.demo.presentation.controller.geojson.create.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CRS {
    /**
     * e.g. name
     */
    private String type;
    private Property properties;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Property {
        /**
         * e.g. urn:ogc:def:crs:OGC:1.3:CRS84
         */
        private String name;
    }
}

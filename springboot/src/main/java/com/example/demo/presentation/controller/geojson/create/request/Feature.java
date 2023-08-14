package com.example.demo.presentation.controller.geojson.create.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feature {
    /**
     * e.g. Feature
     */
    private String type;
    private Property properties;
    private Geometry geometry;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Property {
        /**
         * 行政区域コード
         */
        @JsonProperty("A27_001")
        private String A27_001;
        /**
         * 設置主体
         */
        @JsonProperty("A27_002")
        private String A27_002;
        /**
         * 学校コード
         */
        @JsonProperty("A27_003")
        private String A27_003;
        /**
         * 名称
         */
        @JsonProperty("A27_004")
        private String A27_004;
        /**
         * 所在地
         */
        @JsonProperty("A27_005")
        private String A27_005;
    }
}

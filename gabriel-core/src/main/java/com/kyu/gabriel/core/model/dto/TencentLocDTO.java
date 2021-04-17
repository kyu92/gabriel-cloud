package com.kyu.gabriel.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TencentLocDTO {

    @Data
    public static class Result {
        @Data
        public static class Location {
            private double lat;
            private double lng;
        }

        @Data
        public static class ADInfo {
            private String nation;
            private String province;
            private String city;
            private String district;
            private String adcode;
        }
        private String ip;
        private Location location;
        private ADInfo ad_info;
    }

    private int status;
    private String message;
    private Result result;
}

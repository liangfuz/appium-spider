package com.chris.bean;

import lombok.Data;

import java.util.List;

/**
 * @Description:高德地图地理坐标
 * @Author: zhangliangfu
 * @Create on: 2019-04-25 11:12
 */
@Data
public class AmapGeo {
    private Integer status;
    private String info;
    private String infocode;
    private List<GeoCode> geocodes;

    @Data
    public class GeoCode{
        private String city;
        private String citycode;
        private String location;
    }
}

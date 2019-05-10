package com.chris.bean;

import lombok.Data;

import java.util.List;

/**
 * @Description:高德地图逆地理坐标
 * @Author: zhangliangfu
 * @Create on: 2019-04-25 11:12
 */
@Data
public class AmapReGeo {
    private Integer status;
    private String info;
    private String infocode;
    private RegeoCode regeocode;

    @Data
    public class RegeoCode{
        private AddressComponent addressComponent;
        private String formatted_address;
    }

    @Data
    public class AddressComponent{
        private String city;
        private String citycode;
    }
}

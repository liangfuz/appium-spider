package com.chris.app.address;

import com.chris.bean.AmapGeo;
import com.chris.bean.AmapReGeo;
import com.chris.util.AmapUtil;
import com.chris.util.HttpClientUtil;
import com.chris.util.PinyinUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:获取地址列表
 * @Author: zhangliangfu
 * @Create on: 2019-04-25 10:21
 */
public class AddressObtain {
    public static String LOCATION_GET_URL = "https://restapi.amap.com/v3/geocode/geo";
    public static String BUILDING_GET_URL = "https://restapi.amap.com/v3/geocode/regeo";
    public static String KEY = "441eb5527eabce08f13d4eda29076810";
    public static String POITYPE = "120201|120300";

    public static AmapGeo getLocationByAddress(String city, String address){
        Map<String, String> params = new HashMap();
        params.put("city",city);
        params.put("address",address);
        params.put("key",KEY);
        params.put("output","json");
        AmapGeo amapGeo = null;
        String result = HttpClientUtil.doGet(LOCATION_GET_URL, params);
        System.out.println("高德地图返回：" + result);
        if (StringUtils.isNotEmpty(result)){
            Gson gson = new Gson();
            amapGeo = gson.fromJson(result, AmapGeo.class);
        }
        System.out.println(amapGeo.toString());
        return amapGeo;
    }

    public static AmapReGeo getAddressnByLocation(String location, String radius, String poitype){
        Map<String, String> params = new HashMap();
        params.put("radius",radius);
        params.put("location",location);
        params.put("key",KEY);
        params.put("output","json");
        params.put("poitype",poitype);
        params.put("extensions","all");
        AmapReGeo amapReGeo = null;
        String result = HttpClientUtil.doGet(BUILDING_GET_URL, params);
        if (StringUtils.isNotEmpty(result)){
            Gson gson = new Gson();
            try {
                amapReGeo = gson.fromJson(result, AmapReGeo.class);
                System.out.println(amapReGeo.toString());
            }catch (Exception e){
                System.out.println("Gson转换失败");
            }
        }
        return amapReGeo;
    }
    public static void main(String[] args) {
        List<String> addrs = new ArrayList<>();
        AmapGeo locationByAddress = getLocationByAddress("深圳", "国人通信大厦");
        String location = locationByAddress.getGeocodes().get(0).getLocation();
        List<String> locations = AmapUtil.locations(location, 2500);
        locations.forEach(loc->{
            try {
                Thread.sleep(3000);
                AmapReGeo reGeo = getAddressnByLocation(loc, "1000", POITYPE);
                String address = reGeo.getRegeocode().getFormatted_address();
                System.out.println(address);
                addrs.add(address);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        System.out.println(addrs);
    }

    /*public static void main(String[] args) {
        List<String> addressList = addressList();
        addressList.forEach(address->{
            System.out.println(address);
        });
    }*/

    public static List<String> addressList(){
        List<String> addressList = new ArrayList<>();
        try {
            File file = new File("src/main/resources/address.txt");
            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            // 按行读取字符串
            while ((str = bf.readLine()) != null) {
                addressList.add(PinyinUtil.ToPinyin(str));
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList;
    }
}

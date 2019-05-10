package com.chris.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:高德地图研究
 * @Author: zhangliangfu
 * @Create on: 2019-04-23 10:51
 */
public class AmapUtil {

    private static List<String> locations = new ArrayList<>();

    /*
     * 大地坐标系资料WGS-84 长半径a=6378137 短半径b=6356752.3142 扁率f=1/298.2572236
     */
    /**
     * 长半径a=6378137
     */
    private static double a = 6378137;
    /**
     * 短半径b=6356752.3142
     */
    private static double b = 6356752.3142;
    /**
     * 扁率f=1/298.2572236
     */
    private static double f = 1 / 298.2572236;

    private static int angleN = 0;
    private static int angleS = 180;
    private static int angleW = 270;
    private static int angleE = 90;

    /**
     * 计算另一点经纬度
     *
     * @param lon    经度
     * @param lat    维度
     * @param brng   方位角
     * @param dist   距离（米）
     */
    public static String computerThatLonLat(double lon, double lat, double brng, double dist) {

        double alpha1 = rad(brng);
        double sinAlpha1 = Math.sin(alpha1);
        double cosAlpha1 = Math.cos(alpha1);

        double tanU1 = (1 - f) * Math.tan(rad(lat));
        double cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1));
        double sinU1 = tanU1 * cosU1;
        double sigma1 = Math.atan2(tanU1, cosAlpha1);
        double sinAlpha = cosU1 * sinAlpha1;
        double cosSqAlpha = 1 - sinAlpha * sinAlpha;
        double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

        double cos2SigmaM = 0;
        double sinSigma = 0;
        double cosSigma = 0;
        double sigma = dist / (b * A), sigmaP = 2 * Math.PI;
        while (Math.abs(sigma - sigmaP) > 1e-12) {
            cos2SigmaM = Math.cos(2 * sigma1 + sigma);
            sinSigma = Math.sin(sigma);
            cosSigma = Math.cos(sigma);
            double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
                    - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
            sigmaP = sigma;
            sigma = dist / (b * A) + deltaSigma;
        }

        double tmp = sinU1 * sinSigma - cosU1 * cosSigma * cosAlpha1;
        double lat2 = Math.atan2(sinU1 * cosSigma + cosU1 * sinSigma * cosAlpha1,
                (1 - f) * Math.sqrt(sinAlpha * sinAlpha + tmp * tmp));
        double lambda = Math.atan2(sinSigma * sinAlpha1, cosU1 * cosSigma - sinU1 * sinSigma * cosAlpha1);
        double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        double L = lambda - (1 - C) * f * sinAlpha
                * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

        double revAz = Math.atan2(sinAlpha, -tmp); // final bearing

        double longitude = lon + deg(L);
        double latitude = deg(lat2);
        return  longitude + "," + latitude;
    }

    /**
     * 度换成弧度
     *
     * @param d 度
     * @return 弧度
     */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 弧度换成度
     *
     * @param x 弧度
     * @return 度
     */
    private static double deg(double x) {
        return x * 180 / Math.PI;
    }

    public static List<String> locations(String lonlat, double distance){
        if (count>=100) {
            return null;
        }
        double longitude = Double.parseDouble(lonlat.split(",")[0]);
        double latitude = Double.parseDouble(lonlat.split(",")[1]);
        String lonlatE = computerThatLonLat(longitude, latitude, angleE, distance);
        String lonlatN = computerThatLonLat(longitude, latitude, angleN, distance);
        String lonlatS = computerThatLonLat(longitude, latitude, angleS, distance);
        String lonlatW = computerThatLonLat(longitude, latitude, angleW, distance);
        locations.add(lonlatE);
        locations.add(lonlatN);
        locations.add(lonlatS);
        locations.add(lonlatW);
        count += 4;
        locations(lonlatE, distance);
        locations(lonlatN, distance);
        locations(lonlatS, distance);
        locations(lonlatW, distance);
        return locations;
    }


    private static int count = 0;

    public static void main(String[] args) {
        List<String> locations = locations("113.941133,22.543471", 2500);
        System.out.println(locations.size());
    }

}

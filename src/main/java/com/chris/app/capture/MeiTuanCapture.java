package com.chris.app.capture;

import com.chris.Application;
import com.chris.app.address.AddressObtain;
import com.chris.bean.AmapGeo;
import com.chris.bean.BaseTest;
import com.chris.bean.MtResInfo;
import com.chris.util.KafkaProperties;
import com.chris.util.KafkaSend;
import com.chris.util.MongoDbSave;
import com.google.gson.GsonBuilder;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.boot.test.context.SpringBootTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.appium.java_client.touch.WaitOptions.waitOptions;
import static io.appium.java_client.touch.offset.PointOption.point;

@SpringBootTest(classes = Application.class)
public class MeiTuanCapture extends BaseTest {


    private AndroidDriver<WebElement> driver;

    private KafkaSend kafkaSend = new KafkaSend();
    private MongoDbSave mongoDbSave = new MongoDbSave();
    private final String platformVersion = "5.1.1";
    private final String deviceName = "Android Emulator";
    private final String platformName = "Android";
    private final String noReset = "True";

    private final String meituanAppActivity = "com.sankuai.waimai.business.page.homepage.MainActivity";
    private final String meituanAppPackage = "com.sankuai.meituan.takeoutnew";

    @BeforeClass
    public void setMtEmuUp() throws IOException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("platformName", platformName);
        capabilities.setCapability("appActivity", meituanAppActivity);
        capabilities.setCapability("appPackage", meituanAppPackage);
        capabilities.setCapability("noReset", noReset);
        driver = new AndroidDriver<WebElement>(getServiceUrl(), capabilities);
    }


    @Test()
    public void meituan() {
        List<String> addressList = AddressObtain.addressList();
        long start = System.currentTimeMillis();
        int swipTimes = 0;
        int resCount = 0;
        try {
            AndroidElement close = (AndroidElement)driver.findElementById("close");
            close.click();
            sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            AndroidElement close = (AndroidElement)driver.findElementById("wm_upgrade_force_cancel");
            close.click();
            sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        boolean first = true;
        AndroidElement city_button = (AndroidElement)driver.findElementById("layout_location_box");
        city_button.click();
        sleep(1000);
        AndroidElement city_text = (AndroidElement)driver.findElementById("wm_address_city_location_text");
        city_text.click();
        sleep(1000);
        AndroidElement shenzhen = (AndroidElement)driver.findElementById("waimai_addrsdk_search_edittext");
        shenzhen.sendKeys("shenzhen");
        sleep(1000);
        AndroidElement shenzhenKey = (AndroidElement)driver.findElementById("waimai_addrsdk_choose_city_txt");
        shenzhenKey.click();
        sleep(1000);
        for (String addr :
                addressList) {
            if (!first){
//                AndroidElement city_button2 = (AndroidElement)driver.findElementById("layout_location_box");
                try{
                    city_button.click();
                    sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("爬取地址："+addr);
            AndroidElement addrEle = (AndroidElement)driver.findElementById("address_search_map_txt");
            addrEle.sendKeys(addr);
            sleep(1000);
            AndroidElement addrHeader;
            try {
                addrHeader = (AndroidElement)driver.findElementById("txt_map_adapter_name");
                addrHeader.click();
                sleep(1000);
            }catch (Exception e){
                try {
                    WebElement back = driver.findElementById("back");
                    back.click();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
                continue;
            }
            AndroidElement e = (AndroidElement)driver.findElementById("gridview_major_category");
            List<MobileElement> eElements = e.findElements(By.id("txt_item"));
            for (MobileElement ele :
                    eElements) {
                if (ele.getText().equals("美食")) {
                    ele.click();
                    sleep(1000);
                    break;
                }
            }

            AndroidElement filter_bar_tab = (AndroidElement)driver.findElementById("layout_header_filter_bar");
            MobileElement filter_bar = filter_bar_tab.findElement(By.id("filter_bar"));
            List<MobileElement> filter_text = filter_bar.findElements(By.id("filter_text"));
            for (MobileElement me :
                    filter_text) {
                if (me.getText().equals("距离")){
                    me.click();
                    sleep(1000);
                    break;
                }
            }
            Set<Integer> set = new HashSet<>();
            boolean continueFlag = true;
            while (continueFlag){
                Set<Integer> tmp = new HashSet<>();
                int count = 0;
                List<WebElement> poi_layout = driver.findElements(By.id("parent"));
                for (WebElement webElement:poi_layout) {
                    MtResInfo mtResInfo = new MtResInfo();
                    WebElement poi_name = null;
                    try {
                        poi_name = webElement.findElement(By.id("textview_poi_name"));
                        String resName = poi_name.getText();
                        int hashCode = resName.hashCode();
                        mtResInfo.setResName(resName);
                        mtResInfo.setResHashCode(hashCode);
                        mtResInfo.setCreateTime(new Date());
                        mtResInfo.setCreateTimestamp(System.currentTimeMillis());
                    }catch (Exception ex){
                        continue;
                    }
                    if (set.contains(mtResInfo.getResHashCode())){
                        count++;
                    }
                    try {
                        WebElement avg_price = webElement.findElement(By.id("layout_poi_price_info"));
                        mtResInfo.setAvgPrice(avg_price.getText());
                    }catch (Exception ex){
                    }
                    try {
                        WebElement ratingText = webElement.findElement(By.id("layout_poi_rating_sales"));
                        mtResInfo.setRatingText(ratingText.getText());
                    }catch (Exception ex){
                    }
                    try {
                        WebElement cate = webElement.findElement(By.id("friend_layout"));
                        WebElement category = cate.findElement(By.id("textview_third_category"));
                        mtResInfo.setCate(category.getText());
                    }catch (Exception ex){
                    }
                    try {
                        WebElement distance = webElement.findElement(By.id("layout_poi_distance_time"));
                        String distanceText = distance.getText();
                        mtResInfo.setDistance(distanceText);
                        if (distanceText.contains("km")){
                            //3公里换爬取地址
                            distanceText = distanceText.replace("km","");
                            Double dis = Double.parseDouble(distanceText);
                            if (dis>3){
                                continueFlag = false;
                            }
                        }
                    }catch (Exception ex){
                    }
                    try {
                        WebElement monthSale = webElement.findElement(By.id("textview_month_sales_tip"));
                        mtResInfo.setMonthSale(monthSale.getText());
                    }catch (Exception ex){
                    }
                    try {
                        WebElement delivery = webElement.findElement(By.id("imageview_mt_delivery"));
                        mtResInfo.setDelivery(delivery.getText());
                    }catch (Exception ex){

                    }
                    System.out.println(mtResInfo);
                    poi_name.click();
                    sleep(1000);
                    try {

                        List<WebElement> takeout_txt_tab = driver.findElementsById("takeout_txt_tab");
                        if (takeout_txt_tab!=null){
                            for (WebElement tab :
                                    takeout_txt_tab) {
                                if (tab.getText().equals("商家")){
                                    tab.click();
                                    sleep(1000);
                                    WebElement txt_poi_address = driver.findElementById("txt_poi_address");
                                    String address = txt_poi_address.getText();
                                    System.out.println("地址：" + address);
                                    mtResInfo.setAddress(address);
                                    AmapGeo location = AddressObtain.getLocationByAddress("深圳", address);
                                    if (location.getGeocodes()!=null){
                                        String lonlat = location.getGeocodes().get(0).getLocation();
                                        String[] split = lonlat.split(",");
                                        String lon = split[0];
                                        String lat = split[1];
                                        mtResInfo.setLongitude(Double.parseDouble(lon));
                                        mtResInfo.setLatitude(Double.parseDouble(lat));
                                    }
                                }
                            }
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    if (StringUtils.isNotEmpty(mtResInfo.getResName())){
                        mongoDbSave.save(mtResInfo);
                        kafkaSend.send(KafkaProperties.TOPIC, mtResInfo.getResHashCode(),new GsonBuilder()
                                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                                .create().toJson(mtResInfo));
                        tmp.add(mtResInfo.getResHashCode());
                        resCount++;
                    }
                    sleep(1000);
                    try {
                        WebElement back = driver.findElementById("img_back_light");
                        back.click();
                        sleep(1000);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
                if (set.size()>0&&set.size()<=count){
                    break;
                }else {
                    set.clear();
                    set.addAll(tmp);
                }
                try {
                    gestSwipeVerticalPercentage(0.9,0.2,0.5,2000);
                    swipTimes++;
                    System.out.println("滑屏次数:["+swipTimes+"]");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                sleep(2000);
            }
            try {
                first = false;
                WebElement back = driver.findElementById("back");
                back.click();
                sleep(1000);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }
        System.out.println("抓取门店数:["+resCount+"]");
        System.out.println("抓取任务结束:[退出]");
        long end = System.currentTimeMillis();
        long diff = (end-start)/1000;
        System.out.println("用时["+diff+"]秒");

    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Since15")
    public void gestSwipeVerticalPercentage(double startPercentage, double finalPercentage, double anchorPercentage, int duration) throws Exception {
        Dimension size = driver.manage().window().getSize();
        int anchor, startPoint, endPoint;
        anchor = (int) (size.width * anchorPercentage);
        startPoint = (int) (size.height * startPercentage);
        endPoint = (int) (size.height * finalPercentage);
        System.out.println("--------------------------------------------------");
        System.out.println(anchor+"/"+startPoint+"/"+endPoint);
        new TouchAction(driver).press(point(anchor, startPoint)).waitAction(waitOptions(Duration.ofMillis(duration)))
                .moveTo(point(anchor, endPoint)).release().perform();
    }

}
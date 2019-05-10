package com.chris.bean;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public abstract class BaseTest {

    private static AppiumDriverLocalService service;

    @BeforeSuite
    public void globalSetup () throws IOException {
//        service = AppiumDriverLocalService.buildDefaultService();
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withAppiumJS(new File("C:\\Program Files (x86)\\Appium\\resources\\app\\node_modules\\appium\\build" +
                        "\\lib\\main.js"))
                .withLogFile(new File("/appium/target/logs/sample.txt"))
                .usingAnyFreePort();
        service = builder.build();
        service.start();
    }

    @AfterSuite
    public void globalTearDown () {
        service.stop();
    }

    public URL getServiceUrl () {
        return service.getUrl();
    }

}

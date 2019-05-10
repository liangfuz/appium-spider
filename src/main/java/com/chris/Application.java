package com.chris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @Description:
 * @Author: zhangliangfu
 * @Create on: 2019-03-22 14:17
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = {"com.chris"})
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}

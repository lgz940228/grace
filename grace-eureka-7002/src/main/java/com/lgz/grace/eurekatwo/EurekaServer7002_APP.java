package com.lgz.grace.eurekatwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Created by lgz on 2018/12/25.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer7002_APP {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServer7002_APP.class,args);
    }
}

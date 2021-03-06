package com.lgz.grace.consumer.dept.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Created by lgz on 2018/12/24.
 */
@SpringBootConfiguration
public class ConfigBean {

    @Bean
    @LoadBalanced
    public RestTemplate resetTemplate(){
        return new RestTemplate();
    }
}

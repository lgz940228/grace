package com.lgz.grace.consumer.dept.controller;

import com.lgz.grace.api.entities.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by lgz on 2018/12/24.
 */

@RestController
public class DeptController_Consumer {
    //private static final String REST_RUL_PREFIX = "http://localhost:8001";
    private static final String REST_RUL_PREFIX = "http://GRACE-DEPT";
    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/consumer/dept/add")
    public boolean add(Dept dept){
        return restTemplate.postForObject(REST_RUL_PREFIX+"/dept/add",dept,Boolean.class);
    }

    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept getOne(@PathVariable String id){
        return restTemplate.getForObject(REST_RUL_PREFIX+"/dept/get/"+id,Dept.class);
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/consumer/dept/getAll")
    public List<Dept> getAll(){
        return restTemplate.getForObject(REST_RUL_PREFIX+"/dept/getAll",List.class);
    }

}

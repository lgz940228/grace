package com.lgz.grace.provider.dept.controller;

import com.lgz.grace.api.entities.Dept;
import com.lgz.grace.provider.dept.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by lgz on 2018/11/23.
 */
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;
    /*@Autowired
    private DiscoveryClient client;
*/
    @RequestMapping(value = "/dept/add",method = RequestMethod.GET)
    public boolean add(Dept dept){
        return deptService.addDept(dept);
    }

    @RequestMapping(value = "/dept/get/{id}",method = RequestMethod.GET)
    public Dept findDeptById(@PathVariable("id") Long deptId){
        return deptService.findById(deptId);
    }

    @RequestMapping(value = "/dept/getAll",method = RequestMethod.GET)
    public List<Dept> findDeptAll(){
        return deptService.findAll();
    }
    /*@RequestMapping(value = "/dept/discovery", method = RequestMethod.GET)
    public Object discovery()
    {
        List<String> list = client.getServices();
        System.out.println("**********" + list);

        List<ServiceInstance> srvList = client.getInstances("MICROSERVICECLOUD-DEPT");
        for (ServiceInstance element : srvList) {
            System.out.println(element.getServiceId() + "\t" + element.getHost() + "\t" + element.getPort() + "\t"
                    + element.getUri());
        }
        return this.client;
    }*/
}

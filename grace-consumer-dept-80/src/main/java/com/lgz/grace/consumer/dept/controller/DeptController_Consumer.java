package com.lgz.grace.consumer.dept.controller;

import com.lgz.grace.api.entities.Dept;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by lgz on 2018/12/24.
 */

@Controller
public class DeptController_Consumer {
    //private static final String REST_RUL_PREFIX = "http://localhost:8001";
    private static final String REST_RUL_PREFIX = "http://GRACE-DEPT";
    @Autowired
    private RestTemplate restTemplate;

    @ResponseBody
    @RequestMapping(value = "/consumer/dept/add")
    public boolean add(Dept dept){
        return restTemplate.postForObject(REST_RUL_PREFIX+"/dept/add",dept,Boolean.class);
    }
    @ResponseBody
    @RequestMapping(value = "/consumer/dept/get/{id}")
    public Dept getOne(@PathVariable String id){
        return restTemplate.getForObject(REST_RUL_PREFIX+"/dept/get/"+id,Dept.class);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/consumer/dept/getAll")
    public List<Dept> getAll(){
        return restTemplate.getForObject(REST_RUL_PREFIX+"/dept/getAll",List.class);
    }
    @ResponseBody
    @RequestMapping(value = "excel",method = RequestMethod.POST)
    public void excel(HttpServletResponse response){

        Workbook book = new XSSFWorkbook();
        Sheet sheet = book.createSheet("报表");
        //sheet.setDefaultColumnWidth(256);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("我");
        Cell cell2 = row.createCell(1);
        cell2.setCellValue("是");
        Cell cell3 = row.createCell(2);
        cell3.setCellValue("中");
        Cell cell4 = row.createCell(3);
        cell4.setCellValue("国");
        Cell cell5 = row.createCell(4);
        cell5.setCellValue("人");
        try {
            OutputStream out = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;fileName="+new String("报表".getBytes("utf-8"),"ISO-8859-1")+".xlsx");
            response.setHeader("content-type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            book.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("to")
    public String toE(){
        return "excel";
    }

}

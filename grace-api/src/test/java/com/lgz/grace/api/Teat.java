package com.lgz.grace.api;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by lgz on 2018/11/23.
 */
public class Teat {
    @Test
    public void excelTest(){
        OutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("E:\\workbook.xlsx");
            Workbook excel = new XSSFWorkbook();
            //CreationHelper creationHelper = excel.getCreationHelper();
            Sheet sheet = excel.createSheet("sheet");
            Row row = sheet.createRow(0);
            Workbook excelm = WorkbookFactory.create(new File("E:\\workbookm.xlsx"));
            Sheet sheetm = excelm.getSheetAt(0);
            Row rowm = sheetm.getRow(0);
            CellStyle cellStyle = rowm.getCell(0).getCellStyle();

            row.createCell(0).setCellValue("序列号");

            excel.write(fileOut);
        }catch (Exception io){
            io.printStackTrace();
        }finally {
            try {
                if(fileOut != null){
                    fileOut.close();
                }
            }catch (Exception e){

            }

        }
    }
    @Test
    public void ssl(){
        ConnectionSocketFactory plainsf = new PlainConnectionSocketFactory();
        ConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainsf)
                .register("https", sslsf)
                .build();

        HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
        HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    @Test
    public void ecxcel(){
        HSSFWorkbook wb = new HSSFWorkbook();
        //sheet名
        String sheetName = "专家列表";
    }
}

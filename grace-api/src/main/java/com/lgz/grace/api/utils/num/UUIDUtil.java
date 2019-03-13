package com.lgz.grace.api.utils.num;

import java.util.UUID;

public class UUIDUtil {
    public static String newUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /*public static void main(String args[]){
        try {
            throw new RuntimeException("123");
        }catch (Exception e){
            throw new RuntimeException("321",e);
        }finally {
            System.out.println("finally");
        }
    }*/
}

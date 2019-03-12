package com.lgz.grace.api.entities;

import com.lgz.grace.api.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * Created by lgz on 2019/3/12.
 */
@Data
public class Record {

    @Excel
    private String name;

    @Excel(databaseFormat = "yyyyMMddHHmmss")
    private Date createTime;

}

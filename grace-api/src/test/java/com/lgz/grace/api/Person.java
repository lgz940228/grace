package com.lgz.grace.api;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lgz on 2019/3/12.
 */
@Data
@NoArgsConstructor
@Accessors
public class Person implements Serializable{
    private String id;

    private String name;

    private String sex;

    private String mobile;

    private Date birthDay;
}

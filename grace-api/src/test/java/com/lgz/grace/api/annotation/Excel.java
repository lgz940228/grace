package com.lgz.grace.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by lgz on 2019/3/12.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    String databaseFormat() default "yyyy-MM-dd HH:mm:ss";

}

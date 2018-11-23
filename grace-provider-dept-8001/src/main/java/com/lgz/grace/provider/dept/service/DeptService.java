package com.lgz.grace.provider.dept.service;

import com.lgz.grace.api.entities.Dept;

import java.util.List;

/**
 * Created by lgz on 2018/11/23.
 */
public interface DeptService {

    public boolean addDept(Dept dept);

    public Dept findById(Integer id);

    public List<Dept> findAll();
}

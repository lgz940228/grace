package com.lgz.grace.provider.dept.service.impl;

import com.lgz.grace.api.entities.Dept;
import com.lgz.grace.provider.dept.dao.DeptDao;
import com.lgz.grace.provider.dept.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lgz on 2018/11/23.
 */
@Service
public class DeptServiceImpl implements DeptService{

    @Autowired
    public DeptDao deptDao;

    @Override
    public boolean addDept(Dept dept) {
        return deptDao.addDept(dept);
    }

    @Override
    public Dept findById(Integer deptId) {
        return deptDao.findById(deptId);
    }

    @Override
    public List<Dept> findAll() {
        return deptDao.findAll();
    }
}

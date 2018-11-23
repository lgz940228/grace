package com.lgz.grace.provider.dept.dao;

import com.lgz.grace.api.entities.Dept;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by lgz on 2018/11/23.
 */
@Mapper
public interface DeptDao {

    public boolean addDept(Dept dept);

    public Dept findById(Integer deptId);

    public List<Dept> findAll();
}

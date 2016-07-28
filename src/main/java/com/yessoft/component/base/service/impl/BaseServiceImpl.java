package com.yessoft.component.base.service.impl;

import com.yessoft.common.PageProxy;
import com.yessoft.component.base.dao.AbstractDao;
import com.yessoft.component.base.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by FTY on 2016/7/13.
 */
public abstract class BaseServiceImpl<T> implements AbstractService<T> {


    public abstract AbstractDao<T> getModelDao();


    @Override
    public List<T> findByProperty(String property, String value) {
        return getModelDao().findByProperty(property,value);
    }

    @Override
    public T findUniqueByProperty(String property, String value){
        List<T> list = getModelDao().findByProperty(property,value);
        return list!=null&&list.size()>0?list.get(0):null;
    }

    @Override
    public List<T> findByProperty(String property, String value, PageProxy pageProxy) {
        return getModelDao().findByProperty(property,value,pageProxy);
    }

    @Override
    public List<T> findByExample(T entity){
        return getModelDao().findByExample(entity);
    }

    @Override
    public List<T> findByExample(T entity, PageProxy pageProxy){
        return getModelDao().findByExample(entity,pageProxy);
    }

    @Override
    public void save(T entity){
        getModelDao().save(entity);
    }

    @Override
    public void update(T entity){
        getModelDao().update(entity);
    }

    @Override
    public Integer update(Map<String, String> map, String sqlWhere){
        return getModelDao().update(map,sqlWhere);
    }

    @Override
    public void delete(T entity){
        getModelDao().delete(entity);
    }
    @Override
    public void deleteById(Serializable id){
        getModelDao().deleteById(id);
    }

    @Override
    public int deleteByProperty(String propertyName, String propertyValue){
        return getModelDao().deleteByProperty(propertyName,propertyValue);
    }

    @Override
    public T findByPrimaryKey(Serializable id){
        return getModelDao().findByPrimaryKey(id);
    }

}

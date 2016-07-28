package com.yessoft.component.base.service;

import com.yessoft.common.PageProxy;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by FTY on 2016/7/13.
 */
public interface AbstractService<T> {

    List<T> findByProperty(String property, String value);

    T findUniqueByProperty(String property, String value);

    List<T> findByProperty(String property, String value, PageProxy pageProxy);

    List<T> findByExample(T entity);

    List<T> findByExample(T entity, PageProxy pageProxy);

    void save(T entity);

    void update(T entity);

    Integer update(Map<String, String> map, String sqlWhere);

    void delete(T entity);

    void deleteById(Serializable id);

    int deleteByProperty(String propertyName, String propertyValue);

    T findByPrimaryKey(Serializable id);


}

package com.yessoft.component.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.yessoft.common.PageProxy;
import com.yessoft.component.base.dao.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.Entity;

/**
 * 
 * @author 付天有 数据访问公共类
 * @param <T>
 */
public class BaseDaoForHibernate<T> implements AbstractDao<T>
{
    
    protected Class<T> clazz;
    
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;
    
    @Override
    public SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }
    
    @Override
    public void setSessionFactory(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
    
    @SuppressWarnings("unchecked")
    public BaseDaoForHibernate()
    {
        // 得到被泛型的实体的class
        clazz = (Class<T>)getSuperClassGenricType(this.getClass(), 0);
    }
    
    @Override
    public Session getSession()
    {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    public void save(T entity)
    {
        getSession().save(entity);
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2012-12-5
     * @功能描述 新增或修改实体，会根据唯一键来自行判断，若传入对象有主键，那么判为update，否则为save
     * @返回值 void
     * @参数
     */
    @Override
    public void saveOrUpdate(T entity)
    {
        getSession().saveOrUpdate(entity);
    }
    
    @Override
    public void update(T entity)
    {
        getSession().update(entity);
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2012-11-28
     * @功能描述 更新DAO所泛型实体
     * @返回值 影响行数
     * @参数 map中KEY为 所要更新的列名，value为更新列的值 sqlwhere为 从and或or后追加的条件
     */
    @Override
    public Integer update(Map<String, String> map, String sqlWhere)
    {
        StringBuffer sql = new StringBuffer();
        sql.append("update ").append(getTableName()).append(" set ");
        Set<String> keySet = map.keySet();
        Iterator<String> it = keySet.iterator();
        while (it.hasNext())
        {
            String column = it.next();
            sql.append(column).append(" = ").append(map.get(column));
            if (it.hasNext())
            {
                sql.append(",");
            }
        }
        sql.append(" where 1=1 ");
        sql.append(sqlWhere);
        return executeNativeSql(sql.toString());
    }
    

    
 /*   @Override
    public int update(Map<String, String> map)
    {
        // TODO Auto-generated method stub
        return update(map, "");
    }*/
    
    /**
     * 删除实体 注：确保实体在session中
     */
    @Override
    public void delete(T entity)
    {
        getSession().delete(entity);
    }
    
    /**
     * 根据ID 删除
     */
    @Override
    public void deleteById(Serializable id)
    {
        deleteByProperty(getSessionFactory().getClassMetadata(clazz).getIdentifierPropertyName(), id.toString());
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2014-3-14
     * @功能描述 根据字段删除
     * @返回值 int
     * @参数
     */
    @Override
    public int deleteByProperty(String propertyName, String propertyValue)
    {
        // TODO Auto-generated method stub
        String sql = "delete from" + getTableName() + " where " + propertyName + "=" + propertyValue;
        return executeNativeSql(sql);
    }
    
    /**
     * @作者 付天有
     * @编写日期 2014-4-23
     * @功能描述 根据多个字段删除数据，参数为空不执行
     * @返回值 int
     * @参数
     */
    @Override
    public int deleteByPropertys(Map<String, Object> propertys)
    {
        if (propertys.size() < 1)
        {
            return -2;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ").append(getTableName()).append(" where 1=1");
        for (String propertyName : propertys.keySet())
        {
            sql.append(" and ").append(propertyName).append(" = ");
            if (propertys.get(propertyName) instanceof String)
            {
                sql.append("'").append(propertys.get(propertyName)).append("'");
            }
            else
            {
                sql.append(propertys.get(propertyName));
            }
        }
        executeNativeSql(sql.toString());
        return 0;
    }
    
    /**
     * @Title: deleteByPropertysAndSqlStr
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param @param propertys
     * @param @return
     * @author 付天有
     * @return int 返回类型
     * @throws
     */
    @Override
    public int deleteByPropertysAndSqlStr(Map<String, Object> propertys, String sqlStr)
    {
        if (propertys.size() < 1)
        {
            return -2;
        }
        StringBuffer sql = new StringBuffer();
        sql.append("delete from ").append(getTableName()).append(" where 1=1");
        for (String propertyName : propertys.keySet())
        {
            sql.append(" and ").append(propertyName).append(" = ");
            if (propertys.get(propertyName) instanceof String)
            {
                sql.append("'").append(propertys.get(propertyName)).append("'");
            }
            else
            {
                sql.append(propertys.get(propertyName));
            }
        }
        sql.append(sqlStr);
        executeNativeSql(sql.toString());
        return 0;
    }

    /**
     * 获取全部表信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll()
    {
        return getSession().createCriteria(clazz).list();
        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(DetachedCriteria criteria)
    {
        Criteria exeCriteria = criteria.getExecutableCriteria(getSession());
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache != null)
        {
            exeCriteria.setCacheable(true);
            exeCriteria.setCacheRegion(cache.region());
        }
        return exeCriteria.list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByCriteria(DetachedCriteria criteria, int from, int limit)
    {
        Criteria exeCriteria = criteria.getExecutableCriteria(getSession());
        exeCriteria.setFirstResult(from);
        exeCriteria.setMaxResults(limit);
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache != null)
        {
            exeCriteria.setCacheable(true);
            exeCriteria.setCacheRegion(cache.region());
        }
        return exeCriteria.list();
    }
    
    /**
     * 根据主键查
     */
    @Override
    @SuppressWarnings("unchecked")
    public T findByPrimaryKey(Serializable id)
    {
        return (T)getSession().get(clazz, id);
    }
    
    /**
     * 根据某个字段查、当涉及到关系映射时候value是关系实体对象 亦可以是字符串或数字
     */
    @Override
    public List<T> findByProperty(String propertyName, Object value)
    {
        return findByCriteria(DetachedCriteria.forClass(clazz).add(Restrictions.eq(propertyName, value)));
    }
    
    /**
     * 根据模板查询。参数为该实体类型的一个实例， 此方法会根据该实例中各个属性的当做等于条件查询
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T entity)
    {
        Criteria criteria = getSession().createCriteria(clazz).add(Example.create(entity));
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache != null)
        {
            criteria.setCacheable(true);
            criteria.setCacheRegion(cache.region());
        }
        return criteria.list();
    }
    
    /**
     * 根据原生SQL 查询 返回结果集 转换
     * 
     * @param sql
     * @param columns 转换类的属性声明，如 map.put("columnsName",Hibernate.type)
     * @param toClass 转换的类
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public List findByNativeSQL(String sql, Map<String, Type> columns, Class toClass)
    {
        // TODO Auto-generated method stub
        SQLQuery query = getSession().createSQLQuery(sql);
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache != null)
        {
            query.setCacheable(true);
            query.setCacheRegion(cache.region());
        }
        Set<String> keys = columns.keySet();
        for (String name : keys)
        {
            query.addScalar(name, columns.get(name));
        }
        if (toClass != null)
        {
            query.setResultTransformer(new AliasToBeanResultTransformer(toClass));
        }
        return query.list();
    }
    
    /**
     * 根据条件查询条数，之后设置projection为null方便后续加入排序或者连接查询
     * 
     * @param criteria
     * @return
     */
    @Override
    public Long getCountByCriteria(DetachedCriteria criteria)
    {
        Long count = (Long)findByCriteria(criteria.setProjection(Projections.rowCount())).get(0);
        criteria.setProjection(null);
        criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        return count;
    }
    
    /**
     * 根据exampl查询记录条数
     * 
     * @param entity
     * @return
     */
    @Override
    public Long getCountByExample(T entity)
    {
        return (Long)findByCriteria(DetachedCriteria.forClass(clazz)
            .add(Example.create(entity))
            .setProjection(Projections.rowCount())).get(0);
    }
    
    /**
     * 本方法只适用于有关系映射的实体间 连接
     * 
     * @param foreignProp 关联表的属性 参照entity类中的属性
     * @param joinType 连接方式，具体值通过Criteria接口中的常量获得
     * @param selfCriterions 本表条件
     * @param otherCriterions 连表条件
     * @return
     */
    @Override
    public List<T> getListJoinOtherByCriteria(String foreignProp, String alias, JoinType joinType,
        List<Criterion> selfCriterions, List<Criterion> otherCriterions)
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        if (selfCriterions != null)
        {
            for (Criterion criterion : selfCriterions)
            {
                criteria.add(criterion);
            }
        }
        DetachedCriteria criteria2 = criteria.createCriteria(foreignProp, alias, joinType);
        if (otherCriterions != null)
        {
            for (Criterion criterion : otherCriterions)
            {
                criteria2.add(criterion);
            }
        }
        return findByCriteria(criteria2);
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2014-3-18
     * @功能描述 根据投影查询 projection 可指定查询列 ，统计，去重复
     * @返回值 List<T>
     * @参数
     */
    @Override
    public List<T> findByCriteriaProjectionsPage(DetachedCriteria criteria, Projection projection, PageProxy pageProxy)
    {
        int maxResults = pageProxy.getRecordOfPage();
        pageProxy.setAllRecord(getCountByCriteria(criteria));
        criteria.setProjection(projection);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria, pageProxy.getFirstResult(), maxResults);
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2014-3-18
     * @功能描述 查询指定列映射到实体
     * @返回值 List<T>
     * @参数 criteria 查询条件 columnsAndAlias 列明映射 当数据库与model的名称不一样是需要设置
     */
    @Override
    public List<T> findSomeColumn(DetachedCriteria criteria, Map<String, String> columnsAndAlias)
    {
        // TODO Auto-generated method stub
        ProjectionList projectionList = Projections.projectionList();
        for (String string : columnsAndAlias.keySet())
        {
            projectionList.add(Projections.property(string).as(columnsAndAlias.get(string)));
        }
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria);
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2014-3-18
     * @功能描述 查询指定列映射到实体
     * @返回值 List<T>
     * @参数 criteria 查询条件 columnsAndAlias 列明映射 pageProxy分页
     */
    @Override
    public List<T> findSomeColumn(DetachedCriteria criteria, Map<String, String> columnsAndAlias, PageProxy pageProxy)
    {
        ProjectionList projectionList = Projections.projectionList();
        for (String string : columnsAndAlias.keySet())
        {
            projectionList.add(Projections.property(string).as(columnsAndAlias.get(string)));
        }
        int maxResults = pageProxy.getRecordOfPage();
        pageProxy.setAllRecord(getCountByCriteria(criteria));
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria, pageProxy.getFirstResult(), maxResults);
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2014-3-18
     * @功能描述 查询指定列映射到实体
     * @返回值 List<T>
     * @参数 criteria 查询条件 columnsAndAlias 列明映射，列名与实体字段名称一样能够匹配
     */
    @Override
    public List<T> findSomeColumn(DetachedCriteria criteria, String[] columnsAndAlias)
    {
        ProjectionList projectionList = Projections.projectionList();
        for (String string : columnsAndAlias)
        {
            projectionList.add(Projections.property(string).as(string));
        }
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria);
    }
    
    @Override
    public List<T> findSomeColumn(DetachedCriteria criteria, String[] columnsAndAlias, int from, int limit)
    {
        ProjectionList projectionList = Projections.projectionList();
        for (String string : columnsAndAlias)
        {
            projectionList.add(Projections.property(string).as(string));
        }
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria, from, limit);
    }
    
    @Override
    public List<T> findSomeColumn(DetachedCriteria criteria, String[] columnsAndAlias, PageProxy pageProxy)
    {
        ProjectionList projectionList = Projections.projectionList();
        for (String string : columnsAndAlias)
        {
            projectionList.add(Projections.property(string).as(string));
        }
        int maxResults = pageProxy.getRecordOfPage();
        pageProxy.setAllRecord(getCountByCriteria(criteria));
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria, pageProxy.getFirstResult(), maxResults);
    }
    
    @Override
    public List<T> findSomeColumn(DetachedCriteria criteria, String[] columnsAndAlias, PageProxy pageProxy, Order order)
    {
        ProjectionList projectionList = Projections.projectionList();
        for (String string : columnsAndAlias)
        {
            projectionList.add(Projections.property(string).as(string));
        }
        int maxResults = pageProxy.getRecordOfPage();
        pageProxy.setAllRecord(getCountByCriteria(criteria));
        criteria.addOrder(order);
        criteria.setProjection(projectionList);
        criteria.setResultTransformer(Transformers.aliasToBean(clazz));
        return findByCriteria(criteria, pageProxy.getFirstResult(), maxResults);
    }
    
    /**
     * 执行本地SQL 返回影响行数
     */
    @Override
    public Integer executeNativeSql(final String sql)
    {
        return getSession().createSQLQuery(sql).executeUpdate();
    }
    
    /**
     * 
     * @作者 付天有
     * @编写日期 2014-3-14
     * @功能描述 获取表名
     * @返回值 String
     * @参数
     */
    @Override
    public String getTableName()
    {
        Entity entity = clazz.getAnnotation(Entity.class);
        return " " + entity.name() + " ";
    }
    
    @Override
    public List<T> findByCriteria(DetachedCriteria criteria, PageProxy pageProxy)
    {
        int limit = pageProxy.getRecordOfPage();
        pageProxy.setAllRecord(getCountByCriteria(criteria));
        List<T> list = findByCriteria(criteria, pageProxy.getFirstResult(), limit);
        return list;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByExample(T entity, int from, int limit)
    {
        // TODO Auto-generated method stub
        Criteria criteria = getSession().createCriteria(clazz).add(Example.create(entity));
        criteria.setFirstResult(from);
        criteria.setFetchSize(limit);
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache != null)
        {
            criteria.setCacheable(true);
            criteria.setCacheRegion(cache.region());
        }
        return criteria.list();
    }
    
    @Override
    public List<T> findByExample(T entity, PageProxy pageProxy)
    {
        pageProxy.setAllRecord(getCountByExample(entity));
        return findByExample(entity, pageProxy.getFirstResult(), pageProxy.getRecordOfPage());
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByProperties(Map<String, Object> properties)
    {
        Criteria criteria = getSession().createCriteria(clazz);
        for (String propName : properties.keySet())
        {
            criteria.add(Restrictions.eq(propName, properties.get(propName)));
        }
        Cache cache = clazz.getAnnotation(Cache.class);
        if (cache != null)
        {
            criteria.setCacheable(true);
            criteria.setCacheRegion(cache.region());
        }
        return criteria.list();
    }
    
    @Override
    public List<T> findByProperties(Map<String, Object> properties, PageProxy pageProxy)
    {
        DetachedCriteria criteria = DetachedCriteria.forClass(clazz);
        for (String propName : properties.keySet())
        {
            criteria.add(Restrictions.eq(propName, properties.get(propName)));
        }
        getCountByCriteria(criteria);
        return findByCriteria(criteria, pageProxy);
    }
    
    @Override
    public List<T> findByProperty(String propertyName, Object value, PageProxy pageProxy)
    {
        // TODO Auto-generated method stub
        return findByCriteria(DetachedCriteria.forClass(clazz).add(Restrictions.eq(propertyName, value)));
    }
    
    @Override
    public List<T> getAll(PageProxy pageProxy)
    {
        // TODO Auto-generated method stub
        return findByCriteria(DetachedCriteria.forClass(clazz), pageProxy);
    }
    
    @Override
    public Long getAllCount()
    {
        
        return getCountByCriteria(DetachedCriteria.forClass(clazz));
    }
    
    @Override
    public org.hibernate.Cache getCache()
    {
        return getSessionFactory().getCache();
    }
    
    public static Class<?> getSuperClassGenricType(Class<?> clazz, int index)
        throws IndexOutOfBoundsException
    {
        java.lang.reflect.Type genType = clazz.getGenericSuperclass();
        
        if (!(genType instanceof ParameterizedType))
        {
            return Object.class;
        }
        
        java.lang.reflect.Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        
        if (index >= params.length || index < 0)
        {
            return Object.class;
        }
        if (!(params[index] instanceof Class<?>))
        {
            return Object.class;
        }
        return (Class<?>)params[index];
    }
    
    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * ADD_BY H_YANG
     */
    public Query createQuery(String hql, Object... values)
    {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        for (int i = 0; i < values.length; i++)
        {
            query.setParameter(i, values[i]);
        }
        return query;
    }
    
    public Query createSqlQuery(String hql)
    {
        Assert.hasText(hql);
        Query query = getSession().createSQLQuery(hql);
        return query;
    }
    
    public Query queryCache(String sql, Object... values)
    {
        Assert.hasText(sql);
        Query query = getSession().createQuery(sql);
        for (int i = 0; i < values.length; i++)
        {
            query.setParameter(i, values[i]);
        }
        query.setCacheable(true);
        return query;
    }
    

    public void flush()
    {
        getSession().flush();
    }
    
    public void clear()
    {
        getSession().clear();
    }
    


}

package com.yessoft.component.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.yessoft.common.PageProxy;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.sql.JoinType;
import org.hibernate.type.Type;


public interface AbstractDao<T> {

	public abstract SessionFactory getSessionFactory();

	public abstract void setSessionFactory(SessionFactory sessionFactory);

	public abstract Session getSession();

	public abstract void save(T entity);

	public abstract void saveOrUpdate(T entity);

	public abstract void update(T entity);

	public abstract Integer update(Map<String, String> map, String sqlWhere);
	

	public abstract Long getAllCount();

	public abstract void delete(T entity);

	public abstract void deleteById(Serializable id);

	public abstract int deleteByProperty(String propertyName,
										 String propertyValue);

	public abstract int deleteByPropertys(Map<String, Object> propertys);
	
	public abstract int deleteByPropertysAndSqlStr(Map<String, Object> propertys, String sqlStr);

	public abstract List<T> getAll();

	public abstract List<T> getAll(PageProxy pageProxy);

	public abstract List<T> findByCriteria(DetachedCriteria criteria);

	public abstract List<T> findByCriteria(DetachedCriteria criteria, int from,
										   int limit);

	public abstract List<T> findByCriteria(DetachedCriteria criteria,
										   PageProxy pageProxy);

	public abstract T findByPrimaryKey(Serializable id);

	public abstract List<T> findByProperty(String propertyName, Object value);

	public abstract List<T> findByProperty(String propertyName, Object value,
										   PageProxy pageProxy);

	public abstract List<T> findByProperties(Map<String, Object> properties);

	public abstract List<T> findByProperties(Map<String, Object> properties,
											 PageProxy pageProxy);

	public abstract List<T> findByExample(T entity);

	public abstract List<T> findByExample(T entity, int from, int limit);

	public abstract List<T> findByExample(T entity, PageProxy pageProxy);

	@SuppressWarnings("unchecked")
	public List findByNativeSQL(String sql, Map<String, Type> columns,
								Class toClass);

	public abstract Long getCountByCriteria(DetachedCriteria criteria);

	public abstract Long getCountByExample(T entity);

	public abstract List<T> getListJoinOtherByCriteria(String foreignProp,
													   String alias, JoinType joinType, List<Criterion> selfCriterions,
													   List<Criterion> otherCriterions);

	public abstract List<T> findByCriteriaProjectionsPage(
			DetachedCriteria criteria, Projection projection,
			PageProxy pageProxy);

	public abstract List<T> findSomeColumn(DetachedCriteria criteria,
										   Map<String, String> columnsAndAlias);

	public abstract List<T> findSomeColumn(DetachedCriteria criteria,
										   Map<String, String> columnsAndAlias, PageProxy pageProxy);

	public abstract List<T> findSomeColumn(DetachedCriteria criteria,
										   String[] columnsAndAlias);

	public abstract List<T> findSomeColumn(DetachedCriteria criteria,
										   String[] columnsAndAlias, int from, int limit);

	public abstract List<T> findSomeColumn(DetachedCriteria criteria,
										   String[] columnsAndAlias, PageProxy pageProxy);

	public abstract List<T> findSomeColumn(DetachedCriteria criteria,
										   String[] columnsAndAlias, PageProxy pageProxy, Order order);

	public abstract Integer executeNativeSql(final String sql);

	public abstract String getTableName();
	
	public Cache getCache();

}
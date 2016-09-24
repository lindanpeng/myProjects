package com.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

public class BaseDaoImpl<T> {
SessionFactory sessionFactory;
@Resource
public void setSessionFactory(SessionFactory sessionFactory) {
	this.sessionFactory = sessionFactory;
}
public void add(T entity){
	Session session = sessionFactory.getCurrentSession();
	session.save(entity);
}
public void delete(T entity){
	Session session = sessionFactory.getCurrentSession();
	session.delete(entity);
}
public void update(T entity){
	Session session = sessionFactory.getCurrentSession();
	session.update(entity);
}
public T get(Class<T> entityClass,Serializable id){
	Session session = sessionFactory.getCurrentSession();
	return (T)session.get(entityClass, id);
}
public List<T> getList(Class<T> entityClass,Map<String,Object> restrictions,int startPos,int size){
	Session session = sessionFactory.getCurrentSession();
	Criteria criteria=session.createCriteria(entityClass);
	if(restrictions!=null){
        for(Map.Entry<String, Object> entry:restrictions.entrySet()){
        	criteria.add(Restrictions.eq(entry.getKey(),entry.getValue()));
        }
	}
	List<T> entities=criteria.setFirstResult(startPos).setMaxResults(size).list();
	return entities;
}
}

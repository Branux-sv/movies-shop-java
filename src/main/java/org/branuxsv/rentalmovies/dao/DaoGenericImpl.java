package org.branuxsv.rentalmovies.dao;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;

/**
* Abstract class that contains the methods of CRUD (read, insert, update and delete entities)
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-09 */

public abstract class DaoGenericImpl<T> implements IDaoGeneric<T>  {

	private EntityManager em = JpaUtil.getEntityManager();
	private Class<T> myType; 	
	private  Logger log;
	
	public DaoGenericImpl()
	{ 
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType)t;
		myType = (Class)pt.getActualTypeArguments()[0];
		log = Logger.getLogger(myType); 
	}
	
	@Override
	public List<T> getAll() {
		return em.createQuery("Select o FROM " + myType.getSimpleName() +  " o").getResultList();
	}

	@Override
	public T get(long id) {
		T obj = em.find(myType, id);
		if (obj != null)
			em.refresh(obj);
		return (T)obj;
	}

	@Override
	public T save(T object) {
		try {
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
	        em.persist(object);
	        em.getTransaction().commit();
			return object;
		} catch (Exception e) {
			log.error("Error in Add ", e);
			if (em != null && em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}		
		return null;
	}

	@Override
	public T update(T object) {
		try {
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
	        em.merge(object);
	        em.getTransaction().commit();
			return object;
		} catch (Exception e) {
			log.error("Error in Update", e);
			if (em != null && em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}				
		return null;
	}

	@Override
	public boolean delete(T object) {
		try {

			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
			
			T aux = object;
			if (!em.contains(object)) {
	            aux = em.merge(object);
	        }						
	        em.remove(aux);
	        em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error("Error in Delete ", e);
			if (em != null && em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}		
		return false;
	}
	
}

package org.branuxsv.rentalmovies.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.Movie;
import org.branuxsv.rentalmovies.model.TokenUser;

/**
* The class holds all Data Access logic:  add, update, delete and get TokenUser Entity Data
* 
* @version  1.0
* @author   Branux-SV
* @Date     2020-04-03 */


public class TokenUserDao implements IDao<TokenUser> {

	private Logger log = Logger.getLogger(TokenUserDao.class);
	
	@Override
	public List<TokenUser> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TokenUser get(long id) {
		EntityManager em =  JpaUtil.getEntityManager();
		TokenUser obj = em.find(TokenUser.class, id);
		if (obj != null)
			em.refresh(obj);
		return obj;
	}

	public TokenUser getTokenByUser(long id) {
		EntityManager em =  JpaUtil.getEntityManager();	
		Query query = em.createQuery("SELECT t FROM TokenUser t WHERE t.user_id = :userId AND t.active = 1");	
		query.setParameter("userId", id);		

		TokenUser objReturn = null;
		
		try {
			objReturn = (TokenUser)query.getSingleResult();
		} catch (NoResultException e) {
			log.error("Error in Dao, existsValidTokenForUser NoResultException", e);			
		}		
		catch (Exception e) {
			log.error("Error in Dao, existsValidTokenForUser Exception", e);			
		}		
		
		return objReturn;
	}
	
	@Override
	public boolean save(TokenUser object) {
		EntityManager em =  JpaUtil.getEntityManager();
		try {			
			
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
	        
			em.persist(object);
	        em.getTransaction().commit();
	        return true;
		} catch (Exception e) {
			log.error("error catched in add like to a movie",e);
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}		
		return false;
	}

	@Override
	public boolean update(TokenUser object) {
		EntityManager em =  JpaUtil.getEntityManager();
		try {
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
	        em.merge(object);
	        em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error("Error in Update tokenUser", e);
			if (em != null && em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}		
		return false;
	}

	@Override
	public boolean updateById(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(TokenUser object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(long id) {
		// TODO Auto-generated method stub
		return false;
	}

}

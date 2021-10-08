package org.branuxsv.rentalmovies.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.Client;
import org.branuxsv.rentalmovies.model.User;
/**
* The class holds all Data Access logic:  add, update, delete and get User Entity Data
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

public class UserDao extends DaoGenericImpl<User> implements IUserDao {

	private Logger log = Logger.getLogger(UserDao.class);
	
	public User authenticate(String username, String PassWord) 
	{
		EntityManager em =  JpaUtil.getEntityManager();	
		Query query = em.createQuery("SELECT u FROM User u WHERE u.username = :user AND u.password = :pass ");	
		query.setParameter("user", username);		
		query.setParameter("pass", PassWord);						

		User objReturn = null;
		
		try {
			objReturn = (User)query.getSingleResult();
			objReturn.setError("OK");
		} catch (NoResultException e) {
			log.error("Error in Dao, authenticate NoResultException", e);	
			objReturn = new User();
			objReturn.setError("NOT_FOUND");
		}		
		catch (Exception e) {
			log.error("Error in Dao, authenticate Exception", e);			
			objReturn = new User();
			objReturn.setError("UNKNOWN_ERROR");
		}		
		return objReturn;
	}

	@Override
	public User existsUserByNameOrEmail(String username, String email) {
		EntityManager em =  JpaUtil.getEntityManager();	
		Query query = em.createQuery("SELECT u FROM User u INNER JOIN Client c ON (c.id_user = u.id_user) WHERE u.username = :user OR  c.email = :email");	
		query.setParameter("user", username);		
		query.setParameter("email", email);		
		User objReturn = new User();		
		try {
			objReturn = (User)query.getSingleResult();
			objReturn.setError("USER_FOUND");	
		} catch (NoResultException e) {
			log.error("Error in Dao, existsUserByNameOrEmail NoResultException", e);
			objReturn.setError("OK");
		}		
		catch (Exception e) {
			log.error("Error in Dao, existsUserByNameOrEmail Exception", e);
			objReturn.setError("UNKNOWN_ERROR");
		}		
		return objReturn;
	}

	@Override
	public User existsUserByConfirmToken(String token) {
		EntityManager em =  JpaUtil.getEntityManager();	
		Query query = em.createQuery("SELECT u FROM User u WHERE u.confirm_token = :token");	
		query.setParameter("token", token);		
		User objReturn = new User();		
		try {
			objReturn = (User)query.getSingleResult();
			objReturn.setError("OK");			
		} catch (NoResultException e) {
			log.error("Error in Dao, existsUserByConfirmToken NoResultException", e);
			objReturn.setError("USER_NOTFOUND");	
		}		
		catch (Exception e) {
			log.error("Error in Dao, existsUserByConfirmToken Exception", e);
			objReturn.setError("UNKNOWN_ERROR");
		}		
		return objReturn;
	}
	

}

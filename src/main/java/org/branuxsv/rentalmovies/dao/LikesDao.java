package org.branuxsv.rentalmovies.dao;

import java.util.List;

import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.Likes;

/**
* The class holds all Data Access logic:  add, update, delete and get likes Entity Data
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-28 */


public class LikesDao implements IDao<Likes>{

	private Logger log = Logger.getLogger(LikesDao.class); 

	@Override
	public List<Likes> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Likes get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(Likes object) {
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
	public boolean delete(Likes object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Likes object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateById(long id) {
		// TODO Auto-generated method stub
		return false;
	}

}

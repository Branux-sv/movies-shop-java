package org.branuxsv.rentalmovies.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.branuxsv.rentalmovies.model.Category;

/**
* The class holds all Data Access logic:  add, update, delete and get Category Entity Data
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

public class CategoryDao implements IDao<Category> {

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getAll() {
		EntityManager em =  JpaUtil.getEntityManager();
	   return em.createQuery("select c from Category c").getResultList();				
	}

	@Override
	public Category get(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean save(Category object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Category object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteById(long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Category object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateById(long id) {
		// TODO Auto-generated method stub
		return false;
	}

}

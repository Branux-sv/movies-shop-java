package org.branuxsv.rentalmovies.dao;

import java.util.List;

/**
* public interface to expose the common DAO methods (Obsolete)
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-25 */

public interface IDao<T> {
	public List<T> getAll();
	public T get(long id);	
	public boolean save(T object);
	public boolean update(T object);	
	public boolean updateById(long id);		
	public boolean delete(T object);
	public boolean deleteById(long id);		
}

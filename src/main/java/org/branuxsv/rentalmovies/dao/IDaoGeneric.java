package org.branuxsv.rentalmovies.dao;

import java.util.List;

/**
* public interface to expose the common DAO methods 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-27 */

public interface IDaoGeneric<T> {

	public List<T> getAll();
	public T get(long id);	
	public T save(T object);
	public T update(T object);	
	public boolean delete(T object);
	
}

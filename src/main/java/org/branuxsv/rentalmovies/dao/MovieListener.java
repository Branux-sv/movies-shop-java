package org.branuxsv.rentalmovies.dao;

import javax.persistence.PreUpdate;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.Movie;

/**
* Listener of the entity Movie, listen of change of data specifically the update of the entity
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-02 */


public class MovieListener {

	private Logger log = Logger.getLogger("audit");
	
	@PreUpdate
	public void moviePreUpdate(Movie movie)
	{
		MovieDao dao = new MovieDao();
		Movie movieOld = dao.get(movie.getId_movie());
		if (!movieOld.getTitle().equalsIgnoreCase(movie.getTitle()) 
				|| movieOld.getRental_price() != movie.getRental_price() 
				||  movieOld.getSale_price() != movie.getSale_price())
		{
			log.info("There is change in Title or/and rental price or/and sale price audit is raise...");
			log.info("The user who change was: " + movie.getUserName());
			log.info("Old title: " + movieOld.getTitle() + " new title: " + movie.getTitle());			
			log.info("Old rental price: " + movieOld.getRental_price() + " new rental price: " + movie.getRental_price());			
			log.info("Old sale price: " + movieOld.getSale_price() + " new sale price: " + movie.getSale_price());			
		}
	}
	
	
	
}

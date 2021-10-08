package org.branuxsv.rentalmovies.service;


import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dao.ClientDao;
import org.branuxsv.rentalmovies.dao.LikesDao;
import org.branuxsv.rentalmovies.dao.MovieDao;
import org.branuxsv.rentalmovies.model.Client;
import org.branuxsv.rentalmovies.model.Likes;
import org.branuxsv.rentalmovies.model.Movie;
import org.branuxsv.rentalmovies.util.MyConstants;

/**
* Service class that contains methods of bussiness rules about likes entity 
* and connect DAO objects with APi methods  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-29 */

public class LikesService {


	private Logger log = Logger.getLogger(LikesService.class);
			
	public String addLike(Likes obj)
	{
		String result = "";
		LikesDao objDao = new LikesDao();
		MovieDao movieDao = new MovieDao();
		Movie movie  = movieDao.get(obj.getId_movie());
		if (movie != null)
		{
			if (movie.getAvailability() == MyConstants.CODE_AVAILABILITY)
			{
				ClientDao daoClient = new ClientDao();
				Client data = daoClient.getByIdUser(obj.getId_client());
				if (data != null)
				{
					obj.setId_client(data.getId_client());
					
					if (objDao.save(obj))
						result ="OK";
					else
						result ="ERR";										
				}
				else
				{
					result ="ERR";	
					log.debug("Not Found client data according to the ID User");
				}
					
			}			
			else
			{
				log.debug("Movie found but is Unavailability ");
				result ="UNAVA";							
			}
		}
		else
		{
			log.debug("Movie not found with id: " +  obj.getId_movie());
			result ="NOTFM"; //For time topic I cant create a enumeration... 			
		}
	
		return result;
	}
}

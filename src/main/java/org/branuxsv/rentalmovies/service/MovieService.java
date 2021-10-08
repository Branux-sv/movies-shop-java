package org.branuxsv.rentalmovies.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dao.MovieDao;
import org.branuxsv.rentalmovies.dto.MovieGeneralDto;
import org.branuxsv.rentalmovies.model.Movie;
import org.branuxsv.rentalmovies.util.MyConstants;
import org.branuxsv.rentalmovies.util.Util;

/**
* Service class that contains methods of bussiness rules about movie entity 
* and connect DAO objects with APi methods  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

public class MovieService {

	private Logger log = Logger.getLogger(MovieService.class); 
	
	public List<MovieGeneralDto> getAllMoviesWithFilters(int disponibilidad, String title, String sortBy, String page, String pagesize)
	{
		MovieDao objDao = new MovieDao();
		List<Movie> auxList = objDao.getAllWithFilters(disponibilidad, title, sortBy, page, pagesize);				
		List<MovieGeneralDto> auxListDto = new ArrayList();
			
		Optional.ofNullable(auxList).orElse(Collections.emptyList())
		 .forEach(item-> 
		 {
		     //Convert to DTO object
			 MovieGeneralDto objMovieDto = new MovieGeneralDto();
			 objMovieDto.setId_movie(item.getId_movie());
			 objMovieDto.setCategory(item.getCategory());
			 objMovieDto.setTitle(item.getTitle());
			 
			 auxListDto.add(objMovieDto);
		 });
		
		
		return auxListDto;
	}

	
	public Movie getMovieDetail(long id)
	{
		MovieDao objDao = new MovieDao();				
		Movie objMovie = objDao.get(id);
		
		if (objMovie != null)
		{
			if (objMovie.getAvailability() == MyConstants.CODE_AVAILABILITY)
			{
				return objMovie;		
			}
			else
				log.info("Movie found but is unavaibility");
		}	
		else
			log.info("Movie not found for ID: " + id);
		
		return null;
	}
	
	//Note: The best solution is used an Object Storage services like AWS S3
	public byte[] getMovieImage(long id)
	{
		MovieDao objDao = new MovieDao();		
		Movie objMovie = objDao.get(id);
		
		if (objMovie != null)
		{
			if (objMovie.getAvailability() == MyConstants.CODE_AVAILABILITY)
			{
				return objMovie.getImage();		
			}
			else
				log.info("Image found but movie its unavaibility");
		}	
		else
			log.info("Image not found for movie with ID: " + id);
		return null;
	}

	
	public boolean addMovie(Movie movie)
	{
		MovieDao objDao = new MovieDao();		
		return objDao.save(movie);
	}

	public String addMovie(Movie movie, byte[] file)
	{
		String result = "";		
		MovieDao objDao = new MovieDao();		
		Util util = new Util();		
		try {
			movie.setImage(file);
			log.debug("setting image bytes OK");
			log.debug("image bytes size: " + movie.getImage().length);
		} catch (Exception e) {
			log.error("Error getting image bytes", e);
			result = "ERRIM";
		}				
		if (result.isEmpty())
		{
			//Validate the data
			if (util.countInvalidData(movie) == 0)
			{
				log.debug("The data is valid");
				movie.setAvailability(MyConstants.CODE_AVAILABILITY);
				if (objDao.save(movie))
				{
					result = "OK";
					log.debug("Success create new movie");
				}
				else
				{
					result = "ERR";
					log.debug("Ohh no there was an error, adding movie");
				}
			}
			else
			{
				result = "DATAIN";
				log.debug("The data is Invalid");
			}			
		}								
		return result;
	}
	
	public String deleteMovie(long id)
	{
		String result = "";
		MovieDao objDao = new MovieDao();		
		Movie movie = objDao.get(id);
		if (movie != null)
		{
			if (objDao.delete(movie))
				result = "OK";
			else
				result = "ERR";				
		}		
		else
		{
			log.debug("Movie not found with ID: " + id);
			result = "NOTF";
		}
	
		return result;
	}

	public String updateMovie(Movie movie) 
	{
		String result = "";		
		MovieDao objDao = new MovieDao();		

		Movie auxMovie = objDao.get(movie.getId_movie());
		if (auxMovie != null)
		{
			Util util = new Util();				
			util.copyMovieToUpdate(auxMovie, movie);
			if (objDao.update(auxMovie))
			{
				result = "OK";
				log.debug("Success update movie");
			}
			else
			{
				result = "ERR";
				log.debug("Ohh no there was an error, trying to update the movie");
			}					
		}
		else
		{
			log.debug("Movie not found with ID: " + movie.getId_movie());
			result = "NOTF";
		}
		
		return result;
	}
}

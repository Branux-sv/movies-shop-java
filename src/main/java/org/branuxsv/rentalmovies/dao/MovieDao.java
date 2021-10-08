package org.branuxsv.rentalmovies.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.*;
import org.branuxsv.rentalmovies.util.Util;
import org.eclipse.persistence.config.QueryHints;

/**
* The class holds all Data Access logic:  add, update, delete and get Movie Entity Data
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

public class MovieDao implements IDao<Movie> {

	private static Logger log = Logger.getLogger(MovieDao.class); 
	
	public static void main(String[] args) {
		System.out.println("****** iniciando main en movieDao   ******");
		
		/*
		EntityManager em =  JpaUtil.getEntityManager();
			
		//Error: The join association path is not a valid expression.
		//String strSQL = "SELECT m FROM Movie m LEFT JOIN  (SELECT l.id_movie FROM Likes l) tl ON tl.id_movie = m.id_movie WHERE m.availability = :dis";
		
		//OK
		//String strSQL = "SELECT m FROM Movie m, (SELECT l.id_movie, COUNT(l.id_client) total FROM Likes l GROUP BY l.id_movie) tl WHERE tl.id_movie = m.id_movie AND m.availability = :dis";

		//Ok 
		//String strSQL = "SELECT m FROM Movie m LEFT JOIN Likes l ON l.id_movie = m.id_movie WHERE m.availability = :dis";

		//CASI PERO NO
		//String strSQL = "SELECT m FROM Movie m LEFT JOIN m.likes l WHERE m.availability = :dis GROUP BY m.id_movie ORDER BY COUNT(m.likes), m.title ASC";
		
		//OK-PERFECT!!!
		//String strSQL = "SELECT m FROM Movie m LEFT OUTER JOIN Likes l ON (m.id_movie = l.id_movie) WHERE m.availability = :dis GROUP BY m.id_movie ORDER BY COUNT(l.id_client) DESC ,m.title ASC";
		
		//PAGINATION
		String strSQL = "SELECT m FROM Movie m WHERE m.availability = :dis ";

		
		Query query = em.createQuery(strSQL);	
		query.setParameter("dis", 1);

		//Pagination
		query.setFirstResult(1);
		query.setMaxResults(3);
		
		for(Movie it : (List<Movie>)query.getResultList())
		{
			System.out.println("--> Title Movie: " +  it.getTitle());	
		}
		*/
		//EntityManager em =  JpaUtil.getEntityManager();
		//Movie  obj2 = em.find(Movie.class, 1L);
/*
		String json = "";
		try {
			
			Gson gson = new Gson();
				//Movie dum = new Movie();
			//dum.setTitle("a la gran precia");
			//json = gson.toJson(obj2);	
			Movie obj = new Movie();
			obj.setDescription("This a imaginary description");
			obj.setTitle("Saw 110");
			obj.setStock(12);
			obj.setSale_price(12.99);
			obj.setId_category(5);
			obj.setPenalty_fee(3.25);
			obj.setRental_price(6.5);
			obj.setImage("lala".getBytes());

			Util util = new Util(); 
			
			try {
				

				if (!em.getTransaction().isActive())
					em.getTransaction().begin();
		        em.persist(obj);		        
		        em.getTransaction().commit();
		        //em.flush();

		        Movie auxMovie = em.find(Movie.class, 23L);
		        //json = gson.toJson(auxMovie);
		        em.refresh(auxMovie);
				System.out.println("--> THis is Category object inserterd row " + auxMovie.getCategory().getName());	
				//System.out.println("--> THis is the JSON the Inserted Row " + json);	
				

				
		        Movie auxMovie2 = em.find(Movie.class, 1L);
				System.out.println("--> THis is Category object from existing Movie:  " + auxMovie2.getCategory().getName());	
		        //json = gson.toJson(auxMovie2);
				//System.out.println("--> THis is the JSON of Existen Movie: " + json);
				
				
			} catch (Exception e) {
				log.error("DEGUB Error in Add Movie", e);
				if (em != null && em.getTransaction().isActive())			
					em.getTransaction().rollback();
			}

			
			
			//System.out.println("--> size from Util " + util.countInvalidData(obj) );
			//System.out.println("---> JSON: "+ gson.toJson(obj));
			
			/*
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			
			Set<ConstraintViolation<Movie>> list = validator.validate(obj);
			
			
			for (ConstraintViolation<Movie> violation : list) {
				System.out.println("--> error violation: " + violation.getMessage());
			    //log.error(violation.getMessage()); 
			}		

			
		} catch (Exception e) {
			log.error("eror en save", e);
		}
		*/
		/*
	    BasicConfigurator.configure();

		Logger loger = Logger.getLogger("audit"); 
		Logger log = Logger.getLogger(MovieDao.class);
		
		loger.info("PREBANDO BMAROQUIN");
		log.debug("BMAROQUIN debug");
		log.info("BMAROQUIN info");	
		*/
		
		EntityManager em =  JpaUtil.getEntityManager();
		
		Movie obj = em.find(Movie.class, 27L);
		
		/*
		BeanUtilsBean.getInstance().getConvertUtils().register(true, false, 0);		
		BeanUtils.copyProperties(movie, auxMovie);
		*/
		
		Movie obj2 = new Movie();
		obj2.setId_movie(27L);
		
		obj2.setTitle("Saw Update 850");
		obj2.setSale_price(66.66);
		
		obj2.setUserName("Bmarroquin");
		Util util = new Util();
		
		util.copyMovieToUpdate(obj, obj2);
		
		System.out.println("--> Title " + obj.getTitle() );		
		System.out.println("--> Description " + obj.getDescription() );		
/*
		if (!em.getTransaction().isActive())
			em.getTransaction().begin();
        em.merge(obj2);		        
        em.getTransaction().commit();
        */
		System.out.println("--> End " + obj2.getTitle() );		
		
		
	}
	
	@Override
	public List<Movie> getAll() {
		EntityManager em =  JpaUtil.getEntityManager();
		return em.createQuery("SELECT m FROM Movie m").getResultList();	
	}

	public List<Movie> getAllWithFilters(int disponibilidad, String title, String sortBy, String page, String pagesize) {
		EntityManager em =  JpaUtil.getEntityManager();
		String strSQL = "SELECT m FROM Movie m  ";
		
		if ("likes".equalsIgnoreCase(sortBy))
			strSQL = "SELECT m FROM Movie m LEFT OUTER JOIN Likes l ON (m.id_movie = l.id_movie)  ";

		if (disponibilidad == -1) //All movies availability and Unavailability
			  strSQL += " WHERE m.availability = m.availability ";		
		else //Specific 
			  strSQL += " WHERE m.availability = :dis ";		
		
		if (title != null && !title.isEmpty())
		  strSQL += " AND m.title LIKE :title ";		

		if (!"title".equals(sortBy) && !"likes".equals(sortBy))
			sortBy = "title";
		
		if ("title".equalsIgnoreCase(sortBy))
		  strSQL += " ORDER BY m.title ";						
		else
	      strSQL += " GROUP BY m.id_movie ORDER BY COUNT(l.id_client) DESC ,m.title ASC ";						
		
		Query query = em.createQuery(strSQL);	
		
		if (disponibilidad != -1)
			query.setParameter("dis", disponibilidad);
		
		if (title != null && !title.isEmpty())
			query.setParameter("title", "%" +  title + "%");

		//Pagination
		int pageNumber;		
		int pageSize;
		
		try {
			pageNumber = Integer.parseInt(page);
		} catch (Exception e) {
			pageNumber = 1;
		}
	
		try {
			pageSize  = Integer.parseInt(pagesize);
		} catch (Exception e) {
			pageSize = 10;
		}
	
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);		
		query.setHint(QueryHints.REFRESH, true); //To Fix After insert a movie the category data is not get
		return query.getResultList();	
	}

	
	@Override
	public Movie get(long id) {
		EntityManager em =  JpaUtil.getEntityManager();
		Movie obj = em.find(Movie.class, id);
		if (obj != null)
			em.refresh(obj); //This is to fix the problem that dont refresh category data after insert...		 
		return obj;
	}

	
	@Override
	public boolean save(Movie object) {
		EntityManager em =  JpaUtil.getEntityManager();
		try {
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
	        em.persist(object);
	        em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error("Error in Add Movie", e);
			if (em != null && em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}		
		return false;
	}

	@Override
	public boolean delete(Movie object) {
		EntityManager em =  JpaUtil.getEntityManager();
		try {

			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
			
			Movie aux = object;
			if (!em.contains(object)) {
	            aux = em.merge(object);
	        }						
	        em.remove(aux);
	        em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error("Error in Delete Movie", e);
			if (em != null && em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}		
		return false;
	}

	@Override
	public boolean deleteById(long id) {
		EntityManager em =  JpaUtil.getEntityManager();
		try {
			Movie obj = em.find(Movie.class, id);
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();

	        em.remove(obj);
	        em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error("Error in Delete Movie", e);
			if (em.getTransaction().isActive())			
				em.getTransaction().rollback();
		}			
		return false;
	}
	
	@Override
	public boolean update(Movie object) {
		EntityManager em =  JpaUtil.getEntityManager();
		try {
			if (!em.getTransaction().isActive())
				em.getTransaction().begin();
	        em.merge(object);
	        em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			log.error("Error in Update Movie", e);
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


}

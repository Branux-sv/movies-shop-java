package org.branuxsv.rentalmovies.dao;

import javax.persistence.Persistence;

import org.apache.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;

/**
* Util class the create a EntityManager from the factory
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-25 */

public class JpaUtil {
	private static final EntityManagerFactory emFactory;
	private static final String UNIT_NAME = "JpaForAPI";
	private static Logger log = Logger.getLogger(JpaUtil.class);
	
    static {
    	emFactory = Persistence.createEntityManagerFactory(UNIT_NAME);			
    }
	
	public static EntityManager getEntityManager(){
		log.debug("-> Im in method getEntityManager()");
		try {
			return emFactory.createEntityManager();			
		} catch (Exception e) {
			log.error("Error in getEntityManager of JpaUtil :( ", e);
		}
		return null;
	}
	public static void close(){
		emFactory.close();
	}	
}

package org.branuxsv.rentalmovies.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.Client;

/**
* The class holds all Data Access logic:  add, update, delete and get client Entity Data
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-27 */


public class ClientDao extends DaoGenericImpl<Client> {

	private Logger log = Logger.getLogger(ClientDao.class);

	public Client getByIdUser(long idUser) {
		EntityManager em =  JpaUtil.getEntityManager();	
		Query query = em.createQuery("SELECT c FROM Client c WHERE c.id_user = :idUser AND c.active = 1");	
		query.setParameter("idUser", idUser);		
		
		Client client = null;
		
		try {
			client = (Client)query.getSingleResult();
		} catch (NoResultException e) {
			log.error("Error in Dao, getClientByIdUser", e);
		}		
		
		return client;
	}

}

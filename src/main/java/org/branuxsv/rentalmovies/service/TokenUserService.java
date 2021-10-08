package org.branuxsv.rentalmovies.service;

import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dao.TokenUserDao;
import org.branuxsv.rentalmovies.model.TokenUser;

/**
* Service class that contains methods of bussiness rules about TokenUser entity 
* and connect DAO objects with APi methods  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-05 */


public class TokenUserService {

	private Logger log = Logger.getLogger(TokenUserService.class);

	
	public boolean existsValidTokenForUser(long userId)
	{
		TokenUserDao dao = new TokenUserDao();
		TokenUser objReturn = dao.getTokenByUser(userId);		
		if (objReturn != null)
		{
			log.debug("Token Found, experitation time: " + objReturn.getDate_time());			
			if (objReturn.getDate_time().isBefore(LocalDateTime.now()))
			{
				log.debug("Token has expired");			
				///Process to invalid token...
				objReturn.setActive(0);
				if (dao.update(objReturn))
					log.debug("Token have been invalidate");			
				else
					log.debug("Token Cant be invalidate there was an error");						
				return false;
			}
			else
			{
				log.debug("The current user have a valid and active Token");			
				return true;				
			}
		}
		else
			log.debug("Not active Token found");
		
		return false;
	}
	
	public String addTokenUser(TokenUser tokenUser)
	{
		String result = "";		
		TokenUserDao dao = new TokenUserDao();
		if (dao.save(tokenUser))
		{
			result = "OK";
			log.debug("Success save token");
		}
		else
		{
			result = "ERR";
			log.debug("Ohh no there was an error, saving token");
		}		
		return result;
	}
	
	public String invalidateTokenUser(long userId)
	{
		String result = "";		
		TokenUserDao dao = new TokenUserDao();
		TokenUser obj = dao.getTokenByUser(userId);	
		obj.setActive(0);		
		if (dao.update(obj))
		{
			result = "OK";
			log.debug("Success logOut");
		}
		else
		{
			result = "ERR";
			log.debug("Ohh no there was an error, LogOut");
		}		
		return result;
	}
	
}

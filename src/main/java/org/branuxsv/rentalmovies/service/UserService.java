package org.branuxsv.rentalmovies.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dao.RoleDao;
import org.branuxsv.rentalmovies.dao.UserDao;
import org.branuxsv.rentalmovies.dto.FullUserDto;
import org.branuxsv.rentalmovies.dto.UserDto;
import org.branuxsv.rentalmovies.model.Client;
import org.branuxsv.rentalmovies.model.Role;
import org.branuxsv.rentalmovies.model.User;
import org.branuxsv.rentalmovies.util.MyConstants;
import org.branuxsv.rentalmovies.util.SecurityUtil;
import org.branuxsv.rentalmovies.util.Util;


/**
* Service class that contains methods of bussiness rules about User entity 
* and connect DAO objects with APi methods  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-29 */

public class UserService {

	private Logger log = Logger.getLogger(UserService.class); 
		
	public UserDto authenticate(String username, String PassWord) {
		UserDao objDao = new UserDao(); 
		UserDto objUser = new UserDto();
		
		String encodePass = "";
		
		try {
			encodePass = SecurityUtil.encryptString(PassWord);
		}  
		catch (Exception e) {
			log.error("Error in authenticate when encrypt pass", e);
			encodePass = "-1";
		}		
		User user = objDao.authenticate(username, encodePass);		
		if ("OK".equals(user.getError()))
		{
			if (user.getConfirmed() == 0 && user.getConfirmed_on() == null)
				objUser.setError("NO_CONFIRMED");
			else
			{
				if (user.getActive() == 0)
					objUser.setError("NO_ACTIVE");
				else
				{
					objUser.setId_user(user.getId_user());
					objUser.setUsername(user.getUsername());
					if (user.getRole() != null)
						objUser.setId_role(user.getRole().getId_role());				
					else
						objUser.setId_role(user.getId_role());				
					objUser.setError(user.getError());									
				}
			}
		}
		else
			objUser.setError(user.getError());
		
		return objUser;
	}
	
	public User registerUser(FullUserDto newUser)
	{
		User user = new User();		
		String result = "";				
		
		if (newUser.getPassword() == null )
			result =  "PASS-BAD";
		
		if (newUser.getPassword().isEmpty())
			result = "PASS-BAD";

		user.setError(result);

		if (!result.isEmpty())
			return user;

		UserDao dao = new UserDao();		
		Client client = new Client();
		Util util = new Util();
		
		user.setUsername(newUser.getUsername());
		try {
			user.setPassword(SecurityUtil.encryptString(newUser.getPassword()));
		} catch (NoSuchAlgorithmException e) {
			result = "ERROR_PASS";
			log.error("Error encrypt the password", e);
		} catch (IOException e) {
			log.error("Error encrypt the password", e);
			result = "ERROR_PASS";					
		}		
		user.setConfirm_token(UUID.randomUUID().toString().replace("-", ""));
		user.setConfirmed(0);
		user.setConfirm_token_expiration(LocalDateTime.now().plusDays(MyConstants.EXPIRATION_DAYS_CONFIRMED_REGISTRATION));						
		user.setId_role(newUser.getId_role());

		if (util.countInvalidData(user) == 0)
		{
			client.setActive(0);
			client.setFirst_name(newUser.getFirstName());
			client.setLast_name(newUser.getLastName());
			client.setEmail(newUser.getEmail());
			client.setPhone(newUser.getPhone());
			client.setAddress(newUser.getAddress());
			
			if (util.countInvalidData(user) == 0)
			{
				User auxUser = dao.existsUserByNameOrEmail(user.getUsername(), client.getEmail());
				if ("OK".equals(auxUser.getError()))
				{
					user.setClient(client);
					client.setUser(user);
					
					user = dao.save(user);				
					if (user != null)
						result = "OK";					
					else
						result = "ERROR_SAVE";										
				}
				else
					result = auxUser.getError();									
			}
			else
				result = "DATA_INV";					
		}
		else
			result = "DATA_INV";		
		
		user.setError(result);
		return user;
	}
		
	public User validateConfirmToken(String token)
	{
		UserDao dao = new UserDao();		
		User auxUser = dao.existsUserByConfirmToken(token);
		
		if ("OK".equals(auxUser.getError()))
		{
			//validate expiration time
			log.debug("DEBUG-BM: fecha expiration token: " +auxUser.getConfirm_token_expiration());
			if (auxUser.getConfirm_token_expiration().isBefore(LocalDateTime.now()))
				auxUser.setError("TOKEN_EXP");
			else
			{
				if (auxUser.getConfirmed() == MyConstants.CODE_AVAILABILITY && auxUser.getConfirmed_on() != null)
					auxUser.setError("CONFIRM_ALREADY");									
				else
				{
					auxUser.getClient().setActive(MyConstants.CODE_AVAILABILITY);
					auxUser.setActive(MyConstants.CODE_AVAILABILITY);
					auxUser.setConfirmed(MyConstants.CODE_AVAILABILITY);
					auxUser.setConfirmed_on(LocalDateTime.now());
					
					auxUser =dao.update(auxUser);
					if (auxUser != null)
						auxUser.setError("OK");
					else
					{
						auxUser = new User();
						auxUser.setError("ERROR_UPDATE");										
					}
					
				}
			}			
		}
		
		return auxUser;
	}
	
	public User forgotPassword(String emailOrUserName)
	{
		//NOTE: What Happen if the user is inactive or is confirmed off ???
		UserDao dao = new UserDao();
		User user = dao.existsUserByNameOrEmail(emailOrUserName, emailOrUserName);
		if ("USER_FOUND".equals(user.getError()))
		{
			String auxEcript = "-1";
			user.setData(String.valueOf(Util.getRandomNumber(1, 1000000)));
			
			try {
				auxEcript = SecurityUtil.encryptString(user.getData());
			} catch (Exception e) {
				log.error("error encrypt",e);
			}
			
			if (!"-1".equals(auxEcript))
			{
				user.setPassword(auxEcript);
				user = dao.update(user);
				if (user == null)
					user.setError("ERROR_SAVE");
				else
					user.setError("VALID");					
			}
			else
				user.setError("ERROR_PASS");
		}
		
		return user;
	}

	public String updateUserRole(UserDto userDto)
	{
		String result = "";
		if (userDto.getId_user() <= 0 || userDto.getId_role() <= 0)
			return "DATA_INVALID";
				
		UserDao dao = new UserDao();
		User user = dao.get(userDto.getId_user());
		if (user != null)
		{
			if (user.getConfirmed() == MyConstants.CODE_AVAILABILITY && user.getActive() == MyConstants.CODE_AVAILABILITY)
			{
				RoleDao daoRol = new RoleDao();
				Role rol = daoRol.get(userDto.getId_role());
				if (rol != null)
				{
					user.setId_role(userDto.getId_role());
					user = dao.update(user);
					if (user != null)
						result = "OK"; 			
					else 
						result = "ERROR_UPDATE"; 			
				}
				else
					result = "ROLE_NOTFOUND"; 			
			}
			else
				result = "USER_INACTIVE"; 			
		}
		else
			result = "USER_NOTFOUND"; 
		
		return result;				
	}
}

package org.branuxsv.rentalmovies.util;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.branuxsv.rentalmovies.dto.UserDto;

/**
* Class that expose a series of methods about security logic  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-02 */

public class SecurityUtil {

	private static final String ENCRYPT_ALGORITHM = "ENCRYPT_ALGORITHM";
	
	public static String encryptString(String plainText) throws NoSuchAlgorithmException, IOException
	{
		Util util = new Util();		
	    MessageDigest md = MessageDigest.getInstance(util.getConfigValueByKey(ENCRYPT_ALGORITHM));	
	    
	    byte[] msjDigest = md.digest(plainText.getBytes());
	    
	    BigInteger no = new BigInteger(1, msjDigest);
	    
	    String hashText = no.toString(16);
	    
	    while (hashText.length() < 32)
	    {
	    	hashText = "0" + hashText;
	    }
	    
	    return hashText;	    
	}

	public static boolean isAdminRole(String strUserDto)
	{
		String[] arrValues = strUserDto.split("\\|");
		if (arrValues != null && arrValues.length > 0)
		{				
			if ("1".equals(arrValues[2]))
			{
				return true;
			}
		}
		return false;
	}
	
	public static UserDto getUserDtoData(String strUserDto)
	{
		UserDto user = null ;
		
		String[] arrValues = strUserDto.split("\\|");
		if (arrValues != null && arrValues.length > 0)
		{				
			user = new UserDto();
			user.setId_user(Long.parseLong(arrValues[0]));
			user.setUsername(arrValues[1]);
			user.setId_role(Long.parseLong(arrValues[2]));			
		}
		return user;
	}
	
	
}

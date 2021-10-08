package org.branuxsv.rentalmovies.dto;

import java.io.Serializable;

/**
* The class holds properties of Token response to transport 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-10 */


public class LoginResponseDto implements Serializable{

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;
	
	
	public LoginResponseDto(String jwttoken) {
		this.jwttoken = jwttoken;
	}


	public String getJwttoken() {
		return jwttoken;
	}
	
	
	
	
}

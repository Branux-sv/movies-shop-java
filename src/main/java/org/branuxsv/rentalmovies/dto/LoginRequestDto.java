package org.branuxsv.rentalmovies.dto;

/**
* The class holds properties of Login Information to transport 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-10 */

public class LoginRequestDto {

	private String username;
	private String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
		
	
}

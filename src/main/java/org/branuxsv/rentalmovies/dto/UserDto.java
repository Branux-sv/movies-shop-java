package org.branuxsv.rentalmovies.dto;

/**
* The class holds properties of User to transport 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-01 */

public class UserDto {

	private long id_user;
	private String username;	
	private long id_role;
    private transient String error;
    
	public long getId_user() {
		return id_user;
	}
	public void setId_user(long id_user) {
		this.id_user = id_user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getId_role() {
		return id_role;
	}
	public void setId_role(long id_role) {
		this.id_role = id_role;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}		
	
}

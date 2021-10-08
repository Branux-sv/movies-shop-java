package org.branuxsv.rentalmovies.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* Entity class for model the tokenUser table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-03 */

@Entity
@Table(name ="token_user")
public class TokenUser {

	@Id
	@Column(name="user_id")
	private long user_id;
	
	@Id
	@Column(name="token_id")
	private String token_id;
	
	@Column(name="date_time")
	private LocalDateTime date_time = LocalDateTime.now();
	
	@Column(name="active")
	private int active;

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

	public LocalDateTime getDate_time() {
		return date_time;
	}

	public void setDate_time(LocalDateTime date_time) {
		this.date_time = date_time;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	

}

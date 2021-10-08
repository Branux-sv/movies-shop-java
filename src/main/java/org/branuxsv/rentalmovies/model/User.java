package org.branuxsv.rentalmovies.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
* Entity class for model the user table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-29 */

@Entity
@Table(name ="user")
public class User {
	
	@Id
	@Column(name="id_user")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_user;
	
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name="id_role", referencedColumnName = "id_role", insertable = false, updatable = false)	
	private Role role;
    
    @NotNull(message = "The role Id can't be null")
    @Positive(message = "The role Id can't be zero or negative")
    @Digits(integer = 11, fraction = 0, message = "The role Id can't be have fraction")
	@Column(name="id_role")
	private long id_role;	
	
	@NotBlank(message = "The userName can't be null or empty")
	@Column(name="username")
	private String username;
	
	@NotBlank(message = "The password can't be null or empty")
	@Column(name="password")
	private String password;

	@Column(name="active")
	private int active;
	
	@Column(name="created_date")
	private LocalDateTime created_date = LocalDateTime.now();;
	
	@Column(name="modified_date")
	private LocalDateTime modified_date;

	@OneToOne(mappedBy = "user",cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, optional = false)
	private Client client;

	@Column(name="confirmed")
	private int confirmed;

	@Column(name="confirmed_on")
	private LocalDateTime confirmed_on;

	@NotBlank(message = "The confirm token can't be null or empty")
	@Column(name="confirm_token")
	private String confirm_token;
	
	@NotNull(message = "The token expiration date can't be null or empty")
	@Column(name="confirm_token_expiration")
	private LocalDateTime confirm_token_expiration;

	private transient String error;
	private transient String data;
	
	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public Role getRole() {
		return role;
	}

	public void setRol(Role rol) {
		this.role = rol;
	}

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

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public LocalDateTime getCreated_date() {
		return created_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.created_date = created_date;
	}

	public LocalDateTime getModified_date() {
		return modified_date;
	}

	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public int getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}

	public LocalDateTime getConfirmed_on() {
		return confirmed_on;
	}

	public void setConfirmed_on(LocalDateTime confirmed_on) {
		this.confirmed_on = confirmed_on;
	}

	public String getConfirm_token() {
		return confirm_token;
	}

	public void setConfirm_token(String confirm_token) {
		this.confirm_token = confirm_token;
	}

	public LocalDateTime getConfirm_token_expiration() {
		return confirm_token_expiration;
	}

	public void setConfirm_token_expiration(LocalDateTime confirm_token_expiration) {
		this.confirm_token_expiration = confirm_token_expiration;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}		
	
}

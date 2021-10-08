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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
* Entity class for model the client table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-27 */

@Entity
@Table(name ="client")
public class Client {

	@Id
	@Column(name="id_client")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_client;
	
	@NotBlank(message = "The frist Name can't be null or empty")
	@Column(name="first_name")
	private String first_name;

	@NotBlank(message = "The last Name can't be null or empty")
	@Column(name="last_name")
	private String last_name;

	@OneToOne(optional = false,cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user")
	private User user;
	
	@Column(name="id_user", insertable = false, updatable = false)
	private long id_user;

	@NotBlank(message = "The email can't be null or empty")
	@Email(message = "The Email has an invalid format")
	@Column(name="email")
	private String email;

	@NotBlank(message = "The phone can't be null or empty")	
	@Column(name="phone")
	private String phone;

	@NotBlank(message = "The address can't be null or empty")
	@Column(name="address")
	private String address;

	@Column(name="active")
	private int active;
	
	@Column(name="create_date")
	private LocalDateTime create_date = LocalDateTime.now();;
	
	@Column(name="modified_date")
	private LocalDateTime modified_date;

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public LocalDateTime getCreated_date() {
		return create_date;
	}

	public void setCreated_date(LocalDateTime created_date) {
		this.create_date = created_date;
	}

	public LocalDateTime getModified_date() {
		return modified_date;
	}

	public void setModified_date(LocalDateTime modified_date) {
		this.modified_date = modified_date;
	}
	
	

}

package org.branuxsv.rentalmovies.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* Entity class for model the rental table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-28 */

@Entity
@Table(name ="rental")
public class Rental {

	@Id
	@Column(name="id_rental")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_rental;
	
	@Column(name="id_client")
	private long id_client;
	
	@Column(name="total_rent")
	private double total_rent;
	
	@Column(name="date")
	private LocalDateTime dateTime;
	
	@Column(name="id_status_rental")
	private int id_status_rental;

	public long getId_rental() {
		return id_rental;
	}

	public void setId_rental(long id_rental) {
		this.id_rental = id_rental;
	}

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}

	public double getTotal_rent() {
		return total_rent;
	}

	public void setTotal_rent(double total_rent) {
		this.total_rent = total_rent;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public int getId_status_rental() {
		return id_status_rental;
	}

	public void setId_status_rental(int id_status_rental) {
		this.id_status_rental = id_status_rental;
	}
	

}

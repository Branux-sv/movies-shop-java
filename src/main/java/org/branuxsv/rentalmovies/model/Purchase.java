package org.branuxsv.rentalmovies.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* Entity class for model the purchase table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-29 */


@Entity
@Table(name ="purchase")
public class Purchase {

	@Id
	@Column(name="id_purchase")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_purchase;
	
	@Column(name="id_client")
	private long id_client;
	
	@Column(name="total_payment")
	private double total_payment;
	
	@Column(name="date")
	private LocalDateTime dateTime;
	
	@Column(name="active")
	private int active;

	public long getId_purchase() {
		return id_purchase;
	}

	public void setId_purchase(long id_purchase) {
		this.id_purchase = id_purchase;
	}

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}

	public double getTotal_payment() {
		return total_payment;
	}

	public void setTotal_payment(double total_payment) {
		this.total_payment = total_payment;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	
}

package org.branuxsv.rentalmovies.dto;

import java.time.LocalDate;

/**
* The class holds properties of Detail Shopping Cart to transport 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-10 */

public class DetailShoppingCarDto {

	private long id_item;
	    
	private long id_movie;
	
	private int quantity;
		
	private Integer rented_days;
	
	private LocalDate rented_date;

	private LocalDate renturned_date;

	private double sub_total;

	public long getId_item() {
		return id_item;
	}

	public void setId_item(long id_item) {
		this.id_item = id_item;
	}

	public long getId_movie() {
		return id_movie;
	}

	public void setId_movie(long id_movie) {
		this.id_movie = id_movie;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Integer getRented_days() {
		return rented_days;
	}

	public void setRented_days(Integer rented_days) {
		this.rented_days = rented_days;
	}

	public LocalDate getRented_date() {
		return rented_date;
	}

	public void setRented_date(LocalDate rented_date) {
		this.rented_date = rented_date;
	}

	public LocalDate getRenturned_date() {
		return renturned_date;
	}

	public void setRenturned_date(LocalDate renturned_date) {
		this.renturned_date = renturned_date;
	}

	public double getSub_total() {
		return sub_total;
	}

	public void setSub_total(double sub_total) {
		this.sub_total = sub_total;
	}	
	
}

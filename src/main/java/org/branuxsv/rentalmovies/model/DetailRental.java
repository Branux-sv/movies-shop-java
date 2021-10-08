package org.branuxsv.rentalmovies.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
* Entity class for model the detail rental table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-30 */

@Entity
@Table(name ="detail_rental")
public class DetailRental {
	@Id
	@Column(name="id_detail")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_detail;
	
	@Column(name="id_rental")
	private long id_rental;
	
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name="id_rental", insertable =false, updatable = false)
    private Rental rental;
    
	@Column(name="id_movie")
	private long id_movie;
	
	@Column(name="quantity")
	private int quantity;
		
	@Column(name="sub_total")
	private double sub_total;
		
	@Column(name="rented_date")
	private LocalDate rented_date;

	@Column(name="returned_date")
	private LocalDate returned_date;

	@Column(name="Overdue_day")
	private int Overdue_day;
	
	@Column(name="active")
	private int active;

	public long getId_detail() {
		return id_detail;
	}

	public void setId_detail(long id_detail) {
		this.id_detail = id_detail;
	}

	public long getId_rental() {
		return id_rental;
	}

	public void setId_rental(long id_rental) {
		this.id_rental = id_rental;
	}

	public Rental getRental() {
		return rental;
	}

	public void setRental(Rental rental) {
		this.rental = rental;
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

	public double getSub_total() {
		return sub_total;
	}

	public void setSub_total(double sub_total) {
		this.sub_total = sub_total;
	}

	public LocalDate getRented_date() {
		return rented_date;
	}

	public void setRented_date(LocalDate rented_date) {
		this.rented_date = rented_date;
	}

	public LocalDate getReturned_date() {
		return returned_date;
	}

	public void setReturned_date(LocalDate returned_date) {
		this.returned_date = returned_date;
	}

	public int getOverdue_day() {
		return Overdue_day;
	}

	public void setOverdue_day(int overdue_day) {
		Overdue_day = overdue_day;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
	
	
}

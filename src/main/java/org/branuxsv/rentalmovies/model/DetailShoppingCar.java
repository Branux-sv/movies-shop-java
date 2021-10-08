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
* Entity class for model the detail shopping cart table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-03 */

@Entity
@Table(name ="Detail_shopping_car")
public class DetailShoppingCar {

	@Id
	@Column(name="id_item")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_item;
	
	@Column(name="id_cart", insertable = false, updatable = false)
	private long id_cart;
	
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name="id_cart", insertable =true, updatable = true)
    private ShoppingCar shoppingCar;

    
	@Column(name="id_movie")
	private long id_movie;
	
	@Column(name="quantity")
	private int quantity;
		
	@Column(name="sub_total")
	private double sub_total;
		
	@Column(name="rented_days")
	private int rented_days;
	
	@Column(name="rented_date")
	private LocalDate rented_date;
	
	@Column(name="active")
	private int active;

	public long getId_item() {
		return id_item;
	}

	public void setId_item(long id_item) {
		this.id_item = id_item;
	}

	public long getId_cart() {
		return id_cart;
	}

	public void setId_cart(long id_cart) {
		this.id_cart = id_cart;
	}

	public ShoppingCar getShoppingCar() {
		return shoppingCar;
	}

	public void setShoppingCar(ShoppingCar shoppingCar) {
		this.shoppingCar = shoppingCar;
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

	public int getRented_days() {
		return rented_days;
	}

	public void setRented_days(int rented_days) {
		this.rented_days = rented_days;
	}

	public LocalDate getRented_date() {
		return rented_date;
	}

	public void setRented_date(LocalDate rented_date) {
		this.rented_date = rented_date;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	
}

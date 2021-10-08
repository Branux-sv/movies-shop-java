package org.branuxsv.rentalmovies.model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* Entity class for model the shopping cart table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-04 */

@Entity
@Table(name ="shoppint_cart")
public class ShoppingCar {

	@Id
	@Column(name="id_cart")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_cart;
	
	@Column(name="id_state_cart")
	private long id_state_cart;
	
	@Column(name="id_client")
	private long id_client;

	@Column(name="type_transaction")
	private int type_transaction;

	@Column(name="date")
	private LocalDate date;
	
	@Column(name="total_payment")
	private double total_payment;
	
	@Column(name="currency")
	private String currency;

	@OneToMany(mappedBy = "shoppingCar", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<DetailShoppingCar> listItemsOfCar;

	@Column(name="tax")
	private double tax;

	@Column(name="discount")
	private double discount;
	
	@Column(name="sub_total")
	private double subTotal;
	
	public long getId_cart() {
		return id_cart;
	}

	public void setId_cart(long id_cart) {
		this.id_cart = id_cart;
	}

	public long getId_state_cart() {
		return id_state_cart;
	}

	public void setId_state_cart(long id_state_cart) {
		this.id_state_cart = id_state_cart;
	}

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}

	public int getType_transaction() {
		return type_transaction;
	}

	public void setType_transaction(int type_transaction) {
		this.type_transaction = type_transaction;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getTotal_payment() {
		return total_payment;
	}

	public void setTotal_payment(double total_payment) {
		this.total_payment = total_payment;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<DetailShoppingCar> getListItemsOfCar() {
		return listItemsOfCar;
	}

	public void setListItemsOfCar(List<DetailShoppingCar> listItemsOfCar) {
		this.listItemsOfCar = listItemsOfCar;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}	
				
}

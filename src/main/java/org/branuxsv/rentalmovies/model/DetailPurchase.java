package org.branuxsv.rentalmovies.model;

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
* Entity class for model the detail purchase table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-27 */

@Entity
@Table(name ="detail_purchase")
public class DetailPurchase {

	@Id
	@Column(name="id_detail")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id_detail;
	
	@Column(name="id_purchase")
	private long id_purchase;
	
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name="id_purchase", insertable =false, updatable = false)
    private Purchase purchase;
    
	@Column(name="id_movie")
	private long id_movie;
	
	@Column(name="quantity")
	private int quantity;
		
	@Column(name="sub_total")
	private double sub_total;
			
	@Column(name="active")
	private int active;

	public long getId_detail() {
		return id_detail;
	}

	public void setId_detail(long id_detail) {
		this.id_detail = id_detail;
	}

	public long getId_purchase() {
		return id_purchase;
	}

	public void setId_purchase(long id_purchase) {
		this.id_purchase = id_purchase;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
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

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}
		
}

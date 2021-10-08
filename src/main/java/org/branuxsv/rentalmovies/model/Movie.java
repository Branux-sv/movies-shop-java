package org.branuxsv.rentalmovies.model;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.branuxsv.rentalmovies.dao.MovieListener;

import com.google.gson.annotations.Expose;

/**
* Entity class for model the movie table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

@Entity
@Table(name="movie")
@EntityListeners(MovieListener.class)
public class Movie {
	
	@Id
	@Column(name="id_movie")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose(serialize = true, deserialize = true )
	private long id_movie;
	
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name="id_category", insertable =false, updatable = false)
    @Expose(serialize = true, deserialize = true )
	private Category category;

    //@Column(name="id_category")
    @Expose(serialize = false, deserialize = false )
    @NotNull(message = "The category Id can't be null")
    @Positive(message = "The category Id can't be zero or negative")
    @Digits(integer = 11, fraction = 0, message = "The category Id can't be have fraction")
    private long id_category;
    
    @OneToMany(mappedBy = "movie")
    @Expose(serialize = false, deserialize = false )
    private Collection<Likes> likes;    
    
    @NotBlank(message = "The title can't be null or empty")
    @Column(name="title")
    @Expose(serialize = true, deserialize = true )
    private String title;
	
    @Column(name="description")
    @Expose(serialize = true, deserialize = true )
    @NotBlank(message = "The description can't be null or empty")
    private String description;
    
    @Lob
    @Column(name="image")
    @Expose(serialize = false, deserialize = false ) //There is a method to get the binaries (png or jpg)
    @NotNull(message = "The image can't be null")
    private byte[] image;
    
    @Column(name="stock")
    @Expose(serialize = true, deserialize = true )
    @NotNull(message = "The stock can't be null")
    @Positive(message = "The stock can't be zero or negative")
    @Digits(integer = 11, fraction = 0, message = "The stock can't be have fraction")
    private int stock;
    
    @Column(name="rental_price")
    @Expose(serialize = true, deserialize = true )
    @NotNull(message = "The rental_price can't be null")
    @Positive(message = "The rental_price can't be zero or negative")
    @Digits(integer = 13, fraction = 2, message = "The rental_price has wrong precesion and size")   
    private double rental_price;

    @Column(name="sale_price")
    @Expose(serialize = true, deserialize = true )
    @NotNull(message = "The sale_price can't be null")
    @Positive(message = "The sale_price can't be zero or negative")
    @Digits(integer = 13, fraction = 2, message = "The sale_price has wrong precesion and size")   
    private double sale_price;
    
    @Column(name="penalty_fee")
    @Expose(serialize = true, deserialize = true )
    @NotNull(message = "The penalty_fee can't be null" )
    @Positive(message = "The penalty_fee can't be zero or negative")
    @Digits(integer = 6, fraction = 2, message = "The penalty_fee has wrong precesion and size")       
    private double penalty_fee;
    
    @Column(name="availability")
    @Expose(serialize = true, deserialize = true )
    private int availability;

    @Transient
    @Expose(serialize = false, deserialize = false )
    private String userName;
    
	public long getId_movie() {
		return id_movie;
	}

	public void setId_movie(long id_movie) {
		this.id_movie = id_movie;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public double getRental_price() {
		return rental_price;
	}

	public void setRental_price(double rental_price) {
		this.rental_price = rental_price;
	}

	public double getSale_price() {
		return sale_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}

	public double getPenalty_fee() {
		return penalty_fee;
	}

	public void setPenalty_fee(double penalty_fee) {
		this.penalty_fee = penalty_fee;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

	public long getId_category() {
		return id_category;
	}

	public void setId_category(long id_category) {
		this.id_category = id_category;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}    
	
	
	
}

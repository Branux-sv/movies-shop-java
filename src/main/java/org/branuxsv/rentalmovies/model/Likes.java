package org.branuxsv.rentalmovies.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
* Entity class for model the rental table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-27 */

@Entity
@Table(name = "likes")
public class Likes {

	@Id
	@Column(name ="id_movie")
	private long id_movie;
	
	@Id
	@Column(name ="id_client")
	private long id_client;
	
	@Column(name ="date")	
	private LocalDateTime dateAndTime = LocalDateTime.now();
	
	@ManyToOne(optional = true, cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name="id_movie", referencedColumnName = "id_movie", insertable = false, updatable = false)
   // @JoinColumn(name="id_client", referencedColumnName = "id_client", insertable = false, updatable = false)
	private Movie movie;

	public Likes()
	{}
	
	public long getId_movie() {
		return id_movie;
	}

	public void setId_movie(long id_movie) {
		this.id_movie = id_movie;
	}

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}

	public LocalDateTime getDateAndTime() {
		return dateAndTime;
	}

	public void setDateAndTime(LocalDateTime dateAndTime) {		
		if (dateAndTime == null)
			dateAndTime = LocalDateTime.now();		
		this.dateAndTime = dateAndTime;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}	
		
}

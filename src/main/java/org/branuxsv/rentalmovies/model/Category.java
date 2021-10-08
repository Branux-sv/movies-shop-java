package org.branuxsv.rentalmovies.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

/**
* Entity class for model the category table 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

@Entity
@Table(name="category")
public class Category {

	@Id
	@Column(name="id_category")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose(serialize = true, deserialize = true )
	private int id_category;

    @Column(name="name")
    @Expose(serialize = true, deserialize = true )
    private String name;
	
    @Column(name="description")
    private String description;

    @Column(name="active")
    private int active;
    
    /*
    @OneToMany(mappedBy = "category")
    @Expose(serialize = false, deserialize = false )
    private List<Movie> movies;
    */

	public int getId_category() {
		return id_category;
	}

	public void setId_category(int id_category) {
		this.id_category = id_category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}    
    
}

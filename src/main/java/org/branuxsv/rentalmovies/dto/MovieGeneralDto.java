package org.branuxsv.rentalmovies.dto;

import org.branuxsv.rentalmovies.model.Category;

import com.google.gson.annotations.Expose;

/**
* The class holds properties of Movie to transport 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-27 */


public class MovieGeneralDto {

    @Expose(serialize = true, deserialize = true )
	private long id_movie;
    
    @Expose(serialize = true, deserialize = true )  
	private Category category;    
    
    @Expose(serialize = true, deserialize = true )
    private String title;
 
    @Expose(serialize = true, deserialize = true )
    private byte[] image;
	
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
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}    
	
}

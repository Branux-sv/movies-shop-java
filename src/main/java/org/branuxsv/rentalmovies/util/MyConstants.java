package org.branuxsv.rentalmovies.util;

/**
* Class that save the values of commons constants used in the project 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-07 */

public class MyConstants {

	public static final int CODE_AVAILABILITY = 1;
	public static final int MAX_SIZE_IMAGE = 1124000; //Approximately 1.1MB, size in bytes
	public static final String[] VALID_MEDIA_TYPES = {"image/jpeg","image/png"};
	public static final String CURRENCY = "USD";
	public static final int DEFAULT_STATUS_RENTAL = 1;
	public static final int DEFAULT_STATUS_CART = 1;	
	public static final double DEFAULT_TAX = 0.1;	
	public static final double DEFAULT_DISCOUNT = 0.05;	
	public static final int EXPIRATION_DAYS_CONFIRMED_REGISTRATION = 1;		
	public static final String EMAIL_FROM = "notification@rentalMovie.com";
	public static final String EMAIL_TO = "algo.something@gmail.com";
	public static final String EMAIL_SUBJECT = "Rental Movie App - Confirmation User";
	public static final String EMAIL_SUBJECT_FORGOT = "Rental Movie App - Recovery Password";	
	public static final String HTML_TEMPLATE_CONFIRMATION = "<h1>Hello %s thank you for register please the final step is click the link below </h1> <h2>Click the following link: <a href='http://localhost:8080/rentalmovies/api/V1/movies/user/confirm?token=%s'>Click here to confirm your user</a></h2>";	
	public static final String HTML_TEMPLATE_FORGOT_PASSWORD = "<h1>Hello %s we hope you are ok.</h1> <h2>Please take note this is the temporary password in bold: <strong> %s </strong> you have to go login and then change your temporary password for whatever you want.</h2>";		
	public static final int CODE_ADMIN_ROLE = 1;
	
	public enum TRANSACTION_TYPE 
	{
		PURCHASE,
		RENTAL;
		
		public final int indexValue = 1 + ordinal();
	}
	
	public enum OPERATION_TYPE 
	{
		INSERT,
		UPDATE,
		DELETE,
		ADD_CART
	}

}

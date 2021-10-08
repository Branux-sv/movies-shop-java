package org.branuxsv.rentalmovies.util;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.model.Movie;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import sun.util.logging.resources.logging;


/**
* Util class that contains different kind of general methods 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-26 */

public class Util {
	
	private  Logger log = Logger.getLogger(Util.class); 
	
	private String namePropertieFile ="config.properties";
	private Properties props = new Properties();
	
	public String saveFileAndGetName(byte[] bytesFile) throws IOException
	{
		InputStream inS = getClass().getClassLoader().getResourceAsStream(namePropertieFile);
		if (inS != null)
			props.load(inS);
		else
			throw new FileNotFoundException("Properties File: " + namePropertieFile + " not found");
		
		String saveTmpPath = props.getProperty("PATH_SAVE_TMP");
		String nameFile = String.valueOf(getRandomNumber(0, 100000000));		
		String filePath = saveTmpPath+nameFile + ".txt";					
		Files.write(Paths.get(filePath), bytesFile);		
		
		return filePath;
	}
	
	public static int getRandomNumber(int min, int max){
	    Random random = new Random();
	    return random.ints(min,(max+1)).findFirst().getAsInt();
	}
	
	public String getConfigValueByKey(String myKey) throws IOException
	{		
		InputStream inS = getClass().getClassLoader().getResourceAsStream(namePropertieFile);
		if (inS != null)
			props.load(inS);
		else
			throw new FileNotFoundException("Properties File: " + namePropertieFile + " not found");
		
		return props.getProperty(myKey);
	}
	
	public String getConfigValueByKey(String configFileName,String myKey) throws IOException
	{		
		InputStream inS = getClass().getClassLoader().getResourceAsStream(configFileName);
		if (inS != null)
			props.load(inS);
		else
			throw new FileNotFoundException("Properties File: " + configFileName + " not found");
		
		return props.getProperty(myKey);
	}
	
	
	public String getJsonResponse(String code) 
	{
		Gson gson = new Gson();
		ResponseFrontEnd obj = new ResponseFrontEnd();
		obj.setCode(code);
		String msj = "" ;
		try {
			msj = getConfigValueByKey("errorsMsj.properties",code);		
		} catch (Exception e) {
			log.error("There was error getting the message", e);
			if("0".equals(code))
				msj ="OK";
			else
				msj ="Error";
		}			
		obj.setMessage(msj);		
		return gson.toJson(obj);											
	}
	
	public <T> int countInvalidData(T obj)
	{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();		
		return validator.validate(obj).size();

	}	
	
	public byte[] getBytesInputStream(InputStream file)
	{
		try {
			if (file != null)
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len;

				while ((len = file.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}	
				return os.toByteArray();				
			}
		} catch (Exception e) {
			log.error("Error in getBytes", e);
		}
		return null;
	}

	public void copyMovieToUpdate(Movie des, Movie orig)
	{
		if (orig.getId_category() > 0)
			des.setId_category(orig.getId_category());

		if (orig.getTitle()!= null && !orig.getTitle().isEmpty())
			des.setTitle(orig.getTitle());

		if (orig.getDescription()!= null && !orig.getDescription().isEmpty())
			des.setDescription(orig.getDescription());

		if (orig.getImage()!= null )
			des.setImage(orig.getImage());

		if (orig.getStock() > 0)
			des.setStock(orig.getStock());

		if (orig.getAvailability() > 0)
			des.setAvailability(orig.getAvailability());

		if (orig.getSale_price() > 0)
			des.setSale_price(orig.getSale_price());

		if (orig.getRental_price() > 0)
			des.setRental_price(orig.getRental_price());
		
		if (orig.getPenalty_fee() > 0)
			des.setPenalty_fee(orig.getPenalty_fee());
		
		//return des;
	}

	public LocalDateTime convertToLocalDateTime(Date date)
	{
		return date.toInstant()
				 .atZone(ZoneId.systemDefault())
				 .toLocalDateTime();
	}
	
	public static double getFormat2Decimal(double amount)
	{
		DecimalFormat formatter = new DecimalFormat("####.##");
		return Double.parseDouble(formatter.format(amount));
	}
	
	public <T> String getJsonResponse(String code, T body) 
	{
	    Gson gson = new GsonBuilder()
	    		        .registerTypeAdapter(
	    		        		  LocalDate.class, 
	    		        		  new LocalDateGsonAdapter()
	    		        ).create();
	   
		ResponseFrontEnd obj = new ResponseFrontEnd();
		obj.setCode(code);
		String msj = "" ;
		try {
			msj = getConfigValueByKey("errorsMsj.properties",code);		
		} catch (Exception e) {
			log.error("There was error getting the message", e);
			if("0".equals(code))
				msj ="OK";
			else
				msj ="Error";
		}			
		obj.setMessage(msj);		
		
		if (body != null )
		{
			obj.setBody(gson.toJson(body));			
		}
		
		return gson.toJson(obj);											
	}
	
	public static String getFormat2Decimal(double amount, String currency)
	{
		DecimalFormat formatter = new DecimalFormat("####.##");
		
		//TODO: according currency, set the money symbol
		String symbol = "";
		if ("USD".equals(currency))
			symbol = "$";		
		return symbol + formatter.format(amount);
	}

	public String sendEmail(String from, String to, String subject, String HTML_Str)
	{
		final String username = "";
		final String password = "";		

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); // TLS

		Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);
			message.setDataHandler(new DataHandler(new HTMLDataSource(HTML_Str)));			
			Transport.send(message);			
			return "OK";
		} catch (MessagingException e) {
			log.error("Error sending email",e);
		}

		return "ERROR";		
	}

	public static boolean isNumeric(final String str) 
	{
        if (str == null || str.length() == 0) {
            return false;
        }
        return str.chars().allMatch(Character::isDigit);
    }
	
}

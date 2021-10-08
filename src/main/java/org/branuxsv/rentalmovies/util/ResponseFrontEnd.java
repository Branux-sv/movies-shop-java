package org.branuxsv.rentalmovies.util;

/**
* Class used for give to clients a web response, from theirs web request,
*  contains a code,  message and a body (optional)  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-03-30 */

public class ResponseFrontEnd {

	private String code;
	private String message;
	private String body;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
		
	
}

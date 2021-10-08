
package org.branuxsv.rentalmovies.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
* Util class that help us to proccess JWT tokens on authorization proccess
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-01 */

public class JwtUtil {

	private static Logger log = Logger.getLogger(JwtUtil.class); 
	
	public static String generateToken(String subject, Map<String, Object> claims) throws IOException
	{
		Util util =new Util();
		
		int JWT_TOKEN_VALIDITY = Integer.parseInt(util.getConfigValueByKey("TOKEN_EXPIRATION_TIME")) ;
		String secretKey = util.getConfigValueByKey("JWT_SECRECT");
						
		JwtBuilder myBuilder = Jwts.builder()
			    .setClaims(claims)
				.setId(UUID.randomUUID().toString())
				.setIssuer("Branux-SV")
				.setSubject(subject)
			    .setAudience("All_Public")
			    .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) 
			    .setIssuedAt(new Date(System.currentTimeMillis())) 
			    .signWith(SignatureAlgorithm.HS512, secretKey.getBytes(StandardCharsets.UTF_8));
		
		return  util.getConfigValueByKey("TOKEN_BEARER_PREFIX") + " " + myBuilder.compact();
	}
	
	public static UserDto validateToken(String token) throws IOException {
		
		Util util = new Util();
		
		String secretKey = util.getConfigValueByKey("JWT_SECRECT");
		
		try {
			
			Claims body = Jwts.parser()
					.setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(token)
					.getBody();

			UserDto udto = new UserDto();
			udto.setUsername((String)body.get("username"));
			udto.setId_user((int)body.get("id_user"));
			udto.setId_role((int)body.get("id_role"));

			if (udto.getUsername() != null && !udto.getUsername().isEmpty() && udto.getId_user() > 0 &&  udto.getId_role() > 0 )
				return udto;
			else
				return null;				
			
		} catch (JwtException | ClassCastException e) {			
			log.error("Parse tokenJWT error", e);
		}						
		catch (Exception e) {
			log.error("Unknown error in validateToken", e);
		}
		return null;
	}
	
	public static boolean isTokenJwtExpired(String token) throws IOException
	{		
		Util util = new Util();
		
		String secretKey = util.getConfigValueByKey("JWT_SECRECT");
		
		try {
			
			Claims body = Jwts.parser()
					.setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(token)
					.getBody();
			
			Date dateExp = body.getExpiration();
			return dateExp.before(new Date());
						
		} catch (JwtException | ClassCastException e) {			
			log.error("Parse tokenJWT error when validte expiration", e);
		}
		catch (Exception e) {
			log.error("Unknown error in validte expiration", e);
		}
		
		return false;
	}
	
	public static String getTokenJwtId(String token) throws IOException
	{		
		Util util = new Util();
		
		String secretKey = util.getConfigValueByKey("JWT_SECRECT");
		
		try {
			
			Claims body = Jwts.parser()
					.setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(token)
					.getBody();
			
			return body.getId();
						
		} catch (JwtException | ClassCastException e) {			
			log.error("Parse tokenJWT error in getTokenJwtId", e);
		}
		catch (Exception e) {
			log.error("Unknown error in getTokenJwtId", e);
		}
		
		return "";
	}
	
	public static Date getTokenJwtExpDate(String token) throws IOException
	{		
		Util util = new Util();
		
		String secretKey = util.getConfigValueByKey("JWT_SECRECT");
		
		try {
			
			Claims body = Jwts.parser()
					.setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(token)
					.getBody();
			
			return body.getExpiration();
						
		} catch (JwtException | ClassCastException e) {			
			log.error("Parse tokenJWT error in getTokenJwtExpDate", e);
		}
		catch (Exception e) {
			log.error("Unknown error in getTokenJwtExpDate", e);
		}
		
		return null;
	}
	
}


package org.branuxsv.rentalmovies.api;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dto.UserDto;
import org.branuxsv.rentalmovies.service.TokenUserService;
import org.branuxsv.rentalmovies.util.JwtUtil;
import org.branuxsv.rentalmovies.util.Util;

/**
* Class that help us to analize and validate the headers of a specific request 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-02 */

@Provider
@RequiredAuthorization
@Priority(Priorities.AUTHENTICATION)
public class RequestHeaderAnalyzeFilter  implements ContainerRequestFilter{

	private Logger log = Logger.getLogger(RequestHeaderAnalyzeFilter.class); 
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Util util = new Util();
		boolean isValidReq = false;	
		UserDto userDTO = null;
		String auxComposeKey = "";
		String prefixJwtToken = util.getConfigValueByKey("TOKEN_BEARER_PREFIX");		
		String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
		if (authHeader != null &&  !authHeader.isEmpty())
		{
			if (authHeader.startsWith(prefixJwtToken))
			{
				String tokenJWT = authHeader.replace(prefixJwtToken + " ", "");				
				userDTO = JwtUtil.validateToken(tokenJWT);
				if ( userDTO != null)	
				{
					
					//Validate if has been doing a logout... the Token can be valid but if the 
					//user do a logOut so we need abort the request
					TokenUserService tuC = new TokenUserService();
					if (!tuC.existsValidTokenForUser(userDTO.getId_user()))
					{
						log.info("Not found Active Token, a logOut was made");						
						requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
								.entity(util.getJsonResponse("19")).build());		
						return;
					}
					else
					{
						auxComposeKey = String.valueOf(userDTO.getId_user()) + "|" + userDTO.getUsername() + "|" + String.valueOf(userDTO.getId_role());
						isValidReq = true;											
					}						
	
				}
				else
					log.info("JwtUtil.validateToken return false...");
			}						
			else
				log.info("Dont find the prefix: " + prefixJwtToken +" in the Authorization header");
		}
		else
			log.info("Dont find Authorization headers...");
		
		if (!isValidReq)
		{
			log.info("The token is invalid so the app is going to abort the request...");					
			requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity(util.getJsonResponse("2")).build());		
		}
		else
		{
			String UserComposeKey = auxComposeKey;
			log.info("The token is valid, All its OK!");							

			//Note: Its possible to used CDI, produces, events but for time I used securityContext
			final SecurityContext sc = requestContext.getSecurityContext(); 
			requestContext.setSecurityContext(new SecurityContext() {
				
				@Override
				public boolean isUserInRole(String role) {
					return false;
				}
				
				@Override
				public boolean isSecure() {
					return sc.isSecure();
				}
				
				@Override
				public Principal getUserPrincipal() {
					return () -> UserComposeKey;
				}
				
				@Override
				public String getAuthenticationScheme() {
					return "Bearer";
				}
			});
			
		}		
	}

}

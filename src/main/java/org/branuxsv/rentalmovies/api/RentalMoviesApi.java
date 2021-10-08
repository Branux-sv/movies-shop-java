package org.branuxsv.rentalmovies.api;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.branuxsv.rentalmovies.dto.FullUserDto;
import org.branuxsv.rentalmovies.dto.LoginRequestDto;
import org.branuxsv.rentalmovies.dto.LoginResponseDto;
import org.branuxsv.rentalmovies.dto.ShoppingCarDto;
import org.branuxsv.rentalmovies.dto.UserDto;
import org.branuxsv.rentalmovies.model.Likes;
import org.branuxsv.rentalmovies.model.Movie;
import org.branuxsv.rentalmovies.model.TokenUser;
import org.branuxsv.rentalmovies.model.User;
import org.branuxsv.rentalmovies.service.*;
import org.branuxsv.rentalmovies.util.JwtUtil;
import org.branuxsv.rentalmovies.util.LocalDateGsonAdapter;
import org.branuxsv.rentalmovies.util.MyConstants;
import org.branuxsv.rentalmovies.util.SecurityUtil;
import org.branuxsv.rentalmovies.util.Util;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
* The class holds all or the most API Methods of Rental Movie functionability
* 
* @version 1.0
* @author Branux-SV
* @Date  2020-03-25 */

@Path("movies")
public class RentalMoviesApi {
	
	@Context
	SecurityContext securityContext;

	private Logger log = Logger.getLogger(RentalMoviesApi.class); 

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMovies(
			@DefaultValue("title") @QueryParam("sort") String sort,
			@DefaultValue("1") @QueryParam("page") String page,			
			@DefaultValue("10") @QueryParam("pagesize") String pagesize,			
			@QueryParam("search") String search						
			)
	{	
		
		Gson gson = new Gson();
		MovieService objController = new MovieService();
		String jsonResp = gson.toJson(objController.getAllMoviesWithFilters(MyConstants.CODE_AVAILABILITY, search, sort,page, pagesize));				
		return Response.ok(jsonResp).build();
	}

	@GET
	@Path("/availability")	
	@Produces(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response getMoviesFilterAvailability(
			@DefaultValue("-1") @QueryParam("availability") int availability,
			@DefaultValue("title") @QueryParam("sort") String sort,
			@DefaultValue("1") @QueryParam("page") String page,			
			@DefaultValue("10") @QueryParam("pagesize") String pagesize,			
			@QueryParam("search") String search						
			)
	{	
		//Verify if is admin role... 
		if (!SecurityUtil.isAdminRole(securityContext.getUserPrincipal().getName()))
			availability = MyConstants.CODE_AVAILABILITY; //If Role is not Admin (code 1) Can't filter By availability
		
		Gson gson = new Gson();
		MovieService objController = new MovieService();
		String jsonResp = gson.toJson(objController.getAllMoviesWithFilters(availability, search, sort,page, pagesize));				
		return Response.ok(jsonResp).build();
	}
	
	@GET
	@Path("/{id}")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMovieDetail(@PathParam("id") Long id)
	{		
		Util util = new Util();
		
		Gson gson = new GsonBuilder()
		        .excludeFieldsWithoutExposeAnnotation()
		        .create();		
		
		MovieService objController = new MovieService();
		Movie obj = objController.getMovieDetail(id);
		if (obj != null)
		{
			String jsonResp = gson.toJson(obj);				
			return Response.ok(jsonResp).build();			
		}
		else 
			return Response.status(Response.Status.NOT_FOUND)
					.entity(util.getJsonResponse("10")).build();				
	}
	
	@GET
	@Path("/image/{id}")	
	@Produces({"image/png", "image/jpg"})	
	public Response getMovieImage(@PathParam("id") Long id)
	{		
		Util util = new Util();
		MovieService objController = new MovieService();		
		byte[] bytes = objController.getMovieImage(id);
		if (bytes != null)
			return Response.ok(bytes).build();
		else
			return Response.status(Response.Status.NOT_FOUND)
					.entity(util.getJsonResponse("10")).build();				
	}	
	
	@POST
	@Path("/add")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @RequiredAuthorization
	public Response addMovie(
			@FormDataParam("jsonMovie") String jsonNewMovie, 
			@FormDataParam("image") InputStream fileInputStream,
            @FormDataParam("image") FormDataContentDisposition fileMetaData,
            @FormDataParam("image") FormDataBodyPart body            
			)
	{
		log.debug("***** Init Add new movie *****");
		log.debug("-> json received: " + jsonNewMovie);	
		

		Util util = new Util();				
		
		//Verify if is admin role... 
		if (!SecurityUtil.isAdminRole(securityContext.getUserPrincipal().getName()))
			return Response.status(Response.Status.FORBIDDEN).entity(util.getJsonResponse("3")).build();
					
		if (jsonNewMovie == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (jsonNewMovie.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			
		
		if (fileInputStream == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("11")).build();			


		boolean validImg = Arrays.stream(MyConstants.VALID_MEDIA_TYPES).anyMatch(body.getMediaType().toString()::equals);
		if (!validImg)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("15")).build();			

		byte[] bi = util.getBytesInputStream(fileInputStream);
		if (bi.length > MyConstants.MAX_SIZE_IMAGE) // 1MB max allowed
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("16")).build();			
		
		Gson gson = new Gson();
		MovieService objController = new MovieService();		
		Movie obj = null;

		try {
			obj = gson.fromJson(jsonNewMovie, Movie.class);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();			
		}
		
		if (obj == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();			
		
		String result = objController.addMovie(obj,bi); 
		if ("OK".equals(result))			
			return Response.ok(util.getJsonResponse("0")).build(); 
		else if ("ERRIM".equals(result))
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("11")).build();			
		else if ("DATAIN".equals(result))
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("12")).build();					
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("9")).build();			
		
	}

	@PUT
	@Path("/update")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@RequiredAuthorization
	public Response updateMovie(
			@FormDataParam("jsonMovie") String jsonMovie, 
			@FormDataParam("image") InputStream fileInputStream,
            @FormDataParam("image") FormDataContentDisposition fileMetaData,
            @FormDataParam("image") FormDataBodyPart body            			
			)
	{
		log.debug("***** Init Update new movie *****");
		log.debug("-> json received: " + jsonMovie);			

		Util util = new Util();				
		
		//Verify if is admin role... 
		if (!SecurityUtil.isAdminRole(securityContext.getUserPrincipal().getName()))
			return Response.status(Response.Status.FORBIDDEN).entity(util.getJsonResponse("3")).build();		
		

		//The json is required always, if only need update the image too, just send the ID in json form
		if (jsonMovie == null )  
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (jsonMovie.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		byte[] bytesImg = util.getBytesInputStream(fileInputStream);

		if (bytesImg != null) //The image can be optional for update 
		{
			if (bytesImg.length > MyConstants.MAX_SIZE_IMAGE) // 1MB max allowed
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(util.getJsonResponse("16")).build();			
			
			boolean validImg = Arrays.stream(MyConstants.VALID_MEDIA_TYPES).anyMatch(body.getMediaType().toString()::equals);
			if (!validImg)
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(util.getJsonResponse("15")).build();						
		}
		
		Gson gson = new Gson();
		MovieService objController = new MovieService();		
		Movie obj = null;

		try {
			obj = gson.fromJson(jsonMovie, Movie.class);
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();			
		}
		
		if (obj == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();			
		
		if (bytesImg != null)
			obj.setImage(bytesImg);
	
		UserDto udt = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		if (udt != null)
			obj.setUserName(udt.getUsername()); 
		
		String result = objController.updateMovie(obj); 
		
		if ("OK".equals(result))			
			return Response.ok(util.getJsonResponse("0")).build(); 
		else if ("NOTF".equals(result))
			return Response.status(Response.Status.NOT_FOUND)
					.entity(util.getJsonResponse("10")).build();			
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("8")).build();			
	}

	@DELETE
	@Path("/{id}")	
	@Produces(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response deleteMovie(@PathParam("id") Long id)
	{
		Util util = new Util();		
		//Verify if is admin role... 
		if (!SecurityUtil.isAdminRole(securityContext.getUserPrincipal().getName()))
			return Response.status(Response.Status.FORBIDDEN).entity(util.getJsonResponse("3")).build();
			
		MovieService objController = new MovieService();		
		String result = objController.deleteMovie(id);
		if ("OK".equals(result))			
			return Response.ok(util.getJsonResponse("0")).build(); 
		else if ("NOTF".equals(result))
			return Response.status(Response.Status.NOT_FOUND)
					.entity(util.getJsonResponse("10")).build();			
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("7")).build();			
	}
	
	@POST
	@Path("/like")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response addLike(String json)
	{
		log.debug("-> Init AddLike method...");
		log.debug("-> Request(json) received: " + json);
		
		Gson gson = new Gson();
		Util util = new Util();
		LikesService objController = new LikesService();		
		Likes obj = gson.fromJson(json, Likes.class);		
		
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		
		if (objDto != null)
			obj.setId_client(objDto.getId_user());
		else			
			obj.setId_client(0); //Shouln't happen 	
		
		String result = objController.addLike(obj);
		if ("OK".equalsIgnoreCase(result))
			return Response.ok(util.getJsonResponse("0")).build(); 
		else if ("UNAVA".equals(result) || "NOTFM".equals(result))
			return Response.status(Response.Status.NOT_FOUND)
					.entity(util.getJsonResponse("10")).build();					
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("4")).build();
	}
	
	@POST
	@Path("/login")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(String json)
	{
		log.debug("** Init mehotd login**");
		Util util = new Util();
		Gson gson = new Gson();
		String jsoResp = "";

		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			
		
		UserService objController = new UserService();		
		LoginRequestDto objLoginReq = null;				
		
		try {
			objLoginReq = gson.fromJson(json, LoginRequestDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();			
		}
		
		if (objLoginReq == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();			
		
		
		log.info("Trying to login, user: " + objLoginReq.getUsername());		
		
		try {
			UserDto objUserDto = objController.authenticate(objLoginReq.getUsername(), objLoginReq.getPassword());
			
			if ("OK".equals(objUserDto.getError()))
			{
				//validate that doesn't exists a current(active) token for the user authenticated
				TokenUserService tuC = new TokenUserService();
				if (tuC.existsValidTokenForUser(objUserDto.getId_user()))
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
							.entity(util.getJsonResponse("18")).build();														
					
				Map<String, Object> myClaims = new HashMap();
				myClaims.put("username", objUserDto.getUsername());
				myClaims.put("id_user", objUserDto.getId_user());
				myClaims.put("id_role", objUserDto.getId_role());
				
				String Token = JwtUtil.generateToken(objUserDto.getUsername(), myClaims);				
				
				//Save the token in DB to create logOut logic
				TokenUser tu = new TokenUser();
				String prefixJwtToken = util.getConfigValueByKey("TOKEN_BEARER_PREFIX");		
				String tokenJWT = Token.replace(prefixJwtToken + " ", "");		
				
				tu.setActive(1);
				tu.setUser_id(objUserDto.getId_user());
				tu.setToken_id(JwtUtil.getTokenJwtId(tokenJWT));
				tu.setDate_time(util.convertToLocalDateTime(JwtUtil.getTokenJwtExpDate(tokenJWT)));								
				
				String respTU = tuC.addTokenUser(tu);
				if (!"OK".equals(respTU))
					return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
							.entity(util.getJsonResponse("17")).build();									
				
				LoginResponseDto jwtResp = new LoginResponseDto(Token);				
				jsoResp = gson.toJson(jwtResp);
				return Response.ok(jsoResp).build(); 
			}
			else if ("NOT_FOUND".equals(objUserDto.getError()))
				return Response.status(Response.Status.BAD_REQUEST).entity(util.getJsonResponse("5")).build();				
			else if ("UNKNOWN_ERROR".equals(objUserDto.getError()))
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(util.getJsonResponse("59")).build();				
			else if ("NO_CONFIRMED".equals(objUserDto.getError()))
				return Response.status(Response.Status.UNAUTHORIZED).entity(util.getJsonResponse("60")).build();				
			else if ("NO_ACTIVE".equals(objUserDto.getError()))
				return Response.status(Response.Status.UNAUTHORIZED).entity(util.getJsonResponse("61")).build();				
			else 
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(util.getJsonResponse("6")).build();							
		} catch (Exception e) {
			log.error("Error in login", e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(util.getJsonResponse("6")).build();
		}		
	}
	
	@POST
	@Path("/logout")	
	@Produces(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response logout()
	{
		Util util = new Util();
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		TokenUserService tuC = new TokenUserService();
		String resp = tuC.invalidateTokenUser(objDto.getId_user());

		if ("OK".equals(resp))
			return Response.ok(util.getJsonResponse("0")).build(); 
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("20")).build();
		
	}
	
	@POST
	@Path("/cartcreate")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response createCart(String json)
	{
		
		Util util = new Util();
		
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		
		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();	
		
	    Gson gson = new GsonBuilder()
		        .registerTypeAdapter(
		        		  LocalDate.class, 
		        		  new LocalDateGsonAdapter()
		        ).create();

		ShoppingCarDto objReq = null;				
			
		try {
			objReq = gson.fromJson(json, ShoppingCarDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(util.getJsonResponse("14")).build();			
		}
			
		if (objReq == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();		
		
		ShoppingCarService scc = new ShoppingCarService();
		objReq.setId_client(objDto.getId_user()); 

		ShoppingCarDto objResp = scc.createCart(objReq);		
		if ("OK".equalsIgnoreCase(objResp.getErrorCode()))
			return Response.ok(util.getJsonResponse("0",objResp)).build(); 
		else if ("DTYPE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("22")).build();					
		else if ("IDCLINOTF".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("23")).build();					
		else if ("DITEMS".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("24")).build();					
		else if ("ITEM_M".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("25")).build();				
		else if ("ITEM_Q".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("26")).build();				
		else if ("ITEM_MUNAVA".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("27")).build();				
		else if ("ITEM_MNOFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("28")).build();				
		else if ("ITEM_NORD".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("29")).build();				
		else if ("ITEM_RDBEFORE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("30")).build();				
		else if ("ITEM_NODAY".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("31")).build();						
		else if ("ITEMS_DUPLICATE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("41")).build();										
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("21")).build();
	}
	
	@POST
	@Path("/addCart")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response addCart(String json)
	{
		
		Util util = new Util();		
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		
		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();	
		
	    Gson gson = new GsonBuilder()
		        .registerTypeAdapter(
		        		  LocalDate.class, 
		        		  new LocalDateGsonAdapter()
		        ).create();

		ShoppingCarDto objReq = null;				
			
		try {
			objReq = gson.fromJson(json, ShoppingCarDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(util.getJsonResponse("14")).build();			
		}
			
		if (objReq == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();		
		
		ShoppingCarService scc = new ShoppingCarService();
		objReq.setId_client(objDto.getId_user()); 

		ShoppingCarDto objResp = scc.addCart(objReq);		
		if ("OK".equalsIgnoreCase(objResp.getErrorCode()))
			return Response.ok(util.getJsonResponse("0",objResp)).build(); 
		else if ("DID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("33")).build();				
		else if ("CAR-NOTUSER".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("34")).build();				
		else if ("CAR-NOVALID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("35")).build();				
		else if ("CAR-NOTFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("36")).build();						
		else if ("DITEMS".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("24")).build();					
		else if ("ITEM_M".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("25")).build();				
		else if ("ITEM_Q".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("26")).build();				
		else if ("ITEM_MUNAVA".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("27")).build();				
		else if ("ITEM_MNOFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("28")).build();				
		else if ("ITEM_NORD".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("29")).build();				
		else if ("ITEM_RDBEFORE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("30")).build();				
		else if ("ITEM_NODAY".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("31")).build();						
		else if ("ITEMS_DUPLICATE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("41")).build();								
		else if ("M_ALREADY_EXISTS".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("42")).build();								
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("32")).build();
	}
	
	@PATCH
	@Path("/updateCart")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response updateCart(String json)
	{
		
		Util util = new Util();		
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		
		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();	
		
	    Gson gson = new GsonBuilder()
		        .registerTypeAdapter(
		        		  LocalDate.class, 
		        		  new LocalDateGsonAdapter()
		        ).create();

		ShoppingCarDto objReq = null;				
			
		try {
			objReq = gson.fromJson(json, ShoppingCarDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(util.getJsonResponse("14")).build();			
		}
			
		if (objReq == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();		
		
		ShoppingCarService scc = new ShoppingCarService();
		objReq.setId_client(objDto.getId_user()); 

		ShoppingCarDto objResp = scc.updateCart(objReq);		
		if ("OK".equalsIgnoreCase(objResp.getErrorCode()))
			return Response.ok(util.getJsonResponse("0",objResp)).build(); 
		else if ("DID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("33")).build();				
		else if ("CAR-NOTUSER".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("34")).build();				
		else if ("CAR-NOVALID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("35")).build();				
		else if ("CAR-NOTFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("36")).build();						
		else if ("DITEMS".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("24")).build();					
		
		else if ("ITEM_ID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("37")).build();						
		else if ("ITEM_Q".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("26")).build();		
		else if ("ITEM_NOFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("38")).build();
		else if ("ITEM_NORELCAR".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("39")).build();				
		else if ("ITEM_RENUPNOTV".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("40")).build();						
		else if ("ITEMS_DUPLICATE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("41")).build();								
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("43")).build();
	}
	
	@DELETE
	@Path("/deleteItemCart")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response deleteItemCart(String json)
	{
		
		Util util = new Util();		
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());
		
		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();	
		
	    Gson gson = new GsonBuilder()
		        .registerTypeAdapter(
		        		  LocalDate.class, 
		        		  new LocalDateGsonAdapter()
		        ).create();

		ShoppingCarDto objReq = null;				
			
		try {
			objReq = gson.fromJson(json, ShoppingCarDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(util.getJsonResponse("14")).build();			
		}
			
		if (objReq == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();		
		
		ShoppingCarService scc = new ShoppingCarService();
		objReq.setId_client(objDto.getId_user()); 

		ShoppingCarDto objResp = scc.deleteItemCart(objReq);
		
		if ("OK".equalsIgnoreCase(objResp.getErrorCode()))
			return Response.ok(util.getJsonResponse("0",objResp)).build(); 
		else if ("DID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("33")).build();				
		else if ("CAR-NOTUSER".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("34")).build();				
		else if ("CAR-NOVALID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("35")).build();				
		else if ("CAR-NOTFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("36")).build();						
		else if ("DITEMS".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("24")).build();							
		else if ("ITEM_ID".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("37")).build();						
		else if ("ITEM_NOFOUND".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("38")).build();
		else if ("ITEM_NORELCAR".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("39")).build();						
		else if ("ITEMS_DUPLICATE".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("41")).build();						
		else if ("ERROR_RELOAD".equals(objResp.getErrorCode()) )
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("45")).build();						
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("44")).build();
	}
	
	@POST
	@Path("/user/register")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)	
	public Response registerNewUsers(String json)
	{
		
		Util util = new Util();		
		
		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();	
		
	    Gson gson = new GsonBuilder()
		        .registerTypeAdapter(
		        		  LocalDate.class, 
		        		  new LocalDateGsonAdapter()
		        ).create();

		FullUserDto objUser = null;				
			
		try {
			objUser = gson.fromJson(json, FullUserDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(util.getJsonResponse("14")).build();			
		}
			
		if (objUser == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();		
		
		UserService userService = new UserService();

		User resp = userService.registerUser(objUser);
		
		//send Email 
		String resultEmail = "";
		if ("OK".equalsIgnoreCase(resp.getError()))
		{
			resultEmail =  util.sendEmail(MyConstants.EMAIL_FROM, 
										  MyConstants.EMAIL_TO, 
										  MyConstants.EMAIL_SUBJECT, 
										  String.format(MyConstants.HTML_TEMPLATE_CONFIRMATION, 
													          resp.getClient().getFirst_name() + " " + resp.getClient().getLast_name(),
													          resp.getConfirm_token()
													    )
										  );
		}	
		
		if ("OK".equalsIgnoreCase(resp.getError()))
		{
			if ("OK".equals(resultEmail))
				return Response.ok(util.getJsonResponse("0")).build(); 			
			else
				return Response.status(Response.Status.OK)
						.entity(util.getJsonResponse("62")).build();						
		}
		else if ("PASS-BAD".equals(resp.getError()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("46")).build();						
		else if ("ERROR_PASS".equals(resp.getError()) )
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("47")).build();								
		else if ("DATA_INV".equals(resp.getError()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("48")).build();										
		else if ("USER_FOUND".equals(resp.getError()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("50")).build();		
		else if ("UNKNOWN_ERROR".equals(resp.getError()) )
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("51")).build();										
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("49")).build();
	}
		
	@GET
	@Path("user/confirm")	
	@Produces(MediaType.TEXT_HTML)
	public Response confirmNewUser(@QueryParam("token") String token)
	{
		Util util = new Util();		
		String msjHTML = "Error";
		
		if (token == null )
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","52");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(msjHTML).build();						
		}


		if (token.isEmpty() )
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","52");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(msjHTML).build();								
		}	
		
		UserService service = new UserService();
		User user = service.validateConfirmToken(token);
		
		if ("OK".equals(user.getError()))
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","54");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.OK).entity(msjHTML).build();							
		}
		else if ("USER_NOTFOUND".equals(user.getError()))
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","53");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(msjHTML).build();							
		}
		else if ("UNKNOWN_ERROR".equals(user.getError()))
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","55");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msjHTML).build();							
		}
		else if ("TOKEN_EXP".equals(user.getError()))
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","56");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(msjHTML).build();							
		}					
		else if ("CONFIRM_ALREADY".equals(user.getError()))
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","58");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.BAD_REQUEST).entity(msjHTML).build();							
		}							
		else
		{
			try {
				msjHTML = util.getConfigValueByKey("errorsMsj.properties","57");
			} catch (Exception e) {
				log.error("error getMsj", e);
			}
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(msjHTML).build();							
		}					
	}
	
	@POST
	@Path("user/forgotPassword")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response forgotPassword(String emailOrUserName)
	{
		Util util = new Util();		
		
		if (emailOrUserName == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("63")).build();			

		if (emailOrUserName.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("63")).build();	
		
		
		UserService service = new UserService();
		User resp = service.forgotPassword(emailOrUserName);
		
		if ("VALID".equalsIgnoreCase(resp.getError()))
		{
			String resultEmail =  util.sendEmail(MyConstants.EMAIL_FROM, 
												 MyConstants.EMAIL_TO, 
												 MyConstants.EMAIL_SUBJECT_FORGOT, 
												 String.format(MyConstants.HTML_TEMPLATE_FORGOT_PASSWORD, 
														          resp.getClient().getFirst_name() + " " + resp.getClient().getLast_name(),
														          resp.getData()
														       )
												 );
			if ("OK".equals(resultEmail))
				return Response.ok(util.getJsonResponse("0")).build(); 			
			else
				return Response.status(Response.Status.ACCEPTED)
						.entity(util.getJsonResponse("67")).build();						
		}
		else if ("OK".equals(resp.getError()) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("64")).build();						
		else if ("UNKNOWN_ERROR".equals(resp.getError()) )
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("51")).build();			
		else if ("ERROR_PASS".equals(resp.getError()) )
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("65")).build();	
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("66")).build();		

	}
	
	@POST
	@Path("user/changerole")	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@RequiredAuthorization
	public Response changeRole(String json)
	{
		Util util = new Util();		
		
		UserDto objDto = SecurityUtil.getUserDtoData(securityContext.getUserPrincipal().getName());

		if (objDto.getId_role() != MyConstants.CODE_ADMIN_ROLE)
			return Response.status(Response.Status.FORBIDDEN)
					.entity(util.getJsonResponse("3")).build();				
		
		if (json == null )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();			

		if (json.isEmpty() )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("13")).build();	
		
	    Gson gson = new Gson();
		UserDto objReq = null;				
			
		try {
			objReq = gson.fromJson(json, UserDto.class);				
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST)
				.entity(util.getJsonResponse("14")).build();			
		}
			
		if (objReq == null)
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("14")).build();		
				
		UserService service = new UserService();
		String resp = service.updateUserRole(objReq);
		
		if ("OK".equalsIgnoreCase(resp))
			return Response.ok(util.getJsonResponse("0")).build(); 			
		else if ("DATA_INVALID".equals(resp) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("68")).build();						
		else if ("USER_NOTFOUND".equals(resp) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("69")).build();			
		else if ("USER_INACTIVE".equals(resp) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("70")).build();	
		else if ("ROLE_NOTFOUND".equals(resp) )
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(util.getJsonResponse("71")).build();	
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(util.getJsonResponse("72")).build();		

	}
	
	}

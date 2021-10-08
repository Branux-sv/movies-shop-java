package org.branuxsv.rentalmovies.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.branuxsv.rentalmovies.dao.RoleDao;
import org.branuxsv.rentalmovies.dao.UserDao;
import org.branuxsv.rentalmovies.dto.DetailShoppingCarDto;
import org.branuxsv.rentalmovies.dto.ShoppingCarDto;
import org.branuxsv.rentalmovies.model.Client;
import org.branuxsv.rentalmovies.model.Role;
import org.branuxsv.rentalmovies.model.User;
import org.branuxsv.rentalmovies.service.ShoppingCarService;
import org.branuxsv.rentalmovies.util.MyConstants;
import org.branuxsv.rentalmovies.util.Util;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.sun.javafx.binding.SelectBinding.AsString;

public class ServiceTest {

	@Test(expected = IllegalArgumentException.class)
	@Ignore
	public void serviceValidateInputShoppingCartExeptionIsThrown_Test()
	{
		ShoppingCarService service_Car = new ShoppingCarService();
		ShoppingCarDto objReq = new ShoppingCarDto();
		
		objReq.setOperationType(MyConstants.OPERATION_TYPE.INSERT);
		objReq.setType_transaction(MyConstants.TRANSACTION_TYPE.PURCHASE.indexValue);
		
		List<DetailShoppingCarDto> list  =  new ArrayList();
		DetailShoppingCarDto d = new DetailShoppingCarDto();
		d.setId_item(-666);
		d.setId_movie(0);
		list.add(d);
		
		objReq.setItems(list);		
		objReq = service_Car.validateInputShoppingCart(objReq);	
		
	}
	
	@Test
	@Ignore
	public void serviceValidateInputShoppingCart_Insert_Test()
	{
		ShoppingCarService service_Car = new ShoppingCarService();
		ShoppingCarDto objReq = new ShoppingCarDto();
		
		objReq.setOperationType(MyConstants.OPERATION_TYPE.INSERT);
		objReq.setType_transaction(MyConstants.TRANSACTION_TYPE.RENTAL.indexValue);
		
		List<DetailShoppingCarDto> list  =  new ArrayList();
		DetailShoppingCarDto d = new DetailShoppingCarDto();
		d.setId_movie(4);
		d.setQuantity(10);
		d.setRented_date(LocalDate.now());
		d.setRented_days(3);
		list.add(d);
		
		objReq.setItems(list);		
		objReq = service_Car.validateInputShoppingCart(objReq);	

		assertEquals("OK", objReq.getErrorCode());
		
	}

	@Test	
	@Ignore
	public void serviceValidateInputShoppingCart_Update_Test()
	{
		ShoppingCarService service_Car = new ShoppingCarService();
		ShoppingCarDto objReq = new ShoppingCarDto();
		
		objReq.setOperationType(MyConstants.OPERATION_TYPE.UPDATE);
		objReq.setType_transaction(MyConstants.TRANSACTION_TYPE.RENTAL.indexValue);
		
		List<DetailShoppingCarDto> list  =  new ArrayList();
		DetailShoppingCarDto d = new DetailShoppingCarDto();
		d.setId_item(3);
		d.setId_movie(2);
		d.setQuantity(3);
		d.setRented_date(LocalDate.now());
		d.setRented_days(3);
		list.add(d);
		
		objReq.setItems(list);		
		objReq.setId_Cart(4); //For update/Delete
		objReq.setId_client(1);
		
		
		objReq = service_Car.validateInputShoppingCart(objReq);	

		assertEquals("OK", objReq.getErrorCode());
		
	}

	@Test
	@Ignore
	public void serviceRegisterUser_Test()
	{
		UserDao dao = new UserDao();		

		User user = new User();		
		Client client = new Client();
		

		user.setUsername("Panfilo");
		user.setPassword("123");
		user.setConfirm_token(UUID.randomUUID().toString().replace("-", ""));
		user.setConfirmed(0);
		user.setConfirm_token_expiration(LocalDateTime.now().plusDays(1));						
		user.setId_role(2);;

		client.setActive(0);
		client.setFirst_name("Panfilo");
		client.setLast_name("Matsumoto");
		client.setEmail("chiloyo@gmail.com");
		client.setPhone("78769999");
		client.setAddress("algunn lugar lejos de la porcina");
	
		user.setClient(client);
		client.setUser(user);

		User newUser = dao.save(user);
		
		assertNotNull(newUser);
		
	}

	@Test
	@Ignore
	public void serviceUser_findUserByuserNameOrEmail_Test()
	{
		UserDao dao = new UserDao();		
		
		User user = dao.existsUserByNameOrEmail("CHEPITO", "_2laPurga@gmail.com");
		
		assertEquals("OK", user.getError());		
	}

	@Test
	@Ignore
	public void sendEmail_Test()
	{
		
		Util u = new Util();		
		String re = u.sendEmail("notification@rentalMovie.com", "asd123ZZZ.5@gmail.com", "Rental Movie App - Confirmation User", String.format(MyConstants.HTML_TEMPLATE_CONFIRMATION, "Carolina Martinez", "KALA123KALA"));
		assertEquals("OK",re);
	}
}

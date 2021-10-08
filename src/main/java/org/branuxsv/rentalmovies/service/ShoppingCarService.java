package org.branuxsv.rentalmovies.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.branuxsv.rentalmovies.dao.ClientDao;
import org.branuxsv.rentalmovies.dao.DetailShoppingCartDao;
import org.branuxsv.rentalmovies.dao.MovieDao;
import org.branuxsv.rentalmovies.dao.ShoppingCarDao;
import org.branuxsv.rentalmovies.dto.DetailShoppingCarDto;
import org.branuxsv.rentalmovies.dto.ShoppingCarDto;
import org.branuxsv.rentalmovies.model.Client;
import org.branuxsv.rentalmovies.model.DetailShoppingCar;
import org.branuxsv.rentalmovies.model.Movie;
import org.branuxsv.rentalmovies.model.ShoppingCar;
import org.branuxsv.rentalmovies.util.MyConstants;
import org.branuxsv.rentalmovies.util.MyConstants.OPERATION_TYPE;
import org.branuxsv.rentalmovies.util.Util;

/**
* Service class that contains methods of bussiness rules about shopping cart entity 
* and connect DAO objects with APi methods  
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-15 */

public class ShoppingCarService {

	public ShoppingCar getShoppingCar(long id)
	{
		ShoppingCarDao dao  = new ShoppingCarDao();
		return dao.get(id);		
	}	
	
	public ShoppingCarDto createCart(ShoppingCarDto objReq)
	{
		ShoppingCarDto objRespDto = new ShoppingCarDto();
		objReq.setOperationType(MyConstants.OPERATION_TYPE.INSERT);		

		ClientDao daoClient = new ClientDao();
		Client data = daoClient.getByIdUser(objReq.getId_client());
		objReq.setId_client(data.getId_client());
		
		validateInputShoppingCart(objReq);	
		objRespDto.setErrorCode(objReq.getErrorCode());		
		
		if (objReq.getErrorCode().equals("OK")) //Validation OK ...
		{
			//Convert from DTO to entity Objects...
			ShoppingCar car = new ShoppingCar();
			car.setCurrency(MyConstants.CURRENCY); //Only USD 
			car.setDate(LocalDate.now());
			car.setId_client(objReq.getId_client());
			car.setId_state_cart(MyConstants.DEFAULT_STATUS_CART);
			car.setType_transaction(objReq.getType_transaction());

			calculateTotals(objReq);
			//SubTotal
			car.setSubTotal(objReq.getSub_Total());			
			//Tax...
			car.setTax(objReq.getTax());			
			//Discount
			car.setDiscount(objReq.getDiscount());			
			//Total Payment
			car.setTotal_payment(objReq.getTotal_payment());
			
			//Create relationShip
			car.setListItemsOfCar(getListItemsShoppingCart(objReq, car));
			
			//Insert in DataBase and fill the response object 
			ShoppingCarDao daoCar = new ShoppingCarDao();
			car = daoCar.save(car);
			
			if (car != null)
			{
				//Convert Entity Object to DTO 
				objRespDto.setId_Cart(car.getId_cart());
				//objRespDto.setType_transaction(car.getType_transaction());
				objRespDto.setCurrency(car.getCurrency());
				objRespDto.setDate(car.getDate());
				objRespDto.setSub_Total(Util.getFormat2Decimal(car.getSubTotal()));
				objRespDto.setTax(Util.getFormat2Decimal(car.getTax()) );
				objRespDto.setDiscount( Util.getFormat2Decimal(car.getDiscount()));
				objRespDto.setTotal_payment(Util.getFormat2Decimal(car.getTotal_payment()));
				objRespDto.setTotal_payment_format(Util.getFormat2Decimal(car.getTotal_payment(), car.getCurrency()));
				objRespDto.setCount_items(car.getListItemsOfCar().size());
				objRespDto.setItems(getListItemsShoppingCartDto(car));
			}
			else
				objRespDto.setErrorCode("ERROR_SAVE");			
				
		}						
		return objRespDto;
	}
		
	public List<DetailShoppingCarDto> getListItemsShoppingCartDto(ShoppingCar car)
	{
		List<DetailShoppingCarDto> list = new ArrayList<>();
		
		car.getListItemsOfCar().forEach(item ->{			
			DetailShoppingCarDto newItem = new DetailShoppingCarDto();
			newItem.setId_item(item.getId_item());
			newItem.setId_movie(item.getId_movie());
			newItem.setQuantity(item.getQuantity());
			newItem.setSub_total(Util.getFormat2Decimal(item.getSub_total()));
			if (car.getType_transaction() == MyConstants.TRANSACTION_TYPE.RENTAL.indexValue)
			{
				newItem.setRented_days(item.getRented_days());
				newItem.setRented_date(item.getRented_date());	
				newItem.setRenturned_date(item.getRented_date().plusDays(item.getRented_days()));
			}
			list.add(newItem);
		});
		
		return list;
		
	}
	
	public List<DetailShoppingCar> getListItemsShoppingCart(ShoppingCarDto car, ShoppingCar realCar)
	{
		List<DetailShoppingCar> list = new ArrayList<>();
		
		car.getItems().forEach(item ->{			
			DetailShoppingCar newItem = new DetailShoppingCar();
			newItem.setActive(MyConstants.DEFAULT_STATUS_CART);
			newItem.setId_movie(item.getId_movie());
			newItem.setQuantity(item.getQuantity());
			newItem.setSub_total(item.getSub_total());
			if (car.getType_transaction() == MyConstants.TRANSACTION_TYPE.RENTAL.indexValue)
			{
				newItem.setRented_days(item.getRented_days());
				newItem.setRented_date(item.getRented_date());				
			}
			//for update item to cart
			if (item.getId_item() > 0)
				newItem.setId_item(item.getId_item());
			
			newItem.setShoppingCar(realCar);
			list.add(newItem);
		});
		
		return list;
	}
	
	public void calculateTotals(ShoppingCarDto car)
	{
		
		car.getItems().forEach(item ->{			

			MovieDao daoMovie = new MovieDao();
			Movie movie = daoMovie.get(item.getId_movie());
			if (car.getType_transaction() == MyConstants.TRANSACTION_TYPE.PURCHASE.indexValue)			
				item.setSub_total( movie.getSale_price() * item.getQuantity() );
			else
				item.setSub_total( movie.getRental_price() * item.getQuantity() * item.getRented_days().intValue() );
		});
		
		//****    Set Totals  ********
		
		//SubTotal
		car.setSub_Total(car.getItems().stream().mapToDouble(DetailShoppingCarDto::getSub_total).sum());
		
		//Tax...
		car.setTax(MyConstants.DEFAULT_TAX * car.getSub_Total());
		
		//Discount
		car.setDiscount(MyConstants.DEFAULT_DISCOUNT * car.getSub_Total());
		
		//Total Payment
		car.setTotal_payment(car.getSub_Total() + car.getTax() - car.getDiscount());
		
	}

	public <T> void calculateTotals(T car)
	{
		
		if (car instanceof ShoppingCarDto)
		{
			((ShoppingCarDto)car).getItems().forEach(item ->{			

				MovieDao daoMovie = new MovieDao();
				Movie movie = daoMovie.get(item.getId_movie());
				if (((ShoppingCarDto)car).getType_transaction() == MyConstants.TRANSACTION_TYPE.PURCHASE.indexValue)			
					item.setSub_total( movie.getSale_price() * item.getQuantity() );
				else
					item.setSub_total( movie.getRental_price() * item.getQuantity() * item.getRented_days().intValue() );
			});			
			
			//****    Set Totals  ********
			
			//SubTotal
			((ShoppingCarDto)car).setSub_Total(((ShoppingCarDto)car).getItems().stream().mapToDouble(DetailShoppingCarDto::getSub_total).sum());
			
			//Tax...
			((ShoppingCarDto)car).setTax(MyConstants.DEFAULT_TAX * ((ShoppingCarDto)car).getSub_Total());
			
			//Discount
			((ShoppingCarDto)car).setDiscount(MyConstants.DEFAULT_DISCOUNT * ((ShoppingCarDto)car).getSub_Total());
			
			//Total Payment
			((ShoppingCarDto)car).setTotal_payment(((ShoppingCarDto)car).getSub_Total() + ((ShoppingCarDto)car).getTax() - ((ShoppingCarDto)car).getDiscount());
		}
		else if (car instanceof ShoppingCar)
		{
			((ShoppingCar)car).getListItemsOfCar().forEach(item ->{			

				MovieDao daoMovie = new MovieDao();
				Movie movie = daoMovie.get(item.getId_movie());
				if (((ShoppingCar)car).getType_transaction() == MyConstants.TRANSACTION_TYPE.PURCHASE.indexValue)			
					item.setSub_total( movie.getSale_price() * item.getQuantity() );
				else
					item.setSub_total( movie.getRental_price() * item.getQuantity() * item.getRented_days() );
			});			
			
			//****    Set Totals  ********
			
			//SubTotal
			((ShoppingCar)car).setSubTotal(((ShoppingCar)car).getListItemsOfCar().stream().mapToDouble(DetailShoppingCar::getSub_total).sum());
			
			//Tax...
			((ShoppingCar)car).setTax(MyConstants.DEFAULT_TAX * ((ShoppingCar)car).getSubTotal());
			
			//Discount
			((ShoppingCar)car).setDiscount(MyConstants.DEFAULT_DISCOUNT * ((ShoppingCar)car).getSubTotal());
			
			//Total Payment
			((ShoppingCar)car).setTotal_payment(((ShoppingCar)car).getSubTotal() + ((ShoppingCar)car).getTax() - ((ShoppingCar)car).getDiscount());
			
		}

		
	}

	public ShoppingCarDto validateInputShoppingCart(ShoppingCarDto obj)
	{
		obj.setErrorCode("OK"); //Guess All is OK		
		
		ShoppingCar car = null;
		
		//validate the input JSON		
		if (obj.getOperationType() == OPERATION_TYPE.INSERT)
		{

			if (obj.getType_transaction() == null) //Type transaction is required
			{
				obj.setErrorCode("DTYPE");
				return obj;
			}	
			
			if (!(obj.getType_transaction() > 0)) //Type transaction is required
			{
				obj.setErrorCode("DTYPE");
				return obj;
			}	
			
			if (!(obj.getId_client() > 0)) //ID client
			{
				obj.setErrorCode("IDCLINOTF");
				return obj;
			}		
		}			
		
		if (obj.getOperationType() == OPERATION_TYPE.UPDATE || obj.getOperationType() == OPERATION_TYPE.DELETE
				|| obj.getOperationType() == OPERATION_TYPE.ADD_CART)
		{
			if (!(obj.getId_Cart() > 0)) 
			{
				obj.setErrorCode( "DID");
				return obj;
			}

			ShoppingCarDao daoShop = new ShoppingCarDao();
			car = daoShop.get(obj.getId_Cart());
			if (car != null)
			{
				if (car.getId_state_cart() == MyConstants.DEFAULT_STATUS_CART)
				{
					
					if (car.getId_client() != obj.getId_client())
					{
						//Validate that other Users can't modify the shopping Cart...
						obj.setErrorCode( "CAR-NOTUSER"); 
						return obj;						
					}
					else
						obj.setType_transaction(car.getType_transaction());					 
				}
				else
				{
					obj.setErrorCode( "CAR-NOVALID");
					return obj;
				}
			}
			else
			{
				obj.setErrorCode( "CAR-NOTFOUND");
				return obj;
			}
		}
		
		if (!(obj.getItems() != null) || obj.getItems().isEmpty()) //Items are required!
		{
			obj.setErrorCode("DITEMS");
			return obj;
		}
		
		//News validations... validate there is not duplicated id movie for add/create or id item for update/delete
		List<Long> listIdsItems = null;
		
		if (obj.getOperationType() == OPERATION_TYPE.INSERT || obj.getOperationType() == OPERATION_TYPE.ADD_CART)
			listIdsItems = obj.getItems().stream().map(DetailShoppingCarDto::getId_movie).collect(Collectors.toList());				

		if (obj.getOperationType() == OPERATION_TYPE.UPDATE || obj.getOperationType() == OPERATION_TYPE.DELETE)
			listIdsItems = obj.getItems().stream().map(DetailShoppingCarDto::getId_item).collect(Collectors.toList());
		
		Set<Long> setIdsNoDuplicates = new HashSet<>(listIdsItems); //HashSet dont allow duplicates values
		if (obj.getItems().size() != setIdsNoDuplicates.size())
		{
			obj.setErrorCode("ITEMS_DUPLICATE");
			return obj;
		}		
		
		List<DetailShoppingCar> listItemsInDB = null;
		if (obj.getOperationType() == OPERATION_TYPE.ADD_CART)
		{
			listItemsInDB = car.getListItemsOfCar();			
		}						
		
		//Validate the content of the each Item in the shoppingCart
		obj.getItems().forEach(item -> 
		{
			
			if (obj.getOperationType() == OPERATION_TYPE.UPDATE || obj.getOperationType() == OPERATION_TYPE.DELETE)
			{
				if (!(item.getId_item() > 0)) 
				{
					obj.setErrorCode("ITEM_ID");
					return;
				}
				DetailShoppingCartDao daoItem = new DetailShoppingCartDao();
				DetailShoppingCar detail = daoItem.get(item.getId_item());
				
				if (detail == null)
				{
					obj.setErrorCode("ITEM_NOFOUND");
					return;
				}
				else
				{
					if (detail.getId_cart() != obj.getId_Cart())
					{
						obj.setErrorCode("ITEM_NORELCAR");
						return;
					}
				}
				daoItem = null;
				detail = null;
			}
			
			if (obj.getOperationType() == OPERATION_TYPE.UPDATE)
			{				
				if (obj.getType_transaction() == MyConstants.TRANSACTION_TYPE.RENTAL.indexValue)
				{

					if ((item.getRented_date() == null || item.getRented_date().isBefore(LocalDate.now())) &&
						(item.getRented_days() == null || item.getRented_days() <=0) && 
						item.getQuantity() <= 0)
					{
						obj.setErrorCode("ITEM_RENUPNOTV");
						return;												
					}
					
				}
				else //Purchase
				{
					if (!(item.getQuantity() > 0))
					{
						obj.setErrorCode("ITEM_Q");
						return;
					}
				}
	
			}
			
			if (obj.getOperationType() == OPERATION_TYPE.INSERT || obj.getOperationType() == OPERATION_TYPE.ADD_CART)
			{
				if (!(item.getId_movie() > 0))
				{
					obj.setErrorCode("ITEM_M");
					return;
				}

				if (!(item.getQuantity() > 0))
				{
					obj.setErrorCode("ITEM_Q");
					return;
				}
				
				MovieDao daoMovie = new MovieDao();
				Movie movie = daoMovie.get(item.getId_movie());
				
				if (movie != null)
				{
					
					if (movie.getAvailability() != MyConstants.CODE_AVAILABILITY)
					{
						obj.setErrorCode("ITEM_MUNAVA");
						return;												
					}
				}
				else
				{
					obj.setErrorCode("ITEM_MNOFOUND");
					return;
				}				
				
				movie = null;			
				daoMovie = null;
				
				if (obj.getType_transaction() == MyConstants.TRANSACTION_TYPE.RENTAL.indexValue)
				{
					
					if (item.getRented_date() == null)
					{
						obj.setErrorCode("ITEM_NORD");
						return;						
					}
					else
					{
						if (item.getRented_date().isBefore(LocalDate.now()))
						{
							obj.setErrorCode("ITEM_RDBEFORE");
							return;						
						}						
					}

					if (item.getRented_days() == null)
					{
						obj.setErrorCode("ITEM_NODAY");
						return;						
					}					
					
					if (!(item.getRented_days() > 0))
					{
						obj.setErrorCode("ITEM_NODAY");
						return;						
					}					
					
				}								
			}						

		});
		
		if (obj.getOperationType() == OPERATION_TYPE.ADD_CART)
		{
			List<Long> auxList = listIdsItems;						
			if (listItemsInDB.stream().anyMatch(i -> auxList.contains(i.getId_movie())))
			{
				obj.setErrorCode("M_ALREADY_EXISTS");
				return obj;
			}
		}
		
		return obj;
	}

	public ShoppingCarDto addCart(ShoppingCarDto objReq)
	{
		ShoppingCarDto objRespDto = new ShoppingCarDto();
		objReq.setOperationType(MyConstants.OPERATION_TYPE.ADD_CART);		

		ClientDao daoClient = new ClientDao();
		Client data = daoClient.getByIdUser(objReq.getId_client());
		objReq.setId_client(data.getId_client());

		validateInputShoppingCart(objReq);	
		objRespDto.setErrorCode(objReq.getErrorCode());		
		
		if (objReq.getErrorCode().equals("OK")) //Validation OK ...
		{
			ShoppingCarDao daoShop = new ShoppingCarDao();
			ShoppingCar car = daoShop.get(objReq.getId_Cart());
			
			//create a copy of objReq
			ShoppingCarDto objReqCopy = new ShoppingCarDto();
			objReqCopy.setType_transaction(objReq.getType_transaction());
			List<DetailShoppingCarDto> listDetAux = new ArrayList();			
			objReqCopy.setItems(listDetAux);
			objReqCopy.getItems().addAll(objReq.getItems());
			
			//Pass All Existing items to request
			objReq.getItems().addAll(getListItemsShoppingCartDto(car));
			
			calculateTotals(objReq);
			
			//SubTotal
			car.setSubTotal(objReq.getSub_Total());			
			//Tax...
			car.setTax(objReq.getTax());			
			//Discount
			car.setDiscount(objReq.getDiscount());			
			//Total Payment
			car.setTotal_payment(objReq.getTotal_payment());
			
			//Create relationShip
			calculateTotals(objReqCopy); //for subTotal of details...			
			car.getListItemsOfCar().addAll(getListItemsShoppingCart(objReqCopy, car)); //objReq must be the copy object
			
			//Insert in DataBase and fill the response object 
			car = daoShop.update(car);
			
			if (car != null)
			{
				//Convert Entity Object to DTO 
				objRespDto.setId_Cart(car.getId_cart());
				objRespDto.setCurrency(car.getCurrency());
				objRespDto.setDate(car.getDate());
				objRespDto.setSub_Total(Util.getFormat2Decimal(car.getSubTotal()));
				objRespDto.setTax(Util.getFormat2Decimal(car.getTax()) );
				objRespDto.setDiscount( Util.getFormat2Decimal(car.getDiscount()));
				objRespDto.setTotal_payment(Util.getFormat2Decimal(car.getTotal_payment()));
				objRespDto.setTotal_payment_format(Util.getFormat2Decimal(car.getTotal_payment(), car.getCurrency()));
				objRespDto.setCount_items(car.getListItemsOfCar().size());
				objRespDto.setItems(getListItemsShoppingCartDto(car));
			}
			else
				objRespDto.setErrorCode("ERROR_SAVE");						
									
		}
		
		return objRespDto;
	}

	public ShoppingCarDto updateCart(ShoppingCarDto objReq)
	{
		ShoppingCarDto objRespDto = new ShoppingCarDto();
		objReq.setOperationType(MyConstants.OPERATION_TYPE.UPDATE);		

		ClientDao daoClient = new ClientDao();
		Client data = daoClient.getByIdUser(objReq.getId_client());
		objReq.setId_client(data.getId_client());

		validateInputShoppingCart(objReq);	
		objRespDto.setErrorCode(objReq.getErrorCode());		
		
		if (objReq.getErrorCode().equals("OK")) //Validation OK ...
		{
			ShoppingCarDao daoShop = new ShoppingCarDao();
			ShoppingCar car = daoShop.get(objReq.getId_Cart());

			objReq.setType_transaction(car.getType_transaction());

			List<Long> listIdsModifyItems = objReq.getItems().stream()
									         .map(DetailShoppingCarDto::getId_item)
									         .collect(Collectors.toList());

			List<DetailShoppingCar> listNotModify = car.getListItemsOfCar().stream()
							     .filter(d -> listIdsModifyItems.stream().noneMatch(id -> id.intValue() == d.getId_item()))												
							     .collect(Collectors.toList());
			
			List<DetailShoppingCar> listModify = car.getListItemsOfCar().stream()
								.filter(d -> listIdsModifyItems.stream().anyMatch(id -> id.intValue() == d.getId_item()))												
								.collect(Collectors.toList());
								
			ShoppingCar carCopy = new ShoppingCar();
			
			objReq.getItems().forEach(i -> {
				listModify.forEach(j -> {
					if (i.getId_item() == j.getId_item())
					{
						if (i.getQuantity() > 0 && i.getQuantity() != j.getQuantity())
							j.setQuantity(i.getQuantity());
						
						if (objReq.getType_transaction() == MyConstants.TRANSACTION_TYPE.RENTAL.indexValue)
						{
							if (i.getRented_days() != null && i.getRented_days() > 0 && i.getRented_days() != j.getRented_days())
								j.setRented_days(i.getRented_days());

							if (i.getRented_date() != null && !i.getRented_date().isEqual(j.getRented_date()) )
								j.setRented_date(i.getRented_date());						
						}										
						
					}
				});
			});

			
			List<DetailShoppingCar> mergeList = Stream.of(listModify, listNotModify)
														.flatMap(x -> x.stream())
													    .collect(Collectors.toList());		

			carCopy.setListItemsOfCar(mergeList);
			carCopy.setType_transaction(car.getType_transaction());	
			
			objReq.setItems(getListItemsShoppingCartDto(carCopy));
			
			calculateTotals(objReq);

			//SubTotal
			car.setSubTotal(objReq.getSub_Total());			
			//Tax...
			car.setTax(objReq.getTax());			
			//Discount
			car.setDiscount(objReq.getDiscount());			
			//Total Payment
			car.setTotal_payment(objReq.getTotal_payment());
			
			//update fields			
			car.getListItemsOfCar().forEach(i -> {
				objReq.getItems().forEach(j -> {

					if (i.getId_item() == j.getId_item() 
							&& listIdsModifyItems.stream().anyMatch(id -> id.intValue() == i.getId_item()))
					{
						i.setSub_total(j.getSub_total());
						
						if (j.getQuantity() > 0 && j.getQuantity() != j.getQuantity())
							i.setQuantity(j.getQuantity());
						
						if (objReq.getType_transaction() == MyConstants.TRANSACTION_TYPE.RENTAL.indexValue)
						{
							if (j.getRented_days() > 0 && j.getRented_days() != j.getRented_days())
								i.setRented_days(j.getRented_days());

							if (j.getRented_date() != null && !j.getRented_date().isEqual(i.getRented_date()) )
								i.setRented_date(j.getRented_date());						
						}										
						
					}
				});
			}); 
			
			//Update in DataBase and fill the response object 
			car = daoShop.update(car);
			
			if (car != null)
			{
				//Convert Entity Object to DTO 
				objRespDto.setId_Cart(car.getId_cart());
				objRespDto.setCurrency(car.getCurrency());
				objRespDto.setDate(car.getDate());
				objRespDto.setSub_Total(Util.getFormat2Decimal(car.getSubTotal()));
				objRespDto.setTax(Util.getFormat2Decimal(car.getTax()) );
				objRespDto.setDiscount( Util.getFormat2Decimal(car.getDiscount()));
				objRespDto.setTotal_payment(Util.getFormat2Decimal(car.getTotal_payment()));
				objRespDto.setTotal_payment_format(Util.getFormat2Decimal(car.getTotal_payment(), car.getCurrency()));
				objRespDto.setCount_items(car.getListItemsOfCar().size());
				objRespDto.setItems(getListItemsShoppingCartDto(car));
			}
			else
				objRespDto.setErrorCode("ERROR_SAVE");						
									
			
		}
		
		return objRespDto;		
	}

	public ShoppingCarDto deleteItemCart(ShoppingCarDto objReq)
	{
		//NOTE: DELETE THE CART IF A DLETE ALL ITEMS OR THE LAST ITEM ????
		ShoppingCarDto objRespDto = new ShoppingCarDto();
		objReq.setOperationType(MyConstants.OPERATION_TYPE.DELETE);		

		ClientDao daoClient = new ClientDao();
		Client data = daoClient.getByIdUser(objReq.getId_client());
		objReq.setId_client(data.getId_client());

		validateInputShoppingCart(objReq);	
		objRespDto.setErrorCode(objReq.getErrorCode());		
		
		if (objReq.getErrorCode().equals("OK")) //Validation OK ...
		{
			ShoppingCarDao daoShop = new ShoppingCarDao();
			ShoppingCar car = daoShop.get(objReq.getId_Cart());

			List<Long> listIdsDelete = objReq.getItems().stream()
								         .map(DetailShoppingCarDto::getId_item)
								         .collect(Collectors.toList());
			
			//Iterate from items and delete one by one 
			car.getListItemsOfCar().forEach(i -> { 				
				if (listIdsDelete.stream().anyMatch(j -> j == i.getId_item()))
				{
					DetailShoppingCartDao daoShopDet = new DetailShoppingCartDao();
					DetailShoppingCar det = daoShopDet.get(i.getId_item());
					if (!daoShopDet.delete(det))
					{
						objRespDto.setErrorCode("ERROR_SAVEITEMS");							
						return;
					}

				}
			});

			if (objRespDto.getErrorCode().equals("OK")) 
			{
				//Then get reload the object car from the database
				car = daoShop.get(objReq.getId_Cart());
				
				//Again calculate the monetary fields because it was a change in data
				calculateTotals(car);
							
				//update the object car			
				car = daoShop.update(car);
				
				if (car != null)
				{
					//Convert Entity Object to DTO 
					objRespDto.setId_Cart(car.getId_cart());
					objRespDto.setCurrency(car.getCurrency());
					objRespDto.setDate(car.getDate());
					objRespDto.setSub_Total(Util.getFormat2Decimal(car.getSubTotal()));
					objRespDto.setTax(Util.getFormat2Decimal(car.getTax()) );
					objRespDto.setDiscount( Util.getFormat2Decimal(car.getDiscount()));
					objRespDto.setTotal_payment(Util.getFormat2Decimal(car.getTotal_payment()));
					objRespDto.setTotal_payment_format(Util.getFormat2Decimal(car.getTotal_payment(), car.getCurrency()));
					objRespDto.setCount_items(car.getListItemsOfCar().size());
					objRespDto.setItems(getListItemsShoppingCartDto(car));
				}
				else
					objRespDto.setErrorCode("ERROR_RELOAD");					
			}					

		}
		return objRespDto;
	}
}

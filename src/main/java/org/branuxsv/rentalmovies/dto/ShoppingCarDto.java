package org.branuxsv.rentalmovies.dto;

import java.time.LocalDate;
import java.util.List;

import org.branuxsv.rentalmovies.util.MyConstants;

/**
* The class holds properties of Shopping Cart to transport 
* 
* @version 1.0
* @author  Branux-SV
* @Date    2020-04-10 */

public class ShoppingCarDto {

	private long id_Cart;

	private Integer type_transaction;

	private String currency;
	
	private LocalDate date;

	private double sub_Total;

	private double tax;

	private double discount;
	
	private double total_payment;

	private String total_payment_format;		
	
	private int count_items;

	private List<DetailShoppingCarDto> items;
	
	private transient String errorCode;

	private transient long id_client;

	private transient MyConstants.OPERATION_TYPE operationType; 
	
	public ShoppingCarDto() {}
	
	public long getId_Cart() {
		return id_Cart;
	}

	public void setId_Cart(long id_Cart) {
		this.id_Cart = id_Cart;
	}

	public Integer getType_transaction() {
		return type_transaction;
	}

	public void setType_transaction(Integer type_transaction) {
		this.type_transaction = type_transaction;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getSub_Total() {
		return sub_Total;
	}

	public void setSub_Total(double sub_Total) {
		this.sub_Total = sub_Total;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotal_payment() {
		return total_payment;
	}

	public void setTotal_payment(double total_payment) {
		this.total_payment = total_payment;
	}

	public String getTotal_payment_format() {
		return total_payment_format;
	}

	public void setTotal_payment_format(String total_payment_format) {
		this.total_payment_format = total_payment_format;
	}

	public int getCount_items() {
		return count_items;
	}

	public void setCount_items(int count_items) {
		this.count_items = count_items;
	}

	public List<DetailShoppingCarDto> getItems() {
		return items;
	}

	public void setItems(List<DetailShoppingCarDto> items) {
		this.items = items;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public long getId_client() {
		return id_client;
	}

	public void setId_client(long id_client) {
		this.id_client = id_client;
	}		
	

	public MyConstants.OPERATION_TYPE getOperationType() {
		return operationType;
	}

	public void setOperationType(MyConstants.OPERATION_TYPE operationType) {
		this.operationType = operationType;
	}

}

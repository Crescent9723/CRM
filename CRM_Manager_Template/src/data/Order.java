package data;

import java.sql.Date;

public class Order {
	private int orderID;
	private String accountID;
	private Date date;
	private String productID;
	private String productName;
	private String payment;
	private String description;
	private boolean delivered;
	public Order(int orderID, String accountID, Date date, String productID, String productName,
			String payment, String description, boolean delivered) {
		this.orderID = orderID;
		this.accountID = accountID;
		this.date = date;
		this.productID = productID;
		this.productName = productName;
		this.payment = payment;
		this.description = description;
		this.delivered = delivered;
	}
	
	public String getProductName() {
		return productName;
	}
	public int getOrderID() {
		return orderID;
	}
	public String getAccountID() {
		return accountID;
	}
	public Date getDate() {
		return date;
	}
	public String getProductID() {
		return productID;
	}
	public String getPayment() {
		return payment;
	}
	public String getDescription() {
		return description;
	}
	public boolean isDelivered() {
		return delivered;
	}
	
}

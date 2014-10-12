package gecon.mod.alpha.misc;

import java.sql.Date;

public class Transaction {
	private double price;
	private int gECONItemID;
	private Date date;
	
	public Transaction(int gECONItemID, double price, Date date) {
		this.price = price;
		this.date = date;
		this.gECONItemID = gECONItemID;
	}
	
	public double getPrice() {
		return price;
	}
	
	public Date getDate() {
		return date;
	}
	
	public int getGECONItemID() {
		return gECONItemID;
	}
}
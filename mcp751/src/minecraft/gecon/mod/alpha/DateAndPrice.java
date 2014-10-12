package gecon.mod.alpha;

import java.sql.Date;

public class DateAndPrice {
	private Date date;
	private double price;
	
	/**
	 * Constructor for DateAndPrice
	 * @param date
	 * @param price
	 */
	public DateAndPrice(Date date,double price){
		this.date = date;
		this.price = price;
	}
	
	/**
	 * Gets the price
	 * @return
	 */
	public double getPrice() {
		return price;
	}
	
	/**
	 * Gets the dates
	 * @return
	 */
	public Date getDate() {
		return date;
	}
}

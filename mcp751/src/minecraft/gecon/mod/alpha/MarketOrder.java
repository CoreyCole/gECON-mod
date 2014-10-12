package gecon.mod.alpha;

import java.sql.Date;

import net.minecraft.item.ItemStack;

public class MarketOrder {
	/**
	 * The quantity
	 */
	public int quantity;
	
	/**
	 * The player's name
	 */
	public String playerName;
	
	/**
	 * The order
	 */
	public boolean buyOrder;
	
	/**
	 * The item
	 */
	public BankItem item;
	
	/**
	 * The price
	 */
	public double price;
	
	/**
	 * The date
	 */
	public Date date;
	
	/**
	 * The orderID
	 */
	public int orderID;
	
	/**
	 * The constructor
	 * @param MCItemID
	 * @param q
	 * @param p
	 * @param user
	 * @param buy
	 * @param day
	 * @param id
	 */
	public MarketOrder(String MCItemID, int q, double p, String user, boolean buy, Date day, int id){
		quantity = q;
		item = new BankItem(new ItemStack(Integer.parseInt(MCItemID), q, 0));
		date = day;
		price = p;
		orderID = id;
		buyOrder = buy;
		playerName = user;
	}
	
	/**
	 * Gets the orderID
	 * @return
	 */
	public int getOrderID() {
		return orderID;
	}
	
	/**
	 * Gets the type of order
	 * @return
	 */
	public String getOrderType() {
		if(buyOrder)
			return "BuyOrder";
		else
			return "SellOrder";
	}
	
}

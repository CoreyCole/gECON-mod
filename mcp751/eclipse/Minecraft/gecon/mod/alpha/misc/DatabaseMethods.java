package gecon.mod.alpha.misc;

import gecon.mod.alpha.BankItem;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class DatabaseMethods {
	private static final String fileName = "E:\\gECON_db.accdb";
	private static final String connectioinURL = "jdbc:odbc:DRIVER={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+fileName;
	
	/**
	 * Converts an Minecraft item ID into a gECON item ID
	 * @param MCItemID the Minecraft ID of the item
	 * @return the gECON item ID of the item
	 */
	public static int MCItemIDToGECONItemID(String MCItemID) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			s.execute("SELECT gECONItemID FROM Items WHERE MCItemID='"+MCItemID+"'");
			
			ResultSet rs = s.getResultSet();
			
			int gECONItemID = -1;
			
			while((rs!=null) && (rs.next())) {
				gECONItemID = rs.getInt(1);
			}
			
			s.close();
			conn.close();
			
			return gECONItemID;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Increments the quantity of the passed item name associated with the passed player
	 * @param playerName the String player name
	 * @param MCItemID the Minecraft ID of the item
	 * @return true if access to the database was successful
	 */
	public static boolean incrementItemInBank(String playerName, String MCItemID) {
		return addItemsIntoBankAccount(playerName, MCItemID, 1);
	}
	
	/**
	 * Decrements the quantity of the passed item name associated with the passed player
	 * @param playerName the String player name
	 * @param MCItemID the Minecraft ID of the item
	 * @return true if access to the database was successful
	 */
	public static boolean decrementItemInBank(String playerName, String MCItemID) {
		return addItemsIntoBankAccount(playerName, MCItemID, -1);
	}
	
	/**
	 * Adds a quantity of items into a bank account
	 * @param playerName the String name of the player 
	 * @param MCItemID the Minecraft ID of the item
	 * @param quantity the quantity
	 * @return true if access to the database was successful
	 */
	public static boolean addItemsIntoBankAccount(String playerName, String MCItemID, int quantity) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			int playerID = getPlayerID(playerName);
			int gECONItemID = MCItemIDToGECONItemID(MCItemID);
			
			s.execute("UPDATE BankAccounts SET Quantity = Quantity + "+quantity+ " WHERE (playerID="+playerID+" AND gECONItemID="+gECONItemID+")");
		
			s.close();
			conn.close();
			
			return true;
		} catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Adds coins to a player (this method accepts negative numbers)
	 * @param playerName the String player name
	 * @param quantity the quantity of coins to add (can be negative)
	 * @return true if access to the database was successful
	 */
	public static boolean addCoins(String playerName, int quantity) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
	
			int playerID = getPlayerID(playerName);
	
			s.execute("UPDATE Players SET Coins = Coins + "+quantity+" WHERE PlayerID="+playerID);
		
			s.close();
			conn.close();
		
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Returns the player ID associated with the passed player name
	 * @param playerName the String player name
	 * @return the integer player ID
	 */
	public static int getPlayerID(String playerName) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			s.execute("SELECT PlayerID FROM Players WHERE Username='"+playerName+"'");
		
			ResultSet rs = s.getResultSet();
			
			int playerID = -1;
			
			while((rs!=null) && (rs.next())) {
				playerID = rs.getInt(1);
			}
			
			return playerID;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Returns the number of a specific item stored in all banks
	 * @param MCItemID the Minecraft ID of the item
	 * @return the number of a specific item stored in all banks
	 */
	public static int getTotalNumItemsInBanks(String MCItemID) {
		try {
			String connURL = connectioinURL;
 			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			int gECONItemID = MCItemIDToGECONItemID(MCItemID);
			
			s.execute("SELECT Quantity FROM BankAccounts WHERE gECONItemID="+ gECONItemID);
	        
			ResultSet rs = s.getResultSet();
			
			int sum = 0;
				
			while((rs!=null) && (rs.next())) {
				sum += rs.getInt(1);
			}
					
			s.close();
			conn.close();
					
			return sum;
		} catch(Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Gets the integer item gECON ID of an item
	 * @param MCItemID the Minecraft ID of the item
	 * @return int gECONItemID
	 */
	public static int getGECONItemID(String MCItemID) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			s.execute("SELECT gECONItemID FROM Items WHERE MCItemID='"+MCItemID+"'");
			
			ResultSet rs = s.getResultSet();
			
			int ID = -1;
			
			while((rs!=null) && (rs.next())) {
				ID = rs.getInt(1);
			}
			
			s.close();
			conn.close();
			
			return ID;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Returns the Minecraft item ID using its String name
	 * @param itemName the String name for the item as it appears in Minecraft
	 * @return the Minecraft item ID
	 */
	public static int getMinecraftItemID(String itemName) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			s.execute("SELECT MCItemID FROM Items WHERE ItemName="+itemName);
			
			ResultSet rs = s.getResultSet();
			
			int MCItemID = -1;
			
			while((rs!=null) && (rs.next())) {
				MCItemID = rs.getInt(1);
			}
			
			s.close();
			conn.close();
			
			return MCItemID;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Returns the String name of an item using a Minecraft item ID as a parameter
	 * @param MCItemID the Minecraft item ID of the item
	 * @return the String name of the item as it appears in Minecraft
	 */
	public static String getItemNameFromMCID(String MCItemID) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
		
			s.execute("SELECT ItemName FROM Item WHERE MCItemID='"+MCItemID+"'");
			
			ResultSet rs = s.getResultSet();
			
			String name = "";
			
			while((rs!=null) && (rs.next())) {
				name = rs.getString(1);
			}
			
			s.close();
			conn.close();
			
			return name;
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Returns an ArrayList of BankItems connected to the passed player name
	 * @param playerName the String name of the player using the bank
	 * @return an ArrayList of BankItems connected to the passed player name
	 */
	public static ArrayList<BankItem> getBankItems(String playerName) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			int playerID = getPlayerID(playerName);
			
			s.execute("SELECT gECONItemID FROM BankAccounts WHERE PlayerID="+playerID);
			
			ResultSet rs = s.getResultSet();
			
			ArrayList<BankItem> items = new ArrayList<BankItem>();
			
			while((rs!=null) && (rs.next())) {
				String out = rs.getString(1);
				try {
					BankItem created = new BankItem(new ItemStack(Integer.parseInt(out), 1, 0));
					items.add(created);
				} catch (Exception E) {
					int col = out.indexOf(":");
					int meta = Integer.parseInt(out.substring(col + 1));
					int id = Integer.parseInt(out.substring(0, col));
					BankItem created = new BankItem(new ItemStack(id, 1, meta));
					items.add(created);
				}
			}
			
			return items;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns an ArrayList of BankItems including every item in the game that applies to the economy (unit size > 0)
	 * @return an ArrayList of BankItems including every item in the game
	 */
	public static ArrayList<BankItem> getAllItems() {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			s.execute("SELECT MCItemID FROM Items WHERE UnitSize > 0");
			
			ResultSet rs = s.getResultSet();
			
			ArrayList<BankItem> items = new ArrayList<BankItem>();
			
			while((rs!=null) && (rs.next())) {
				String out = rs.getString(1);
				try{
					BankItem created = new BankItem(new ItemStack(Integer.parseInt(out), 1, 0));
					items.add(created);
//					System.out.println(created.items.get(0).getDisplayName());

				}catch (Exception E){
					int col = out.indexOf(":");
					int meta = Integer.parseInt(out.substring(col + 1));
					int id = Integer.parseInt(out.substring(0, col));
					BankItem created = new BankItem(new ItemStack(id, 1, meta));
					items.add(created);
//					System.out.println(created.items.get(0).getDisplayName());
				}
			}
			
			s.close();
			conn.close();
			
			return items;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the String name of a specific item
	 * @param mcItemID mcItemID the minecraft ID of the item
	 * @return the String name of a specific item
	 */
	public static String getItemNameFromGECONID(int gECONItemID) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			s.execute("SELECT ItemName FROM Items WHERE gECONItemID="+gECONItemID);
			
			ResultSet rs = s.getResultSet();
			
			String name = "";
			
			while((rs!=null) && (rs.next())) {
				name = rs.getString(1);
			}
			
			s.close();
			conn.close();
			
			return name;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the quantity of an item stored in the bank assosiated with a player
	 * @param MCItemID the Minecraft ID of the item
	 * @param itemName the name of the item as it appears in Minecraft
	 * @return
	 */
	public static int getNumItemsInBank(String playerName, String MCItemID) {
		try {
			String connURL = connectioinURL;
 			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			int playerID = getPlayerID(playerName);
			
			s.execute("SELECT Quantity FROM BankAccounts WHERE (MCItemID='"+MCItemID+"' AND playerID="+playerID+")");
	        
			ResultSet rs = s.getResultSet();
			
			int sum = 0;
			
			while((rs!=null) && (rs.next())) {
				sum += rs.getInt(1);
			}
					
			s.close();
			conn.close();
					
			return sum;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Returns the amount of coins assosiated with a player
	 * @param playerName the String player name
	 * @return the integer number of coins a player has
	 */
	public static int getCoins(String playerName) {
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
		
			int playerID = getPlayerID(playerName);
		
			s.execute("SELECT Coins FROM Players WHERE PlayerID="+playerID);
		
			ResultSet rs = s.getResultSet();
		
			int coins = 0;
		
			while((rs!=null) && (rs.next())) {
				coins = rs.getInt(1);
			}
			
			s.close();
			conn.close();
			
			return coins;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * Returns the item price of an item
	 * @param MCItemID the Minecraft ID of the item
	 * @return double the price of an item
	 */
	public static double getItemPrice(String MCItemID) {
		return 0;
	}
	
	/**
	 * Returns a two-dimensional array of Strings containing Dates and Prices of the last 10% of transactions
	 * @param itemName the name of the item as it appears in Minecraft
	 * @return a two-dimensional array of Strings containing Dates and Prices of the last 10% of transactions
	 */
	public static String[][] getLastTenPercentTransactions(String MCItemID) {
		ArrayList<String> arrDates = new ArrayList<String>();
		ArrayList<Double> arrPrices = new ArrayList<Double>();
		
		try {
			String connURL = connectioinURL;
			Connection conn = DriverManager.getConnection(connURL, "","");
			Statement s = conn.createStatement();
			
			int gECONItemID = MCItemIDToGECONItemID(MCItemID);
			
			s.execute("SELECT TOP 10 PERCENT TransactionDate, TransactionPrice FROM (SELECT Transactions.TransactionDate, Transactions.TransactionPrice, BuyOrders.gECONItemID"
				   	+ " FROM BuyOrders INNER JOIN Transactions ON BuyOrders.[BuyOrderID] = Transactions.[BuyOrderID]"
					+ " WHERE (((BuyOrders.gECONItemID)=" + gECONItemID + ")))");
        
			ResultSet rs = s.getResultSet();
			
			while((rs!=null) && (rs.next())) {
				String S = rs.getString(1);
				double D = rs.getDouble(2);
				
				arrDates.add(S);
				arrPrices.add(D);
			}
			
			String[][] datesAndPrices = new String [2][arrDates.size()];
			
			for (int i = 0; i < arrDates.size(); i++) {
				datesAndPrices[0][i] = arrDates.get(i);
			}
			
			for (int i = 0; i < arrDates.size(); i++) {
				datesAndPrices[1][i] = Double.toString(arrPrices.get(i));
			}

			return datesAndPrices;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Sets the new calculated price for an item based on recent market transactions
	 * @param itemName the name of the item as it appears in Minecraft
	 * @return boolean true if successful
	 */
	public static boolean setNewItemPrice(String MCItemID) {
		return false;
	}
	
	/**
	 * Returns the gECON suggestion of a specific item
	 * @param itemName the name of the item as it appears in Minecraft
	 * @return the gECON suggestion of a specific item
	 */
	public static String getGECONSuggestion(String MCItemID) {
		return "";
	}
}


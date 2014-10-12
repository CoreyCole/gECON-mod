package gecon.mod.alpha;

import gecon.mod.alpha.misc.DatabaseMethods;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class BankItem {
	/**
	 * ArrayList of stored items in a BankItem
	 */
	public ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	
	/**
	 * Name of the items stored
	 */
	public String name;
	
	/**
	 * Super short name
	 */
	public String sName;
	
	/**
	 * Current Suggested Price
	 */
	public double Sprice; 
	
	/**
	 * The size
	 */
	public int size = 0;
	
	/**
	 * ID of the items stored.
	 */
	public int ID;
	
	/**
	 * meta data
	 */
	public int meta = 0;
	
	/**
	 * Constructor
	 * @param par1Item ItemStack to be modeled after.
	 */
	public BankItem(ItemStack par1Item){
		size = par1Item.stackSize;
		items.add(par1Item);
		meta = par1Item.getItemDamage();
		ID = par1Item.itemID;
		setName(par1Item);
		
	}
	
	/**
	 * Updates the sellprice
	 */
	public void updateSPrice(){
		Sprice = DatabaseMethods.getCurrentSuggestedPrice("" + ID);
	}
	
	/** 
	 * Sets and shortens the name of the ItemStack
	 * @param par1Item Item to be named afterr
	 */
	public void setName(ItemStack par1Item){
		if(par1Item.getDisplayName().length() < 8){
			this.name = par1Item.getDisplayName();
		}else{
			this.name = par1Item.getDisplayName().substring(0, 8);
				
		}
		
		if(par1Item.getDisplayName().length() < 6){
			this.sName = par1Item.getDisplayName();
		}else{
			this.sName = par1Item.getDisplayName().substring(0, 6);
				
		}
	}
	
	/**
	 * Add an ItemStack to a BankItem
	 * @param par1Item ItemStack to be added.
	 */
	public void add(ItemStack par1Item){
		int i = par1Item.stackSize;
		size += i;
		ItemStack x = items.get(items.size() - 1);
		if(x.stackSize < 64){
			i = i - (64 - x.stackSize);
			x.stackSize = 64;
			items.add(new ItemStack(par1Item.stackSize, i, 0));
		}else{
			items.add(new ItemStack(par1Item.stackSize, i, 0));
		}
	}
	
	/**
	 * Sets the size
	 * @param i
	 */
	public void setSize(int i){
		size = i;
	}
	
	/**
	 * Decrement the size of the BankItem
	 * @param q Size to be decremented by.
	 */
	public void decr(int q){
			int i = items.size() - 1;
			
			while(i >= 0){
				ItemStack x = items.get(i);
				if(x.stackSize >= q){
					x.stackSize -= q;
					break;
				}else if(i > 1){
					q -= x.stackSize;
					items.remove(x);
					i--;
					x = items.get(i);
					x.stackSize -= q;
					break;
				}
			}
	}
}

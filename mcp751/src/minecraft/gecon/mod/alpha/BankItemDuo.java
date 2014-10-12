package gecon.mod.alpha;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;

public class BankItemDuo {
	/**
	 * A list of items
	 */
	public ArrayList<ItemStack> items = new ArrayList<ItemStack>();
	
	/**
	 * Shared name of the comparator object.
	 */
	public String name;
	
	/**
	 * Shared ID of the comparator object.
	 */
	public int ID;
	
	/**
	 * The physical item on the left.
	 */
	public BankItem leftItem = null;
	
	/**
	 * The physical item on the right.
	 */
	public BankItem rightItem = null;
	
	/**
	 * The quantity of the left item.
	 */
	public int leftQty = 0;
	
	/** 
	 * The Quantity of the right item.
	 */
	public int rightQty = 0;
	
	/**
	 * Constructor
	 * @param par1BankItem The left Bank item.
	 * @param par2BankItem The right Bank item
	 */
	public BankItemDuo(BankItem par1BankItem, BankItem par2BankItem){
		if(par1BankItem != null){
			leftQty = par1BankItem.size;
			leftItem = par1BankItem;
			name = par1BankItem.name;
			ID = par1BankItem.ID;
		}
		if(par2BankItem != null){
			rightQty = par2BankItem.size;
			rightItem = par2BankItem;
			name = par2BankItem.name;
			ID = par2BankItem.ID;
		}
	}
	
	/**
	 * Sets the name of the shared items
	 */
	public void setName(ItemStack par1Item){
		if(par1Item.getDisplayName().length() < 6){
			this.name = par1Item.getDisplayName();
		}else{
			this.name = par1Item.getDisplayName().substring(0, 6);
		}
	}
	
	/** 
	 * Sets the left item post construction
	 * @param item Item being set
	 */
	public void setLeftItem(BankItem item){
		leftItem = item;
		leftQty = item.size;
	}
	
	/**
	 * Sets the right item post construction
	 * @param item Item being set
	 */
	public void setRightItem(BankItem item){
		rightItem = item;
		rightQty = item.size;
	}
	
}

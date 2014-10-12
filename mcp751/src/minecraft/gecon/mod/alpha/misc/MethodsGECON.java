/**
 * Just a Javadoc to store all the methods which may be of general use for later.
 */
package gecon.mod.alpha.misc;
import gecon.mod.alpha.BankItem;
import gecon.mod.alpha.block.BlockBank;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MethodsGECON {
	private EntityPlayer player;
	private ArrayList<BankItem> bankStoredItems;
	
	/**
	 * Constructor
	 * @param player
	 */
	public MethodsGECON(EntityPlayer player){
		this.player = player;
	}
	
	/**
	 * Collates items from a bankList
	 */
	public void collateItems() {
		ArrayList<BankItem> list = new ArrayList<BankItem>();
		list.add(new BankItem(new ItemStack(1, 30, 0)));
		boolean turp = false;
		
		for(ItemStack x: BlockBank.bankList) {
			turp = false;
			
			for(BankItem y: list) {
				if(x.itemID == y.ID) {
					y.add(x);
					turp = true;
					break;
				}
			}
			
			if(!turp) {
				list.add(new BankItem(x));
			}
		}
		
		for(ItemStack x: player.inventory.mainInventory){
			turp = false;
			
			if(x != null) {
				for(BankItem y: list) {
					if(x != null && x.itemID == y.ID) {
						y.add(x);
						turp = true;
						break;
					}
				}
				
				if(!turp) {
					list.add(new BankItem(x));
				}
			}
		}
			
		for(BankItem x: list) {
			System.out.println(x.name + x.size);
		}
		
		bankStoredItems = list;
	}
}

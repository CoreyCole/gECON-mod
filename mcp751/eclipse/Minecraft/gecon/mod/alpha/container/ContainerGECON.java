package gecon.mod.alpha.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;

public class ContainerGECON extends Container {
	/**
	 * Constructor
	 * @param player Player accessing the container
	 * @param world World of the container
	 * @param x Container's x
	 * @param y Container's y
	 * @param z Container's z
	 */
	public ContainerGECON(EntityPlayer player, World world, int x, int y, int z) {

	}
	/**
	 * If the container is accessible by the player.
	 */
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
}

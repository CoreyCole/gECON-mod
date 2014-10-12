package gecon.mod.alpha.block;

import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.gui.GuiBank;
import gecon.mod.alpha.gui.GuiTreasury;
import gecon.mod.alpha.gui.GuiMarket;
import gecon.mod.alpha.gui.GuiMarketAnalysis;
import gecon.mod.alpha.misc.DatabaseMethods;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockTreasury extends BlockGECON {
	
	/**
	 * The player who right clicks the block
	 */
	public static EntityPlayer player;
	
	/**
	 * Constructor
	 * @param id the ID number of the trading table
	 * @param par2Material the material of which the block is made
	 */
	public BlockTreasury(int id, Material par2Material) {
		super(id, par2Material);
	}
	
	/**
	 * Displays the GUI to the player
	 * Client side only
	 * @param world the world in which the block is
	 * @param x the x coordinate of the block location
	 * @param y the y coordinate of the block location
	 * @param z the z coordinate of the block location
	 * @param entityPlayer the player viewing the GUI
	 * @param f TBD
	 * @param a TBD
	 * @param b TBD
	 * @param c TBD
	 */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int f, float a, float b, float c) {
    	this.player = player;
		if (player instanceof EntityPlayerMP) {
			ModLoader.serverOpenWindow((EntityPlayerMP) player, new ContainerGECON(player, world, x, y, z), 31, x, y, z);
		} else {
			ModLoader.openGUI((EntityPlayerSP) player, new GuiTreasury(player, world, x, y, z));
		}
//		DatabaseMethods.hasPlayerAccount(par5EntityPlayer.username);
//    	
//		int uy = 0;
//		for(int i = 0; i < 64; i++)
//    	if(par5EntityPlayer.inventory.consumeInventoryItem(266)){
//    		uy++;
//    	}
//    	DatabaseMethods.addCoins(par5EntityPlayer.username, (int)(DatabaseMethods.getDefaultPrice("266"))*uy);
		return true;
	}
}

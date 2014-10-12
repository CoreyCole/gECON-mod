package gecon.mod.alpha.block;

import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.gui.GuiMarketAnalysis;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMarketAnalysis extends BlockGECON {

	/**
	 * Constructor
	 * @param id the ID number of the market block
	 * @param par2Material the Material of which the block is made
	 */
	public BlockMarketAnalysis(int id, Material par2Material) {
		super(id, par2Material);
	}
	
	/**
	 * Displays the GUI to the player
	 * Client side only
	 * @param world the world in which the block is
	 * @param x the x coordinate of the block location
	 * @param y the y coordinate of the block location
	 * @param z the z coordinate of the block location
	 * @param player the player viewing the GUI
	 * @param f TBD
	 * @param a TBD
	 * @param b TBD
	 * @param c TBD
	 */
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int f, float a, float b, float c) {
		if (player instanceof EntityPlayerMP) {
			ModLoader.serverOpenWindow((EntityPlayerMP) player, new ContainerGECON(player, world, x, y, z), 31, x, y, z);
		} else {
			ModLoader.openGUI((EntityPlayerSP) player, new GuiMarketAnalysis(player, world, x, y, z));
		}
		
		return true;
	}
}

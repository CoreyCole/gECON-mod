package gecon.mod.alpha.block;

import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.gui.GuiMarket;
import gecon.mod.alpha.gui.GuiMarketAnalysis;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;

public class BlockMarket extends BlockGECON{

	/**
	 * Constructor
	 * @param id the ID number for the market block
	 * @param par2Material the Material of which the block is made
	 */
	public BlockMarket(int id, Material par2Material) {
		super(id, par2Material);
		// TODO Auto-generated constructor stub
	}
	
	@SideOnly(Side.CLIENT)
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int f, float a, float b, float c) {
		if (player instanceof EntityPlayerMP) {
			ModLoader.serverOpenWindow((EntityPlayerMP) player, new ContainerGECON(player, world, x, y, z), 31, x, y, z);
		} else {
			ModLoader.openGUI((EntityPlayerSP) player, new GuiMarket(player, world, x, y, z));
		}
		
		return true;
	}
}

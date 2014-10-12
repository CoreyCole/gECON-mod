package gecon.mod.alpha.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockTradingTable extends BlockGECON {
	
	/**
	 * Constructor
	 * @param id the ID number of the trading table
	 * @param par2Material the material of which the block is made
	 */
	public BlockTradingTable(int id, Material par2Material) {
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
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
    	  if (par1World.isRemote) {
              return true;
          } else {
              TileEntityFurnace tileentityfurnace = (TileEntityFurnace)par1World.getBlockTileEntity(par2, par3, par4);

              if (tileentityfurnace != null) {
                  par5EntityPlayer.displayGUIFurnace(tileentityfurnace);
              }

              return true;
          }   
    }
}

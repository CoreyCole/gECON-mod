package gecon.mod.alpha.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;

public class CreativeTabGECON extends CreativeTabs {
	
	/**
	 * Stores the integer index ID of the creative tab for the gECON mod
	 */
	private int tabIndexID;
	
	/**
	 * Stores the String value of the tab's name
	 */
	private String tabName;
	
	/**
	 * Constructs the creative tab
	 * @param par1 Used by the super class
	 * @param par2 the index ID
	 * @param par3Str Used by the super class
	 * @param par4Str the String tab name
	 */
	public CreativeTabGECON(int par1, int par2, String par3Str, String  par4Str) {
		super(par1, par3Str);
	
		this.tabName = par4Str;
		this.tabIndexID = par2;
	}
	
	/**
	 * @return the tab index
	 */
	@SideOnly(Side.CLIENT)
	public int getTabIconItemIndex() {
		return this.tabIndexID;
	}
	
	/**
	 * @return the String tab name
	 */
	public String getTranslatedTabLabel() {
		return this.tabName;
	}
}

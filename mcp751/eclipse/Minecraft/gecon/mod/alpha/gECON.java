package gecon.mod.alpha;

import gecon.mod.alpha.block.BlockBank;
import gecon.mod.alpha.block.BlockMarket;
import gecon.mod.alpha.block.BlockMarketAnalysis;
import gecon.mod.alpha.block.BlockTradingTable;
import gecon.mod.alpha.gui.GuiBank;
import gecon.mod.alpha.misc.CreativeTabGECON;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = gECON.modid, name = "gECON Alpha", version = "0.1a")
public class gECON extends BaseMod {
	
	/**
	 * the name of the mod
	 */
	 public static final String modid = "woodnet_gECON";
	 /**
	  * the tab added to the creative mode inventory
	  */
	 public static CreativeTabs tabGECON;

	 /**
	  * TESTING PURPOSES ONLY
	  */
	 public static ArrayList<ItemStack> globalBank;
	 
	 //Blocks
	 /**
	  * the Bank block construction
	  */
	 public static Block bankBlock = new BlockBank(501, Material.rock).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("bankBlock");
	 
	 /**
	  * the Trading Table block construction
	  */
	 public static Block tradingTableBlock = new BlockTradingTable(502, Material.rock).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("tradingTableBlock");
	 
	 /**
	  * the Market Block block construction
	  */
	 public static Block marketBlock = new BlockMarket(503, Material.rock).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("marketBlock");
	 
	 /**
	  * the bank Market Analysis block construction
	  */
	 public static Block marketAnalysisBlock = new BlockMarketAnalysis(504, Material.rock).setHardness(1.5F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("marketAnalysisBlock");

	 
	 //@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)

	 //Creative Tab
	 
	 /**
	  * The event of mod-loading is captured and all the objects are registered
	  * @param event the event that is recorded when the mod is loaded
	  */
	 @Init
	 public void load(FMLInitializationEvent event) {
		 //Setup the creative tab
		 tabGECON = new CreativeTabGECON(CreativeTabs.getNextID(), bankBlock.blockID, "tabgECON", "gECON Tab");
		 
		 // Add items and blocks to creative tab
		 bankBlock.setCreativeTab(gECON.tabGECON);
		 tradingTableBlock.setCreativeTab(gECON.tabGECON);
		 marketBlock.setCreativeTab(tabGECON);
		 marketAnalysisBlock.setCreativeTab(tabGECON);

		 //Register the GUI link here
		 ModLoader.registerContainerID(this, 30); //Bank Block ID
		 ModLoader.registerContainerID(this, 31);
		 	 
		 //Register the tile entities here
		 
		 //Adds it to the game registry.
		 GameRegistry.registerBlock(bankBlock, modid + bankBlock.getUnlocalizedName2());
		 GameRegistry.registerBlock(tradingTableBlock, modid + tradingTableBlock.getUnlocalizedName2());
		 GameRegistry.registerBlock(marketBlock, modid + marketBlock.getUnlocalizedName2());
		 GameRegistry.registerBlock(marketAnalysisBlock, modid + marketAnalysisBlock.getUnlocalizedName2());
		 
		 //Add a name to the object
		 LanguageRegistry.addName(bankBlock, "Bank");
		 LanguageRegistry.addName(tradingTableBlock, "Trading Table");
		 LanguageRegistry.addName(marketBlock, "Market");
		 LanguageRegistry.addName(marketAnalysisBlock, "Market Analysis");

		 //Add recipes
		 GameRegistry.addRecipe(new ItemStack(bankBlock), new Object[]{"GRG", "GCG", "GRG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('R'), Item.redstone, Character.valueOf('C'), Block.chest});
		 GameRegistry.addRecipe(new ItemStack(tradingTableBlock), new Object[]{"GRG", "GCG", "GRG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('R'), Item.redstone, Character.valueOf('C'), Block.brick});
		 GameRegistry.addRecipe(new ItemStack(marketBlock), new Object[]{"GRG", "GCG", "GRG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('R'), Item.redstone, Character.valueOf('C'), Block.blockDiamond});
		 GameRegistry.addRecipe(new ItemStack(marketAnalysisBlock), new Object[]{"GRG", "GCG", "GRG", Character.valueOf('G'), Item.ingotGold, Character.valueOf('R'), Item.redstone, Character.valueOf('C'), gECON.marketBlock});
		 
		 //Create the bank database
		 globalBank = new ArrayList<ItemStack>();
	 }
	 
	 /**
	  * Gets the GUI to be diplayed
	  * @param player the player accessing the GUI
	  * @param id the ID number of the GUI
	  * @param x the x-coordinate of the location
	  * @param y the y-coordinate of the location
	  * @param z the z-coordinate of the location
	  */
	 public GuiContainer getContainerGUI(EntityClientPlayerMP player, int id, int x, int y, int z) {
		 switch(id) {
		 	case 30:
		 		return new GuiBank(player, player.worldObj, x, y, z);
		 	default:
		 		return null;
		 }
	 }
	 


	/**
	 * @Return the version number of the mod
	 */
	@Override
	public String getVersion() {
		return "Pre-Alpha v0.1";
	}

	/**
	 * Required code to load the mode.
	 */
	@Override
	public void load() {
		// TODO Auto-generated method stub
	}
}

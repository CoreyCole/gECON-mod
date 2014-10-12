package gecon.mod.alpha.gui;

import gecon.mod.alpha.gECON;
import gecon.mod.alpha.block.BlockBank;
import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.misc.DatabaseMethods;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public class GuiTreasury extends GuiContainer{
    private int ingots = 0;
	private EntityPlayer player;
	private int coins = 0;
	private int cooldown = 5000;
	private int timer = 0;
	private boolean cooling = false;
	private int xSize = 100;
	private int ySize = 90;
	private int amt = 0;
	private double originalXfer = 200;
	private double currentXfer = 0;
	private GuiTextField amount;
	
	/**
	 * Constructor
	 * @param player
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 */
	public GuiTreasury(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerGECON(player, world, x, y, z));		
		this.player = player;
		DatabaseMethods.hasPlayerAccount(player.username);
		coins = DatabaseMethods.getCoins(player.username);

	}
	
	/**
	 * Initializes the GUI
	 */
	@Override
	public void initGui(){
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
		this.buttonList.add(new GuiButton(0, x - 23, y + 5, 46, 20, "Grind"));
		amount = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 + 8, scaledRes.getScaledHeight()/2 - 7, 38, 10);
		amount.setMaxStringLength(6);
		amount.setFocused(false);
	}
	
	/**
	 * Handles a mouse click
	 */
	public void mouseClicked(int i, int j, int k){
		super.mouseClicked(i, j, k);
		amount.mouseClicked(i, j, k);
	}
	
	/**
	 * Method to grab keys that are typed
	 */
	public void keyTyped(char c, int i) {
		if(c != 'e') {
			super.keyTyped(c, i);
		}
		
		amount.textboxKeyTyped(c, i);
		
		if(amount.getText().length() > 0) {
			try {
				amt = Integer.parseInt(amount.getText());
				if(amt > ingots) {
					amt = ingots;
					amount.setText("" + ingots);
				}
			} catch (Exception E) {
				amount.setText("0");
				amt = 0;
			}
		}
		
		if(amount.getText().length() == 0){
			amt = 0;
		}
	}
	
	/**
	 * Draws the screen
	 */
	public void drawScreen(int x, int y, float f){
		super.drawScreen(x, y, f);
		amount.drawTextBox();		
	}
	
	/**
	 * Updates the screen
	 */
	public void updateScreen(){
	
	}
	
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/grinder.png");
		
		this.drawTexturedModalRect((scaledRes.getScaledWidth() - this.xSize)/2, (scaledRes.getScaledHeight() - this.ySize)/2, 0, 0, this.xSize, this.ySize);		
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		if(cooling) {
			timer++;
		}
		
		if(timer==cooldown) {
			cooling = false;
			timer = 0;
		}
		
		if(!cooling) {
			currentXfer = DatabaseMethods.getCurrentSuggestedPrice("266");
			coins = DatabaseMethods.getCoins(player.username);
			ingots = DatabaseMethods.getNumItemsInBank(player.username, "266");
			
			cooling = true;
		}
		
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        
        if(currentXfer > originalXfer) {
        	this.fontRenderer.drawString("" + currentXfer, x - 20,  y - 23, 0xf20505);
        }
        
        if(currentXfer < originalXfer) {
        	this.fontRenderer.drawString("" + currentXfer, x - 20,  y - 23, 0x3df205);
        } else {
        	this.fontRenderer.drawString("" + currentXfer, x - 20,  y - 23, 0xFFF000);
        }

		this.fontRenderer.drawString("Treasury",  x - 24,  y - 38 , 0xFFF000);
		this.fontRenderer.drawString("Ingots: " + ingots, x - 45,  y - 5, 0x0);
		this.fontRenderer.drawString("Coins: " + coins, x - 45,  y + 28, 0x0);
	}
	
	/**
	 * Handles a clicked button
	 */
	public void actionPerformed(GuiButton button) {
		if(button.id == 0) {
			if(amt > 0) {
				DatabaseMethods.addCoins(player.username, (int)(amt*currentXfer));
				DatabaseMethods.addItemsIntoBankAccount(player.username, "266", -1*amt);
				coins = DatabaseMethods.getCoins(player.username);
				ingots = DatabaseMethods.getNumItemsInBank(player.username, "266");
				currentXfer = DatabaseMethods.getCurrentSuggestedPrice("266");
				amt = 0;
			}
		}
	}	
}

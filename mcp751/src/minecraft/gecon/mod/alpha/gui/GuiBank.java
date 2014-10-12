package gecon.mod.alpha.gui;

import gecon.mod.alpha.BankItem;
import gecon.mod.alpha.BankItemDuo;
import gecon.mod.alpha.gECON;
import gecon.mod.alpha.block.BlockBank;
import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.misc.DatabaseMethods;
import gecon.mod.alpha.misc.Searcher;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiBank extends GuiContainer {
	
	/**
	 * The inventory of the player
	 */
	public InventoryPlayer inventoryPlayer;
	
	/**
	 * The player
	 */
	public EntityPlayer entityPlayer;
	
	/**
	 * The reference of the container
	 */
	public TileEntity tileEntity;
	
	/**
	 * The textbox used for refining a search
	 */
    private GuiTextField searchField;
    
    
    private String searching = "";
    
    
    private GuiTextField quantityField;
    
    
    private int transQuantity = 1;
    
    /**
     * An arrayList of the 4 items to be displayed.
     */
    public ArrayList<BankItemDuo> currentlyDisplayedItems;
   
    /**
     * The stored bank items
     */
    public ArrayList<BankItem> bankStoredItems;
  
    /**
     * The player stored items
     */
    public ArrayList<BankItem> playerStoredItems;
    
    private ScaledResolution PermscaledRes;
	
    private int Permx;
    
    private int Permy;
   
    /**
     * an index for where to begin the item search
     */
	private int index = 0;
	
	/**
	 * Width of the bank items
	 */
	private int xSize = 195;
	
	/**
	 * Height of the bank items
	 */
	private int ySize = 136;
	
	/**
	 * The quantity which you are withdrawing and depositing at.
	 */
	private String coins;
	private int qty = 1;
	private int cooldown = 100;
	private int tickCount = 0;
	private boolean coolingDown = false;
	
	/**
	 * Constructor
	 * @param player The player accessing the GUI
	 * @param world The world the GUI is in
	 * @param x X Coordinate of the Player
	 * @param y Y Coordinate of the Player
	 * @param z Z Coordinate of the Player
	 */
	public GuiBank(EntityPlayer player, World world, int x, int y, int z) {
		super(new ContainerGECON(player, world, x, y, z));		
		this.entityPlayer = BlockBank.player;
		compile();
		DatabaseMethods.hasPlayerAccount(entityPlayer.username);
		coins = Integer.toString(DatabaseMethods.getCoins(entityPlayer.username));
	}
	
	/**
	 * Grab all the info and display it in the list.
	 */
	public void compile() {
		this.convertToBankList();
		this.compareList();
	}
	
	/**
	 * Initiate the GUI's buttons
	 */
	public void initGui() {
		int screenPosX = (this.width - this.xSize) / 2;
		int screenPosY = (this.height - this.ySize) / 2;
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
		PermscaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		Permx = PermscaledRes.getScaledWidth()/2;
		Permy = PermscaledRes.getScaledHeight()/2;
    	this.searchField = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 + 35, scaledRes.getScaledHeight()/2 - 60, 53, 10);
	    this.searchField.setMaxStringLength(6);
        this.searchField.setFocused(false);
        this.quantityField = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 - 60, scaledRes.getScaledHeight()/2 + 45, 33, 18);
	    this.quantityField.setMaxStringLength(4);
        this.quantityField.setFocused(false);
        
		this.buttonList.add(new GuiButton(2, x - 20, y + 44, 20, 20, "<"));
		this.buttonList.add(new GuiButton(1, x, y + 44, 20, 20, ">"));
		
		//Incrementation Buttons
		
		//Set A
		this.buttonList.add(new GuiButton(3, x - 88, y - 35, 10, 10, ">"));
		this.buttonList.add(new GuiButton(4, x - 88, y - 25, 10, 9, "<"));
		
		//Set B
		this.buttonList.add(new GuiButton(5, x - 88, y - 16, 10, 10, ">"));
		this.buttonList.add(new GuiButton(6, x - 88, y - 6, 10, 9, "<"));
		
		//Set C
		this.buttonList.add(new GuiButton(7, x - 88, y + 3, 10, 10, ">"));
		this.buttonList.add(new GuiButton(8, x - 88, y + 13, 10, 9, "<"));
		
		//Set D
		this.buttonList.add(new GuiButton(9, x - 88, y + 22, 10, 10, ">"));
		this.buttonList.add(new GuiButton(10, x - 88, y + 32, 10, 9, "<"));
		
		
		this.buttonList.add(new GuiButton(11, x - 88, y + 44, 20, 20, "Qty"));

		this.buttonList.add((new GuiButton(12, x + 22, y + 44, 30, 10, "Coins:")));
			this.collateItems();

	}
	
	/**
	 * Draw the background layer of the GUI
	 */
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/bank.png");

		this.drawTexturedModalRect((scaledRes.getScaledWidth() - this.xSize)/2, (scaledRes.getScaledHeight() - this.ySize)/2, 0, 0, this.xSize, this.ySize);
	}
	
	/**
	 * Draw everything on the screen
	 */
	public void drawScreen(int x, int y, float f) {
		super.drawScreen(x, y, f);
        this.searchField.drawTextBox();
        this.quantityField.drawTextBox();	
	}
	
	/**
	 * Updates the graphics on the screen
	 */
	@Override
	public void updateScreen() {
		if(searchField.getText().length() > 0) {
			searching = searchField.getText();
		}
		compile();
	}
	
	/**
	 * Handles a mouse click
	 */
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		searchField.mouseClicked(i, j, k);
		quantityField.mouseClicked(i, j, k);

	}
	
	/**
	 * Method to grab keys that are typed
	 */
	public void keyTyped(char c, int i) {
		if(c != 'e') {
			super.keyTyped(c, i);
		}
		searchField.textboxKeyTyped(c, i);
		quantityField.textboxKeyTyped(c, i);
		if(quantityField.getText().length() > 0)
		try {
			Integer.parseInt(quantityField.getText());
		} catch (Exception E) {
			quantityField.setText("1");
		}
		searching = searchField.getText();
		compile();
	}
	
	/**
	 * Draw the foremost layer of the GUI with text
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
	
        int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;

		this.fontRenderer.drawString("Name", x - 74,  y - 45, 0xFFFFFF);
		this.fontRenderer.drawString("Bank",  x - 4,  y - 45 , 0xFFFFFF);
		this.fontRenderer.drawString("Player", x + 40,  y - 45, 0xFFFFFF);

		int screenPosX = (this.width - this.xSize) / 2;
		int screenPosY = (this.height - this.ySize) / 2;
		
		if(coolingDown){
			this.fontRenderer.drawString(". . .", Permx + 40,Permy + 55, 0xf40909); //10, 9
		} else {
			this.fontRenderer.drawString("Ready!", Permx + 40,Permy + 55, 0x00ff29); //10, 9
		}
		
		this.fontRenderer.drawString(coins, Permx + 53,Permy + 46, 0x0); //10, 9

		this.fontRenderer.drawString(entityPlayer.getEntityName() +"'s Bank", x - 88,y - 59, 0xFFFFFF); //10, 9

		if(coolingDown) {
			tickCount++;
		}

		if(tickCount >= cooldown){
			coolingDown = false;
			tickCount = 0;
		}

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/bank.png");
		
		this.drawItems();
	}
	
	/**
	 * Captures the event of button being clicked.
	 */
	public void actionPerformed(GuiButton button) {
		if(button.id == 1) {
			if(currentlyDisplayedItems.size() > index + 4) {
				index += 4;
			}
		}
			
		if(button.id == 2) {
			if(index >= 4) {
				index -= 4;
			}
		}
		
		if(button.id == 11 && !coolingDown) {
			if(quantityField.getText().length() > 0) {
				transQuantity = Integer.parseInt(quantityField.getText());
			}
		}
		
		if(button.id == 12 && !coolingDown) {
			coins = Integer.toString(DatabaseMethods.getCoins(entityPlayer.username));
			coolingDown = true;
		}
		
		//Incrementation
		int i = button.id;
		if(button.id == 3 || button.id == 5 || button.id == 7 || button.id == 9) {
			int j = (i - 3)/2;
			try {
				if(j + index < currentlyDisplayedItems.size() && currentlyDisplayedItems.get(index + j).rightItem.size > 0 && !coolingDown){
					String itemID = Integer.toString(currentlyDisplayedItems.get(index + j).ID);
					int numInBank;
					
					if(transQuantity > (numInBank = DatabaseMethods.getNumItemsInBank(entityPlayer.username, itemID))) {
						transQuantity = numInBank;
					}

					for(int f = 0; f < transQuantity; f++){
						BlockBank.player.inventory.addItemStackToInventory(new ItemStack(currentlyDisplayedItems.get(index + j).ID, 1, 0));
					}
					
					DatabaseMethods.addItemsIntoBankAccount(entityPlayer.getEntityName(), itemID, -1*transQuantity);
				
					collateItems();
					coolingDown = true;
					compile();
				}
			} catch (Exception E) {
				System.out.println(E);
			}
		}
		
		if(button.id == 4 || button.id == 6 || button.id == 8 || button.id == 10) {
			int j = (i - 4)/2;
			if(j + index < currentlyDisplayedItems.size() && currentlyDisplayedItems.get(index + j).leftItem.size > 0 && !coolingDown) {
				if(transQuantity > currentlyDisplayedItems.get(index + j).leftItem.size) {
					transQuantity = currentlyDisplayedItems.get(index + j).leftItem.size;
				}
				
				for(int f = 0; f < transQuantity; f++) {
					BlockBank.player.inventory.consumeInventoryItem(currentlyDisplayedItems.get(index + j).ID);
				}
				
				DatabaseMethods.addItemsIntoBankAccount(entityPlayer.getEntityName(), Integer.toString(currentlyDisplayedItems.get(index + j).ID), transQuantity);
				
				collateItems();
				coolingDown = true;
				compile();
			}
		}
		

	}
	
	/**
	 * Draws the elements in the list.
	 */
	private void drawItems(){
		int itemWidth = 153;
		int itemHeight = 19;
		int locFull = 136;
		int locNull = 155;
		int up = 4;
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = (scaledRes.getScaledWidth() - 153)/2;
        int y = (scaledRes.getScaledHeight())/2;
		
        for (int i = index; i < index + up; i++) {
			try {
				if(this.currentlyDisplayedItems.get(i) != null) {
					this.drawTexturedModalRect(x, y - 34 + (i%4) * 19, 0, locFull, itemWidth, itemHeight);
				} else {
					this.drawTexturedModalRect(x, y - 34 + (i%4) * 19, 0, locNull, itemWidth, itemHeight);
				}
				
			} catch (IndexOutOfBoundsException e) {
				this.drawTexturedModalRect(x, y - 34 + (i%4) * 19, 0, locNull, itemWidth, itemHeight);
			}catch(NullPointerException e){
				System.out.println(e);
			}
		}
		
		for (int i = index; i < index + up; i++){
			try {
			if(currentlyDisplayedItems.get(i) != null) {
				this.fontRenderer.drawString(this.currentlyDisplayedItems.get(i).name, x + 4,  y - 30  + (i%4) * 19, 0xFFFFFF);
				this.fontRenderer.drawString(Integer.toString(this.currentlyDisplayedItems.get(i).rightQty), x + 74,  y - 30  + (i%4) * 19, 0xFFFFFF);
				this.fontRenderer.drawString(Integer.toString(this.currentlyDisplayedItems.get(i).leftQty), x + 118, y - 30 + (i%4) * 19, 0xFFFFFF);
			}
			
			} catch(IndexOutOfBoundsException e) {
				System.out.println(e);
			} catch (NullPointerException e) {
				this.fontRenderer.drawString("0", x + 118, y - 30 + (i%4) * 19, 0xFFFFFF);

			}
		} 
	}
	
	/**
	 * Populate the lists with items to be displayed.
	 */
	public void collateItems(){
//		ArrayList<BankItem> list = new ArrayList<BankItem>();
//		boolean turp = false;
//			for(ItemStack x: BlockBank.bankList){
//				turp = false;
//				for(BankItem y: list){
//					if(x.itemID == y.ID){
//						y.add(x);
//						turp = true;
//						break;
//					}
//				}
//				if(!turp){
//					list.add(new BankItem(x));
//				}
//			}
		bankStoredItems = DatabaseMethods.getBankItems(entityPlayer.getEntityName());
	}
	
	/**
	 * Converts so bank can process and display
	 */
	public void convertToBankList() {
		ArrayList<BankItem> list = new ArrayList<BankItem>();

		boolean turp = false;
		
		for(ItemStack x: entityPlayer.inventory.mainInventory) {
			turp = false;
			if(x != null) {
				for(BankItem y: list) {
					if(x != null && x.itemID == y.ID && y.meta == x.getItemDamage()) {
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
		playerStoredItems = list;
	}
	
	/**
	 * Create the data of the compared lists.
	 */
	public void compareList() {
		ArrayList<BankItemDuo> list = new ArrayList<BankItemDuo>();
		boolean turp = false;
		for(BankItem x: playerStoredItems) {
			list.add(new BankItemDuo(x, null));
		}
		try {
			for(BankItem x: bankStoredItems) {
				turp = false;
				for(BankItemDuo y: list) {
					if(y.leftItem != null && y.leftItem.ID == x.ID ){
						y.setRightItem(x);
						turp = true;
						break;
					}
				}
				if(!turp) {
					list.add(new BankItemDuo(null, x));
				}
			}
		} catch (Exception E) {
			System.out.println(E);
		}
		currentlyDisplayedItems = list;
		if(searching.length() > 0){
			currentlyDisplayedItems = Searcher.recomb2(currentlyDisplayedItems, searching);
		}
	}
}

package gecon.mod.alpha.gui;

import gecon.mod.alpha.BankItem;
import gecon.mod.alpha.DateAndPrice;
import gecon.mod.alpha.gECON;
import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.misc.DatabaseMethods;
import gecon.mod.alpha.misc.ItemConvertor;
import gecon.mod.alpha.misc.Searcher;
import gecon.mod.alpha.misc.Transaction;

import java.sql.Date;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GuiMarketAnalysis extends GuiContainer {
	
	/**
	 * Stores a reference to the player using the gui
	 */
	public EntityPlayer player;
	
    private GuiTextField searchField;
    private String searching = "";
    
    /**
     * The list of items stored in the bank
     */
    public ArrayList<BankItem> bankStoredItems;
    
    /**
     * The list of visible items
     */
    public ArrayList<BankItem> showingItems;
    
    private ArrayList<Transaction> transactions;
    private int index = 0;
	private int xSize = 256;
	private int ySize = 136;
	private int currentPage;
	private double pages;
	private int qty = 1;
	private BankItem currentItem;
	private int cooldown = 100;
    private double basePrice;
	private int tickCount = 0;
	private boolean coolingDown = false;
	private String suggestion = "";
	
	/**
	 * Constructs the gui window
	 * @param par1Player player using the gui
	 * @param par2World world in which the player is located
	 * @param x the x-coordinate of the location
	 * @param y the y-coordinate of the location
	 * @param z the z-coordinate of the location
	 */
	public GuiMarketAnalysis(EntityPlayer par1Player, World par2World, int x, int y, int z) {
		super(new ContainerGECON(par1Player, par2World, x, y, z));
		
		this.player = par1Player;
	}
	
	public void initGui() {
		bankStoredItems = DatabaseMethods.getAllItems();
		
		int screenPosX = (this.width - this.xSize) / 2;
		int screenPosY = (this.height - this.ySize) / 2;
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		
    	this.searchField = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 + 68, scaledRes.getScaledHeight()/2 - 60, 53, 10);
	    this.searchField.setMaxStringLength(6);
        this.searchField.setFocused(false);
        
		int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        
		this.buttonList.add(new GuiButton(8, x + 80, y + 49, 15, 15, "<"));
		this.buttonList.add(new GuiButton(9, x + 95, y + 49, 15, 15, ">"));

		int buttX = 60;
		int buttY = y - 35;
		this.buttonList.add(new GuiButton(0, x + buttX, buttY, 8, 19, "="));
		this.buttonList.add(new GuiButton(1, x + buttX, buttY + 19, 8, 19, "="));
		this.buttonList.add(new GuiButton(2, x + buttX, buttY + 38, 8, 19, "="));
		this.buttonList.add(new GuiButton(3, x + buttX, buttY + 57, 8, 19, "="));
	}
	
	/**
	 * Draws the background layer of the gui containing the .png with a ModalRectangle frame over it
	 */
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/marketAnalysis.png");
		this.drawTexturedModalRect((scaledRes.getScaledWidth() - this.xSize)/2, (scaledRes.getScaledHeight() - this.ySize)/2, 0, 0, this.xSize, this.ySize);
	}
	
	/**
	 * Draws the screen
	 */
	public void drawScreen(int x, int y, float f) {
		super.drawScreen(x, y, f);
        this.searchField.drawTextBox();
	}
	
	/**
	 * Updates the screen
	 */
	public void updateScreen() {
		if(searchField.getText().length() > 0) {
			searching = searchField.getText().toLowerCase();
		}
	}
	
	/**
	 * Handles a mouse click
	 */
	public void mouseClicked(int i, int j, int k) {
		super.mouseClicked(i, j, k);
		searchField.mouseClicked(i, j, k);
	}
	
	/**
	 * Method to grab keys that are typed
	 */
	public void keyTyped(char c, int i) {
		if(c != 'e') {
			super.keyTyped(c, i);
		}
		
		searchField.textboxKeyTyped(c, i);
		searching = searchField.getText();
		
		collateItems();
		index = 0;
	}
	
	public void actionPerformed(GuiButton button) {
		int i = button.id;
		
		if(button.id == 9) {
			if(showingItems.size() > index + 4) {
				index += 4;
			}
		}
			
		if(button.id == 8) {
			if(index >= 4) {
				index -= 4;
			}
		}
		
		if((button.id == 0 || button.id == 1 || button.id == 2 || button.id == 3) && !coolingDown) {
			if(i + index < showingItems.size()) {
				coolingDown = true;
				currentItem = showingItems.get(i + index);
				transactions = DatabaseMethods.getLastTwentyTransactions(Integer.toString(currentItem.ID));
				suggestion = DatabaseMethods.getEconomySuggestion("" + currentItem.ID);
				basePrice = DatabaseMethods.getDefaultPrice(Integer.toString(currentItem.ID));
			} else {
				currentItem = null;
			}
		}
	}
	
	private void drawGraph() {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        
        int numdiv = 5;
        
        int begDate = 0;
        int endDate = 10;
        
    	this.fontRenderer.drawString("-", x - 108,y + 43, 0x0);
    	this.fontRenderer.drawString(Float.toString(0), x - 122,y + 43, 0x0);
    	this.fontRenderer.drawString("l", x - 104,y + 46, 0x0);
		this.fontRenderer.drawString("l", x + 58,y + 46, 0x0);
		
		int scaledGraphWidth = 162;
		
    	try {
    		Date date = transactions.get(0).getDate();
    		this.fontRenderer.drawString(date.toString(), x - 105,y + 54, 0x0);

    	} catch (ArrayIndexOutOfBoundsException E) {
        	this.fontRenderer.drawString("N/A", x - 110,y + 54, 0x0);

    	} catch (IndexOutOfBoundsException E) {
        	this.fontRenderer.drawString("N/A", x - 110,y + 54, 0x0);

    	}
    	
		try {
    		Date date = transactions.get(transactions.size() - 1).getDate();
			this.fontRenderer.drawString(date.toString(), x - 145 + 162,y + 54, 0x0);

	    } catch (ArrayIndexOutOfBoundsException E) {
			this.fontRenderer.drawString("N/A", x - 110 + 162,y + 54, 0x0);
			
	    } catch (IndexOutOfBoundsException E) {
        	this.fontRenderer.drawString("N/A", x - 110 + 162,y + 54, 0x0);

    	}
		
        for(int i = 1; i <= numdiv; i++) {
        	this.fontRenderer.drawString("-", x - 108,y + 43 - (i)*(77/(numdiv)), 0x0);
    		this.fontRenderer.drawString(Integer.toString(i*40), x - 125,y + 43 - (i)*(77/(numdiv)), 0x0);
        }
        
        //Add points
        int l = 77;
        int locY = y + 5;
        int baseY = locY + 37;
//      double basePrice = 1;
        
    	int numDiv = transactions.size();
    	
    	//Draw Raw Line
        for(int i = 0; i < 32; i++){
        	this.fontRenderer.drawString("~", x - 104 + i*5,locY + 2, 0x0);
        }

        //Add Points
        int separator = scaledGraphWidth/(transactions.size() - 1);
        String coordChar = "x";
        
        for(int i = 0; i < transactions.size(); i++) {
       		double price = transactions.get(i).getPrice();
       		double scaledHeight = price/basePrice;
        		
       		if(i > 0) {
       			if(price < transactions.get(i - 1).getPrice()) {
       				this.fontRenderer.drawString(coordChar, x - 104 + i*separator,baseY - (int)(38*scaledHeight), 0xf40909);
       			} else if(price > transactions.get(i - 1).getPrice()) {
       				this.fontRenderer.drawString(coordChar, x - 104 + i*separator,baseY - (int)(38*scaledHeight), 0x00ff29);
       			} else {
       				this.fontRenderer.drawString(coordChar, x - 104 + i*separator,baseY - (int)(38*scaledHeight), 0x000000);
      			}
       		} else {
    			this.fontRenderer.drawString(coordChar, x - 104 + i*separator,baseY - (int)(38*scaledHeight), 0xf7f7f1);
        	}
        }
        
    	this.fontRenderer.drawString(Double.toString(basePrice), x - 104 + 75,locY, 0xda00ff);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        
		this.fontRenderer.drawString("Market Analysis ", x - 104,y - 59, 0xFFF000); 
		this.fontRenderer.drawString(suggestion, x - 10,y - 59, 0xda00ff); 
		
		if(coolingDown) {
			this.fontRenderer.drawString(". . .", x - 35,y + 55, 0xf40909); 
		} else {
			this.fontRenderer.drawString("Ready!", x - 35,y + 55, 0x00ff29);
		}
		
		if(currentItem != null) {
			this.fontRenderer.drawString("Item Name: " + currentItem.items.get(0).getDisplayName() , x - 100,y - 44, 0xFFFFFF); 
			drawGraph();
		} else {
			this.fontRenderer.drawString("Item Name: N/A", x - 100,y - 44, 0xFFFFFF); 
		}
		
		if(coolingDown) {
			tickCount++;
		}

		if(tickCount >= cooldown) {
			coolingDown = false;
			tickCount = 0;
		}
		
		this.collateItems();

		this.drawItems();
	}
	
	/**
	 * Draws the foreground layer containing all the components of the gui
	 */
	public void drawGuiContainerForegroundLayer() {
		
	}
	
	private void drawItems() {
		int itemWidth = 53;
		int itemHeight = 19;
		
		int locFull = 136;
		int locNull = 155;
		
		int up = 4;
		
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		
        int x = (scaledRes.getScaledWidth() - 53)/2;
        int y = (scaledRes.getScaledHeight())/2;

		this.fontRenderer.drawString("", x - 104,y + 46, 0xFFFFFF);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/marketAnalysis.png");

		for (int i = index; i < index + up; i++) {
			try {
				if(this.showingItems.get(i) != null) {
					this.drawTexturedModalRect(x + 95 , y - 35 + (i%4) * 19, 0, locFull, itemWidth, itemHeight);
				}
				
			} catch(IndexOutOfBoundsException e) {
				this.drawTexturedModalRect(x + 95 , y - 35 + (i%4) * 19, 0, locNull, itemWidth, itemHeight);

			} catch(NullPointerException e) {
				System.out.println(e);
				
			}
		}
		
		for (int i = index; i < index + up; i++) {
			try {
				if(showingItems.get(i) != null){
					this.fontRenderer.drawString(this.showingItems.get(i).name, x + 99,  y - 30 + (i%4) * 19, 0xFFFFFF);
				}
			} catch (IndexOutOfBoundsException e){
				System.out.println(e);
				
			} catch(NullPointerException e) {
				System.out.println(e);
			}
		} 
	}
	
	/**
	 * Collates the item from the search recombine
	 */
	public void collateItems() {
		if(searching.length() > 0) {
			showingItems = Searcher.recomb(bankStoredItems, searching);
		} else {
			showingItems = bankStoredItems;
		}
	}
}

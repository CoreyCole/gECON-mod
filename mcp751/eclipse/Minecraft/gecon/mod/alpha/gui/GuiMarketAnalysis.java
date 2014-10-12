package gecon.mod.alpha.gui;

import gecon.mod.alpha.BankItem;
import gecon.mod.alpha.gECON;
import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.misc.DatabaseMethods;
import gecon.mod.alpha.misc.ItemConvertor;
import gecon.mod.alpha.misc.Searcher;

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
	 * Constructs the gui window
	 * @param par1Player player using the gui
	 * @param par2World world in which the player is located
	 * @param x the x-coordinate of the location
	 * @param y the y-coordinate of the location
	 * @param z the z-coordinate of the location
	 */
    public ArrayList<BankItem> bankStoredItems;
    public ArrayList<BankItem> showingItems;
    private String[][] dap;
	private int index = 0;
	private int xSize = 256;
	private int ySize = 136;
	private int currentPage;
	private double pages;
	private int qty = 1;
	private BankItem currentItem;
	public GuiMarketAnalysis(EntityPlayer par1Player, World par2World, int x, int y, int z) {
		super(new ContainerGECON(par1Player, par2World, x, y, z));
		
		this.player = par1Player;
	}
	public void initGui(){
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

		int buttX = 58;
		int buttY = y - 35;
		this.buttonList.add(new GuiButton(0, x + buttX, buttY, 10, 19, "*"));
		this.buttonList.add(new GuiButton(1, x + buttX, buttY + 19, 10, 19, "*"));
		this.buttonList.add(new GuiButton(2, x + buttX, buttY + 38, 10, 19, "*"));
		this.buttonList.add(new GuiButton(3, x + buttX, buttY + 57, 10, 19, "*"));
	}
	/**
	 * Draws the background layer of the gui containing the .png with a ModalRectangle frame over it
	 */
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/marketAnalysis.png");
		this.drawTexturedModalRect((scaledRes.getScaledWidth() - this.xSize)/2, (scaledRes.getScaledHeight() - this.ySize)/2, 0, 0, this.xSize, this.ySize);
	}

	public void drawScreen(int x, int y, float f){
		super.drawScreen(x, y, f);
        this.searchField.drawTextBox();

		

		
	}
	public void updateScreen(){
		if(searchField.getText().length() > 0){
			searching = searchField.getText().toLowerCase();
		}
	}
	public void mouseClicked(int i, int j, int k){
		super.mouseClicked(i, j, k);
		searchField.mouseClicked(i, j, k);
	}
	
	/**
	 * Method to grab keys that are typed
	 */
	public void keyTyped(char c, int i){
		if(c != 'e')
			super.keyTyped(c, i);
		searchField.textboxKeyTyped(c, i);
		searching = searchField.getText();
		collateItems();
		index = 0;
	}
	
	
	public void actionPerformed(GuiButton button){
		int i = button.id;
		
		if(button.id == 9)
			if(showingItems.size() > index + 4)
				index += 4;
			
		if(button.id == 8)
			if(index >= 4)
				index -= 4;
		
		
		if(button.id == 0 || button.id == 1 || button.id == 2 || button.id == 3){
			if(i + index < showingItems.size() ){
				currentItem = showingItems.get(i + index);
				dap = DatabaseMethods.getLastTenPercentTransactions(Integer.toString(currentItem.ID));
			}
			else
				currentItem = null;
		}
	}
	
	private void drawGraph(){
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        int numdiv = 5;
        
        int begDate = 0;
        int endDate = 10;
    	this.fontRenderer.drawString("-", x - 108,y + 43, 0x0);
    	this.fontRenderer.drawString(Float.toString(0), x - 122,y + 43, 0x0);
    	this.fontRenderer.drawString("l", x - 104,y + 46, 0x0);
    	
    	try{
    		String date = dap[0][0];
    		this.fontRenderer.drawString(ItemConvertor.transactionToDate(date), x - 105,y + 54, 0x0);

    	}catch (ArrayIndexOutOfBoundsException E){
        	this.fontRenderer.drawString("N/A", x - 110,y + 54, 0x0);

    	}
    	
		try{
			System.out.println(dap[0][dap[0].length - 1]);
    		String date = dap[0][dap[0].length - 1];
			this.fontRenderer.drawString(ItemConvertor.transactionToDate(date), x - 145 + 162,y + 54, 0x0);

	    }catch (ArrayIndexOutOfBoundsException E){
				this.fontRenderer.drawString("N/A", x - 110 + 162,y + 54, 0x0);
	    }
		this.fontRenderer.drawString("l", x + 58,y + 46, 0x0);

        for(int i = 1; i <= numdiv; i++){
        	this.fontRenderer.drawString("-", x - 108,y + 43 - (i)*(77/(numdiv)), 0x0);
    		this.fontRenderer.drawString(Integer.toString(i*40), x - 125,y + 43 - (i)*(77/(numdiv)), 0x0);
        }
        
        //Add points
        int l = 77;
        int locY = y + 5;
        int baseY = locY + 38;
        for(int i = 0; i < 160; i += 5){
        	int u = 1;
        	String j = "";
        	if((i) < dap[1].length){
        		j = dap[1][i];
        	}else{
        		j = dap[1][dap[1].length - 1];
        	}
//        		double q = DatabaseMethods.getItemPrice(Integer.toString(currentItem.ID));.
        		double q = 2;
        		double r = Double.parseDouble(j);
        		System.out.println(r/q);
        		double percOfOld = r/q;
        		
        		int ScaledPos = (int)(38*(r/q));
        		
            	this.fontRenderer.drawString("~", x - 104 + i,locY, 0x0);
            	
            	this.fontRenderer.drawString("-", x - 104 + i,baseY - ScaledPos, 0x0);

        	
        }

	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
		this.fontRenderer.drawString("Market Analysis ", x - 104,y - 59, 0xFFF000); //10, 9

		if(currentItem != null){
			this.fontRenderer.drawString("Item Name: " + currentItem.items.get(0).getDisplayName() , x - 100,y - 44, 0xFFFFFF); //10, 9
			drawGraph();
		}else{
			this.fontRenderer.drawString("Item Name: N/A", x - 100,y - 44, 0xFFFFFF); //10, 9
		}
		
		this.collateItems();

		this.drawItems();

	}
	
	/**
	 * Draws the foreground layer containing all the components of the gui
	 */
	public void drawGuiContainerForegroundLayer() {
		
	}
	private void drawItems(){
		int itemWidth = 53;
		int itemHeight = 19;
		int locFull = 136;
		int locNull = 155;
		int up = 4;
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = (scaledRes.getScaledWidth() - 53)/2;
        int y = (scaledRes.getScaledHeight())/2;
//System.out.println(y);
        
        
//      Legacy Code
		this.fontRenderer.drawString("", x - 104,y + 46, 0xFFFFFF);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/marketAnalysis.png");

		for (int i = index; i < index + up; i++){
			try{
				if(this.showingItems.get(i) != null){
					this.drawTexturedModalRect(x + 95 , y - 35 + (i%4) * 19, 0, locFull, itemWidth, itemHeight);
				}
				
			}catch(IndexOutOfBoundsException e){
				this.drawTexturedModalRect(x + 95 , y - 35 + (i%4) * 19, 0, locNull, itemWidth, itemHeight);

			}catch(NullPointerException e){

			}
		}
		
		
		for (int i = index; i < index + up; i++){
			try{
			if(showingItems.get(i) != null){
				this.fontRenderer.drawString(this.showingItems.get(i).name, x + 99,  y - 30 + (i%4) * 19, 0xFFFFFF);


			}
			}catch(IndexOutOfBoundsException e){
			}catch(NullPointerException e){

			}
		} 
	}
	public void collateItems(){
		if(searching.length() > 0){
			showingItems = Searcher.recomb(bankStoredItems, searching);
		}else{
			showingItems = bankStoredItems;
		}
	}
}

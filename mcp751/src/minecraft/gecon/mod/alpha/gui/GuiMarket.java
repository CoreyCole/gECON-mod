package gecon.mod.alpha.gui;

import gecon.mod.alpha.BankItem;
import gecon.mod.alpha.MarketOrder;
import gecon.mod.alpha.gECON;
import gecon.mod.alpha.container.ContainerGECON;
import gecon.mod.alpha.misc.DatabaseMethods;
import gecon.mod.alpha.misc.Searcher;
import gecon.mod.alpha.misc.Transaction;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiMarket extends GuiContainer {
	
	/**
	 * Stores a reference to the player using the gui
	 */
	public EntityPlayer player;
	
    private GuiTextField searchField;
    private GuiTextField qtyFieldC;
    private GuiTextField priceFieldC;
    private GuiTextField qtyFieldF;
    
    private String searching = "";

    /**
     * The arraylist of all items
     */
    public ArrayList<BankItem> allItem;
    
    /**
     * The arraylist of items stored in the bank
     */
    public ArrayList<BankItem> bankStoredItems;
    
    /**
     * The arraylist of visible items
     */
    public ArrayList<BankItem> showingItems;
    
    /**
     * The arraylist of visible market orders
     */
    public ArrayList<MarketOrder> viewingOrders;
    
    private int index = 0;
	private int indexOrder = 0;
	
	private int xSize = 256;
	private int ySize = 207;
	
	private String lastMessage ="Standby";
	
	private boolean creating = false;
	
	private int qty = 0;
	private int Sqty = 0;
	private int haveCurrently = 0;
	private double Sprice = 0;
	
	private BankItem currentItem;
	private MarketOrder currentOrder;
	
	private GuiButton create;
	private GuiButton cancel;
	private GuiButton items;
	private GuiButton submit;
	
	public boolean canCancel = false;
	
	private int messageCooldown = 300;
	private int messageTimer = 0;
	private boolean messageCooling = false;
	
	private int cooldown = 50;
    private double suggestedPrice;
    
    String coins = "";
    
    private boolean viewBank = true;
    private boolean buying = true;
	private int tickCount = 0;
	private boolean coolingDown = false;
	
	/**
	 * Constructs the gui window
	 * @param par1Player player using the gui
	 * @param par2World world in which the player is located
	 * @param x the x-coordinate of the location
	 * @param y the y-coordinate of the location
	 * @param z the z-coordinate of the location
	 */
	public GuiMarket(EntityPlayer par1Player, World par2World, int x, int y, int z) {
		super(new ContainerGECON(par1Player, par2World, x, y, z));
		
		this.player = par1Player;
		DatabaseMethods.hasPlayerAccount(this.player.username);
		coins = Integer.toString(DatabaseMethods.getCoins(this.player.username));
		
	}
	
	public void initGui() {
		allItem = DatabaseMethods.getAllItems();
		bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
		
		int screenPosX = (this.width - this.xSize) / 2;
		int screenPosY = (this.height - this.ySize) / 2;
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
		
    	this.searchField = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 + 62, scaledRes.getScaledHeight()/2 - 96, 56, 10);
	    this.searchField.setMaxStringLength(6);
        this.searchField.setFocused(false);
             
    	this.qtyFieldF = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 - 62, scaledRes.getScaledHeight()/2 + 90, 56, 10);
	    this.qtyFieldF.setMaxStringLength(6);
        this.qtyFieldF.setFocused(false);
        
        this.qtyFieldC = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 - 80, scaledRes.getScaledHeight()/2 + 69, 46, 10);
	    this.qtyFieldC.setMaxStringLength(6);
        this.qtyFieldC.setFocused(false);
        
    	this.priceFieldC = new GuiTextField(this.fontRenderer, scaledRes.getScaledWidth()/2 + 10, scaledRes.getScaledHeight()/2 + 69, 80, 10);
	    this.priceFieldC.setMaxStringLength(12);
        this.priceFieldC.setFocused(false);
       
		int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        
		this.buttonList.add(new GuiButton(8, x + 78, y + 15, 15, 15, "<"));
		this.buttonList.add(new GuiButton(9, x + 91, y + 15, 15, 15, ">"));

		this.buttonList.add(new GuiButton(20, x - 80, y + 10, 15, 20, "<"));
		this.buttonList.add(new GuiButton(21, x - 67, y + 10, 15, 20, ">"));

		int buttX = 60;
		int buttY = y - 75;
		this.buttonList.add(new GuiButton(0, x + buttX, buttY, 8, 19, "="));
		this.buttonList.add(new GuiButton(1, x + buttX, buttY + 19, 8, 19, "="));
		this.buttonList.add(new GuiButton(2, x + buttX, buttY + 38, 8, 19, "="));
		this.buttonList.add(new GuiButton(3, x + buttX, buttY + 57, 8, 19, "="));
		
		this.buttonList.add(new GuiButton(4, x + buttX + 1, buttY + 76, 59, 14, "Refresh"));
		
		items = new GuiButton(5, x + buttX + 1, buttY - 10, 59, 10, "Bank");
		this.buttonList.add(items);
		
		buttY += 8;
		this.buttonList.add(new GuiButton(10, x + buttX - 180, buttY, 8, 19, ">"));
		this.buttonList.add(new GuiButton(11, x + buttX - 180, buttY + 19, 8, 19, ">"));
		this.buttonList.add(new GuiButton(12, x + buttX - 180, buttY + 38, 8, 19, ">"));
		this.buttonList.add(new GuiButton(13, x + buttX - 180, buttY + 57, 8, 19, ">"));
		
		buttX -= 71;
		this.buttonList.add(new GuiButton(30, x + buttX + 2, buttY - 19, 61, 10, "Toggle"));
				
		create = new GuiButton(50, x + buttX + 72, buttY + 105, 61, 10, "Fulfiling");
		this.buttonList.add(create);

		cancel = new GuiButton(90, x + buttX + 11, buttY + 155, 61, 10, "Cancel");
		this.buttonList.add(cancel);
		
		submit = new GuiButton(60, x + buttX + 72, buttY + 155, 61, 10, "Submit");
		this.buttonList.add(submit);
		
		this.buttonList.add(new GuiButton(40, x + buttX, buttY, 65, 19, "Quick Fulfil"));
		this.buttonList.add(new GuiButton(41, x + buttX, buttY + 19, 65, 19, "Quick Fulfil"));
		this.buttonList.add(new GuiButton(42, x + buttX, buttY + 38, 65, 19, "Quick Fulfil"));
		this.buttonList.add(new GuiButton(43, x + buttX, buttY + 57, 65, 19, "Quick Fulfil"));
	}
	
	/**
	 * Draws the background layer of the gui containing the .png with a ModalRectangle frame over it
	 */
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/market.png");
		this.drawTexturedModalRect((scaledRes.getScaledWidth() - this.xSize)/2, (scaledRes.getScaledHeight() - this.ySize)/2, 0, 0, this.xSize, this.ySize);
	}

	public void drawScreen(int x, int y, float f) {
		super.drawScreen(x, y, f);
        this.searchField.drawTextBox();
        if(creating) {
        	this.qtyFieldC.drawTextBox();
        	this.priceFieldC.drawTextBox();
        } else if(currentOrder != null) {
        	this.qtyFieldF.drawTextBox();
        }
	}
	
	public void updateScreen() {
		if(searchField.getText().length() > 0){
			searching = searchField.getText().toLowerCase();
		}
	}
	
	public void mouseClicked(int i, int j, int k){
		super.mouseClicked(i, j, k);
		
		searchField.mouseClicked(i, j, k);
		qtyFieldF.mouseClicked(i, j, k);
		priceFieldC.mouseClicked(i, j, k);
		qtyFieldC.mouseClicked(i, j, k);
	}
	
	/**
	 * Method to grab keys that are typed
	 */
	public void keyTyped(char c, int i){
		if(c != 'e') {
			super.keyTyped(c, i);
		}
		searchField.textboxKeyTyped(c, i);
		searching = searchField.getText();
		qtyFieldF.textboxKeyTyped(c, i);
		priceFieldC.textboxKeyTyped(c, i);
		qtyFieldC.textboxKeyTyped(c, i);

		if(qtyFieldF.getText().length() > 0) {
			try {
				qty = Integer.parseInt(qtyFieldF.getText());
				if(qty > currentOrder.quantity){
					qty = currentOrder.quantity;
					qtyFieldF.setText("" +qty);
					setMessage("Max transfer for this order is " + qty + "!");
				}
			} catch (Exception E) {
				setMessage("Can Only Enter Numbers!");
				qtyFieldF.setText("0");
				qty = 0;
			}
		}
		
		if(qtyFieldF.getText().length() == 0) {
			qty = 0;
		}
	
		if(qtyFieldC.getText().length() > 0) {
			try {
				Sqty = Integer.parseInt(qtyFieldC.getText());
				if(Sqty > haveCurrently && buying) {
					Sqty = haveCurrently;
					qtyFieldC.setText("" +Sqty);
					setMessage("You Only Have " + Sqty + " " + currentItem.name +"!");
				}
			} catch (Exception E) {
				setMessage("Can Only Enter Numbers!");
				qtyFieldC.setText("0");
				Sqty = 0;
			}
		}
		
		if(qtyFieldC.getText().length() == 0) {
			Sqty = 0;
		}
		
		if(priceFieldC.getText().length() > 0) {
			try {
				Sprice = Double.parseDouble(priceFieldC.getText());
			} catch (Exception E) {
				setMessage("Can Only Enter Numbers!");
				priceFieldC.setText("0");
				Sprice = 0;
			}
		}
		
		if(priceFieldC.getText().length() == 0) {
			Sprice = 0;
		}
		
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
		
		if(button.id == 20) {
			try {
				if(index >= 4) {
					indexOrder -= 4;
				}
			
			} catch (NullPointerException E) {
				setMessage("No more pages!");
			}
		}
		
		if(button.id == 21) {
			try {
				if(viewingOrders.size() > index + 4) {
					indexOrder += 4;
				}
			} catch (NullPointerException E) {
				setMessage("No more pages!");
			}
		}
		
		if((button.id == 0 || button.id == 1 || button.id == 2 || button.id == 3) && !coolingDown) {
			if(i + index < showingItems.size()) {
				coolingDown = true;
				
				currentItem = showingItems.get(i + index);
				currentItem.updateSPrice();
				
				haveCurrently = DatabaseMethods.getNumItemsInBank(player.username, currentItem.ID + "");
				suggestedPrice = 0; //Method to getSuggested Price
				
				if(buying) {
					viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentItem.ID);
				} else {
					viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentItem.ID);
				}
			} else {
				currentItem = null;
			}
		}
		
		if(button.id == 4 && !coolingDown) {
			coolingDown = true;
			if(viewBank) {
				bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
			} else {
				bankStoredItems = allItem;
			}
		}
		
		if(button.id == 50) {
			creating = !creating;
			
			if(creating) {
				create.displayString = "Creating";
			} else {
				create.displayString = "Fulfiling";
			}
			currentItem = null;
			canCancel = false;
		}
		
		if(button.id == 30 && !coolingDown) {
			coolingDown = true;
			buying = !buying;
			
			qtyFieldC.setText("0");
			Sqty = 0;
			
			if(buying && currentItem != null) {
				viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentItem.ID);
			} else if (currentItem != null) {
				viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentItem.ID);
			}
			currentOrder = null;
		}
		
		if(button.id == 5 && !coolingDown) {
			coolingDown = true;
			viewBank = !viewBank;
			if(viewBank) {
				bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
				items.displayString = "Bank";
			} else {
				bankStoredItems = allItem;
				items.displayString = "All";
			}	
		}
		
		if((button.id == 10 || button.id == 11 || button.id == 12 || button.id == 13) && !coolingDown) {
				i = button.id - 10;
				try {
					if(i + indexOrder < viewingOrders.size()) {
						currentOrder = viewingOrders.get(i);
						if(currentOrder.playerName.equals(player.username) && !creating){
							canCancel = true;
						} else {
							canCancel = false;
						}
					} else {
						currentOrder = null;
					}
				} catch (Exception E){
					System.out.println(E);
				}
		}
		
		if((button.id == 40 || button.id == 41 || button.id == 42 || button.id == 43) && !coolingDown) {
			i = button.id - 40;
			try {
				if(i + indexOrder < viewingOrders.size()) {
					currentOrder = viewingOrders.get(i);
					coins = "" + DatabaseMethods.getCoins(player.username);

					if(currentOrder != null) {	
						if(!currentOrder.buyOrder && currentOrder.quantity*currentOrder.price <= Integer.parseInt(coins)) {
							System.out.println("Done3");

							DatabaseMethods.addCoins(currentOrder.playerName, (int)(currentOrder.quantity*currentOrder.price));
							DatabaseMethods.addCoins(player.username, (int)(-1*currentOrder.quantity*currentOrder.price));
							DatabaseMethods.addItemsIntoBankAccount(player.username, Integer.toString(currentOrder.item.ID), qty);
						} else if(currentOrder.buyOrder && currentOrder.quantity <= DatabaseMethods.getNumItemsInBank(player.username, currentOrder.item.ID + "")) {
							System.out.println("Done4");

							DatabaseMethods.addCoins(player.username, (int)(currentOrder.quantity*currentOrder.price));
							DatabaseMethods.addItemsIntoBankAccount(player.username, Integer.toString(currentOrder.item.ID), -1*qty);
						}
						
						DatabaseMethods.setOrderFilled(currentOrder);
						DatabaseMethods.recordTransaction(new Transaction(DatabaseMethods.MCItemIDToGECONItemID(Integer.toString(currentOrder.item.ID)), currentOrder.price, null));	
					}
					
					if(buying) {
						viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentOrder.item.ID);
					} else {
						viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentOrder.item.ID);
					}
					
					if(viewBank) {
						bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
					} else {
						bankStoredItems = allItem;
					}
					
					currentOrder = null;
					
				} else {
					currentOrder = null;
				}
				
			} catch (Exception E) {
				lastMessage = "Emptry Entries!";
			}
		}

		if(button.id == 60) {
				if(!creating && qty > 0 && !coolingDown && buying) {
					coolingDown = true;
					
					if(Double.parseDouble((coins = "" + DatabaseMethods.getCoins(player.username))) >= qty*currentOrder.price) {
						if(qty == currentOrder.quantity) {
							DatabaseMethods.setOrderFilled(currentOrder);
						} else if(qty < currentOrder.quantity) {
							DatabaseMethods.setOrderPartiallyFilled(currentOrder, currentOrder.quantity - qty);
						}
						
						DatabaseMethods.addCoins(player.username, (int)(-1*qty*currentOrder.price));
						DatabaseMethods.addCoins(currentOrder.playerName, (int)(qty*currentOrder.price));

						DatabaseMethods.addItemsIntoBankAccount(player.username, Integer.toString(currentOrder.item.ID), qty);
						
						java.util.Date today = new java.util.Date();
						java.sql.Date sqlToday = new java.sql.Date(today.getTime());
						DatabaseMethods.recordTransaction(new Transaction(DatabaseMethods.MCItemIDToGECONItemID(Integer.toString(currentOrder.item.ID)), currentOrder.price, sqlToday));
						
						if(buying) {
							viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentOrder.item.ID);
						} else {
							viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentOrder.item.ID);	
						}
						
						currentOrder = null;
						
						if(viewBank) {
							bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
						} else {
							bankStoredItems = allItem;
						}
						
					} else {
						setMessage("Insufficient Funds");
					}
					
				} else if(!creating && qty > 0 && !coolingDown && !buying) {
					coolingDown = true;
					
					if(DatabaseMethods.getNumItemsInBank(player.username, currentOrder.item.ID + "") >= qty) {
						if(qty == currentOrder.quantity){
							DatabaseMethods.setOrderFilled(currentOrder);
						}else if(qty < currentOrder.quantity){
							DatabaseMethods.setOrderPartiallyFilled(currentOrder, currentOrder.quantity - qty);
						}
						
						DatabaseMethods.addCoins(player.username, (int)(qty*currentOrder.price));
						DatabaseMethods.addItemsIntoBankAccount(player.username, Integer.toString(currentOrder.item.ID), -1*qty);
						DatabaseMethods.addItemsIntoBankAccount(currentOrder.playerName, Integer.toString(currentOrder.item.ID), qty);

						java.util.Date today = new java.util.Date();
						java.sql.Date sqlToday = new java.sql.Date(today.getTime());
						DatabaseMethods.recordTransaction(new Transaction(DatabaseMethods.MCItemIDToGECONItemID(Integer.toString(currentOrder.item.ID)), currentOrder.price, sqlToday));
							
						if(buying) {
							viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentOrder.item.ID);
						} else {
							viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentOrder.item.ID);	
						}
						
						if(viewBank) {
							bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
						} else {
							bankStoredItems = allItem;
						}
						
						currentOrder = null;
					} else {
						setMessage("Insufficient " + currentOrder.item.items.get(0).getDisplayName());
					}
					
				} else if(creating && Sqty > 0 && Sprice > 0 && !coolingDown) {
					coolingDown = true;
					
					try {
						if(buying) {
							DatabaseMethods.createSellOrder(player.username, ""+ currentItem.ID, Sqty, Sprice);
							viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentItem.ID);
							bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
						} else {						
							if(Sqty*Sprice <= Integer.parseInt(coins)) {
								DatabaseMethods.createBuyOrder(player.username, ""+ currentItem.ID, Sqty, Sprice);
								viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentItem.ID);	
							} else {
								setMessage("Insufficient Funds");
							}
						}
						
					} catch (NullPointerException E) {
						lastMessage = "No Item Selected!";
					}
				}
				
				Sqty = 0;
				Sprice = 0;
				qty = 0;
				
				qtyFieldC.setText("");		
				priceFieldC.setText("");
				qtyFieldF.setText("");
				
				coins = "" + DatabaseMethods.getCoins(player.username);
		}
		
		if(button.id == 90 && !coolingDown) {
			if(currentOrder != null) {
				coolingDown = true;
				
				if(currentOrder.buyOrder) {
					DatabaseMethods.addCoins(player.username, (int)(currentOrder.quantity*currentOrder.price));
				} else {
					DatabaseMethods.addItemsIntoBankAccount(player.username, currentOrder.item.ID + "", currentOrder.quantity);
				}
				
				DatabaseMethods.setOrderFilled(currentOrder);
				
				if(buying) {
					viewingOrders = DatabaseMethods.getTotalSellOrders("" + currentOrder.item.ID);
				} else {
					viewingOrders = DatabaseMethods.getTotalBuyOrders("" + currentOrder.item.ID);	
				}
				
				currentOrder = null;
				
				if(viewBank) {
					bankStoredItems = DatabaseMethods.getBankItems(this.player.username);
				} else {
					bankStoredItems = allItem;
				}
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		cancel.enabled = canCancel;
		
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = scaledRes.getScaledWidth()/2;
        int y = scaledRes.getScaledHeight()/2;
        
        String message = "";
        
        if(viewBank) {
        	message = "Bank";
        } else {
        	message = "All";
        }
        
		this.fontRenderer.drawString("Market; Viewing: " + message, x - 116,y - 94, 0xFFF000); 
		
		this.fontRenderer.drawString("Price", x - 108,y - 78, 0xFFF000); 
		this.fontRenderer.drawString("|", x - 60,y - 78, 0xFFF000); 
		this.fontRenderer.drawString("Qty", x - 55,y - 78, 0xFFF000); 

		if(buying) {
			this.fontRenderer.drawString("Sell Orders", x -  8, y - 94, 0xAA60B2); 
		} else {
			this.fontRenderer.drawString("Buy Orders", x - 8,y - 94, 0x60B2B2); 
		}


		this.fontRenderer.drawString("Coins: " + coins, x - 43,y + 20, 0x0); 

		
		if(coolingDown) {
			this.fontRenderer.drawString(". . .", x - 120,y + 20, 0xf40909); 
		} else {
			this.fontRenderer.drawString("Ready!", x - 120,y + 20, 0x00ff29);
		}
		
		if(currentItem != null) {
			this.fontRenderer.drawString("Selected: " + currentItem.items.get(0).getDisplayName() , x - 122,y + 40, 0x0); 
		} else {
			this.fontRenderer.drawString("Selected: N/A", x - 122,y + 40, 0x0); 
		}
		
		if(currentOrder != null && !creating) {
			if(buying) {
				this.fontRenderer.drawString("Selected Order: " + currentOrder.item.name + " Price: " + currentOrder.price + " Qty: " + currentOrder.quantity, x - 122,y + 50, 0xAA60B2); 
				this.fontRenderer.drawString("Date Opened: " + currentOrder.date+ " User: " + currentOrder.playerName, x - 122,y + 60, 0xAA60B2); 
				Calendar cal1 = Calendar.getInstance(); cal1.setTime(currentOrder.date);
				Date d = new Date(System.currentTimeMillis());
				Calendar cal2 = Calendar.getInstance(); cal2.setTime(d);
				this.fontRenderer.drawString(daysBetween(cal1, cal2) + " Day(s) ago", x - 122,y + 70, 0xAA60B2); 
				this.fontRenderer.drawString("Buying " + qty + " of " + currentOrder.item.name + " for " + currentOrder.price*qty , x - 98,y + 80, 0xAA60B2); 
			} else {
				this.fontRenderer.drawString("Selected Order: " + currentOrder.item.name + " Price: " + currentOrder.price + " Qty: " + currentOrder.quantity, x - 122,y + 50, 0x60B2B2); 
				this.fontRenderer.drawString("Date Opened: " + currentOrder.date+ " User: " + currentOrder.playerName, x - 122,y + 60, 0x60B2B2); 
				Calendar cal1 = Calendar.getInstance(); cal1.setTime(currentOrder.date);
				Date d = new Date(System.currentTimeMillis());
				Calendar cal2 = Calendar.getInstance(); cal2.setTime(d);
				this.fontRenderer.drawString(daysBetween(cal1, cal2) + " Day(s) ago", x - 122,y + 70, 0x60B2B2); 
			}
			
		} else if(creating && currentItem != null && !buying) {
			this.fontRenderer.drawString("Suggested Price: " + currentItem.Sprice, x - 122,y + 50, 0x60B2B2); 
			this.fontRenderer.drawString("Buying: "+ Sqty + " " + currentItem.name + " for " + Sprice, x - 122,y + 60, 0x60B2B2); 
			this.fontRenderer.drawString("Quantity", x - 122,y + 70, 0x60B2B2); 
			this.fontRenderer.drawString("Price", x - 18,y + 70, 0x60B2B2); 
			this.fontRenderer.drawString("Total: " + Sprice*Sqty, x- 122,y + 90, 0x60B2B2); 

		} else if(creating && currentItem != null && buying) {
			this.fontRenderer.drawString("Suggested Price: " + currentItem.Sprice, x - 122,y + 50, 0xAA60B2); 
			this.fontRenderer.drawString("Selling: "+ Sqty + " " + currentItem.name + " for " + Sprice, x - 122,y + 60, 0xAA60B2); 
			this.fontRenderer.drawString("Quantity", x - 122,y + 70, 0xAA60B2); 
			this.fontRenderer.drawString("Price", x - 18,y + 70, 0xAA60B2); 
			this.fontRenderer.drawString("Total: " + Sprice*Sqty, x- 122,y + 90, 0xAA60B2); 
			
		} else {
			this.fontRenderer.drawString("Selected Order: N/A", x - 122,y + 50, 0x0); 
		}
		
		this.fontRenderer.drawString("Console: " + lastMessage, x- 122,y + 105, 0xFF0000); 

		if(coolingDown) {
			tickCount++;
		}

		if(tickCount >= cooldown){
			coolingDown = false;
			tickCount = 0;
		}
		
		if(messageCooling) {
			tickCount++;
		}

		if(messageTimer >= messageCooldown){
			messageCooling = false;
			messageTimer = 0;
		}
		
		this.collateItems();

		this.drawItems();
		this.drawOrders();
	}
	
	/**
	 * Draws the foreground layer containing all the components of the gui
	 */
	public void drawGuiContainerForegroundLayer() {
		
	}
	
	/**
	 * Sets the message to the console
	 * @param mesage
	 * @return
	 */
	public boolean setMessage(String mesage){
		lastMessage = mesage;
		messageCooling = true;
		return true;
	}
	
	/**
	 * Calculates the days between two Calendar dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
	    }
	    return daysBetween;
	}
	
	private void drawItems(){
		int itemWidth = 53;
		int itemHeight = 19;
		
		int locFull = 207;
		int locNull = 226;
		
		int up = 4;
		
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = (scaledRes.getScaledWidth() - 53)/2;
        int y = (scaledRes.getScaledHeight())/2;    
        
		this.fontRenderer.drawString("", x - 104,y + 46, 0xFFFFFF);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/market.png");

		//Draws the boxes for bank
		for (int i = index; i < index + up; i++) {
			try {
				if(this.showingItems.get(i) != null) {
					this.drawTexturedModalRect(x + 95 , y - 75 + (i%4) * 19, 0, locFull, itemWidth, itemHeight);
				}
				
			} catch(IndexOutOfBoundsException e) {
				this.drawTexturedModalRect(x + 95 , y - 75 + (i%4) * 19, 0, locNull, itemWidth, itemHeight);

			} catch(NullPointerException e) {
				System.out.println(e);
			}
		}
		
		for (int i = index; i < index + up; i++) {
			try {
				if(showingItems.get(i) != null){
				
					if(viewBank){
						this.fontRenderer.drawString(this.showingItems.get(i).sName, x + 99,  y - 70 + (i%4) * 19, 0xFFFFFF);
						this.fontRenderer.drawString("" + this.showingItems.get(i).size, x + 134,  y - 70 + (i%4) * 19, 0x0056ff);
					} else {
						this.fontRenderer.drawString(this.showingItems.get(i).name, x + 99,  y - 70 + (i%4) * 19, 0xFFFFFF);
					}
				}
				
			} catch(IndexOutOfBoundsException e) {
				System.out.println(e);
			} catch(NullPointerException e) {
				System.out.println(e);
			}
		} 
	}
	
	private void drawOrders(){
		int itemWidth = 101;
		int itemHeight = 19;
		int locBox = 0;
		
		if(buying) {
			locBox = 53;
		} else {
			locBox = 154;
		}
		
		int locFull = 207;
		int locNull = 226;
		
		int up = 4;
		
		ScaledResolution scaledRes = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int x = (scaledRes.getScaledWidth() - 53)/2;
        int y = (scaledRes.getScaledHeight())/2;
           
		this.fontRenderer.drawString("", x - 104,y + 46, 0xFFFFFF);

		this.mc.renderEngine.bindTexture("/mods/" + gECON.modid + "/gui/bank/market.png");

		//Draws the boxes for orders
		for (int i = indexOrder; i < indexOrder + up; i++) {
			try{
				if(this.viewingOrders.get(i) != null) {
						this.drawTexturedModalRect(x - 85 ,y - 67 + (i%4) * 19, locBox, locFull, itemWidth, itemHeight);
				}
				
			} catch(IndexOutOfBoundsException e) {
				this.drawTexturedModalRect(x - 85, y - 67 + (i%4) * 19, 53, locNull, itemWidth, itemHeight);

			} catch(NullPointerException e) {
				this.drawTexturedModalRect(x - 85, y - 67 + (i%4) * 19, 53, locNull, itemWidth, itemHeight);

			}
		}
		
		
		for (int i = indexOrder; i < indexOrder + up; i++){
			try {
				if(viewingOrders.get(i) != null) {
					this.fontRenderer.drawString(this.viewingOrders.get(i).price + "", x - 81,  y - 62 + (i%4) * 19, 0xFFFFFF);
					this.fontRenderer.drawString("|", x - 33, y - 62 + (i%4) * 19, 0xFFFFFF); 
					this.fontRenderer.drawString(this.viewingOrders.get(i).quantity + "", x - 28, y - 62 + (i%4) * 19, 0xFFFFFF); 
				}
			} catch(IndexOutOfBoundsException e) {
				System.out.println(e);
			} catch(NullPointerException e) {
				System.out.println(e);
			}
		} 
	}
	
	/**
	 * Collates the items from a search recombine
	 */
	public void collateItems(){
		if(searching.length() > 0) {
			showingItems = Searcher.recomb(bankStoredItems, searching);
		} else {
			showingItems = bankStoredItems;
		}
	}
}

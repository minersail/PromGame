package woohoo.framework;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import java.util.Iterator;
import woohoo.framework.IDManager.ItemData;
import woohoo.framework.contactcommands.ContactData;
import woohoo.framework.input.InventoryCloseCommand;
import woohoo.gameobjects.components.ContactComponent;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.InventoryComponent;
import woohoo.gameobjects.components.ItemDataComponent;
import woohoo.gameobjects.components.ItemDataComponent.ItemType;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.WeaponComponent;
import woohoo.gameworld.InputSystem;
import woohoo.gameworld.Mappers;
import woohoo.gameworld.RenderSystem;
import woohoo.gameworld.WeaponSystem;
import woohoo.gameworld.gamestates.InventoryState;
import woohoo.inventory.InventorySlot;
import woohoo.inventory.InventorySlot.SlotType;
import woohoo.inventory.InventorySource;
import woohoo.inventory.InventoryTarget;
import woohoo.inventory.SlotListener;
import woohoo.inventory.WeaponSource;
import woohoo.inventory.WeaponTarget;
import woohoo.inventory.inventoryactions.InventoryAction;
import woohoo.screens.PlayingScreen;

/**
 * Note: the UI uses the bottom-left coordinate system
 * @author jordan
 */
public class InventoryManager
{	
    public static final int INVENTORY_WIDTH = 5;
    public static final int INVENTORY_HEIGHT = 8;
    public static final int ITEMX = 64;
    public static final int ITEMY = 64;
	public static final int ITEMBORDERX = 8;
	public static final int ITEMBORDERY = 8;
	
	public final int PADDING_LEFT = 100;
	public final int PADDING_RIGHT = 100;
//	public final int INVENTORY_BOTTOM = (Gdx.graphics.getHeight() - ((INVENTORY_HEIGHT + 1) * ITEMY)) / 2;
//	public final int INVENTORY_TOP = INVENTORY_BOTTOM + (INVENTORY_HEIGHT * ITEMY);
//	public final int INVENTORY_LEFT = PADDING_LEFT;
//	public final int INVENTORY_RIGHT = INVENTORY_LEFT + (INVENTORY_WIDTH * ITEMX);
	
	public final int DRAG_OFFSET_X = 32;
	public final int DRAG_OFFSET_Y = -32;
	
	private final TextureRegion slotBackground;
	private final TextureRegion blankItem;
    
	private InventoryComponent otherInventory; // Inventory of the other character; null if only player inventory is open
    private PlayingScreen screen;
	private Table table;
	private Table table2;
	private TextButton closeButton;
	private Label moneyLabel;
	private InventorySlot weaponSlot;
	
	private Array<SlotListener> listeners;

	/**
	 * Initializes the UI with blank items, to be later filled with fillInventory()
	 * @param scr Reference to the game screen
	 * @param atlas Atlas containing backgrounds and borders for the inventory
	 * @param skin Really only necessary to get the font. (Can change later)
	 */
    public InventoryManager(PlayingScreen scr, TextureAtlas atlas, Skin skin) 
    {
        screen = scr;
        table = new Table();
        table2 = new Table();
		listeners = new Array<>();
		
        slotBackground = atlas.findRegion("itemframe");
		blankItem = atlas.findRegion("blank");
                
        closeButton = new TextButton("x", skin);
        weaponSlot = new InventorySlot(slotBackground, blankItem, skin, screen.getUI());
		weaponSlot.setType(SlotType.Weapon);
        table.add(closeButton).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);   
        table.add(weaponSlot).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);
		table.addActor(weaponSlot.getToolTip());
        table.row();
        
        closeButton.addListener(new ClickListener()
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {
                    screen.getEngine().getSystem(InputSystem.class).addCommand(new InventoryCloseCommand(screen));
                }
            }        
        );		
        
        DragAndDrop dnd = new DragAndDrop();
		
		// Initialize weapon slot
		WeaponSource weaponSource = new WeaponSource(weaponSlot);
		WeaponTarget weaponTarget = new WeaponTarget(weaponSlot);
		
		dnd.addSource(weaponSource);
        dnd.addTarget(weaponTarget);
		
		listeners.add(weaponSource);
		listeners.add(weaponTarget);
        
		// Drag settings
        dnd.setDragActorPosition(DRAG_OFFSET_X, DRAG_OFFSET_Y);
		dnd.setDragTime(0);
		
		// Initialize rest of slots
        for (int i = 0; i < INVENTORY_HEIGHT; i++) 
        {
            for (int j = 0; j < INVENTORY_WIDTH; j++)
            {
				// Player inventory
                InventorySlot slot = new InventorySlot(slotBackground, blankItem, skin, screen.getUI());
				slot.setType(SlotType.Player);
                table.add(slot).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);
				table.addActor(slot.getToolTip()); // addActor() instead of add() so that the tooltips float rather than mess up cell formatting

				// Source/targets for first slot
				InventorySource source1 = new InventorySource(slot);
				InventoryTarget target1 = new InventoryTarget(slot);
				
                dnd.addSource(source1);
                dnd.addTarget(target1);
				
				listeners.add(source1);
				listeners.add(target1);
				
				// Other inventory
                InventorySlot slot2 = new InventorySlot(slotBackground, blankItem, skin, screen.getUI());
				slot2.setType(SlotType.Other);
                table2.add(slot2).prefSize(ITEMX + ITEMBORDERX, ITEMY + ITEMBORDERY);
				table2.addActor(slot2.getToolTip());

				// Source/targets for second slot
				InventorySource source2 = new InventorySource(slot2);
				InventoryTarget target2 = new InventoryTarget(slot2);
				
                dnd.addSource(source2);
                dnd.addTarget(target2);
				
				listeners.add(source2);
				listeners.add(target2);
            }
            table.row();
			table2.row();
        }
		
		moneyLabel = new Label("", skin);
		table.add(moneyLabel).colspan(INVENTORY_WIDTH).prefSize((ITEMX + ITEMBORDERX) * INVENTORY_WIDTH, ITEMY);

        table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.align(Align.left);
		table.padLeft(PADDING_LEFT);
		
		table2.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table2.align(Align.right);
		table2.padRight(PADDING_RIGHT);
    }
	
	public void act(float delta)
	{
		for (SlotListener listener : listeners)
		{
			for (Iterator<InventoryAction> iter = listener.getActions().iterator(); iter.hasNext();)
			{
				InventoryAction action = iter.next();
				
				action.run(this);
				iter.remove();
			}
		}
	}

    public void showInventory() 
    {
        screen.getUI().addActor(table);
		screen.setState(new InventoryState());
    } 
	
	public void showInventory(InventoryComponent other) 
    {
		showInventory();
		
		fillInventory(other);
		screen.getUI().addActor(table2);
		
		otherInventory = other;
    }
	
	public void closeInventory()
	{
		FileHandle handle = Gdx.files.local("data/inventories.xml");

		XmlReader reader = new XmlReader();
		Element root = reader.parse(handle.readString());
		Element playerInv = root.getChild(0);
		
		// Clear xml player inventory
		for (Element item : playerInv.getChildrenByName("item"))
		{
			item.remove();
		}
		
		// Write player inventory to xml
		for (int i = 0; i < table.getCells().size; i++)
		{
			if (!(table.getCells().get(i).getActor() instanceof InventorySlot)) continue;
			
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			if (slot.getItem() != null)
			{
				Element element = new Element("item", playerInv);
				Element metadata = new Element("metadata", element);
				
				element.setAttribute("id", Integer.toString(Mappers.items.get(slot.getItem()).id));
				
				// Convert the entity's inventorycomponent's metadata objectmap to xml
				ObjectMap.Entries<String, String> entries = Mappers.items.get(slot.getItem()).metaData.entries();
				while (entries.hasNext())
				{
					ObjectMap.Entry<String, String> entry = entries.next();
					metadata.setAttribute(entry.key, entry.value);
				}
				element.addChild(metadata);
				playerInv.addChild(element);
			}
		}
		
		if (otherInventory != null) // if closing from screen with two inventories
		{
			Element objectInv = root.getChild(otherInventory.id);
			
			// Clear xml other's inventory
			for (Element item : objectInv.getChildrenByName("item"))
			{
				item.remove();
			}
			
			for (int i = 0; i < table2.getCells().size; i++)
			{
				InventorySlot slot = (InventorySlot)table2.getCells().get(i).getActor();
				if (slot.getItem() != null)
				{
					Element element = new Element("item", objectInv);
					Element metadata = new Element("metadata", element);
					
					element.setAttribute("id", Integer.toString(Mappers.items.get(slot.getItem()).id));

					// Convert the entity's inventorycomponent's metadata objectmap to xml
					ObjectMap.Entries<String, String> entries = Mappers.items.get(slot.getItem()).metaData.entries();
					while (entries.hasNext())
					{
						ObjectMap.Entry<String, String> entry = entries.next();
						metadata.setAttribute(entry.key, entry.value);
					}
					element.addChild(metadata);
					objectInv.addChild(element);
				}
			}
		}
		handle.writeString(root.toString(), false);
		
		table.remove();
		table2.remove();
		
		otherInventory = null; // ensure inventory functions aren't called when the ui isn't open
		
		for (int i = 0; i < table2.getCells().size; i++) // Empty inventory ui
		{
			InventorySlot slot = (InventorySlot)table2.getCells().get(i).getActor();
			slot.empty();
		}
	}
	
	/**
	 * Whether or not the inventory as it is was opened from a dialogue (e.g. shop, chest)
	 * @return false if opened by using escape (just player inventory)
	 */
	public boolean openedFromDialogue()
	{
		return otherInventory != null;
	}
	
	public void updateMoney(int newMoney)
	{
		moneyLabel.setText(Integer.toString(newMoney));
	}
	
	/**
	 * Loads an inventoryComponent from XML
	 * @param comp component to load
	 */
	public void loadFromXML(InventoryComponent comp)
	{
		FileHandle handle = Gdx.files.local("data/inventories.xml");
        
        XmlReader xml = new XmlReader();
        XmlReader.Element root = xml.parse(handle.readString());
		XmlReader.Element inventoryEl = root.getChild(comp.id);
        
        for (XmlReader.Element e : inventoryEl.getChildrenByName("item"))
        {	
            Entity item = new Entity();
			
			ItemData data = screen.getIDManager().getItem(e.getInt("id"));
			
			ItemDataComponent itemData = new ItemDataComponent(e.getInt("id"), e.getChildByName("metadata").getAttributes(), data.toObjectMap());
			item.add(itemData);
			
            comp.addItem(item);
        }
	}
	
	public void fillPlayerInventory(InventoryComponent inventory)
	{
		Iterator<Entity> iterator = inventory.getItems().iterator();
		
		for (int i = 0; i < table.getCells().size; i++)
		{		
			// If table item is not invetory slot (x button, gold display) or is a Weapon slot
			if (!(table.getCells().get(i).getActor() instanceof InventorySlot) || 
				((InventorySlot)table.getCells().get(i).getActor()).getType() == SlotType.Weapon) continue;
			
			if (!iterator.hasNext()) return; // Finished loading player inventory
			Entity item = iterator.next();

			// Use the id from the itemdatacomponent to retrive a texture from the id manager
			TextureRegion region = (TextureRegion)Mappers.items.get(item).baseData.get("texture");
			Image image = new Image(region);
			image.setSize(ITEMX, ITEMY);

			((InventorySlot)table.getCells().get(i).getActor()).setImage(image).setItem(item).setCount(1);
		}
	}
	
	/**
	 * Function to fill the inventory UI with a character's inventory
	 * @param inventory Character to fill inventory UI with
	 */
	public void fillInventory(InventoryComponent inventory)
	{		
		// Starts at 0 (right side of table)
		for (int i = 0; i < table2.getCells().size; i++)
		{		
			if (i >= inventory.getItems().size()) break;

			Entity item = inventory.getItems().get(i);

			// Use the id from the itemdatacomponent to retrive a texture from the id manager
			TextureRegion region = (TextureRegion)Mappers.items.get(item).baseData.get("texture");
			Image image = new Image(region);
			image.setSize(ITEMX, ITEMY);

			((InventorySlot)table2.getCells().get(i).getActor()).setImage(image).setItem(item).setCount(1);
		}
	}
	
	/**
	 * Function to add a single item into the inventory of the character
	 * 
	 * Can only be called after fillInventory() has been called at least once
	 * @param item The item to be added
	 */
	public void addItem(Entity item)
	{
		Mappers.inventories.get(screen.getEngine().getPlayer()).addItem(item);
		
		for (int i = 0; i < table.getCells().size; i++)
		{		
			if (!(table.getCells().get(i).getActor() instanceof InventorySlot)) continue;
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			if (slot.getType() == SlotType.Weapon) continue;
			
			if (slot.getCount() == 0)
			{
				Mappers.mapObjects.get(item).getTextureRegion().flip(false, true); // Because the world is in y-down and the UI is in y-up
				Image image = new Image(Mappers.mapObjects.get(item).getTextureRegion());
				slot.setImage(image).setItem(item).setCount(1);
				
				// Remove unnecessary components while item is transferred to inventory
				screen.getEngine().getSystem(RenderSystem.class).getRenderer().getMap().getLayers().get("Items").getObjects().remove(Mappers.mapObjects.get(item));
				item.remove(MapObjectComponent.class);
				screen.getWorld().destroyBody(Mappers.hitboxes.get(item).mass);
				item.remove(HitboxComponent.class);
				item.remove(PositionComponent.class);
				return;
			}
		}
		
		// If the code reaches here the inventory is full
	}
	
	/**
	 * Removes an item from both the UI and the current character's inventory
	 * (as set by either addItem() or fillInventory())
	 * and drops it into the game world
	 * @param item item to be dropped
	 */
	public void dropItem(Entity item)
	{				
		for (int i = 0; i < table.getCells().size; i++)
		{
			if (!(table.getCells().get(i).getActor() instanceof InventorySlot)) continue;
			InventorySlot slot = (InventorySlot)table.getCells().get(i).getActor();
			
			if (slot.getItem() != null && slot.getItem().equals(item))
			{			
				// Create empty slot
				slot.empty();

				playerInventory().removeItem(item);

				// Create position/mapObject components to go along with the item
				PositionComponent position = new PositionComponent(Mappers.positions.get(screen.getEngine().getPlayer()).position.cpy());
				MapObjectComponent mapObject = new MapObjectComponent(screen.getIDManager().getItem(Integer.parseInt((String)Mappers.items.get(item).metaData.get("id"))).getItemTexture());

				HitboxComponent hitbox = new HitboxComponent(screen.getWorld(), false, ContactType.Item);
				hitbox.mass.setTransform(position.position.cpy().add(0.5f, 0.5f), 0);
				item.add(hitbox);
				item.add(position);
				item.add(mapObject);

				screen.getEngine().addEntity(item);
				return;
			}
		}
	}
	
	public InventorySlot getWeaponSlot()
	{
		return weaponSlot;
	}
	
	public void equipWeapon(Entity item)
	{		
		ItemDataComponent itemData = Mappers.items.get(item);
		
		WeaponComponent weapon = new WeaponComponent(screen.getWorld());
		weapon.damage = Float.parseFloat((String)itemData.metaData.get("damage", "0.25f"));
		weapon.knockback = Float.parseFloat((String)itemData.metaData.get("knockback", "1"));

		weapon.mass.setUserData(new ContactData(ContactComponent.ContactType.Weapon, screen.getEngine().getPlayer()));
		screen.getEngine().getPlayer().add(weapon);
		screen.getEngine().getSystem(WeaponSystem.class).equip(screen.getEngine().getPlayer(), weapon);
	}
	
	public void unequipWeapon()
	{
		screen.getEngine().getSystem(WeaponSystem.class).unequip(screen.getEngine().getPlayer());
	}
	
	public void sellItem(Entity item)
	{
		playerInventory().removeItem(item);
		otherInventory.addItem(item);
		Mappers.players.get(screen.getEngine().getPlayer()).money += Integer.parseInt((String)Mappers.items.get(item).metaData.get("sell"));
	}
	
	public void buyItem(Entity item)
	{
		otherInventory.removeItem(item);
		playerInventory().addItem(item);
		Mappers.players.get(screen.getEngine().getPlayer()).money -= Integer.parseInt((String)Mappers.items.get(item).metaData.get("buy"));
	}
	
	// Shortcut for accessing the player's inventory component
	private InventoryComponent playerInventory()
	{
		return Mappers.inventories.get(screen.getEngine().getPlayer());
	}
}

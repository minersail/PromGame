package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import java.util.ArrayList;

public class InventoryComponent implements Component
{
	private ArrayList<Entity> items;
	public int id; // Inventory id to link to an id in inventories.xml
	
	public InventoryComponent(int inventoryID)
	{
		items = new ArrayList<>();
		id = inventoryID;
	}
	
	public void addItem(Entity item)
	{
		items.add(item);
	}
	
	public void removeItem(Entity item)
	{
		items.remove(item);
	}
	
	public ArrayList<Entity> getItems()
	{
		return items;
	}
}

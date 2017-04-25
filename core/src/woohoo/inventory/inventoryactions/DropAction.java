package woohoo.inventory.inventoryactions;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;

public class DropAction implements InventoryAction
{
	Entity dropped; // Entity to be dropped
	
	public DropAction(Entity drop)
	{
		dropped = drop;
	}
	
	@Override
	public void run(InventoryManager im)
	{
		im.dropItem(dropped);
	}
}

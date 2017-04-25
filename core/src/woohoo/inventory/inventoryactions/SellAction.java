package woohoo.inventory.inventoryactions;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;

public class SellAction implements InventoryAction
{
	private Entity sold;
	
	public SellAction(Entity sell)
	{
		sold = sell;
	}
	
	@Override
	public void run(InventoryManager im)
	{
		im.sellItem(sold);
	}
}

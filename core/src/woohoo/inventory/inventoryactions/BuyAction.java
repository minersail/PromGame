package woohoo.inventory.inventoryactions;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;

public class BuyAction implements InventoryAction
{
	private Entity bought;
	
	public BuyAction(Entity buy)
	{
		bought = buy;
	}
	
	@Override
	public void run(InventoryManager im)
	{
		im.buyItem(bought);
	}
}

package woohoo.inventory.inventoryactions;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Color;
import woohoo.framework.InventoryManager;

public class DeselectAction implements InventoryAction
{
	private Entity deselected;
	
	public DeselectAction(Entity deselect)
	{
		deselected = deselect;
	}
	
	@Override
	public void run(InventoryManager im)
	{
		im.getWeaponSlot().setColor(Color.WHITE);
	}
}

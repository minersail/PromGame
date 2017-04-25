package woohoo.inventory;

import com.badlogic.gdx.utils.Array;
import woohoo.inventory.inventoryactions.InventoryAction;

/**
 * All source/targets will be a listener
 * InventoryManager will keep a master list of SlotListener(s)
 * @author jordan
 */
public interface SlotListener
{
	public Array<InventoryAction> getActions();
}

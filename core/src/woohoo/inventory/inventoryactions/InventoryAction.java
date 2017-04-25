package woohoo.inventory.inventoryactions;

import woohoo.framework.InventoryManager;

/**
 * Allows the InventoryTarget/InventorySlot to affect the InventoryManager 
 * without directly exposing the InventoryManager
 * @author jordan
 */
public interface InventoryAction
{
	public void run(InventoryManager im);
}
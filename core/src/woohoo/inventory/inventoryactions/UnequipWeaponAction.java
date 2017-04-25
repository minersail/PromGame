package woohoo.inventory.inventoryactions;

import woohoo.framework.InventoryManager;

public class UnequipWeaponAction implements InventoryAction
{
	@Override
	public void run(InventoryManager im)
	{
		im.unequipWeapon();
	}
}

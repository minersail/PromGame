package woohoo.inventory.inventoryactions;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;

public class EquipWeaponAction implements InventoryAction
{
	private Entity equipped;
	
	public EquipWeaponAction(Entity equip)
	{
		equipped = equip;
	}
	
	@Override
	public void run(InventoryManager im)
	{
		im.equipWeapon(equipped);
	}
}

package woohoo.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import woohoo.inventory.inventoryactions.UnequipWeaponAction;

public class WeaponSource extends InventorySource
{
	public WeaponSource(InventorySlot slot)
	{
		super(slot);
	}
	
	@Override
	public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target)
	{
		if (target == null) // Payload was not dropped on a target (e.g. outside the inventory)
		{
			actions.add(new UnequipWeaponAction());
		}
		
		super.dragStop(event, x, y, pointer, payload, target);
	}
}

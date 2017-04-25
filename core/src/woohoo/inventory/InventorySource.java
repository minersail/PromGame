package woohoo.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import woohoo.framework.InventoryManager;
import woohoo.inventory.InventorySlot.SlotType;
import woohoo.inventory.inventoryactions.DeselectAction;
import woohoo.inventory.inventoryactions.DropAction;
import woohoo.inventory.inventoryactions.InventoryAction;
import woohoo.inventory.inventoryactions.SelectAction;

/**
 *
 */
public class InventorySource extends Source implements SlotListener
{
	protected Array<InventoryAction> actions;
	
	public InventorySource(InventorySlot slot)
	{
		super(slot);
		actions = new Array<>();
	}

	/*
	Called when a drag is started
	*/
	@Override
	public Payload dragStart(InputEvent event, float x, float y, int pointer)
	{
		InventorySlot slot = (InventorySlot) getActor(); // Get source
		// Can't drag empty frame
		if (slot.getCount() == 0)
		{
			return null;
		}
		
		actions.add(new SelectAction(slot));

		slot.setDragged(true); // Let the item be freely dragged

		Payload payload = new Payload();
		payload.setDragActor(slot.getImage());

		return payload;
	}

	/*
		Called when a drag is stopped
	 */
	@Override
	public void dragStop(InputEvent event, float x, float y, int pointer, Payload payload, Target target)
	{
		((InventorySlot) getActor()).setDragged(false);

		if (target == null) // Payload was not dropped on a target (e.g. outside the inventory)
		{
			// Can't drop items out of a shopkeeper or chest
			if (((InventorySlot) getActor()).getType() == SlotType.Other)
			{
				return;
			}

			// Remove the item from the inventory and add it to the world
			Entity dropped = ((InventorySlot) getActor()).getItem();

			actions.add(new DropAction(dropped));
			actions.add(new DeselectAction(dropped));
		}
	}

	@Override
	public Array<InventoryAction> getActions()
	{
		return actions;
	}
}

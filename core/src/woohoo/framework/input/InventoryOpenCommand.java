package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.InventoryManager;

/**
 * Used to be more complicated
 * @author jordan
 */
public class InventoryOpenCommand implements InputCommand
{
	private InventoryManager manager;
	
	public InventoryOpenCommand(InventoryManager im)
	{
		manager = im;
	}
	
	@Override
	public void execute(Entity player)
	{
		manager.showInventory();
	}
}

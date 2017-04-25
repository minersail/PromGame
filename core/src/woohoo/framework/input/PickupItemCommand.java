package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import java.util.Iterator;
import woohoo.framework.InventoryManager;
import woohoo.gameworld.GameEngine;
import woohoo.gameworld.Mappers;

public class PickupItemCommand implements InputCommand
{
	InventoryManager manager;
	GameEngine engine;
	
	public PickupItemCommand(InventoryManager im, GameEngine ge)
	{
		manager = im;
		engine = ge;
	}
	
	@Override
	public void execute(Entity entity)
	{
		if (Mappers.players.get(entity) == null) return;
		
		for (Iterator<Entity> it = Mappers.players.get(entity).touchedItems.iterator(); it.hasNext();)
		{
			Entity item = it.next();
			
			manager.addItem(item);
			engine.removeEntity(item);
			
			it.remove();
		}
	}
}

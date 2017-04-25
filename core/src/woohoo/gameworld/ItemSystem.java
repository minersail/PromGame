package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.ItemDataComponent;
import woohoo.gameobjects.components.PositionComponent;

public class ItemSystem extends IteratingSystem
{
	public ItemSystem()
	{
		super(Family.all(PositionComponent.class, HitboxComponent.class, ItemDataComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		ItemDataComponent item = Mappers.items.get(entity);
	}
}

package woohoo.framework.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.PositionComponent;

public class MoveEvent implements Event<Entity>
{
	PositionComponent component;
	Vector2 position;
	
	/**
	 * Switches character's AIMode to MoveTo and moves to given location
	 * @param comp PositionComponent of the entity to move
	 * @param pos Position entity will move to
	 */
	public MoveEvent(PositionComponent comp, Vector2 pos)
	{
		component = comp;
		position = pos;
	}
	
	@Override
	public void activate()
	{
		component.position = position;
	}
}

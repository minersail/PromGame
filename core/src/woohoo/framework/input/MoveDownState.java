package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameworld.Mappers;

public class MoveDownState implements InputState
{
	@Override
	public void execute(Entity entity)
	{
		if (!Mappers.movements.has(entity)) return;

		Mappers.movements.get(entity).direction = MovementComponent.Direction.Down;
	}
}

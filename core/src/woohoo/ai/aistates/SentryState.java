package woohoo.ai.aistates;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class SentryState implements AIState
{
	public Array<Vector2> positions;
	private int index;
	
	public SentryState(Array<Vector2> sentryLocations)
	{
		positions = sentryLocations;
		index = 0;
	}
	
	@Override
	public Direction getDirection(AIComponent ai, PositionComponent pos)
	{
		Vector2 target = positions.get(index);
		
		// Reached patrol location
		if (pos.position.dst(target) < 0.5f)
			index++;
		
		if (index >= positions.size)
			index = 0;
		
		return ai.getDirectionFromPath(pos.position, target);
	}
}

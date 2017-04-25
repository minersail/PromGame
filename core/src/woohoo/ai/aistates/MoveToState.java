package woohoo.ai.aistates;

import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class MoveToState implements AIState
{
	public Vector2 target;
	
	public MoveToState(Vector2 targetPosition)
	{
		target = targetPosition;
	}
	
	@Override
	public Direction getDirection(AIComponent ai, PositionComponent pos)
	{
		if (pos.position.dst(target) < 0.5f)
			ai.setState(new StayState());
		
		return ai.getDirectionFromPath(pos.position, target);
	}
}

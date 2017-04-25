package woohoo.ai.aistates;

import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class StayState implements AIState
{
	public StayState()
	{
	}
	
	@Override
	public Direction getDirection(AIComponent ai, PositionComponent pos)
	{
		return Direction.None;
	}
}

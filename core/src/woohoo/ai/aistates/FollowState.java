package woohoo.ai.aistates;

import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class FollowState implements AIState
{
	public PositionComponent target;
	
	public FollowState(PositionComponent targetEntityPosition)
	{
		target = targetEntityPosition;
	}
	
	@Override
	public Direction getDirection(AIComponent ai, PositionComponent pos)
	{
		return ai.getDirectionFromPath(pos.position, target.position);
	}
}

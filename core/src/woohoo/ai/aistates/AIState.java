package woohoo.ai.aistates;

import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public interface AIState
{
	public Direction getDirection(AIComponent ai, PositionComponent pos);
}
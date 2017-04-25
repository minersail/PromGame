package woohoo.ai.aistates;

import com.badlogic.gdx.math.Vector2;
import woohoo.ai.Node;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.MovementComponent.Direction;
import woohoo.gameobjects.components.PositionComponent;

public class RandomState implements AIState
{
	private Vector2 randomLoc;
	private int stillTimer; 
	private int stillMax; // How long the entity should stand still after reaching target location, in multiples of entity's timestep
						  // Probably not ideal, but not high priority for now. Default is 6 * 0.5s = 3s;
	public RandomState()
	{
		this(6);
	}
	
	public RandomState(int max)
	{
		stillTimer = 1; // Start the timer by default
		stillMax = max;
		randomLoc = null;
	}

	@Override
	public Direction getDirection(AIComponent ai, PositionComponent pos)
	{	
		if (stillTimer > 0) // Still timer has been started
		{
			stillTimer++;		
			if (stillTimer >= stillMax) // Timer reaches max; generate new random location and disable timer
			{
				randomLoc = generateRandomLoc(ai);
				stillTimer = 0;
			}
			return Direction.None;
		}
		else
		{	
			if (pos.position.dst(randomLoc) < 0.5f)
			{
				stillTimer++; // Start the timer
			}
			
			return ai.getDirectionFromPath(pos.position, randomLoc);
		}
	}
	
	private Vector2 generateRandomLoc(AIComponent ai)
	{
		Node node = ai.getAIMap().getRandomNode();
		
		return new Vector2(node.x, node.y);
	}
}

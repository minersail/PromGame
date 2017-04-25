package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.ai.aistates.FollowState;
import woohoo.ai.aistates.SentryState;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.LOSComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.screens.PlayingScreen;

public class LineOfSightSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public LineOfSightSystem(PlayingScreen scr)
	{
		super(Family.all(LOSComponent.class, PositionComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		LOSComponent los = Mappers.sightLines.get(entity);
		PositionComponent pos = Mappers.positions.get(entity);
		
		// If hitbox exists use hitbox position
		if (Mappers.hitboxes.has(entity))
			los.mass.setTransform(Mappers.hitboxes.get(entity).mass.getPosition(), 0);
		else // Use default position
			los.mass.setTransform(pos.position.cpy(), 0);
		
		los.rotate(pos.orientation);
		
		if (los.playerNotSeenTime == 0) // Gains sight of player
		{
			if (Mappers.ai.has(entity))
			{
				AIComponent brain = Mappers.ai.get(entity);
				if (brain.getState() instanceof SentryState) // If ai is in sentry mode
				{
					brain.setState(new FollowState(Mappers.positions.get(screen.getEngine().getPlayer())));
				}
			}
		}
		else if (los.playerNotSeenTime > 5) // Lost sight of player for 5 secs
		{
			if (Mappers.ai.has(entity))
			{
				AIComponent brain = Mappers.ai.get(entity);
				// If AI was a sentry, and is now following, and has lost sight
				if (brain.getState() instanceof FollowState && brain.getCachedState() instanceof SentryState)
				{
					brain.setState(brain.getCachedState());
				}
			}
		}
		
		los.playerNotSeenTime += deltaTime; // If player is seen again the total time will be reset to 0
	}
}

package woohoo.framework.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import woohoo.ai.aistates.SentryState;
import woohoo.gameobjects.components.AIComponent;

public class AISentryEvent implements Event<Entity>
{
	private AIComponent aiChar;
	private Array<Vector2> patrol;
	
	public AISentryEvent(AIComponent character, Array<Vector2> patrolLocations)
	{
		aiChar = character;
		patrol = patrolLocations;
	}
	
	@Override
	public void activate()
	{
		aiChar.setState(new SentryState(patrol));
	}	
}
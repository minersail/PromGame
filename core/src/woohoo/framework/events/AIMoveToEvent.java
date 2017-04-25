package woohoo.framework.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import woohoo.ai.aistates.MoveToState;
import woohoo.gameobjects.components.AIComponent;

public class AIMoveToEvent implements Event<Entity>
{
	private AIComponent aiChar;
	private Vector2 target;
	
	public AIMoveToEvent(AIComponent character, float x, float y)
	{
		aiChar = character;
		target = new Vector2(x, y);
	}
	
	@Override
	public void activate()
	{
		aiChar.setState(new MoveToState(target));
	}	
}

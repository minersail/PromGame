package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;

/**
 * Similar to input command, but run every frame from key press until key release instead of once
 * @author jordan
 */
public interface InputState
{
	public void execute(Entity entity);
}

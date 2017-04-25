package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;

/**
 * Runs a command on key press
 * @author jordan
 */
public interface InputCommand
{
	public void execute(Entity entity);
}
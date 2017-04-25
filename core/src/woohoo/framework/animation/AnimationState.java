package woohoo.framework.animation;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.AnimMapObjectComponent;

public interface AnimationState
{
	public void enter(AnimMapObjectComponent animated);
	public void animate(float delta, Entity entity);
	public String getAnimString(Entity entity);
}

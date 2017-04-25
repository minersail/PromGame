package woohoo.framework.animation;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameworld.Mappers;

public class DeathAnimState implements AnimationState
{
	@Override
	public void enter(AnimMapObjectComponent animated)
	{
		animated.animationTime = 0;
	}

	@Override
	public void animate(float delta, Entity entity)
	{
		AnimMapObjectComponent animation = Mappers.animMapObjects.get(entity);
		
		animation.addTime(delta);
	}

	@Override
	public String getAnimString(Entity entity)
	{
		return "death";
	}
}

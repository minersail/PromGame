package woohoo.framework.animation;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameworld.Mappers;

public class WalkAnimState implements AnimationState
{
	@Override
	public void enter(AnimMapObjectComponent animated)
	{
		
	}

	@Override
	public void animate(float delta, Entity entity)
	{
		AnimMapObjectComponent animation = Mappers.animMapObjects.get(entity);
		
		animation.addTime(delta);
		
		if (Mappers.movements.get(entity).isStopped(0.25f))
		{
			animation.setAnimationState(new IdleAnimState());
		}
	}

	@Override
	public String getAnimString(Entity entity)
	{
		return Mappers.positions.get(entity).orientation.text() + "_walk";
	}
}

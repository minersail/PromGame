package woohoo.framework.animation;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameworld.Mappers;

public class FightAnimState implements AnimationState
{
	private float fightTime;
	private float fightTimer;
	
	public FightAnimState()
	{
		this(-1);
	}
	
	// Constructor if the attack animation should persist for a certain amount of time
	public FightAnimState(float time)
	{
		fightTimer = 0;
		fightTime = time;
	}
	
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
		fightTimer += delta;
		
		if (fightTime == -1) // No special time specified
		{
			if (fightTimer > animation.getAnimation("down_fight").getAnimationDuration()) // Animation will only last as long as it needs to
				animation.setAnimationState(new IdleAnimState());
		}
		else
		{
			if (fightTimer > fightTime)
				animation.setAnimationState(new IdleAnimState());
		}
	}

	@Override
	public String getAnimString(Entity entity)
	{
		return Mappers.positions.get(entity).orientation.text() + "_fight";
	}
}

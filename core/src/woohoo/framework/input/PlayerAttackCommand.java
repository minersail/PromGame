package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.framework.animation.FightAnimState;
import woohoo.gameworld.Mappers;

public class PlayerAttackCommand implements InputCommand
{
	@Override
	public void execute(Entity entity)
	{
		if (!Mappers.weapons.has(entity)) return;
		
		Mappers.weapons.get(entity).swing();
		
		if (Mappers.animMapObjects.has(entity))
		{
			Mappers.animMapObjects.get(entity).setAnimationState(new FightAnimState());
		}
	}
}

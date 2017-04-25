package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.gameworld.Mappers;

public class PrintPosCommand implements InputCommand
{
	@Override
	public void execute(Entity entity)
	{
		if (Mappers.positions.get(entity) == null) return;
		
		System.out.println(Mappers.positions.get(entity).position);
	}
}

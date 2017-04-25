package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;

public class OpacityComponent implements Component
{
	public float runTime;
	
	public float calculateOpacity()
	{
		return ((float)Math.sin(3 * runTime) + 1) / 2;
	}
}

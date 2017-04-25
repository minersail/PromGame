package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;

public class IDComponent implements Component
{
	public String name;
	
	public IDComponent(String str)
	{
		name = str;
	}
}

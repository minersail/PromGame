package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component
{
	public enum Orientation
	{
		North("up"), 
		South("down"),
		West("left"),
		East("right");
		
		private String text;
		
		Orientation(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static Orientation fromString(String str) 
		{
			for (Orientation b : Orientation.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No Orientation with text " + str + " found.");
		}
	}
	
	public Vector2 position;
	public Orientation orientation;
	
	public PositionComponent()
	{
		this(0, 0);
	}
	
	public PositionComponent(float startX, float startY)
	{
		this(new Vector2(startX, startY));
	}
	
	public PositionComponent(Vector2 pos)
	{
		position = pos;
		orientation = Orientation.South;
	}
}

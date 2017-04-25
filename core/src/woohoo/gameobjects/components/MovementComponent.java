package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class MovementComponent implements Component
{		
	public enum Direction
	{
		Up("up"), 
		Down("down"),
		Left("left"),
		Right("right"),
		None("none");
		
		private String text;
		
		Direction(String str)
		{
			text = str;
		}
		
		public String text()
		{
			return text;
		}
		
		public static Direction fromString(String str) 
		{
			for (Direction b : Direction.values()) 
			{
				if (b.text.equalsIgnoreCase(str))
				{
					return b;
				}
			}
			throw new IllegalArgumentException("No Direction with text " + str + " found.");
		}
		
		public static boolean isHorizontal(Direction dir)
		{
			return dir == Left || dir == Right;
		}
		
		public static boolean isVertical(Direction dir)
		{
			return dir == Up || dir == Down;
		}
	}
	
	public Vector2 velocity;
	public Direction direction;
	public float speed;
	
	public MovementComponent()
	{
		this(1);
	}
	
	public MovementComponent(float entitySpeed)
	{
		direction = Direction.None;
		velocity = new Vector2(0, 0);
		speed = entitySpeed;
	}
	
	public boolean isStopped(float tolerance)
	{
		return velocity.isZero(tolerance);
	}
}

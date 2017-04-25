package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GateComponent implements Component
{
    private Vector2 playerOffset;
	
	public Vector2 position; // NOT gate's position, but rather it's intended teleportation spot
	public Vector2 size;
	public Vector2 playerPos; // Where the player will exit the gate
	public int destArea;
	public boolean triggered; // Whether the gate was activated
	public boolean enabled; // Whether the gate can be activated again
	public int narrationID; // -1 if no narration will popup, otherwise an ID that will link to XML

    public GateComponent(World world)
    {        		
        // setPlayerOffset() must be called before playerPos can be used
        playerOffset = null;
		playerPos = null;
		
		triggered = false;
		enabled = true;
    }

    public void setPlayerOffset(Vector2 offset)
    {
        // Took me forever to figure this out
        // Reposition the character relative to the gate's exit location based on where the player entered the gate's entrance
        playerOffset = new Vector2(Math.min(Math.max(0, offset.x), size.x - 1), Math.min(Math.max(0, offset.y), size.y - 1));
		playerPos = position.cpy().add(playerOffset);
    }
	
	public Vector2 getPlayerOffset()
	{
		return playerOffset;
	}
}

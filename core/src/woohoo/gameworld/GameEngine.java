package woohoo.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import woohoo.screens.PlayingScreen;

/*
All objects are data that will be drawn by the tiles

Their updates will be called in this class
*/
public class GameEngine extends Engine
{
	private PlayingScreen screen;
    
    public float runtime;
	
	public GameEngine(PlayingScreen scr)
	{
		screen = scr;
	}
    
	@Override
    public void update(float delta)
    {
		super.update(delta);
        adjustCamera(getPlayer());
		runtime += delta;
    }
	
	public void animate(float delta)
	{
		getSystem(RenderSystem.class).update(delta);
	}
	
	@Override
	public void addEntity(Entity entity)
	{
		super.addEntity(entity);
	}
	    
    public Entity getPlayer()
    {
        return getEntity("player");
    }
	
	public Entity getEntity(String name)
	{
		for (Entity entity : getEntities())
        {
			if (Mappers.ids.has(entity) && Mappers.ids.get(entity).name.equals(name))
				return entity;
        }
		
        Gdx.app.log("ERROR", "Entity " + name + " does not exist in GameWorld");
        return null;
	}
	
	/*
	If map is smaller than screen, center it in screen. If it is larger, pan it based on player's location
	*/
	public void adjustCamera(Entity player)
	{
		Vector2 p = Mappers.positions.get(player).position;
		Vector2 newPos = new Vector2(p);
				
		if (screen.mapWidth + 1 > screen.WORLD_WIDTH)
		{
			newPos.x = Math.min(Math.max(newPos.x, screen.WORLD_WIDTH / 2), screen.mapWidth - screen.WORLD_WIDTH / 2);
		}
		else
		{
			float extraX = (float)(screen.WORLD_WIDTH - screen.mapWidth);
			newPos.x = screen.getCamera().viewportWidth / 2 - Math.max(0, extraX / 2);
		}
		
		// Move x and y camera coordinates independently
		if (screen.mapHeight > screen.WORLD_HEIGHT)
		{
			newPos.y = Math.min(Math.max(newPos.y, screen.WORLD_HEIGHT / 2), screen.mapHeight - screen.WORLD_HEIGHT / 2);
		}
		else
		{
			float extraY = (float)(screen.WORLD_HEIGHT - screen.mapHeight);
			newPos.y = screen.getCamera().viewportHeight / 2 - Math.max(0, extraY / 2);
		}
		
		screen.setCamera(newPos.x, newPos.y);
        getSystem(RenderSystem.class).getRenderer().setView(screen.getCamera());
	}
	
	public ArrayList<Entity> getDuplicateList()
	{
		ArrayList<Entity> list = new ArrayList<>();
		for (Entity entity : getEntities())
		{
			list.add(entity);
		}
		
		return list;
	}
}

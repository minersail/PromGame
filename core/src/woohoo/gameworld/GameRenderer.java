package woohoo.gameworld;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.ChaseComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.MapObjectComponent;

/*
Anything that is drawn will be managed by this class
(Currently only the TileMap)
*/
public class GameRenderer extends OrthogonalTiledMapRenderer
{    
	private boolean skipNextFrame;
	
    public GameRenderer(TiledMap map, float scale)
	{
		super(map, scale);
		super.getBatch().enableBlending();
	}
	
	@Override
	public void renderObject(MapObject object)
	{		
		if (object instanceof AnimMapObjectComponent)
		{
			AnimMapObjectComponent obj = (AnimMapObjectComponent)object;

			Color oldColor = batch.getColor();
			batch.setColor(obj.getColor());
			batch.draw(obj.getAnimation(obj.animString).getKeyFrame(obj.animationTime), obj.getX(), obj.getY(), obj.size.x, obj.size.y);
			batch.setColor(oldColor);
		}
		else if (object instanceof MapObjectComponent)
		{			
			MapObjectComponent obj = (MapObjectComponent)object;
			
			Color oldColor = batch.getColor();
			batch.setColor(obj.getColor());
			batch.draw(obj.getTextureRegion(), obj.getX(), obj.getY(), obj.size.x, obj.size.y);
			batch.setColor(oldColor);
		}		
        else if (object instanceof HealthBarComponent)
        {
            HealthBarComponent healthBar = (HealthBarComponent)object;
			
            healthBar.draw(batch);
        }	
        else if (object instanceof ChaseComponent)
        {
            ChaseComponent chaseBar = (ChaseComponent)object;
            chaseBar.draw(batch);
        }
	}
	
	@Override
	public void render()
	{		
		if (!skipNextFrame)
			super.render();
		else
			skipNextFrame = false;
	}
	
	// Creates a one frame buffer between screens
	public void skipFrame()
	{
		skipNextFrame = true;
	}
}

package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;

public class MapObjectComponent extends TextureMapObject implements Component
{	
	public Vector2 size;
	
	public MapObjectComponent(TextureRegion spr)
    {
		spr.flip(false, true);
		
		super.setTextureRegion(spr);
		
		size = new Vector2(1, 1);
	}
}

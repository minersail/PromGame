package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class ChaseComponent extends TextureMapObject implements Component
{
	private TextureRegion orange;
    private TextureRegion grey;
	
	public float chaseMeter;
	public final float METER_HEIGHT = 0.125f;
    
    public ChaseComponent(TextureAtlas healthBar)
    {
        orange = new TextureRegion(healthBar.findRegion("orange"));
        grey = new TextureRegion(healthBar.findRegion("grey"));
        
        // Maybe use texturemapobject's setTexture to set border
    }
    
    public void draw(Batch batch)
    {
        batch.draw(grey, getX(), getY(), 1, METER_HEIGHT);
        batch.draw(orange, getX(), getY(), chaseMeter / 100, METER_HEIGHT);
    }
}

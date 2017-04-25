package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;

public class HealthBarComponent extends TextureMapObject implements Component 
{
    private TextureRegion green;
    private TextureRegion red;
	
	public float percentLeft;
    
    public HealthBarComponent(TextureAtlas healthBar)
    {
        green = new TextureRegion(healthBar.findRegion("green"));
        red = new TextureRegion(healthBar.findRegion("red"));
        
        // Maybe use texturemapobject's setTexture to set border
    }
    
    public void draw(Batch batch)
    {
        batch.draw(red, getX(), getY(), 1, 0.125f);
        batch.draw(green, getX(), getY(), percentLeft, 0.125f);
    }
}

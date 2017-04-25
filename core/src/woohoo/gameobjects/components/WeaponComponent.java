package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import woohoo.gameobjects.components.PositionComponent.Orientation;

/**
 * Class containing the box2D components of a weapon
 */
public class WeaponComponent implements Component
{    
	public Body mass;
	public Fixture fixture;
	
	public float weaponAngle; // Radians
	public Orientation weaponDirection;
	public boolean isActive;
	
	public float damage;
	public float knockback;
    
    public WeaponComponent(World world) 
    {        
        BodyDef bodyDef = new BodyDef();
		mass = world.createBody(bodyDef);
        mass.setType(BodyDef.BodyType.DynamicBody);
		
		PolygonShape shape = new PolygonShape();	
		shape.setAsBox(0.5f, 0.125f, new Vector2(0.375f, 0), 0);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		
        fixture = mass.createFixture(fixtureDef);
		fixture.setSensor(true);
        fixture.setDensity(0.001f);
		
		isActive = false;
    }    
	
	public void swing()
	{
		isActive = true;
		mass.setFixedRotation(false);
		mass.setTransform(mass.getTransform().getPosition(), weaponAngle);
		mass.setAngularVelocity(50);
	}
}

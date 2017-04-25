package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameobjects.components.PositionComponent.Orientation;
import woohoo.gameobjects.components.WeaponComponent;

public class WeaponSystem extends IteratingSystem
{
	public WeaponSystem()
	{
		super(Family.all(WeaponComponent.class, PositionComponent.class).get());
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		WeaponComponent weapon = Mappers.weapons.get(entity);
		
		setAngle(weapon, Mappers.positions.get(entity).orientation);
		
		// Check if weapon is done with swing
		float rotation = weapon.mass.getTransform().getRotation();
		if (rotation < 0) rotation += 2 * (float)Math.PI;
		
		// Between 95% and 105% of the desired angle
		float upperBound = 0.95f * (weapon.weaponAngle + 2 * (float)Math.PI / 3);
		float lowerBound = 1.05f * (weapon.weaponAngle + 2 * (float)Math.PI / 3);
		
		if (lowerBound > 2 * (float)Math.PI) lowerBound -= 2 * (float)Math.PI;
		if (upperBound > 2 * (float)Math.PI) upperBound -= 2 * (float)Math.PI;
		
		// Stop swing
		if (rotation >= upperBound && rotation <= lowerBound)
		{
			weapon.isActive = false;
			weapon.mass.setTransform(weapon.mass.getTransform().getPosition(), weapon.weaponAngle);
			weapon.mass.setAngularVelocity(0);
			weapon.mass.setFixedRotation(true);
		}
	}
	
	public void equip(Entity equipped, WeaponComponent weapon)
    {
		equipped.add(weapon);
        
        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.bodyA = Mappers.hitboxes.get(equipped).mass;
        jointDef.bodyB = weapon.mass;
        
        Mappers.hitboxes.get(equipped).mass.getWorld().createJoint(jointDef);
			
		setAngle(weapon, Mappers.positions.get(equipped).orientation);
    }
	
	public void unequip(Entity equipped)
	{
		WeaponComponent weapon = Mappers.weapons.get(equipped);
		weapon.mass.getWorld().destroyBody(weapon.mass);
		equipped.remove(WeaponComponent.class);
	}
	
	public void setAngle(WeaponComponent weapon, Orientation dir)
	{
		switch (dir)
		{
			case West:
				weapon.weaponAngle = 2 * (float)Math.PI / 3;
				break;
			case North:
				weapon.weaponAngle = 7 * (float)Math.PI / 6;
				break;
			case East:
				weapon.weaponAngle = 5 * (float)Math.PI / 3;
				break;
			case South:
				weapon.weaponAngle = (float)Math.PI / 6;
				break;
		}
		
		weapon.weaponDirection = dir;
	}
}

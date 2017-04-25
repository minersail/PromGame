package woohoo.framework.contactcommands;

import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameworld.Mappers;

public class EnemyTouchPlayerContact extends ContactCommand 
{
	public EnemyTouchPlayerContact() 
	{
		super(ContactType.Enemy, ContactType.Player);
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB) 
	{
		if (contactA.type == ContactType.Player) // Switch; Parameters can come in either order but code requires Enemy to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
				
		HitboxComponent hitbox = Mappers.hitboxes.get(contactB.owner);
		
		switch(Mappers.positions.get(contactA.owner).orientation)
		{
			case North:			
				hitbox.mass.applyForceToCenter(new Vector2(0, -50000), true);
				break;
			case South:			
				hitbox.mass.applyForceToCenter(new Vector2(0, 50000), true);
				break;
			case West:			
				hitbox.mass.applyForceToCenter(new Vector2(-50000, 0), true);
				break;
			case East:			
				hitbox.mass.applyForceToCenter(new Vector2(50000, 0), true);
				break;
		}
		
		Mappers.lives.get(contactB.owner).damage(1);		
	}	
}

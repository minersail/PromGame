package woohoo.framework.contactcommands;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameworld.Mappers;

public class PlayerSightedContact extends ContactCommand
{
	private RaycastData data;
	
	public PlayerSightedContact()
	{
		super(ContactType.Player, ContactType.SightLine);
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == ContactType.SightLine) // Switch; Parameters can come in either order but code requires Player to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
		
		data = new RaycastData();
		RayCastCallback callback = new RayCastCallback() 
		{
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) 
			{
                if ( fraction < data.fraction ) 
				{
                    data.fraction = fraction;
					data.fixture = fixture;
                }

                return 1;
            }
        };
		
		Vector2 losPos = Mappers.hitboxes.get(contactB.owner).mass.getPosition();
		Vector2 playerPos = Mappers.hitboxes.get(contactA.owner).mass.getPosition();
		
		Mappers.sightLines.get(contactB.owner).fixture.getBody().getWorld().rayCast(callback, losPos, playerPos);
		
		if (data.fixture == Mappers.hitboxes.get(contactA.owner).fixture)
		{
			Mappers.sightLines.get(contactB.owner).playerNotSeenTime = 0;
		}
	}
	
	private class RaycastData 
	{
		public RaycastData()
		{
			fixture = null;
			fraction = 1;
		}
		
		public float fraction;
		public Fixture fixture;
	}
}

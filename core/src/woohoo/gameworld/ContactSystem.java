package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import java.util.Objects;
import woohoo.framework.contactcommands.*;
import woohoo.gameobjects.components.ContactComponent;

public class ContactSystem extends IteratingSystem
{
	private Array<ContactDataPair> collisions;
	private Array<ContactCommand> commands;
	
	public ContactSystem(World world)
	{
		super(Family.all(ContactComponent.class).get());
		
		collisions = new Array<>();
		commands = new Array<>();
        commands.add(new GateContact());
        commands.add(new EnemyHitByPlayerContact());
        commands.add(new EnemyTouchPlayerContact());
        commands.add(new ItemContact());
        commands.add(new ChaseContact());
		commands.add(new PlayerSightedContact());
		
		world.setContactListener(new ContactListener() 
		{
			@Override
			public void beginContact(Contact contact)
			{				
				ContactData data1 = (ContactData)contact.getFixtureA().getBody().getUserData();
				ContactData data2 = (ContactData)contact.getFixtureB().getBody().getUserData();
				
				ContactDataPair pair = new ContactDataPair(data1, data2);
				collisions.add(pair);
			}

			@Override
			public void endContact(Contact contact)
			{
				ContactData data1 = (ContactData)contact.getFixtureA().getBody().getUserData();
				ContactData data2 = (ContactData)contact.getFixtureB().getBody().getUserData();
				
				ContactDataPair pair = new ContactDataPair(data1, data2);
				collisions.removeValue(pair, false);
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {}
		});
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		
		for (ContactDataPair pair : collisions) // Iterate through all collisions in world
		{
			for (ContactCommand command : commands) // Iterate through all loaded ContactCommands
			{
				if (command.verify(pair.A(), pair.B()))
					command.activate(pair.A(), pair.B());
			}
		}
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		ContactComponent contact = Mappers.contacts.get(entity);
	}
	
	public void clearContacts()
	{
		collisions.clear();
	}
	
	public class ContactDataPair
	{
		private ContactData A;
		private ContactData B;
		
		public ContactDataPair(ContactData contactA, ContactData contactB)
		{
			A = contactA;
			B = contactB;
		}
		
		public boolean contains(ContactData contact)
		{
			return contact == A || contact == B;
		}
		
		public ContactData A()
		{
			return A;
		}
		
		public ContactData B()
		{
			return B;
		}
		
		@Override
		public boolean equals(Object other)
		{
			if (!(other instanceof ContactDataPair)) return false;
			
			ContactDataPair pair = (ContactDataPair)other;
			
			// If the pairs contain the same two ContactDatas
			return (pair.A == A && pair.B == B) || (pair.A == B && pair.B == A);
		}

		@Override
		public int hashCode()
		{
			int hash = 7;
			hash = 31 * hash + Objects.hashCode(this.A);
			hash = 31 * hash + Objects.hashCode(this.B);
			return hash;
		}
        
        @Override
        public String toString()
        {
            return "ContactDataPair: " + A.type + "|" + B.type;
        }
	}
}

package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.ChaseComponent;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameworld.Mappers;

public class ChaseContact extends ContactCommand
{
	public ChaseContact()
	{
		super(ContactType.Chaser, ContactType.Player);
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == ContactType.Player) // Switch; Parameters can come in either order but code requires Chaser to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
		
		ChaseComponent chase = Mappers.chasers.get(contactB.owner);
		
		if (chase.chaseMeter < 100)
			chase.chaseMeter++;
		else
		{
			Mappers.lives.get(contactB.owner).kill(); // Game over for player
		}
	}
}

package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.PlayerComponent;
import woohoo.gameworld.Mappers;

public class ItemContact extends ContactCommand
{
	public ItemContact()
	{
		super(ContactType.Item, ContactType.Player);
	}

	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == ContactType.Player) // Switch; Parameters can come in either order but code requires Item to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
		
		PlayerComponent player = Mappers.players.get(contactB.owner);
		
		player.touchedItems.add(contactA.owner);
	}
}

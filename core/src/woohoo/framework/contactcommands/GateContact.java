package woohoo.framework.contactcommands;

import woohoo.gameobjects.components.GateComponent;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.Mappers;

/**
 * Contains all the code for when the player interacts with a gate
 * @author jordan
 */
public class GateContact extends ContactCommand
{	
	public GateContact()
	{
		super(ContactType.Gate, ContactType.Player);
	}
	
	@Override
	public void activate(ContactData contactA, ContactData contactB)
	{
		if (contactA.type == ContactType.Player) // Switch; Parameters can come in either order but code requires Gate to be A
		{
			ContactData temp = contactA;
			contactA = contactB;
			contactB = temp;
		}
		
		
		GateComponent gate = Mappers.gates.get(contactA.owner);
		PositionComponent gatePos = Mappers.positions.get(contactA.owner);
		PositionComponent playerPos = Mappers.positions.get(contactB.owner);
		
		if (gate.enabled)
		{
			gate.setPlayerOffset(playerPos.position.cpy().sub(gatePos.position)); // Distance from top-left of player to top-left of gate
			gate.triggered = true;
			gate.enabled = false; // Gate can only activate once
		}
	}
}

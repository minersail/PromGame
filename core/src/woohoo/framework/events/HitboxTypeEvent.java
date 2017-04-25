package woohoo.framework.events;

import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.HitboxComponent;

public class HitboxTypeEvent implements Event 
{
	private ContactType type;
	private HitboxComponent hitbox;
	
	public HitboxTypeEvent(String newContactType, HitboxComponent hb)
	{
		type = ContactType.fromString(newContactType);
		hitbox = hb;
	}
	
	@Override
	public void activate()
	{
		hitbox.getContactData().type = type;
		hitbox.hitboxType = type;
	}	
}

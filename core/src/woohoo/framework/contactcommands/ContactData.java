package woohoo.framework.contactcommands;

import com.badlogic.ashley.core.Entity;
import woohoo.gameobjects.components.ContactComponent.ContactType;

/**
 * ContactData is attached to bodies through getUserData()
 * Any component that owns a body will have a ContactData
 * Entities can have multiple ContactData, one per body
 * 
 * @author jordan
 */
public class ContactData
{
	public ContactType type;
	public Entity owner;
	
	public ContactData(ContactType contactType, Entity owningEntity)
	{
		type = contactType;
		owner = owningEntity;
	}
}

package woohoo.framework.events;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.EntityLoader;

public class AddComponentEvent<T> implements Event<T> 
{
	private EntityLoader loader;
	private Entity entity;
	private Element component;
	
	public AddComponentEvent(EntityLoader eloader, Entity e, Element xmlcomponent)
	{
		loader = eloader;
		entity = e;
		component = xmlcomponent;
	}
	
	@Override
	public void activate() 
	{
		loader.loadComponent(entity, component);
	}	
}

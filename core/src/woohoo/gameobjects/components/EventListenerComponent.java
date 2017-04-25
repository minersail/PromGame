package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import woohoo.framework.events.EventListeners;

public class EventListenerComponent implements Component
{
	public EventListeners<Entity> listeners = new EventListeners<>();
}

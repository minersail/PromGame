package woohoo.framework.events;

import java.util.ArrayList;
import java.util.Arrays;

public class EventListener<T>
{
	public enum EventListenerState
	{
		Enabled, Disabled, Autodisable
	}
	
	ArrayList<Event> events;
	EventTrigger trigger;
	EventListenerState listenerState;
	int id;
	int area;
	
	public EventListener(String state, int ID, int eventArea, EventTrigger eT)
	{
		trigger = eT;		
		events = new ArrayList<>();
		id = ID;
		area = eventArea;
		
		switch(state)
		{
			case "enabled": // Event will load every time player re-enters screen
				listenerState = EventListenerState.Enabled;
				break;
			case "disabled": // Event will not load
				listenerState = EventListenerState.Disabled;
				break;
			case "autodisable": // Event will load first time player enters screen
				listenerState = EventListenerState.Autodisable;
				break;
		}
	}
	
	public EventListener(String state, int ID, int eventArea, EventTrigger eT, Event... e)
	{
		this(state, ID, eventArea, eT);
		events.addAll(Arrays.asList(e));
	}
	
	public void addEvent(Event e)
	{
		events.add(e);
	}
	
	public boolean notify(T listenerHolder)
	{
		if (trigger.check(listenerHolder))
		{
			for (Event event : events)
				event.activate();
			
			return true;
		}
		
		return false;
	}
	
	public EventListenerState getListenerState()
	{
		return listenerState;
	}
	
	public int getArea()
	{
		return area;
	}
	
	public int getID()
	{
		return id;
	}
}

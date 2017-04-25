package woohoo.framework.events;

public class ScreenTrigger<T> implements EventTrigger<T>
{
	@Override
	public boolean check(T listenerType)
	{
		return true; // Returns true immediately; This event triggers on screen load
	}
}

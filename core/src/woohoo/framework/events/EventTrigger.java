package woohoo.framework.events;

public interface EventTrigger<T>
{
	public boolean check(T listenerType);
}

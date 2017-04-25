package woohoo.framework.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import java.util.ArrayList;
import java.util.ListIterator;
import woohoo.framework.events.EventListener.EventListenerState;

public class EventListeners<T>
{
	private ArrayList<EventListener> listeners = new ArrayList<>();
	
	public void notifyAll(T listenerHolder)
	{		
		for (ListIterator<EventListener> iter = listeners.listIterator(); iter.hasNext();)
		{
			EventListener listener = iter.next();
			
			if (listener.notify(listenerHolder))
			{
				if (listener.getListenerState() == EventListenerState.Autodisable)
				{
					FileHandle handle = Gdx.files.local("data/events.xml");

					XmlReader reader = new XmlReader();
					XmlReader.Element root = reader.parse(handle.readString());
					XmlReader.Element targetListener = root.getChild(listener.getArea()).getChild(listener.getID());

					targetListener.setAttribute("state", "disabled");

					handle.writeString(root.toString(), false);
				}
					
				iter.remove();
			}
		}
	}
	
	public void addListener(EventListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeListener(EventListener listener)
	{
		listeners.remove(listener);
	}
	
	public void clearListeners()
	{
		listeners.clear();
	}
	
	public int getCount()
	{
		return listeners.size();
	}
}

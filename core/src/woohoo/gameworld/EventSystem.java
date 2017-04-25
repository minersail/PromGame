package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.events.*;
import woohoo.gameobjects.components.EventListenerComponent;
import woohoo.screens.PlayingScreen;

public class EventSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public EventSystem(PlayingScreen scr)
	{
		super(Family.all(EventListenerComponent.class).get());
		
		screen = scr;
	}
	
	public void initialize(int area)
	{
		FileHandle handle = Gdx.files.local("data/events.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());       
        Element eventListeners = root.getChild(area);        
        
        for (Element eventListener : eventListeners.getChildrenByName("eventlistener"))
        {		
			if (eventListener.get("state").equals("disabled")) continue;
			
			EventTrigger trigger;
			Event event;
			
			Element triggerEl = eventListener.getChildByName("trigger");
			Array<XmlReader.Element> eventEls = eventListener.getChildrenByName("event");
			
			// Create event trigger
			switch (triggerEl.get("type").toLowerCase())
			{
				case "move":
					trigger = new MoveTrigger(new Vector2(triggerEl.getFloat("locX"), triggerEl.getFloat("locY")), triggerEl.getFloat("dist"));
					break;
				case "screen":
					trigger = new ScreenTrigger();
					break;
				case "cutscene":
					trigger = new CutsceneTrigger();
					break;
				default:
					trigger = null;
					break;
			}
			
			EventListener EL = new EventListener(eventListener.get("state"), eventListener.getInt("id"), area, trigger);
			
			// Create events to be activated by the event trigger
			for (final Element eventEl : eventEls)
			{				
				switch (eventEl.get("type").toLowerCase())
				{
					case "cutscene":
						event = new CutsceneEvent(screen.getCutsceneManager(), eventEl.getInt("id"));
						break;
					case "aifollow":
						event = new AIFollowEvent(Mappers.ai.get(screen.getEngine().getEntity(eventEl.get("entity"))), 
												  Mappers.positions.get(screen.getEngine().getEntity(eventEl.get("targetChar"))));
						break;
					case "aimoveto":
						event = new AIMoveToEvent(Mappers.ai.get(screen.getEngine().getEntity(eventEl.get("entity"))), 
												  eventEl.getFloat("targetX"), eventEl.getFloat("targetY"));
						break;
					case "aisentry":
						Array<Vector2> patrol = new Array<>();
						for (Element patrolEl : eventEl.getChildrenByName("patrol"))
						{
							patrol.add(new Vector2(patrolEl.getFloat("x"), patrolEl.getFloat("y")));
						}
						
						event = new AISentryEvent(Mappers.ai.get(screen.getEngine().getEntity(eventEl.get("entity"))), patrol);
						break;
					case "quest":
						event = new QuestEvent(eventEl.getInt("id"), eventEl.get("action"), screen.getQuestManager());
						break;
					case "editxml":
						event = new AttributeXMLEvent(eventEl.get("filename"), eventEl.get("attribute"), eventEl.get("value"), eventEl.getInt("area"), 
													  eventEl.get("elementname"), eventEl.get("selectorname"), eventEl.get("selectorvalue"));
						break;
					case "hitboxtype":
						event = new HitboxTypeEvent(eventEl.get("contacttype"), Mappers.hitboxes.get(screen.getEngine().getEntity(eventEl.get("entity"))));
						break;
					case "addcomponent":
						event = new AddComponentEvent(screen.getEntityLoader(), screen.getEngine().getEntity(eventEl.get("entity")), eventEl.getChild(0));
						break;
					default:
						event = null;
						break;
				}
				
				EL.addEvent(event);
			}		
			
			// Combine the event trigger and events and allocate them to the correct place
			if (eventListener.get("owner").equals("entity"))
			{
				Mappers.eventListeners.get(screen.getEngine().getEntity(eventListener.get("entity"))).listeners.addListener(EL);
			}
			else if (eventListener.get("owner").equals("system"))
			{
				switch (eventListener.get("system"))
				{
					case "cutscene":
						screen.getCutsceneManager().getListeners().addListener(EL);
						break;
				}					
			}
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		Mappers.eventListeners.get(entity).listeners.notifyAll(entity);
	}
}

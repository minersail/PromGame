package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.contactcommands.ContactData;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.ChaseComponent;
import woohoo.gameobjects.components.ContactComponent.ContactType;
import woohoo.gameobjects.components.GateComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.screens.PlayingScreen;

public class GateSystem extends IteratingSystem
{
	private GateComponent triggeredGate;
	private boolean switchArea;
	private final PlayingScreen screen;
	
	public GateSystem(PlayingScreen scr)
	{
		super(Family.all(GateComponent.class).get());
		screen = scr;
	}
	
	/**
	 * Called after entities have been added to the systems
	 * Uses the gateID in every GateComponent to create a box2D body
	 * @param area game area to load
	 */
	public void initialize(int area)
	{
		FileHandle handle = Gdx.files.local("data/gates.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());   
        Element gates = root.getChild(area);      
        
        for (Element gateElement : gates.getChildrenByName("gate"))
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.StaticBody;
			// Make it so the XML refers to the top-left coordinate as opposed to center for box2D bodies
            bodyDef.position.set(gateElement.getFloat("locX") + gateElement.getFloat("sizeX") / 2, gateElement.getFloat("locY") + gateElement.getFloat("sizeY") / 2);

            Body body = screen.getWorld().createBody(bodyDef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(gateElement.getFloat("sizeX") / 2 - 0.02f, gateElement.getFloat("sizeY") / 2 - 0.02f); // subtract 0.02f so slightly smaller than tile

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
            
            Entity entity = new Entity();
            GateComponent gate = new GateComponent(screen.getWorld());
            gate.size = new Vector2(gateElement.getFloat("sizeX"), gateElement.getFloat("sizeY"));
            gate.position = new Vector2(gateElement.getFloat("destX"), gateElement.getFloat("destY")); // Top-left of spawn position
            gate.destArea = gateElement.getInt("destArea");
			gate.narrationID = gateElement.getInt("narration", -1);
			PositionComponent position = new PositionComponent(gateElement.getFloat("locX"), gateElement.getFloat("locY"));
            entity.add(gate);
			entity.add(position);

            body.setUserData(new ContactData(ContactType.Gate, entity));
            
            screen.getEngine().addEntity(entity);
        }
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		GateComponent gate = Mappers.gates.get(entity);
		
		if (gate.triggered)
		{
			triggeredGate = gate;
			switchArea = true;
			gate.triggered = false;
		}                 
	}
	
	/*
    Checks if a game area switch has been triggered, and switches areas if it has.
    */
    public void updateArea()
	{ 
		if (!switchArea) return;
		
        Array<Body> bodies = new Array<>();
		screen.getWorld().getBodies(bodies);
		
		// Clear all bodies, except for player
		for (Body body : bodies)
		{
            if (((ContactData)body.getUserData()).owner != screen.getEngine().getPlayer())
                screen.getWorld().destroyBody(body);
		}
		
		// Remove all entities from game world		
		for (Entity entity : screen.getEngine().getDuplicateList())
		{
			if (entity != screen.getEngine().getPlayer()) // Except player
			{
				screen.getEngine().removeEntity(entity);
			}
		}
		
		// Remove event handlers from old screen
		Mappers.eventListeners.get(screen.getEngine().getPlayer()).listeners.clearListeners();
		screen.getCutsceneManager().getListeners().clearListeners();
		
		// Create new map, discarding old one
		final TiledMap map = screen.getMapLoader().load(triggeredGate.destArea);
		map.getLayers().get("Entities").getObjects().add(screen.getEngine().getPlayer().getComponent(AnimMapObjectComponent.class));
		map.getLayers().get("Entities").getObjects().add(screen.getEngine().getPlayer().getComponent(HealthBarComponent.class));
		screen.getRenderer().setMap(map);
        
		// Move the player to the entrance of the new map based on where he exited previous map (Took forever to figure out)
        Mappers.positions.get(screen.getEngine().getPlayer()).position.set(triggeredGate.playerPos);
		
		// Load all entities, gates, events, pathfinding, etc. for new game area
        screen.setArea(triggeredGate.destArea);
		
		if (triggeredGate.narrationID == -1)
			screen.initialize(triggeredGate.destArea);
		else
			screen.startNarration(triggeredGate.narrationID);
		
		// There's one frame where new map is not quite loaded, so skip the frame. Nobody will even notice
		screen.getRenderer().skipFrame();
		
		// Clear contacts from old screen
		screen.getEngine().getSystem(ContactSystem.class).clearContacts();
		
		// Temporary fix
		screen.getEngine().getPlayer().remove(ChaseComponent.class);
		
		switchArea = false;
	}
}

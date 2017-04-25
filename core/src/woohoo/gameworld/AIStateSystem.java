package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.ai.aistates.StayState;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.screens.PlayingScreen;

public class AIStateSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public AIStateSystem(PlayingScreen scr)
	{
		super(Family.all(MovementComponent.class, PositionComponent.class, AIComponent.class).get());
		
		screen = scr;
	}
	
	/**
	 * Initializes pathfinding grid for all entities in current game area
	 * @param area
	 */
	public void initialize(int area)
	{
		FileHandle handle = Gdx.files.local("data/pathfinding.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());    
		Element data = root.getChild(area);
		
		for (Entity entity : getEntities())
		{
			AIComponent brain = Mappers.ai.get(entity);
			brain.initializePathfinding(screen.getEngine().getSystem(RenderSystem.class).getRenderer().getMap(), screen.getWorld(), data);
		}
	}
    
	/**
	 * Initializes pathfinding grid for a single entity. <br>
	 * Useful when AIComponents are added to an entity after the global initialization
	 * @param entity
	 * @param area
	 */
	public void initialize(Entity entity, int area)
    {
        FileHandle handle = Gdx.files.local("data/pathfinding.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());    
		Element data = root.getChild(area);
		
		AIComponent brain = Mappers.ai.get(entity);
        brain.initializePathfinding(screen.getEngine().getSystem(RenderSystem.class).getRenderer().getMap(), screen.getWorld(), data);
    }

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		MovementComponent movement = Mappers.movements.get(entity);
		PositionComponent position = Mappers.positions.get(entity);
		AIComponent brain = Mappers.ai.get(entity);
		
		brain.timer += deltaTime;
		
		if (brain.timer >= brain.timeStep) // Only recalculates pathfinding after a certain interval
		{
			movement.direction = brain.getState().getDirection(brain, position);
			brain.timer = 0;
		}
		
		if (Mappers.hitboxes.has(entity))
		{
			HitboxComponent hitbox = Mappers.hitboxes.get(entity);
			
			hitbox.mass.setType(brain.getState() instanceof StayState ? BodyType.StaticBody : BodyType.DynamicBody); // Still entities will not be pushable
		}
	}
}

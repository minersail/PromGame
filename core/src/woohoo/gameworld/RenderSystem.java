package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.TiledMap;
import woohoo.gameobjects.components.AnimMapObjectComponent;
import woohoo.gameobjects.components.ChaseComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.MapObjectComponent;
import woohoo.gameobjects.components.OpacityComponent;
import woohoo.gameobjects.components.PositionComponent;

public class RenderSystem extends IteratingSystem
{
	private final GameRenderer renderer;
	
	public RenderSystem(TiledMap map, float scale)
	{
		super(Family.all(PositionComponent.class).one(MapObjectComponent.class, AnimMapObjectComponent.class, HealthBarComponent.class, ChaseComponent.class).get());
		
		renderer = new GameRenderer(map, scale);
	}
	
	@Override
	public void update(float delta)
	{
		super.update(delta);
		renderer.render();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		PositionComponent pos = Mappers.positions.get(entity);
		
		if (Mappers.mapObjects.has(entity))
		{
			MapObjectComponent mapObject = Mappers.mapObjects.get(entity);
			
			mapObject.setX(pos.position.x);
			mapObject.setY(pos.position.y);			
		
			if (Mappers.opacities.has(entity))
			{
				OpacityComponent opacity = Mappers.opacities.get(entity);

				opacity.runTime += deltaTime;
				mapObject.setColor(new Color(1, 1, 1, opacity.calculateOpacity()));
			}
		}
		
		if (Mappers.animMapObjects.has(entity))
		{
			AnimMapObjectComponent animMapObject = Mappers.animMapObjects.get(entity);
			
			animMapObject.setX(pos.position.x);
			animMapObject.setY(pos.position.y);
		}
		
		if (Mappers.healthBars.has(entity))
		{
			HealthBarComponent healthBar = Mappers.healthBars.get(entity);
			
			healthBar.setX(pos.position.x);
			healthBar.setY(pos.position.y);
		}
		
		if (Mappers.chasers.has(entity))
		{
			ChaseComponent chaseBar = Mappers.chasers.get(entity);
			
			chaseBar.setX(pos.position.x);
			chaseBar.setY(pos.position.y - chaseBar.METER_HEIGHT);
		}
	}
	
	public GameRenderer getRenderer()
	{
		return renderer;
	}
}

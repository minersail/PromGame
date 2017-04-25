package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.gameobjects.components.MovementComponent;
import woohoo.gameobjects.components.PositionComponent;
import woohoo.gameworld.gamestates.PlayingState;
import woohoo.screens.PlayingScreen;

public class MovementSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public MovementSystem(PlayingScreen scr)
	{
		super(Family.all(MovementComponent.class, PositionComponent.class, HitboxComponent.class).get());
		
		screen = scr;
	}
	
	public void initialize()
	{
		for (Entity entity : getEntities())
		{
			Mappers.hitboxes.get(entity).mass.setTransform(Mappers.positions.get(entity).position.cpy().add(0.5f, 0.5f), 0);
		}
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{		
		// Only entities in the cutscene should be able to move during cutscenes
		if (!(screen.getState() instanceof PlayingState) && !screen.getCutsceneManager().getEntities().contains(entity)) return;
		
		MovementComponent movement = Mappers.movements.get(entity);
		HitboxComponent hitbox = Mappers.hitboxes.get(entity);
		PositionComponent position = Mappers.positions.get(entity);
		
        if (movement.direction != MovementComponent.Direction.None)
            position.orientation = PositionComponent.Orientation.fromString(movement.direction.text()); // The enums have built in strings for conversion
        
		switch (movement.direction)
		{
			case Up:
				movement.velocity.set(0, -movement.speed);
				break;
			case Down:
				movement.velocity.set(0, movement.speed);
				break;
			case Left:
				movement.velocity.set(-movement.speed, 0);
				break;
			case Right:
				movement.velocity.set(movement.speed, 0);
				break;
			case None:
				movement.velocity.setZero();
				break;
		}
			
		hitbox.mass.setLinearVelocity(movement.velocity);
		
		position.position = hitbox.mass.getPosition().cpy().sub(0.5f, 0.5f); // Center vs top-left
	}
}

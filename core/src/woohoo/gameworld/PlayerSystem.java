package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.gameobjects.components.PlayerComponent;
import woohoo.screens.PlayingScreen;

public class PlayerSystem extends IteratingSystem
{
	PlayingScreen screen;
	
	public PlayerSystem(PlayingScreen scr)
	{
		super(Family.all(PlayerComponent.class).get());
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) 
	{
		Mappers.players.get(entity).touchedItems.clear();
		screen.getInventoryManager().updateMoney(Mappers.players.get(entity).money);
	}	
}

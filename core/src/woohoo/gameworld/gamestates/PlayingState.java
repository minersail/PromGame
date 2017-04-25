package woohoo.gameworld.gamestates;

import woohoo.gameworld.GateSystem;
import woohoo.screens.PlayingScreen;

public class PlayingState implements GameState
{
	@Override
	public void enter(PlayingScreen screen)
	{
	}

	@Override
	public void update(PlayingScreen screen, float delta)
	{
		screen.getEngine().getSystem(GateSystem.class).updateArea(); // Only works outside of the engine update loop
		screen.getWorld().step(delta, 6, 2);
	}

	@Override
	public void exit(PlayingScreen screen)
	{
	}
}

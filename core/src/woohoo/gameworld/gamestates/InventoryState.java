package woohoo.gameworld.gamestates;

import woohoo.screens.PlayingScreen;

public class InventoryState implements GameState
{
	@Override
	public void enter(PlayingScreen screen)
	{
	}

	@Override
	public void update(PlayingScreen screen, float delta)
	{
		screen.getInventoryManager().act(delta);
	}

	@Override
	public void exit(PlayingScreen screen)
	{
		screen.getInventoryManager().closeInventory();
	}
}

package woohoo.gameworld.gamestates;

import woohoo.gameworld.ContactSystem;
import woohoo.screens.PlayingScreen;

public class DialogueState implements GameState
{
	@Override
	public void enter(PlayingScreen screen)
	{
		screen.getEngine().getSystem(ContactSystem.class).setProcessing(false);		
	}

	@Override
	public void update(PlayingScreen screen, float delta)
	{
	}

	@Override
	public void exit(PlayingScreen screen)
	{
		screen.getEngine().getSystem(ContactSystem.class).setProcessing(true);		
	}
}

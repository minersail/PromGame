package woohoo.gameworld.gamestates;

import woohoo.gameworld.ContactSystem;
import woohoo.screens.PlayingScreen;

public class CutsceneState implements GameState
{
	@Override
	public void enter(PlayingScreen screen)
	{
		screen.getEngine().getSystem(ContactSystem.class).setProcessing(false);
	}

	@Override
	public void update(PlayingScreen screen, float delta)
	{
		screen.getCutsceneManager().update(delta);
		screen.getWorld().step(delta, 6, 2);
	}

	@Override
	public void exit(PlayingScreen screen)
	{
		screen.getEngine().getSystem(ContactSystem.class).setProcessing(true);
		screen.getEngine().getSystem(ContactSystem.class).clearContacts();
	}
}

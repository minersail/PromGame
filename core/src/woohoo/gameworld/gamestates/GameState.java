package woohoo.gameworld.gamestates;

import woohoo.screens.PlayingScreen;

public interface GameState
{
	public void enter(PlayingScreen screen);
	public void update(PlayingScreen screen, float delta);
	public void exit(PlayingScreen screen);
}

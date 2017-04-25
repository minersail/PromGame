package woohoo.gameworld.gamestates;

import woohoo.screens.PlayingScreen;

public class QuestState implements GameState
{
	@Override
	public void enter(PlayingScreen screen)
	{
		screen.getQuestManager().showQuests();
	}

	@Override
	public void update(PlayingScreen screen, float delta)
	{
	}

	@Override
	public void exit(PlayingScreen screen)
	{
		screen.getQuestManager().closeQuests();
	}
}

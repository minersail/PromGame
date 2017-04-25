package woohoo.framework.input;

import com.badlogic.ashley.core.Entity;
import woohoo.gameworld.gamestates.DialogueState;
import woohoo.gameworld.gamestates.PlayingState;
import woohoo.screens.PlayingScreen;

/**
 * Used to be more complicated
 * @author jordan
 */
public class InventoryCloseCommand implements InputCommand
{
	private PlayingScreen screen;
	
	public InventoryCloseCommand(PlayingScreen scr)
	{
		screen = scr;
	}
	
	@Override
	public void execute(Entity player)
	{
		if (screen.getInventoryManager().openedFromDialogue())
		{
			screen.setState(new DialogueState());
			screen.getDialogueManager().advanceDialogue();
			screen.getDialogueManager().toggleUI(true);
		}
		else
		{
			screen.setState(new PlayingState());
		}
	}
}

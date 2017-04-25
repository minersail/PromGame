package woohoo.framework.events;

import woohoo.framework.CutsceneManager;

public class CutsceneTrigger implements EventTrigger<CutsceneManager>
{
	@Override
	public boolean check(CutsceneManager cutsceneManager)
	{
		return cutsceneManager.getActionsLeft() == 0;
	}
}

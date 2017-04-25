package woohoo.framework.events;

import woohoo.framework.CutsceneManager;

public class CutsceneEvent implements Event
{
	private CutsceneManager cutscenes;
	private int cutsceneID;
	
	public CutsceneEvent(CutsceneManager cutsceneManager, int id)
	{
		cutscenes = cutsceneManager;
		cutsceneID = id;
	}
	
	@Override
	public void activate()
	{
		cutscenes.startCutscene(cutsceneID);
	}
}

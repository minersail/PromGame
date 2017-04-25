package woohoo.framework.events;

import woohoo.framework.QuestManager;
import woohoo.framework.quests.Quest.QuestState;

public class QuestEvent implements Event
{
	int questID;
	String action;
	QuestManager manager;
	
	public final String START = "start";
	public final String END = "end";
	public final String DISCOVER = "discover";
	
	public QuestEvent(int id, String st, QuestManager qm)
	{
		questID = id;
		manager = qm;
		action = st;		
	}
	
	@Override
	public void activate()
	{
		switch(action)
		{
			case START:
				manager.getQuest(questID).setState(QuestState.Current);
				manager.startQuest(questID);
				break;
			case END:
				manager.getQuest(questID).setState(QuestState.Completed);	
				manager.endQuest(questID);
				break;
			case DISCOVER:
				manager.getQuest(questID).setState(QuestState.Discovered);
				manager.discoverQuest(questID);
				break;
		}
	}
}

package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.framework.quests.Quest;
import woohoo.framework.quests.Quest.QuestState;
import woohoo.gameworld.Mappers;
import woohoo.gameworld.RenderSystem;
import woohoo.screens.PlayingScreen;

public class QuestManager
{
	private IntMap<QuestUI> quests;
	private PlayingScreen screen;
	
	private Label header;
	private Table paneTable;
	private ScrollPane pane;
	
	public QuestManager(PlayingScreen scr, Skin skin)
	{
		quests = new IntMap<>();
		header = new Label("Quests", skin);
		paneTable = new Table();
		pane = new ScrollPane(paneTable);
		
		screen = scr;
		
		// All quests are loaded hidden
		FileHandle handle = Gdx.files.local("data/quests.xml");
        
        XmlReader xml = new XmlReader();
        Element root = xml.parse(handle.readString());
        
        for (Element questEl : root.getChildrenByName("quest"))
        {		
			Quest quest = new Quest(questEl.getInt("id"), questEl.get("description"), screen.getAssets().get("ui/quests/" + questEl.get("type") + "quest.png", Texture.class));
			
			quest.setState(QuestState.Current);
			Mappers.positions.get(quest.getIndicator()).position.set(questEl.getFloat("locX"), questEl.getFloat("locY"));
			
			QuestUI line = new QuestUI(quest, skin);
			quests.put(quest.getID(), line);
		}
		
		header.setSize(Gdx.graphics.getWidth(), 100);
		header.setPosition(0, Gdx.graphics.getHeight() - 100);
		header.setAlignment(Align.center);
		paneTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		pane.setSize(Gdx.graphics.getWidth(), 200);
		pane.setPosition(0, Gdx.graphics.getHeight() - 300);
	}
	
	public void discoverQuest(int id)
	{
		paneTable.add(quests.get(id)).prefSize(Gdx.graphics.getWidth(), 100);
		paneTable.row();
		
		quests.get(id).setQuestIcon("discovered");
	}
	
	// Adds indicator, creates popup
	public void startQuest(int id)
	{
		screen.getEngine().addEntity(quests.get(id).getQuest().getIndicator());
		screen.getAlertManager().alert("quest", quests.get(id).getQuest().getDescription());
		
		quests.get(id).setQuestIcon("current");
	}
	
	// Removes indicator
	public void endQuest(int id)
	{
		screen.getEngine().getSystem(RenderSystem.class).getRenderer().getMap().getLayers().get("Entities").getObjects().remove(Mappers.mapObjects.get(quests.get(id).getQuest().getIndicator()));
		screen.getEngine().removeEntity(quests.get(id).getQuest().getIndicator());
		
		quests.get(id).setQuestIcon("completed");
	}
	
	public void showQuests()
	{
		screen.getUI().addActor(pane);
		screen.getUI().addActor(header);
	}
	
	public void closeQuests()
	{
		pane.remove();
		header.remove();
	}
	
	public Quest getQuest(int id)
	{
		return quests.get(id).getQuest();
	}
	
	public class QuestUI extends HorizontalGroup
	{
		private Label description;
		private Stack imageStack;
		private ImageButton imageBG;
		private Image image;
		private Quest quest;
		
		public QuestUI(Quest q, Skin skin)
		{
			quest = q;
			description = new Label(quest.getDescription(), skin);
			description.setSize(Gdx.graphics.getWidth() - 100, 100);
			description.setX(100);
			description.setAlignment(Align.center);
			description.layout();
			
			imageBG = new ImageButton(skin);
			imageBG.setSize(100, 100);
			
			image = new Image();
			image.setSize(100, 100);
			
			imageStack = new Stack(imageBG, image);
						
			super.addActor(imageStack);
			super.addActor(description);
			super.setLayoutEnabled(false);
		}
		
		public Quest getQuest()
		{
			return quest;
		}
		
		public void setQuestIcon(String icon)
		{
			image.setDrawable(new TextureRegionDrawable(new TextureRegion(screen.getAssets().get("ui/quests/" + icon + ".png", Texture.class))));
			image.layout();
		}
	}
}

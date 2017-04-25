package woohoo.framework;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameworld.Mappers;
import woohoo.gameworld.gamestates.CutsceneState;
import woohoo.gameworld.gamestates.DialogueState;
import woohoo.gameworld.gamestates.GameState;
import woohoo.gameworld.gamestates.PlayingState;
import woohoo.screens.PlayingScreen;

public class DialogueManager
{
	private PlayingScreen screen;
	private Skin skin;
	private Entity dialogueEntity;
    private DialogueComponent currentDialogue;
    private Image face;
	private Label message;
	private Label name;
	private Array<TextButton> choices;
	
	private final int MARGIN = 100;
	private final int NAMEWIDTH = 100;
	private final int NAMEHEIGHT = 100;
    
    public DialogueManager(PlayingScreen scr, Skin sk)
    {
		screen = scr;
		skin = sk;
				
		message = new Label("", skin);
		message.setSize(Gdx.graphics.getWidth() - MARGIN * 2 - NAMEWIDTH, NAMEHEIGHT);
		message.setPosition(MARGIN + NAMEWIDTH, 0);
		message.setAlignment(Align.center);
		message.setWrap(true);
		message.setFontScale(0.75f);
		
		name = new Label("", skin);
		name.setSize(NAMEWIDTH, NAMEHEIGHT);
		name.setPosition(MARGIN, 0);
		name.setAlignment(Align.bottom);
		name.setWrap(true);	
		name.setFontScale(0.4f);
		
		face = new Image();
		face.setSize(64, 64);
		face.setPosition(MARGIN + 18, 30);
		face.setAlign(Align.center);
		
		choices = new Array<>();
    }
    
	/*
	Dialogue was started by entity
	*/
    public void startDialogue(Entity entity)
    {
		dialogueEntity = entity;
		startDialogue(Mappers.dialogues.get(entity));
    }
	
	/*
	Dialogue was not started by entity, but rather by cutscene manager
	*/
	public void startDialogue(DialogueComponent component)
	{
		currentDialogue = component;
		
		// Use the id of the dialogue line to map to a face using the id manager
		TextureRegionDrawable faceRegion = new TextureRegionDrawable(screen.getIDManager().getCharacter(currentDialogue.getCurrentLine().id()).getFace());
		
        message.setText(currentDialogue.getCurrentLine().text());
		name.setText(currentDialogue.getCurrentLine().name());
		face.setDrawable(faceRegion);
		
		screen.getUI().addActor(message);
		screen.getUI().addActor(name);
		screen.getUI().addActor(face);
		
		screen.setState(new DialogueState());
	}
	
	public void advanceDialogue()
	{
		// Disable space bar while choices are up
		if (choices.size > 0) return;
		
		currentDialogue.advance();
		if (currentDialogue.getCurrentLine() == null)
		{
			endDialogue(new PlayingState());
			return;
		}
		
		if (currentDialogue.getCurrentLine().id() == -1)
		{
			if (currentDialogue.getCurrentLine().name().equals("Choice"))
			{
				currentDialogue.advanceToChoiceEnd();
			}
			
			switch (currentDialogue.getCurrentLine().text())
			{
				case "LOOP":
					currentDialogue.loop();
					endDialogue(new PlayingState());
					break;
				case "BREAK":
					currentDialogue.advance();
					endDialogue(new PlayingState());
					break;
				case "CUTSCENE":
					currentDialogue.advance();
					endDialogue(new CutsceneState());
					break;
				case "CHOICE":
					startChoices();
					break;
				case "ENDCHOICE":
					advanceDialogue();
					return; // Return instead of break so that message/face don't crash the program
				case "INVENTORY":
					if (dialogueEntity != null)
						screen.getInventoryManager().showInventory(Mappers.inventories.get(dialogueEntity));
					toggleUI(false);
					return;
				default:
					break;
			}
			return;
		}
		
		TextureRegionDrawable faceRegion = new TextureRegionDrawable(screen.getIDManager().getCharacter(currentDialogue.getCurrentLine().id()).getFace());
		
		message.setText(currentDialogue.getCurrentLine().text());
		name.setText(currentDialogue.getCurrentLine().name());	
		face.setDrawable(faceRegion);
	}
	
	private void startChoices()
	{
		int choiceNum = currentDialogue.getCurrentLine().choices();
		Array<String> choiceStrings = currentDialogue.getChoices();
		
		for (int i = 0; i < currentDialogue.getCurrentLine().choices(); i++)
		{
			final TextButton choice = new TextButton("", skin);
			choice.setSize(message.getWidth() / choiceNum, message.getHeight());
			choice.setOrigin(Align.center);
			choice.getLabel().setFontScale(0.6f);
			choice.getLabel().setWrap(true);
			choice.setPosition(message.getX() + i * choice.getWidth(), message.getY());
			choice.setText(choiceStrings.get(i));
			
			choice.addListener(new ClickListener()
			{
				private int choice;
				
				@Override
				public void clicked(InputEvent event, float x, float y)
				{
					currentDialogue.advanceChoice(choice);
					endChoices();
				}
				
				public ClickListener initialize(int id)
				{
					choice = id;
					return this;
				}
			}.initialize(i));
			
			screen.getUI().addActor(choice);
			choices.add(choice);
		}
	}
	
	private void endChoices()
	{
		for (TextButton choiceButton : choices)
		{
			choiceButton.remove();
		}

		choices.clear();
		advanceDialogue();
	}
	
	public void endDialogue(GameState newState)
	{
		message.remove();
		name.remove();
		face.remove();
		
		screen.setState(newState);
		dialogueEntity = null;
	}
	
	/**
	 * Should only be used if ui should disappear mid-dialogue (such as opening an inventory)
	 * Otherwise use endDialogue()
	 * @param visible whether to make them visible or not
	 */
	public void toggleUI(boolean visible)
	{
		message.setVisible(visible);
		name.setVisible(visible);
		face.setVisible(visible);
	}
	
	public DialogueComponent getCurrentDialogue()
	{
		return currentDialogue;
	}
}

package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import java.util.Iterator;
import woohoo.framework.input.*;
import woohoo.gameobjects.components.InputComponent;
import woohoo.gameworld.gamestates.*;
import woohoo.screens.PlayingScreen;

public class InputSystem extends IteratingSystem implements InputProcessor
{
	PlayingScreen screen;
	
	public InputSystem(PlayingScreen scr)
	{
		super(Family.all(InputComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		for (Iterator<InputCommand> it = Mappers.inputs.get(entity).commands.iterator(); it.hasNext();)
		{
			InputCommand command = it.next();
			command.execute(entity);
			it.remove(); // Commands are only executed once
		}
		
		for (InputState state : Mappers.inputs.get(entity).states)
		{
			state.execute(entity);
		}
	}
	
	public void addCommand(InputCommand command)
	{
		for (Entity entity : getEntities())
		{
			Mappers.inputs.get(entity).commands.add(command);
		}
	}
	
	public void addState(InputState state)
	{
		for (Entity entity : getEntities())
		{
			Mappers.inputs.get(entity).states.add(state);
		}
	}
	
	/*
	States will not be automatically removed
	*/
	public void removeState(InputState state)
	{
		for (Entity entity : getEntities())
		{
			for (Iterator<InputState> it = Mappers.inputs.get(entity).states.iterator(); it.hasNext();)
			{
				InputState inputState = it.next();
				
				if (inputState.getClass() == state.getClass()) // Compare equality by class (All MoveUpStates should be inherently equal)
				{
					it.remove();
				}
			}
		}
	}

	@Override
	public boolean keyDown(int keycode)
	{
		if (screen.getState() instanceof PlayingState)
		{
			switch (keycode) 
			{            
				case Input.Keys.UP:
					addState(new MoveUpState());
					break;
				case Input.Keys.DOWN:
					addState(new MoveDownState());
					break;
				case Input.Keys.LEFT:
					addState(new MoveLeftState());
					break;
				case Input.Keys.RIGHT:
					addState(new MoveRightState());
					break;
				case Input.Keys.SPACE:
					addCommand(new PickupItemCommand(screen.getInventoryManager(), screen.getEngine()));
					addCommand(new NPCTalkCommand(screen.getDialogueManager(), screen.getEngine()));
					break;
				case Input.Keys.ESCAPE:
					addCommand(new InventoryOpenCommand(screen.getInventoryManager()));
					break;
				case Input.Keys.F1:
					screen.setState(new QuestState());
					break;
				case Input.Keys.S:
					addCommand(new PrintPosCommand());
					break;
				case Input.Keys.A:
					addCommand(new PlayerAttackCommand());
					break;
			}
		}
		else if (screen.getState() instanceof DialogueState)
		{
			switch (keycode)
			{
				case Input.Keys.SPACE:
					screen.getDialogueManager().advanceDialogue();
					break;
			}
		}
		else if (screen.getState() instanceof InventoryState)
		{
			switch (keycode)
			{
				case Input.Keys.ESCAPE:
					addCommand(new InventoryCloseCommand(screen));
					break;
			}
		}
		else if (screen.getState() instanceof QuestState)
		{
			switch (keycode)
			{
				case Input.Keys.F1:
					screen.setState(new PlayingState());
					break;
			}
		}
		else if (screen.getState() instanceof CutsceneState)
		{
			switch(keycode)
			{
				
			}
		}
        
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
		if (screen.getState() instanceof PlayingState)
		{
			switch (keycode) {
				case Input.Keys.UP:
					removeState(new MoveUpState());
					break;
				case Input.Keys.DOWN:
					removeState(new MoveDownState());
					break;
				case Input.Keys.LEFT:
					removeState(new MoveLeftState());
					break;
				case Input.Keys.RIGHT:
					removeState(new MoveRightState());
					break;
			}
		}
		else if (screen.getState() instanceof DialogueState)
		{
			switch(keycode)
			{

			}
		}
		
		if (!Gdx.input.isKeyPressed(Input.Keys.UP) && !Gdx.input.isKeyPressed(Input.Keys.DOWN) && 
			!Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			addCommand(new StopCommand());
		}
        
		return false;
	}

	@Override
	public boolean keyTyped(char character)
	{
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer)
	{
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY)
	{
		return false;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}
}

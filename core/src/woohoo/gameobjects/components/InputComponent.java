package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import woohoo.framework.input.InputCommand;
import woohoo.framework.input.InputState;

public class InputComponent implements Component
{
	public InputComponent()
	{
		states = new Array<>();
		commands = new Array<>();
	}
	
	public Array<InputState> states;
	public Array<InputCommand> commands;
}

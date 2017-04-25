package woohoo.screens;

import com.badlogic.gdx.graphics.Color;

public class ScreenFader
{
	private float fadeSpeed;
	private Color targetFade;
	private Color currentFade;
	private Fadeable fadeScreen;	
	
	public void startFade(Fadeable screen, Color start, Color target, float speed)
	{
		fadeScreen = screen;
		targetFade = target.cpy();
		currentFade = start.cpy();
		fadeSpeed = speed;		
		
		screen.getBatch().setColor(start);
	}
	
	public boolean isFading()
	{
		return fadeScreen != null;
	}
	
	public void fadeScreen()
	{
		if (!isFading()) return;
				
		currentFade.add(fadeSpeed, fadeSpeed, fadeSpeed, 0);
		fadeScreen.getBatch().setColor(currentFade);
		
		if (currentFade.equals(targetFade))
		{
			fadeSpeed = 0;
			targetFade = null;
			currentFade = null;
			fadeScreen = null;
		}
	}
}

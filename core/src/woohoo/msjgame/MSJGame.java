package woohoo.msjgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import woohoo.gameworld.InputSystem;
import woohoo.screens.GameOverScreen;
import woohoo.screens.NarrationScreen;
import woohoo.screens.PlayingScreen;
import woohoo.screens.ScreenFader;
import woohoo.screens.SplashScreen;

public class MSJGame extends Game
{	
	private PlayingScreen playingScreen;
	private SplashScreen splashScreen;
	private GameOverScreen gameOverScreen;
	private NarrationScreen narrationScreen;
	
	private ScreenFader fader;
	
	@Override
	public void create()
	{
		resetData();
		playingScreen = new PlayingScreen(this);
		splashScreen = new SplashScreen(this);
		gameOverScreen = new GameOverScreen(this);
		narrationScreen = new NarrationScreen(this);
		
		fader = new ScreenFader();
		
		setScreen(splashScreen);
		Gdx.input.setInputProcessor(splashScreen);
	}

	@Override
	public void render()
	{
		fader.fadeScreen();
		super.render();
	}
	
	public void switchToPlay()
	{		
        Gdx.input.setInputProcessor(new InputMultiplexer(playingScreen.getUI(), playingScreen.getEngine().getSystem(InputSystem.class)));
		setScreen(playingScreen);
		playingScreen.initialize(playingScreen.currentArea);
	}
	
	public void switchToNarration(int id)
	{
		Gdx.input.setInputProcessor(narrationScreen);
		setScreen(narrationScreen);
		narrationScreen.showNarration(id);
	}
	
	public PlayingScreen getPlayingScreen()
	{
		return playingScreen;
	}
	
	public SplashScreen getSplashScreen()
	{
		return splashScreen;
	}
	
	public GameOverScreen getGameOverScreen()
	{
		return gameOverScreen;
	}
	
	public NarrationScreen getNarrationScreen()
	{
		return narrationScreen;
	}
	
	public ScreenFader getFader()
	{
		return fader;
	}
	
	public void resetData()
	{
		FileHandle raw = Gdx.files.internal("raw/data");
		
		for (FileHandle handle : raw.list())
		{
			handle.copyTo(Gdx.files.local("data/" + handle.name()));
		}
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
	}
}

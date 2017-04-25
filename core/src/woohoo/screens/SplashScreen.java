package woohoo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import woohoo.msjgame.MSJGame;

public class SplashScreen implements Screen, InputProcessor, Fadeable
{
	private final MSJGame game;
	
	private final SpriteBatch batcher;
	private final Texture background;
	
	public SplashScreen(MSJGame g)
	{
		game = g;
		
		batcher = new SpriteBatch();
		batcher.enableBlending();
		background = new Texture("images/screens/title.png");
	}
	
	@Override
	public void render(float delta)
	{				
		batcher.begin();
		batcher.draw(background, 0, 0);
		batcher.end();
		
		// Key has been pressed and fade has completed
		if (Gdx.input.getInputProcessor() == null && !game.getFader().isFading())
		{
			game.switchToPlay();
		}
	}

	@Override
	public Batch getBatch()
	{
		return batcher;
	}
	
	@Override
    public void resize(int width, int height)
    {
        System.out.println("MenuScreen - resize called");
    }

    @Override
    public void show()
    {
        System.out.println("MenuScreen - show called");
    }

    @Override
    public void hide()
    {
        System.out.println("MenuScreen - hide called");
    }

    @Override
    public void pause()
    {
        System.out.println("MenuScreen - pause called");
    }

    @Override
    public void resume()
    {
        System.out.println("MenuScreen - resume called");
    }

    @Override
    public void dispose()
    {
    }

	@Override
	public boolean keyDown(int keycode)
	{
		game.getFader().startFade(this, Color.WHITE, Color.BLACK, -0.02f);
		Gdx.input.setInputProcessor(null);
		return false;
	}

	@Override
	public boolean keyUp(int keycode)
	{
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

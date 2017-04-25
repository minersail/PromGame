package woohoo.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import woohoo.msjgame.MSJGame;

public class GameOverScreen implements Screen, Fadeable
{
	private final MSJGame game;
	
	private final SpriteBatch batcher;
	private final Texture background;
	
	public GameOverScreen(MSJGame g)
	{
		game = g;
		
		batcher = new SpriteBatch();
		batcher.enableBlending();
		background = new Texture("images/screens/gameover.png");
	}
	
	@Override
	public void render(float delta)
	{		
		batcher.begin();
		batcher.draw(background, 0, 0);
		batcher.end();
	}

	@Override
	public Batch getBatch()
	{
		return batcher;
	}
	
	@Override
	public void show() 
	{	
		game.getFader().startFade(this, Color.BLACK, Color.WHITE, 0.005f);
	}

	@Override
	public void resize(int width, int height) 
	{		
	}

	@Override
	public void pause() 
	{	
	}

	@Override
	public void resume() 
	{		
	}

	@Override
	public void hide() 
	{		
	}

	@Override
	public void dispose() 
	{		
	}
}
package woohoo.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import woohoo.msjgame.MSJGame;

public class NarrationScreen implements Screen, InputProcessor, Fadeable
{
	private final MSJGame game;
	
	private final Stage stage;
	private final Label text;
	private final Image background;
	
	private final Array<String> narration;
	private final int MARGIN_X = 100;
	private int narrIndex;
	
	public NarrationScreen(MSJGame g)
	{
		game = g;
		
		narration = new Array<>();
		narrIndex = 0;
				
		// Initialize UI
		Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		stage = new Stage();
		stage.getBatch().enableBlending();
		background = new Image(new Texture("images/screens/narration.png"));
		text = new Label("", new LabelStyle(skin.getFont("text"), Color.WHITE));
		text.setAlignment(Align.center);
		text.setWrap(true);
		text.setSize(Gdx.graphics.getWidth() - 2 * MARGIN_X, Gdx.graphics.getHeight());
		text.setX(MARGIN_X);
		
		stage.addActor(background);
		stage.addActor(text);
	}
	
	public void showNarration(int sceneID)
	{		
		XmlReader reader = new XmlReader();
		Element root = reader.parse(Gdx.files.internal("data/narration.xml").readString());
		Element scene = root.getChild(sceneID);
		
		for (Element line : scene.getChildrenByName("line"))
		{
			narration.add(line.get("text"));
		}
		
		narrIndex = 0;
		text.setText(narration.get(narrIndex));
	}
	
	public void endNarration()
	{
		narration.clear();
		game.switchToPlay();
	}
	
	@Override
	public void render(float delta)
	{				
		// When the outro fade is complete switch text and start intro fade
		if (Gdx.input.getInputProcessor() == null && !game.getFader().isFading())
		{
			game.getFader().startFade(this, Color.BLACK, Color.WHITE, 0.01f);
			Gdx.input.setInputProcessor(this);
			advanceNarration();
		}
		
		text.setColor(stage.getBatch().getColor());
		stage.act();
		stage.draw();
	}
	
	private void advanceNarration()
	{
		narrIndex++;
		
		if (narrIndex >= narration.size)
			endNarration();
		else
			text.setText(narration.get(narrIndex));	
	}

	@Override
	public Batch getBatch()
	{
		return stage.getBatch();
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

	@Override
	public boolean keyDown(int keycode)
	{
		game.getFader().startFade(this, Color.WHITE, Color.BLACK, -0.01f);
		Gdx.input.setInputProcessor(null); // Disconnect input
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

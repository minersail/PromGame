package woohoo.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import woohoo.screens.PlayingScreen;

public class AlertManager
{
	private PlayingScreen screen;
	
	private Label label;
	private ImageButton imageBG;
	private Image image;
	
	private float fadeTime;
	private float fadeSpeed;
	
	public AlertManager(PlayingScreen scr, Skin skin)
	{
		screen = scr;
		fadeTime = 0;
		fadeSpeed = 0.01f;
		
		label = new Label("", skin);
		label.setSize(400, 100);
		label.setPosition(150, Gdx.graphics.getHeight() - 150);
		label.setAlignment(Align.center);
		label.setWrap(true);
		label.setFontScale(0.6f);
		
		imageBG = new ImageButton(skin);
		imageBG.setSize(100, 100);
		imageBG.setPosition(50, Gdx.graphics.getHeight() - 150);
		
		image = new Image();
		image.setSize(100, 100);
		image.setPosition(50, Gdx.graphics.getHeight() - 150);
		image.setTouchable(Touchable.disabled);
		
		imageBG.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				fadeTime = 0;
				fadeSpeed = 0.05f;
			}
		});
	}
	
	public void act(float delta)
	{
		if (fadeTime <= 0)
		{
			if (label.getColor().a <= 0) return;
			
			label.setColor(label.getColor().sub(0, 0, 0, fadeSpeed));
			imageBG.setColor(imageBG.getColor().sub(0, 0, 0, fadeSpeed));
			image.setColor(image.getColor().sub(0, 0, 0, fadeSpeed));
		}
		
		fadeTime -= delta;
	}
	
	public void alert(String imageName, String message)
	{
		image.setDrawable(new TextureRegionDrawable(new TextureRegion(screen.getAssets().get("ui/alerts/" + imageName + ".png", Texture.class))));
		label.setText(message);
		
		screen.getUI().addActor(label);
		screen.getUI().addActor(imageBG);
		screen.getUI().addActor(image);
		
		fadeTime = 5;
		fadeSpeed = 0.01f;
		label.setColor(Color.WHITE);
		imageBG.setColor(Color.WHITE);
		image.setColor(Color.WHITE);
	}
}

package woohoo.gameobjects.components;

import woohoo.framework.animation.IdleAnimState;
import woohoo.framework.animation.AnimationState;
import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Vector2;
import java.util.HashMap;
import java.util.Map;

public class AnimMapObjectComponent extends TextureMapObject implements Component
{	
    private AnimationState animState;	
	private Map<String, Animation<TextureRegion>> animation;	
	
	public String animString;	
	public float animationTime = 0;
	
	public Vector2 size;
	
	public AnimMapObjectComponent(TextureAtlas atlas)
    {
		animation = new HashMap<>();
		
		addAnimation("left_walk", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("right_walk", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("up_walk", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("down_walk", atlas, 0.166f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("left_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("right_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("up_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("down_idle", atlas, 0.5f, Animation.PlayMode.LOOP_PINGPONG);
		addAnimation("left_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("right_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("up_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("down_fight", atlas, 0.166f, Animation.PlayMode.NORMAL);
		addAnimation("death", atlas, 0.5f, Animation.PlayMode.NORMAL);
		
		size = new Vector2(1, 1);
		animationTime = 0;
		animString = "down_idle";
		setAnimationState(new IdleAnimState());
	}
	
	public final void setAnimationState(AnimationState state)
	{
		animState = state;
		animState.enter(this);
	}
	
	public AnimationState getAnimationState()
	{
		return animState;
	}
	
	public void addTime(float time)
	{
		animationTime += time;
	}
		
	public Animation<TextureRegion> getAnimation(String str)
	{
		return animation.get(str);
	}
	
	private void addAnimation(String name, TextureAtlas atlas, float frameTime, Animation.PlayMode mode)
	{
		if (atlas.findRegions(name).size > 0) // Animations are not missing
			animation.put(name, new Animation<TextureRegion>(frameTime, atlas.findRegions(name), mode));
	}
}

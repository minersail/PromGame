package woohoo.gameworld;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import woohoo.framework.animation.DeathAnimState;
import woohoo.gameobjects.components.DialogueComponent;
import woohoo.gameobjects.components.HealthBarComponent;
import woohoo.gameobjects.components.HealthComponent;
import woohoo.gameobjects.components.HitboxComponent;
import woohoo.screens.PlayingScreen;

public class DamageSystem extends IteratingSystem
{
	private PlayingScreen screen;
	
	public DamageSystem(PlayingScreen scr)
	{
		super(Family.all(HealthComponent.class).get());
		
		screen = scr;
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime)
	{
		HealthComponent life = Mappers.lives.get(entity);
		
		life.damageCooldown = Math.max(0, life.damageCooldown - deltaTime);		
		
		life.currentHealth = Math.max(0, life.currentHealth - life.getIncomingDamage());
		life.resetDamage();
		
		if (Mappers.healthBars.has(entity))
		{
			HealthBarComponent healthBar = Mappers.healthBars.get(entity);
			healthBar.percentLeft = life.currentHealth / life.maxHealth;
		}
			
		if (life.currentHealth <= 0 && !life.dead) // Only run kill code once
			kill(entity);
	}
	
	private void kill(Entity entity)
	{		
		if (Mappers.animMapObjects.has(entity))
		{
			if (Mappers.animMapObjects.get(entity).getAnimation("death") != null)
				Mappers.animMapObjects.get(entity).setAnimationState(new DeathAnimState());
		}
		
		if (Mappers.hitboxes.has(entity))
		{
			screen.getWorld().destroyBody(Mappers.hitboxes.get(entity).mass);
			entity.remove(HitboxComponent.class);
		}
		
		entity.remove(DialogueComponent.class);
		Mappers.lives.get(entity).dead = true;
		
		if (Mappers.players.has(entity))
		{
			screen.gameOver();
		}
	}
}

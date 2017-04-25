package woohoo.gameobjects.components;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component
{
	public float maxHealth;
	public float currentHealth;
	public boolean invulnerable;
	public boolean dead;
	
	public float damageCooldown; // Timer so that entities only take damage once every X seconds
	
	private float incomingDamage; // Running total of all damage entity has taken this game tick
	
	public HealthComponent()
	{
		this(10);
	}	
	
	public HealthComponent(float max)
	{
		currentHealth = maxHealth = max;
		invulnerable = false;
	}	
    
    public void damage(float damage)
    {
        if (!invulnerable && damageCooldown == 0)
		{
			incomingDamage += damage;
			damageCooldown = 0.5f;
		}
    }
	
	public float getIncomingDamage()
	{
		return incomingDamage;
	}
	
	public void resetDamage()
	{
		incomingDamage = 0;
	}
	
	public void kill()
	{
		currentHealth = 0;
	}
}

package woohoo.gameworld;

import com.badlogic.ashley.core.ComponentMapper;
import woohoo.gameobjects.components.*;

public class Mappers 
{
	public static ComponentMapper<EventListenerComponent> eventListeners = ComponentMapper.getFor(EventListenerComponent.class);
	public static ComponentMapper<AnimMapObjectComponent> animMapObjects = ComponentMapper.getFor(AnimMapObjectComponent.class);
	public static ComponentMapper<InventoryComponent> inventories = ComponentMapper.getFor(InventoryComponent.class);
	public static ComponentMapper<MapObjectComponent> mapObjects = ComponentMapper.getFor(MapObjectComponent.class);
	public static ComponentMapper<HealthBarComponent> healthBars = ComponentMapper.getFor(HealthBarComponent.class);
	public static ComponentMapper<PositionComponent> positions = ComponentMapper.getFor(PositionComponent.class);
	public static ComponentMapper<MovementComponent> movements = ComponentMapper.getFor(MovementComponent.class);
	public static ComponentMapper<DialogueComponent> dialogues = ComponentMapper.getFor(DialogueComponent.class);
	public static ComponentMapper<OpacityComponent> opacities = ComponentMapper.getFor(OpacityComponent.class);
	public static ComponentMapper<ContactComponent> contacts = ComponentMapper.getFor(ContactComponent.class);
	public static ComponentMapper<ItemDataComponent> items = ComponentMapper.getFor(ItemDataComponent.class);
	public static ComponentMapper<HitboxComponent> hitboxes = ComponentMapper.getFor(HitboxComponent.class);
	public static ComponentMapper<WeaponComponent> weapons = ComponentMapper.getFor(WeaponComponent.class);
	public static ComponentMapper<PlayerComponent> players = ComponentMapper.getFor(PlayerComponent.class);
	public static ComponentMapper<HealthComponent> lives = ComponentMapper.getFor(HealthComponent.class);
	public static ComponentMapper<ChaseComponent> chasers = ComponentMapper.getFor(ChaseComponent.class);
	public static ComponentMapper<LOSComponent> sightLines = ComponentMapper.getFor(LOSComponent.class);
	public static ComponentMapper<InputComponent> inputs = ComponentMapper.getFor(InputComponent.class);
	public static ComponentMapper<GateComponent> gates = ComponentMapper.getFor(GateComponent.class);
	public static ComponentMapper<IDComponent> ids = ComponentMapper.getFor(IDComponent.class);
	public static ComponentMapper<AIComponent> ai = ComponentMapper.getFor(AIComponent.class);
}

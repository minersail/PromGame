package woohoo.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import woohoo.framework.InventoryManager;
import woohoo.gameworld.Mappers;

/**
* Stores the image data for a single slot in the inventory UI
* 
* Setters return this item for method chaining
*/
public class InventorySlot extends Image
{
	public enum SlotType
	{
		Player, Other, Weapon
	};
	
	private TextureRegion background; // Reference to background
	
	private InventoryTooltip tooltip;	
	private Entity item; // Item entity, starts as null
	private Image itemImage; // Scene2D actor used for payload
	
	private boolean dragged;
	private int count;

	private SlotType type;

	public InventorySlot(TextureRegion border, TextureRegion backgroundRegion, Skin skin, Stage ui)
	{
		super(border);
		background = backgroundRegion;
		
		itemImage = new Image(backgroundRegion);
		itemImage.setSize(InventoryManager.ITEMX, InventoryManager.ITEMY);

		tooltip = new InventoryTooltip(skin);
		tooltip.setVisible(false);			
		ui.addListener(new InputListener()
		{
			@Override
			public boolean mouseMoved(InputEvent event, float x, float y)
			{
				Vector2 local = itemImage.stageToLocalCoordinates(new Vector2(x, y));
				boolean overTooltip = itemImage.hit(local.x, local.y, false) != null;

				if (item != null)
				{
					tooltip.setVisible(overTooltip);
					tooltip.toFront();
				}
				return false;
			}
		});
	}

	public Image getImage()
	{
		return itemImage;
	}

	public Entity getItem()
	{
		return item;
	}

	public InventorySlot setImage(Image image)
	{
		itemImage = image;			
		itemImage.setSize(InventoryManager.ITEMX, InventoryManager.ITEMY);
		return this;
	}

	public InventorySlot setItem(Entity entity)
	{		
		item = entity;
		if (entity != null)
		{
			tooltip.setTitle((String)Mappers.items.get(item).baseData.get("name"));
			tooltip.setDescription((String)Mappers.items.get(item).baseData.get("description"));
		}
		else
			tooltip.setVisible(false);
		return this;
	}

	public InventorySlot setDragged(boolean drag)
	{
		dragged = drag;			
		return this;
	}

	public InventorySlot setType(SlotType stype)
	{
		type = stype;
		return this;
	}

	public InventorySlot setCount(int newCount)
	{
		count = newCount;
		return this;
	}

	public InventorySlot empty()
	{
		this.setItem(null).setImage(new Image(background)).setCount(0);
		return this;
	}

	public boolean isDragged()
	{
		return dragged;
	}

	public SlotType getType()
	{
		return type;
	}

	public int getCount()
	{
		return count;
	}

	public InventoryTooltip getToolTip()
	{
		return tooltip;
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);            
		itemImage.draw(batch, parentAlpha);

		tooltip.setPosition(itemImage.getX() + getWidth(), itemImage.getY() - tooltip.getHeight());

		if (!dragged)
		{            
			itemImage.setPosition(getX() + InventoryManager.ITEMBORDERX / 2, getY() + InventoryManager.ITEMBORDERY / 2);   
		}
	}
}

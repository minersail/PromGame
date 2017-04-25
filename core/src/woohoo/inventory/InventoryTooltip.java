package woohoo.inventory;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

public class InventoryTooltip extends Window
{
	public InventoryTooltip(Skin skin)
	{
		super("", skin);
		super.setSize(300, 200);
		super.setMovable(false); // Will automatically move with item
		super.left();
		super.top();

		super.getTitleLabel().setFontScale(0.40f);

		Cell cell = super.add("", "text", "special"); // Adds blank label using font "text" and color "special" specified in the uiskin.json
		cell.prefWidth(280);
		cell.padLeft((300 - cell.getPrefWidth()) / 2);
		cell.padRight(cell.getPadLeft());
		cell.padTop(cell.getPadLeft());
		
		Label description = (Label)cell.getActor();	
		description.setFontScale(0.4f);
		description.setWrap(true);
		description.setWidth(280);
		description.setAlignment(Align.top);
	}

	public void setTitle(String message)
	{
		getTitleLabel().setText(message);
	}

	public void setDescription(String message)
	{
		((Label)super.getCells().get(0).getActor()).setText(message);
		((Label)super.getCells().get(0).getActor()).setAlignment(Align.left);
	}
}

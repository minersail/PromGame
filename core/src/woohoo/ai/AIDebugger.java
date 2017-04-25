package woohoo.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import woohoo.gameobjects.components.AIComponent;
import woohoo.gameobjects.components.PositionComponent;

public class AIDebugger
{
	ShapeRenderer renderer;
	
	public AIDebugger()
	{
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
	}
	
	public void renderConnections(Entity entity, Camera camera)
	{
		renderer.setProjectionMatrix(camera.combined);
		
		AIMap map = entity.getComponent(AIComponent.class).getAIMap();		
		Vector2 pos = entity.getComponent(PositionComponent.class).position;
		
		Node node = map.get(Math.round(pos.x), Math.round(pos.y));
		
		renderer.begin();
		
		renderer.setColor(Color.FIREBRICK);
		renderer.rect(pos.x, pos.y, 1, 1);
		
		renderer.setColor(Color.ROYAL);
		for (DefaultConnection connection : node.getConnections())
		{
			renderer.line(((Node)connection.getFromNode()).x + 0.5f, ((Node)connection.getFromNode()).y + 0.5f, 
						  ((Node)connection.getToNode()).x + 0.5f, ((Node)connection.getToNode()).y + 0.5f);
		}
		renderer.end();
	}
}

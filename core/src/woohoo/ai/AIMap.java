package woohoo.ai;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import java.util.ArrayList;

public class AIMap implements IndexedGraph<Node>
{
	private IntMap<Node> nodes;
	private int width;
	private int height;
	private int offsetRow;
	private int offsetCol;
	
	/**
	 * AIMap constructor creates the pathfinding grid: <br>
	 *	- Creates nodes for every 1x1 tile <br>
	 *  - Excludes wall tiles <br>
	 *  - Excludes a tiles that have an entity on top of them <br>
	 *  - Exceptions to entity exclusion are passed in as an array of CollisionComponents <br>
	 *  - Extra nodes can manually be added that are outside of the regular game map (e.g. (-1, 0)) <br>
	 *  - In order for the indexing algorithm to work properly, minimum and maximum rows and columns must
	 *	  be set (if the extra nodes are to work) <br>
	 * @param map to create the base grid
	 * @param world to obtain all entity fixtures obstructing the map (and thus nodes to be excluded)
	 * @param extraNodes list of extra nodes to manually added
	 * @param topRow minimum row (Note: these are additive, not definite: e.g. left row of 2 does not mean the largest row is 2, but rather that 2 is added onto the right row)
	 * @param botRow maximum row
	 * @param leftCol minimum column
	 * @param rightCol maximum column
	 */
	public AIMap(Map map, World world, ArrayList<Vector2> extraNodes, int topRow, int botRow, int leftCol, int rightCol)
	{
		nodes = new IntMap<>();
		
		TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get("Base");
		width = layer.getWidth() + leftCol + rightCol;
		height = layer.getHeight() + topRow + botRow;
		offsetRow = topRow;
		offsetCol = leftCol;
		
		for (Vector2 nodeXY : extraNodes)
		{
			int x = (int)nodeXY.x;
			int y = (int)nodeXY.y;
			
			Node node = new Node(x, y);
			nodes.put(index(x, y), node);
		}
		
		// Nodes are every 1x1 unit, except walls
		for (int j = 0; j < layer.getHeight(); j++)
		{
			for (int i = 0; i < layer.getWidth(); i++)
			{
				if (!layer.getCell(i, j).getTile().getProperties().get("isWall", Boolean.class))
				{					
					Node node = new Node(i, j);
					nodes.put(index(i, j), node);
				}				
				
				world.QueryAABB(new EntityQuery(nodes, i, j), i + 1, j + 1, i, j);
			}
		}
		
		for (Node node : nodes.values())
		{
			generateConnections(node);
		}
	}
	
	@Override
	public Array getConnections(Node fromNode)
	{
		return fromNode.getConnections();
	}
	
	final public void generateConnections(Node node)
	{
		int x = node.x;
		int y = node.y;
		
		if (get(x, y - 1) != null)
			node.createConnection(get(x, y - 1));
		if (get(x, y + 1) != null)
			node.createConnection(get(x, y + 1));
		if (get(x - 1, y) != null)
			node.createConnection(get(x - 1, y));
		if (get(x + 1, y) != null)
			node.createConnection(get(x + 1, y));
	}

	/*
	Used by the path finder
	
	The nodes are stored in an IntMap, with an index mapping to each node, where index is
	calculated as x + y * width. However, this index cannot be returned because getIndex()
	expects an integer between 0 and n, where n is the number of nodes.
	*/
	@Override
	public int getIndex(Node node)
	{
		return nodes.keys().toArray().indexOf(index(node.x, node.y));
	}
	
	/**
	 * Calculates an index given x and y coordinates
	 * @param x value of the node
	 * @param y value of the node
	 * @return index of the node
	 */
	private int index(int x, int y)
	{
		return (x + offsetCol) + (y + offsetRow) * width;
	}
	
	/**
	 * Returns null if no node exists at these coordinates
	 * @param x coordinate of desired node
	 * @param y coordinate of desired node
	 * @return
	 */
	public Node get(int x, int y)
	{
		return nodes.get(index(x, y));
	}
	
	public Node getFirst()
	{
		return nodes.values().toArray().get(0);
	}
	
	public Node getLast()
	{
		return nodes.values().toArray().get(nodes.size - 1);
	}

	@Override
	public int getNodeCount()
	{
		return nodes.size;
	}
	
	public Node getRandomNode()
	{
		// Random int between 0 and size
		int random = (int)Math.floor(Math.random() * nodes.size);
		
		// Use the random int to get an index from the keys, then retrieve a node based on that index
		return nodes.get(nodes.keys().toArray().items[random]);
	}
	
	public boolean contains(int index)
	{
		return nodes.containsKey(index);
	}
	
	/**
	 * Class created to check if any fixtures overlap a tile
	 */
	public class EntityQuery implements QueryCallback
	{
		IntMap<Node> nodeList;
		int x;
		int y;
		
		public EntityQuery(IntMap<Node> nodes, int X, int Y)
		{
			nodeList = nodes;
			x = X;
			y = Y;
		}
		
		@Override
		public boolean reportFixture(Fixture fixture)
		{				
			// Finds a fixture overlapping the tile, and that fixture has collsion activated
			if (!fixture.isSensor() && fixture.getBody().getType() == BodyType.StaticBody)
			{
				// Remove this as a possible node for the pathfinding graph
				nodeList.remove(index(x, y));
				return false; // Only need to check for one
			}
			
			return true; // Keep searching
		}
	}
}

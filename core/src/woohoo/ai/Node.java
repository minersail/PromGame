package woohoo.ai;

import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.utils.Array;

public class Node
{
	public int x;
	public int y;
	private Array<DefaultConnection> connections;
	
	public Node(int X, int Y)
	{
		x = X;
		y = Y;
		connections = new Array<>();
	}
		
	@Override 
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Node)) return false;
		
		return ((Node)obj).x == x && ((Node)obj).y == y;
	}
	
	public void createConnection(Node other)
	{
		connections.add(new DefaultConnection(this, other));
	}
	
	public Array<DefaultConnection> getConnections()
	{
		return connections;
	}

	// This was automatically generated
	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 29 * hash + this.x;
		hash = 29 * hash + this.y;
		return hash;
	}
	
	@Override
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}

package woohoo.ai;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class AIHeuristic implements Heuristic<Node>
{
	@Override
	public float estimate(Node start, Node end)
	{
		// Manhattan distance
		return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
	}
}

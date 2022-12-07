package travel_recommender.test;

import org.junit.Before;
import org.junit.Test;

import travel_recommender.control.DijkstraShortestPathAlg;
import travel_recommender.model.Graph;

public class ShortestPathAlgTest
{
	Graph graph = null;

	@Before
	public void setUp() throws Exception
	{
		// Import the graph from a file
		graph = new Graph("data/test_50");
	}

	@Test
	public void testShorstPathAlg()
	{
		System.out.println("Testing Dijkstra Algorithm.");
		DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(graph);
		System.out.println(alg.get_shortest_path(graph.get_vertex(0), graph.get_vertex(38),4));
	}
}

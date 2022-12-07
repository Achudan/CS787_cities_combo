package travel_recommender.test;

import java.util.List;

import org.junit.Test;

import travel_recommender.model.Graph;
import travel_recommender.model.Path;
import travel_recommender.model.VariableGraph;
import travel_recommender.control.DijkstraShortestPathAlg;
import travel_recommender.control.YenTopKShortestPathsAlg;

public class YenTopKShortestPathsAlgTest
{
	// The graph should be initiated only once to guarantee the correspondence 
	// between vertex id and node id in input text file. 
	static Graph graph = new VariableGraph("data/test_6_2");
	
	//@Test
	public void testDijkstraShortestPathAlg()
	{
		System.out.println("Testing Dijkstra Shortest Path Algorithm!");
		DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(graph);
		System.out.println(alg.get_shortest_path(graph.get_vertex(4), graph.get_vertex(5),1));
	}
	
	@Test
	public void testYenShortestPathsAlg()
	{		
		System.out.println("Testing batch processing of top-k shortest paths!");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
		List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
				graph.get_vertex(4), graph.get_vertex(5), 100, 3);
		System.out.println(":"+shortest_paths_list);
		System.out.println(yenAlg.get_result_list().size());	
	}
	
	//@Test
	public void testYenShortestPathsAlg2()
	{
		System.out.println("Obtain all paths in increasing order! - updated!");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(
				graph, graph.get_vertex(4), graph.get_vertex(5));
		int i=0;
		while(yenAlg.has_next())
		{
			System.out.println("Path "+i+++" : "+yenAlg.next());
		}
		
		System.out.println("Result # :"+i);
		System.out.println("Candidate # :"+yenAlg.get_cadidate_size());
		System.out.println("All generated : "+yenAlg.get_generated_path_size());
		
	}
	
}

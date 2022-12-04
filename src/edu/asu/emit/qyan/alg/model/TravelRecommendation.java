package edu.asu.emit.qyan.alg.model;

import java.util.List;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;

public class TravelRecommendation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = new VariableGraph("data/test_6_2");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
		List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
				graph.get_vertex(4), graph.get_vertex(5), 100, 3);
		System.out.println(":"+shortest_paths_list);
		System.out.println(yenAlg.get_result_list().size());
	}

}

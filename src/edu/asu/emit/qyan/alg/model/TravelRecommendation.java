package edu.asu.emit.qyan.alg.model;

import java.util.ArrayList;
import java.util.List;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;

public class TravelRecommendation {

	public static void main(String[] args) {
		Graph graph = new VariableGraph("data/mockData_v2_chk.csv");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
		List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
				graph.get_vertex(9), graph.get_vertex(11), 500, 4);

		for (Path path : shortest_paths_list) {

			ArrayList<String> routeList = new ArrayList<String>();
			for (BaseVertex vertex : path._vertex_list) {
				routeList.add(Graph.cityMapInverse.get(vertex.get_id()));
			}
			path.paths.add(routeList);
		}

		for (Path routes : shortest_paths_list) {
			for (List<String> rt : routes.paths) {
				int stop = 0;
				for (String place : rt) {
					if (stop == 0)
						System.out.println("source: " + place);
					else {
						if (stop == rt.size() - 1)
							System.out.println("destination: " + place);
						else
							System.out.println("Stop " + stop + ": " + place);
					}
					stop++;
				}
			}
			System.out.println("cost: " + routes._weight);
			System.out.println("---------------------------------------_----------------------------------\n");
		}
		System.out.println(":" + shortest_paths_list);
		System.out.println(yenAlg.get_result_list().size());
	}

}

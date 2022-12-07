package travel_recommender.model;

import java.util.ArrayList;
import java.util.List;

import travel_recommender.control.YenTopKShortestPathsAlg;
import travel_recommender.model.abstracts.BaseVertex;

public class TravelRecommendation {

	public static void main(String[] args) {
		Graph graph = new VariableGraph("data/mockData_v2_chk - Copy.csv");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
		List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
				graph.get_vertex(0), graph.get_vertex(1), 500, 4);

		for (int i = 0; i < shortest_paths_list.size(); i++) {
			Path path = shortest_paths_list.get(i);
			for (int j = 0; j < path._vertex_list.size() - 2; j++) {
				if (graph.get_edge_StartDate(path._vertex_list.get(j), path._vertex_list.get(j + 1))
						.compareTo(graph.get_edge_StartDate(path._vertex_list.get(j + 1),
								path._vertex_list.get(j + 2))) > 0) {
					shortest_paths_list.remove(i--);
				}
			}
		}

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

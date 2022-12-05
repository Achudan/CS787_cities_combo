package edu.asu.emit.qyan.alg.model;

import java.util.ArrayList;
import java.util.List;

import edu.asu.emit.qyan.alg.control.YenTopKShortestPathsAlg;
import edu.asu.emit.qyan.alg.model.abstracts.BaseVertex;

public class TravelRecommendation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Graph graph = new VariableGraph("data/mockData_v2_chk.csv");
		YenTopKShortestPathsAlg yenAlg = new YenTopKShortestPathsAlg(graph);
		List<Path> shortest_paths_list = yenAlg.get_shortest_paths(
				graph.get_vertex(4), graph.get_vertex(5), 500, 6);
		
//		System.out.println(":"+shortest_paths_list);
		for(Path path: shortest_paths_list) {
			
			ArrayList<String> routeList = new ArrayList<String>();
			for(BaseVertex vertex: path._vertex_list) {
				routeList.add(Graph.cityMapInverse.get(vertex.get_id()));
			}
			path.paths.add(routeList);
		}
		System.out.println(":"+shortest_paths_list);
		System.out.println(yenAlg.get_result_list().size());
	}

}

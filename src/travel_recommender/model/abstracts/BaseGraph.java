package travel_recommender.model.abstracts;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface BaseGraph
{
	List<BaseVertex> get_vertex_list();
	
	double get_edge_weight(BaseVertex source, BaseVertex sink);
	Date get_edge_StartDate(BaseVertex source, BaseVertex sink);
	double get_edge_Duration(BaseVertex source, BaseVertex sink);
	double get_edge_Cost(BaseVertex source, BaseVertex sink);
	Set<BaseVertex> get_adjacent_vertices(BaseVertex vertex);
	Set<BaseVertex> get_precedent_vertices(BaseVertex vertex);
}

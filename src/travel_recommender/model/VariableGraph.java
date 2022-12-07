package travel_recommender.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import travel_recommender.control.DijkstraShortestPathAlg;
import travel_recommender.model.abstracts.BaseVertex;


public class VariableGraph extends Graph
{
	Set<Integer> _rem_vertex_id_set = new HashSet<Integer>();
	Set<Pair<Integer, Integer>> _rem_edge_set = new HashSet<Pair<Integer, Integer>>();


	public VariableGraph(){};
	
	public VariableGraph(String data_file_name)
	{
		super(data_file_name);
	}

	public VariableGraph(Graph graph)
	{
		super(graph);
	}

	public void set_rem_vertex_id_list(Collection<Integer> _rem_vertex_list)
	{
		this._rem_vertex_id_set.addAll(_rem_vertex_list);
	}

	public void set_rem_edge_hashcode_set(Collection<Pair<Integer, Integer>> rem_edge_collection)
	{
		_rem_edge_set.addAll(rem_edge_collection);
	}

	public void remove_edge(Pair<Integer, Integer> edge)
	{
		_rem_edge_set.add(edge);
	}

	public void remove_vertex(Integer vertex_id)
	{
		_rem_vertex_id_set.add(vertex_id);
	}
	
	public void recover_removed_edges()
	{
		_rem_edge_set.clear();
	}

	public void recover_removed_edge(Pair<Integer, Integer> edge)
	{
		_rem_edge_set.remove(edge);
	}
	
	public void recover_removed_vertices()
	{
		_rem_vertex_id_set.clear();
	}
	
	public void recover_removed_vertex(Integer vertex_id)
	{
		_rem_vertex_id_set.remove(vertex_id);
	}
	
	public double get_edge_weight(BaseVertex source, BaseVertex sink)
	{
		int source_id = source.get_id();
		int sink_id = sink.get_id();
		
		if(_rem_vertex_id_set.contains(source_id) || _rem_vertex_id_set.contains(sink_id) 
				|| _rem_edge_set.contains(new Pair<Integer, Integer>(source_id, sink_id)))
		{
			return Graph.DISCONNECTED;
		}
		return super.get_edge_weight(source, sink);
	}

	public double get_edge_weight_of_graph(BaseVertex source, BaseVertex sink)
	{
		return super.get_edge_weight(source, sink);
	}

	public Set<BaseVertex> get_adjacent_vertices(BaseVertex vertex)
	{
		Set<BaseVertex> ret_set = new HashSet<BaseVertex>();
		int starting_vertex_id = vertex.get_id();
		if(!_rem_vertex_id_set.contains(starting_vertex_id))
		{
			Set<BaseVertex> adj_vertex_set = super.get_adjacent_vertices(vertex);
			for(BaseVertex cur_vertex : adj_vertex_set)
			{
				int ending_vertex_id = cur_vertex.get_id();
				if(_rem_vertex_id_set.contains(ending_vertex_id)
				|| _rem_edge_set.contains(
						new Pair<Integer,Integer>(starting_vertex_id, ending_vertex_id)))
				{
					continue;
				}
				
				// 
				ret_set.add(cur_vertex);
			}
		}
		return ret_set;
	}

	public Set<BaseVertex> get_precedent_vertices(BaseVertex vertex)
	{
		Set<BaseVertex> ret_set = new HashSet<BaseVertex>();
		if(!_rem_vertex_id_set.contains(vertex.get_id()))
		{
			int ending_vertex_id = vertex.get_id();
			Set<BaseVertex> pre_vertex_set = super.get_precedent_vertices(vertex);
			for(BaseVertex cur_vertex : pre_vertex_set)
			{
				int starting_vertex_id = cur_vertex.get_id();
				if(_rem_vertex_id_set.contains(starting_vertex_id) 
				|| _rem_edge_set.contains(
						new Pair<Integer, Integer>(starting_vertex_id, ending_vertex_id))) 
				{
					continue;
				}
				
				//
				ret_set.add(cur_vertex);
			}
		}
		return ret_set;
	}

	public List<BaseVertex> get_vertex_list()
	{
		List<BaseVertex> ret_list = new Vector<BaseVertex>();
		for(BaseVertex cur_vertex : super.get_vertex_list())
		{
			if(_rem_vertex_id_set.contains(cur_vertex.get_id())) continue;
			ret_list.add(cur_vertex);
		}
		return ret_list;
	}

	public BaseVertex get_vertex(int id)
	{
		if(_rem_vertex_id_set.contains(id))
		{
			return null;
		}else
		{
			return super.get_vertex(id);
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Welcome to the class VariableGraph!");
		
		VariableGraph graph = new VariableGraph("data/test_50");
		graph.remove_vertex(13);
		graph.remove_vertex(12);
		graph.remove_vertex(10);
		graph.remove_vertex(23);
		graph.remove_vertex(47);
		graph.remove_vertex(49);
		graph.remove_vertex(3);
		graph.remove_edge(new Pair<Integer, Integer>(26, 41));
		DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(graph);
		System.out.println(alg.get_shortest_path(graph.get_vertex(0), graph.get_vertex(20),10));
	}
}

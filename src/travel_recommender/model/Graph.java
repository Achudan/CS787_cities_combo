package travel_recommender.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import travel_recommender.model.abstracts.BaseGraph;
import travel_recommender.model.abstracts.BaseVertex;

public class Graph implements BaseGraph
{
	public final static double DISCONNECTED = Double.MAX_VALUE;
	
	// index of fan-outs of one vertex
	protected Map<Integer, Set<BaseVertex>> _fanout_vertices_index =
		new HashMap<Integer, Set<BaseVertex>>();
	
	// index for fan-ins of one vertex
	protected Map<Integer, Set<BaseVertex>> _fanin_vertices_index =
		new HashMap<Integer, Set<BaseVertex>>();
	
	// index for edge weights in the graph
	protected Map<Pair<Integer, Integer>, Double> _vertex_pair_weight_index = 
		new HashMap<Pair<Integer,Integer>, Double>();
	
	//index for edge params in the graph
	protected Map<Pair<Integer, Integer>, GraphParams> _vertex_pair_graph_params_index= 
			new HashMap<Pair<Integer,Integer>, GraphParams>();
	
	// index for vertices in the graph
	protected Map<Integer, BaseVertex> _id_vertex_index = 
		new HashMap<Integer, BaseVertex>();
	
	// list of vertices in the graph 
	protected List<BaseVertex> _vertex_list = new Vector<BaseVertex>();
	
	// the number of vertices in the graph
	protected int _vertex_num = 0;
	
	// the number of arcs in the graph
	protected int _edge_num = 0;
	
	static private int counter = 0;
	
	static public Map<String, Integer> cityMap = new HashMap<>();
	static public Map<Integer, String> cityMapInverse = new HashMap<>();
	
	
	/**
	 * Constructor 1 
	 * @param data_file_name
	 */
	public Graph(final String data_file_name)
	{
		import_from_file(data_file_name);
	}
	
	/**
	 * Constructor 2
	 * 
	 * @param graph
	 */
	public Graph(final Graph graph_)
	{
		_vertex_num = graph_._vertex_num;
		_edge_num = graph_._edge_num;
		_vertex_list.addAll(graph_._vertex_list);
		_id_vertex_index.putAll(graph_._id_vertex_index);
		_fanin_vertices_index.putAll(graph_._fanin_vertices_index);
		_fanout_vertices_index.putAll(graph_._fanout_vertices_index);
		_vertex_pair_weight_index.putAll(graph_._vertex_pair_weight_index);
		_vertex_pair_graph_params_index.putAll(graph_._vertex_pair_graph_params_index);
	}
	
	/**
	 * Default constructor 
	 */
	public Graph(){};
	
	/**
	 * Clear members of the graph.
	 */
	public void clear()
	{
		_vertex_num = 0;
		_edge_num = 0; 
		_vertex_list.clear();
		_id_vertex_index.clear();
		_fanin_vertices_index.clear();
		_fanout_vertices_index.clear();
		_vertex_pair_weight_index.clear();
		_vertex_pair_graph_params_index.clear();
	}
	
	/**
	 * There is a requirement for the input graph. 
	 * The ids of vertices must be consecutive. 
	 *  
	 * @param data_file_name
	 */
	public void import_from_file(final String data_file_name)
	{
		try
		{
			// 1. read the file and put the content in the buffer
			FileReader input = new FileReader(data_file_name);
			BufferedReader bufRead = new BufferedReader(input);

			boolean is_first_line = true;
			String line; 	// String that holds current file line
			
			// 2. Read first line
			line = bufRead.readLine();
			while(line != null)
			{
				// 2.1 skip the empty line
				if(line.trim().equals("")) 
				{
					line = bufRead.readLine();
					continue;
				}
				
				// 2.2 generate nodes and edges for the graph
				if(is_first_line)
				{
					//2.2.1 obtain the number of nodes in the graph 
					
					is_first_line = false;
					_vertex_num = Integer.parseInt(line.trim());
					for(int i=0; i<_vertex_num; ++i)
					{
						BaseVertex vertex = new Vertex();
						_vertex_list.add(vertex);
						_id_vertex_index.put(vertex.get_id(), vertex);
					}
					
				}else
				{
					//2.2.2 find a new edge and put it in the graph  
//					String[] str_list = line.trim().split("\\s");
					String[] str_list = line.trim().split(",");
					int start_vertex_id = 0;
					int end_vertex_id = 0;
					if(!cityMap.containsKey(str_list[0])) {
						start_vertex_id = counter++;
						addToMap(str_list[0], start_vertex_id);
					}
					else {
						start_vertex_id = cityMap.get(str_list[0]);
					}
					
					if(!cityMap.containsKey(str_list[1])) {
						end_vertex_id = counter++;
						addToMap(str_list[1], end_vertex_id);
					}
					else {
						end_vertex_id = cityMap.get(str_list[1]);
					}
					
//					int start_vertex_id = Integer.parseInt(str_list[0]);
//					int end_vertex_id = Integer.parseInt(str_list[1]);
					GraphParams gp = new GraphParams();
					gp.setWeightFlag("cost");
					gp.setCost(Double.parseDouble(str_list[4]));
					gp.setStartDate(new Date(Long.parseLong(str_list[5])));
					gp.setDuration(durationInMs(str_list[4]));
					gp.setWeight();
//					ArrayList<String> params = new ArrayList<>();
//					params.add(String.valueOf(str_list[2]));
					add_edge(start_vertex_id, end_vertex_id, gp);
				}
				//
				line = bufRead.readLine();
			}
			bufRead.close();

		}catch (IOException e)
		{
			// If another exception is generated, print a stack trace
			e.printStackTrace();
		}
	}

	private void addToMap(String k, int v) {
		// TODO Auto-generated method stub
		cityMap.put(k, v);
		cityMapInverse.put(v, k);
	}

	private double durationInMs(String duration) {
		double d = Double.parseDouble(duration);
        long hrs = (long) d;
        d = d - Math.floor(d);
        long halfHour = 0;
        if (d == 0.5)
            halfHour = 30;
        long HOUR = 3600 * 1000;
        double durationInDouble = hrs * HOUR + (halfHour * 60 * 1000) + (30 * 60 * 1000);
		return durationInDouble;
	}

	/**
	 * Note that this may not be used externally, because some other members in the class
	 * should be updated at the same time. 
	 * 
	 * @param start_vertex_id
	 * @param end_vertex_id
	 * @param weight
	 */
	protected void add_edge(int start_vertex_id, int end_vertex_id, GraphParams gp)
	{
		double weight = gp.getWeight();
		// actually, we should make sure all vertices ids must be correct. 
		if(!_id_vertex_index.containsKey(start_vertex_id)
		|| !_id_vertex_index.containsKey(end_vertex_id) 
		|| start_vertex_id == end_vertex_id)
		{
			throw new IllegalArgumentException("The edge from "+start_vertex_id
					+" to "+end_vertex_id+" does not exist in the graph.");
		}
		
		// update the adjacent-list of the graph
		Set<BaseVertex> fanout_vertex_set = new HashSet<BaseVertex>();
		if(_fanout_vertices_index.containsKey(start_vertex_id))
		{
			fanout_vertex_set = _fanout_vertices_index.get(start_vertex_id);
		}
		fanout_vertex_set.add(_id_vertex_index.get(end_vertex_id));
		_fanout_vertices_index.put(start_vertex_id, fanout_vertex_set);
		
		//
		Set<BaseVertex> fanin_vertex_set = new HashSet<BaseVertex>();
		if(_fanin_vertices_index.containsKey(end_vertex_id))
		{
			fanin_vertex_set = _fanin_vertices_index.get(end_vertex_id);
		}
		fanin_vertex_set.add(_id_vertex_index.get(start_vertex_id));
		_fanin_vertices_index.put(end_vertex_id, fanin_vertex_set);

		// store the new edge 
		_vertex_pair_weight_index.put(
				new Pair<Integer, Integer>(start_vertex_id, end_vertex_id), 
				weight);
		
		_vertex_pair_graph_params_index.put(
				new Pair<Integer, Integer>(start_vertex_id, end_vertex_id), 
				gp);
		
		++_edge_num;
	}
	
	/**
	 * Store the graph information into a file. 
	 * 
	 * @param file_name
	 */
	public void export_to_file(final String file_name)
	{
		//1. prepare the text to export
		StringBuffer sb = new StringBuffer();
		sb.append(_vertex_num+"\n\n");
		for(Pair<Integer, Integer> cur_edge_pair : _vertex_pair_weight_index.keySet())
		{
			int starting_pt_id = cur_edge_pair.first();
			int ending_pt_id = cur_edge_pair.second();
			double weight = _vertex_pair_weight_index.get(cur_edge_pair);
			sb.append(starting_pt_id+"	"+ending_pt_id+"	"+weight+"\n");
		}
		//2. open the file and put the data into the file. 
		Writer output = null;
		try {
			// use buffering
			// FileWriter always assumes default encoding is OK!
			output = new BufferedWriter(new FileWriter(new File(file_name)));
			output.write(sb.toString());
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}finally {
			// flush and close both "output" and its underlying FileWriter
			try
			{
				if (output != null) output.close();
			} catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.emit.qyan.alg.model.abstracts.BaseGraph#get_adjacent_vertices(edu.asu.emit.qyan.alg.model.abstracts.BaseVertex)
	 */
	public Set<BaseVertex> get_adjacent_vertices(BaseVertex vertex)
	{
		return _fanout_vertices_index.containsKey(vertex.get_id()) 
				? _fanout_vertices_index.get(vertex.get_id()) 
				: new HashSet<BaseVertex>();
	}

	/* (non-Javadoc)
	 * @see edu.asu.emit.qyan.alg.model.abstracts.BaseGraph#get_precedent_vertices(edu.asu.emit.qyan.alg.model.abstracts.BaseVertex)
	 */
	public Set<BaseVertex> get_precedent_vertices(BaseVertex vertex)
	{
		return _fanin_vertices_index.containsKey(vertex.get_id()) 
				? _fanin_vertices_index.get(vertex.get_id()) 
				: new HashSet<BaseVertex>();
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.emit.qyan.alg.model.abstracts.BaseGraph#get_edge_weight(edu.asu.emit.qyan.alg.model.abstracts.BaseVertex, edu.asu.emit.qyan.alg.model.abstracts.BaseVertex)
	 */
	public double get_edge_weight(BaseVertex source, BaseVertex sink)
	{
		return _vertex_pair_weight_index.containsKey(
					new Pair<Integer, Integer>(source.get_id(), sink.get_id()))? 
							_vertex_pair_weight_index.get(
									new Pair<Integer, Integer>(source.get_id(), sink.get_id())) 
						  : DISCONNECTED;
	}
	
	public Date get_edge_StartDate(BaseVertex source, BaseVertex sink)
	{
		return _vertex_pair_graph_params_index.containsKey(
					new Pair<Integer, Integer>(source.get_id(), sink.get_id()))? 
							_vertex_pair_graph_params_index.get(
									new Pair<Integer, Integer>(source.get_id(), sink.get_id())).getStartDate() 
						  : null;
	}
	
	public double get_edge_Duration(BaseVertex source, BaseVertex sink)
	{
		return _vertex_pair_graph_params_index.containsKey(
					new Pair<Integer, Integer>(source.get_id(), sink.get_id()))? 
							_vertex_pair_graph_params_index.get(
									new Pair<Integer, Integer>(source.get_id(), sink.get_id())).getDuration() 
						  : null;
	}
	
	public double get_edge_Cost(BaseVertex source, BaseVertex sink)
	{
		return _vertex_pair_graph_params_index.containsKey(
					new Pair<Integer, Integer>(source.get_id(), sink.get_id()))? 
							_vertex_pair_graph_params_index.get(
									new Pair<Integer, Integer>(source.get_id(), sink.get_id())).getCost()
						  : null;
	}

	/**
	 * Set the number of vertices in the graph
	 * @param num
	 */
	public void set_vertex_num(int num)
	{
		_vertex_num = num;
	}
	
	/**
	 * Return the vertex list in the graph.
	 */
	public List<BaseVertex> get_vertex_list()
	{
		return _vertex_list;
	}
	
	/**
	 * Get the vertex with the input id.
	 * 
	 * @param id
	 * @return
	 */
	public BaseVertex get_vertex(int id)
	{
		return _id_vertex_index.get(id);
	}
}

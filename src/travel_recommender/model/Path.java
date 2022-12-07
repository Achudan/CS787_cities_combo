package travel_recommender.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import travel_recommender.model.abstracts.BaseElementWithWeight;
import travel_recommender.model.abstracts.BaseVertex;

/**
 * @author <a href='mailto:Yan.Qi@asu.edu'>Yan Qi</a>
 * @version $Revision: 673 $
 * @latest $Date: 2009-02-05 01:19:18 -0700 (Thu, 05 Feb 2009) $
 */
public class Path implements BaseElementWithWeight
{
	List<BaseVertex> _vertex_list = new Vector<BaseVertex>();
	double _weight = -1;
	public ArrayList<ArrayList<String>> paths = new ArrayList<ArrayList<String>>();
	
	public Path(){};
	
	public Path(List<BaseVertex> _vertex_list, double _weight)
	{
		this._vertex_list = _vertex_list;
		this._weight = _weight;
	}

	public double get_weight()
	{
		return _weight;
	}
	
	public void set_weight(double weight)
	{
		_weight = weight;
	}
	
	public List<BaseVertex> get_vertices()
	{
		return _vertex_list;
	}
	
	@Override
	public boolean equals(Object right)
	{
		if(right instanceof Path)
		{
			Path r_path = (Path) right;
			return _vertex_list.equals(r_path._vertex_list);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return _vertex_list.hashCode();
	}
	
	public String toString()
	{
		return paths.toString()+":"+_weight;
	}
}
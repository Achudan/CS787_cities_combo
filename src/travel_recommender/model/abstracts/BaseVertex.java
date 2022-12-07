package travel_recommender.model.abstracts;

public interface BaseVertex
{
	int get_id();
	double get_weight();
	void set_weight(double weight);
}

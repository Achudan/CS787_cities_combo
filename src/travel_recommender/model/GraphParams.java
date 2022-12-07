package travel_recommender.model;

import java.util.Date;

public class GraphParams {
	
	private double weight = 0d;
	private double cost = 0d;
	private Date startDate = null;
	private double duration = 0l;
	private int weightFlag = 0;
	
	public void setWeightFlag(String constraint) {
		weightFlag = constraint.equals("cost") ? 1 : 0;
	}
	
	public void setWeight() {
		weight = weightFlag==1 ? cost : duration;
	}
	
	public void setCost(double c) {
		cost = c;
	}
	
	public void setStartDate(Date s) {
		startDate = s;
	}
	
	public void setDuration(double d) {
		duration = d;
	}
	
	public int getWeightFlag() {
		return weightFlag;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public double getCost() {
		return cost;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public double getDuration() {
		return duration;
	}
	

}

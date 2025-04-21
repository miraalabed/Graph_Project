package application;

import java.util.ArrayList;
import java.util.List;

public class Graph {// this class represents the graph for the dijkstra algo that will contains the
					// vertics city and the edges roads and the cost and weight between the cities
					// for each one from the source
	public ArrayList<City> cities;// array list to stire the cities thatt represents the vertics
	public ArrayList<Road> roads;// array list to store the roads that are represents the edges between vertics
	public ArrayList<Double> distances;// an array list the store the distance that represent distence between the city
										// virtics and the roads edges path in double form

	public Graph() {// no argument constructor that represents the array lists
					// content
		this.cities = new ArrayList<>();
		this.roads = new ArrayList<>();
		this.distances = new ArrayList<>();
	}

	public Graph(List<City> cities, List<Road> roads, List<Double> distances) {// argument constructor to
																								// set initial values
																								// for cities, roads,
																								// and distances
		this.cities = new ArrayList<>();
		this.roads = new ArrayList<>();
		this.distances = new ArrayList<>();
	}

	public ArrayList<City> getCities() {// get the cities
		return cities;
	}

	public void setCities(ArrayList<City> cities) {// set the cities
		this.cities = cities;
	}

	public ArrayList<Road> getRoads() {// get the road
		return roads;
	}

	public void setRoads(ArrayList<Road> roads) {// set the road
		this.roads = roads;
	}

	public ArrayList<Double> getDistances() {// get the distance
		return distances;
	}

	public void setDistances(ArrayList<Double> distances) {// set the distance
		this.distances = distances;
	}

	public void addCity(City city) {// method to add city that i want
		Double infinity = Double.MAX_VALUE;// set the initial value for the vertics city as infinity
		cities.add(city);// add the city
		distances.add(infinity);// make the initial value for the city is the infinity value
	}

	public void addRoad(City city1, City city2, double cost, double time) {
	    Road road = new Road(city1, city2, cost, time); 
	    roads.add(road); 
	}

	public double getDistance(City city) {// method to get the distance for the cpecific city
		int index = cities.indexOf(city);// take the value of the city vertics index
		if (index != -1 && index < distances.size()) {// if there is element and the index is less than the array list
														// size
			return distances.get(index);// then get the distance on specific index
		}
		return Double.MAX_VALUE;// other wise keep the distance infinity
	}

	public void setDistance(City city, double distance) {// method to set the destance by taking the city and the
															// distance
		int index = cities.indexOf(city);// take the value of the city vertics index
		if (index != -1 && index < distances.size()) {// if there is element and the index is less than the array list
														// size
			distances.set(index, distance);// then set the distance on specific index
		} else {
			System.out.println("not found!!!!!");// else print error message that not found
		}
	}

	@Override
	public String toString() {// to string method that will print out the information of the graph class city
								// and road and distance that represents the cost
		return "Graph [cities=" + cities + ", roads=" + roads + ", distances=" + distances + "]";
	}
}

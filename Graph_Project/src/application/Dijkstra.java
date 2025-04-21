package application;

import java.util.List;
import java.util.ArrayList;

public class Dijkstra {
    private Graph graph;
    private double[] distances;
    private boolean[] visited;
    private City[] previous;
    private String criteria; // "distance", "cost", or "time"

    public Dijkstra(Graph graph, String criteria) {
        this.graph = graph;
        this.criteria = criteria;
    }

    public void findShortestPath(City source) {
        int n = graph.getCities().size();
        distances = new double[n];
        visited = new boolean[n];
        previous = new City[n];

        // Initialize distances to infinity and visited to false
        for (int i = 0; i < n; i++) {
            distances[i] = Double.MAX_VALUE;
            visited[i] = false;
        }

        // Set the distance for the source city to 0
        int sourceIndex = graph.getCities().indexOf(source);
        if (sourceIndex == -1) {
            System.out.println("Source city not found in the graph.");
            return;
        }
        distances[sourceIndex] = 0;

        // Main Dijkstra algorithm loop
        for (int i = 0; i < n; i++) {
            // Find the vertex with the smallest unknown distance
            int v = getSmallestUnknownDistanceVertex();
            if (v == -1) {
                break; // All reachable vertices processed
            }

            visited[v] = true;
            City currentCity = graph.getCities().get(v);

            // Update distances for each neighbor
            for (Road road : graph.getRoads()) {
                if (road.getSource().equals(currentCity)) {
                    City neighbor = road.getDestination();
                    int neighborIndex = graph.getCities().indexOf(neighbor);

                    if (!visited[neighborIndex]) {
                        double newDistance = distances[v] + getCriteriaValue(road);
                        if (newDistance < distances[neighborIndex]) {
                            distances[neighborIndex] = newDistance;
                            previous[neighborIndex] = currentCity;
                        }
                    }
                }
            }
        }
    }
    public List<City> getShortestPath(City source, City destination) {
        List<City> path = new ArrayList<>();
        int destinationIndex = graph.getCities().indexOf(destination);

        if (destinationIndex == -1 || distances[destinationIndex] == Double.MAX_VALUE) {
            System.out.println("Destination not reachable from the source.");
            return path;
        }

        for (City at = destination; at != null; at = previous[graph.getCities().indexOf(at)]) {
            path.add(0, at);
        }

        return path;
    }

    private int getSmallestUnknownDistanceVertex() {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;

        // Iterate over all vertices to find the smallest unknown distance
        for (int i = 0; i < distances.length; i++) {
            if (!visited[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                minIndex = i;
            }
        }

        return minIndex; // Return the index of the vertex with the smallest distance
    }

    private double getCriteriaValue(Road road) {
        switch (criteria) {
            case "cost":
                return road.getCost();
            case "time":
                return road.getTime();
            case "distance":
            default:
                return road.getDistance();
        }
    }
}

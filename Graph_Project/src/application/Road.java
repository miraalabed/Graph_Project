package application;

public class Road {
    private City source;
    private City destination;
    private double cost;
    private double time;
    private double distance;  // Distance calculated using the Haversine formula

    // Constructor
    public Road(City source, City destination, double cost, double time) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.time = time;
        this.distance = calculateDistance(source, destination);  // Calculate distance when the object is created
    }

    // Getter for the source city
    public City getSource() {
        return source;
    }

    // Getter for the destination city
    public City getDestination() {
        return destination;
    }

    // Getter for the cost of the road
    public double getCost() {
        return cost;
    }

    // Getter for the time required to travel the road
    public double getTime() {
        return time;
    }

    // Getter for the distance of the road
    public double getDistance() {
        return this.distance;  // Return the stored distance in the Road object
    }

    // Function to calculate distance using the Haversine formula
    private double calculateDistance(City city1, City city2) {
        final int R = 6371; // Radius of the Earth in kilometers

        // Convert latitude and longitude from degrees to radians
        double lat1 = Math.toRadians(city1.getLatitude());
        double lon1 = Math.toRadians(city1.getLongitude());
        double lat2 = Math.toRadians(city2.getLatitude());
        double lon2 = Math.toRadians(city2.getLongitude());

        // Calculate the difference in latitude and longitude
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Calculate distance using the Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in kilometers
        double distance = R * c;
        return distance;
    }
}

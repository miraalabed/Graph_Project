package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
	private final List<City> cities = new ArrayList<>();
	private final List<Road> road = new ArrayList<>();
	private City sourceCity = null;
	private City targetCity = null;
	private final Pane mapPane = new Pane();
	private final double W = 1000;
	private final double H = 518;
	private ImageView mapView;
	public ComboBox<String> sourceComboBox = new ComboBox<>();
	public ComboBox<String> targetComboBox = new ComboBox<>();
	TextField timeField;
	TextArea pathTextArea;
	TextField distanceField;
	TextField costField;
	Graph graph;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

		// Create the main layout
		HBox root = new HBox(10);
		BorderPane bPane = new BorderPane();
		bPane.setPadding(new Insets(10));
		// Load the world map image
		Image worldMapImage;
		try {
			worldMapImage = new Image(getClass().getResource("map2.png").toExternalForm());
		} catch (NullPointerException e) {
			showAlert("Resource Error", "Map image not found! Ensure it's located in src/main/resources/");
			return;
		}

		// Set up the map view
		mapView = new ImageView(worldMapImage);
		mapView.setPreserveRatio(true);
		mapView.setFitWidth(W);
		mapView.setFitHeight(H);

		mapPane.getChildren().add(mapView);
		VBox Vmap = new VBox(mapPane);
		Vmap.setAlignment(Pos.CENTER);
		root.getChildren().add(Vmap);

		// Create the right panel
		VBox rightPanel = new VBox(8);
		rightPanel.setStyle("-fx-background-color: #f4f4f4;");

		Label sourceLabel = new Label("Source:");
		sourceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		sourceLabel.setStyle("-fx-text-fill: #ecf7f9;");
		sourceComboBox = new ComboBox<>();
		sourceComboBox.setStyle("-fx-font-size: 12px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
				+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
				+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 8px;");
		sourceComboBox.setOnMouseEntered(
				e -> sourceComboBox.setStyle("-fx-font-size: 12px; " + "-fx-background-color: #191970; "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));

		sourceComboBox.setOnMouseExited(e -> sourceComboBox
				.setStyle("-fx-font-size: 12px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 8px;"));
		VBox b1 = new VBox(sourceLabel, sourceComboBox);
		Label targetLabel = new Label("Target:");
		targetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		targetLabel.setStyle("-fx-text-fill: #ecf7f9;");
		targetComboBox = new ComboBox<>();
		targetComboBox.setStyle("-fx-font-size: 12px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
				+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
				+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 8px;");
		targetComboBox.setOnMouseEntered(
				e -> targetComboBox.setStyle("-fx-font-size: 12px; " + "-fx-background-color: #191970; "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));

		targetComboBox.setOnMouseExited(e -> targetComboBox
				.setStyle("-fx-font-size: 12px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 8px;"));
		VBox b2 = new VBox(targetLabel, targetComboBox);

		HBox bx = new HBox(10, b1, b2);

		Label filterLabel = new Label("Filter:");
		filterLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		filterLabel.setStyle("-fx-text-fill: #ecf7f9;");
		ComboBox<String> filterComboBox = new ComboBox<>();
		filterComboBox.setStyle("-fx-font-size: 12px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
				+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
				+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 8px;");
		filterComboBox.setOnMouseEntered(
				e -> filterComboBox.setStyle("-fx-font-size: 12px; " + "-fx-background-color: #191970; "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));

		filterComboBox.setOnMouseExited(e -> filterComboBox
				.setStyle("-fx-font-size: 12px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 8px;"));

		filterComboBox.getItems().addAll("shortest path", "shortest time", "lowest cost");

		Label pathLabel = new Label("Path:");
		pathLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		pathLabel.setStyle("-fx-text-fill: #ecf7f9;");
		pathTextArea = new TextArea();
		pathTextArea.setPrefHeight(100);
		pathTextArea.setEditable(false);

		Label distanceLabel = new Label("Distance:");
		distanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		distanceLabel.setStyle("-fx-text-fill: #191970;");
		distanceField = new TextField();
		distanceField.setEditable(false);

		Label costLabel = new Label("Cost:");
		costLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		costLabel.setStyle("-fx-text-fill: #191970;");
		costField = new TextField();
		costField.setEditable(false);

		Label timeLabel = new Label("Time:");
		timeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		timeLabel.setStyle("-fx-text-fill: #191970;");
		timeField = new TextField();
		timeField.setEditable(false);

		Button runButton = new Button("Run");
		runButton.setStyle("-fx-font-size: 17px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
				+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
				+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;");

		runButton.setOnMouseEntered(e -> runButton.setStyle("-fx-font-size: 17px; " + "-fx-background-color: #191970; "
				+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
				+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));

		runButton.setOnMouseExited(e -> runButton
				.setStyle("-fx-font-size: 17px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));
		Button resetButton = new Button("Reset");
		resetButton.setOnAction(e -> {
			resetSelection();
			sourceComboBox.setValue(null);
			targetComboBox.setValue(null);
			filterComboBox.setValue(null);
			timeField.setText(null);
			pathTextArea.setText(null);
			costField.setText(null);
			distanceField.setText(null);
		});

		resetButton.setStyle("-fx-font-size: 17px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
				+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
				+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;");

		resetButton
				.setOnMouseEntered(e -> resetButton.setStyle("-fx-font-size: 17px; " + "-fx-background-color: #191970; "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));

		resetButton.setOnMouseExited(e -> resetButton
				.setStyle("-fx-font-size: 17px; " + "-fx-background-color: linear-gradient(#191970,#ecf7f9); "
						+ "-fx-font-weight: bold; " + "-fx-text-fill: #ecf7f9; " + "-fx-cursor: hand; "
						+ "-fx-border-width: 2px; " + "-fx-padding: 2px 2px;" + "-fx-border-radius: 10px;"));
		HBox x1 = new HBox(10, resetButton, runButton);
		HBox x = new HBox(25, filterComboBox, x1);
		rightPanel.getChildren().addAll(bx, filterLabel, x, pathLabel, pathTextArea, distanceLabel, distanceField,
				costLabel, costField, timeLabel, timeField);
		rightPanel.setAlignment(Pos.CENTER_LEFT);
		rightPanel.setStyle("-fx-background-color: linear-gradient(#191970,#ecf7f9);");

		root.getChildren().add(rightPanel);
		root.setAlignment(Pos.CENTER);
		bPane.setCenter(root);
		bPane.setStyle("-fx-background-color: linear-gradient(#191970,#ecf7f9);");
		// Load cities from file
		loadCitiesFromFile("C:\\Users\\lenovo\\.eclipse\\org\\Graph_Project\\src\\application\\cities");//test
		//loadCitiesFromFile("C:\\Users\\lenovo\\.eclipse\\org\\Graph_Project\\src\\application\\test");//cities

		// Add city circles to the map
		for (City city : cities) {
			sourceComboBox.getItems().add(city.getName());
			targetComboBox.getItems().add(city.getName());
			Circle circle = createCircle(city);
			if (circle != null) {
				mapPane.getChildren().add(circle);
			}
		}
		runButton.setOnAction(e -> {
			if (graph.getCities().isEmpty()) {
				System.out.println("No cities loaded in the graph!");
				return;
			}
			
			if (graph.getRoads().isEmpty()) {
				System.out.println("No roads loaded in the graph!");
				return;
			}

			if (sourceComboBox.getValue() != null && targetComboBox.getValue() != null) {
				handleCityClick(sourceComboBox.getValue());
				handleCityClick(targetComboBox.getValue());

				// Find specific cities 
				City sourceCity = findCityByName(sourceComboBox.getValue());
				City targetCity = findCityByName(targetComboBox.getValue());

				if (sourceCity == null || targetCity == null) {
					showAlert("Selection Error", "One or both selected cities are invalid!");
					return;
				}

				// Check if a criterion is selected in the ComboBox
				String selectedCriteria = filterComboBox.getValue();
				if (selectedCriteria == null) {
					showAlert("Selection Error", "Please select a filter (distance, time, or cost).");
					return;
				}

				// Determine the appropriate criterion
				String criteria;
				switch (selectedCriteria.toLowerCase()) {
				case "shortest path":
					criteria = "distance";
					break;
				case "shortest time":
					criteria = "time";
					break;
				case "lowest cost":
					criteria = "cost";
					break;
				default:
					showAlert("Filter Error", "Invalid filter selection.");
					return;
				}

				// Create a Dijkstra object and call the algorithm
				Dijkstra dijkstra = new Dijkstra(graph, criteria);
				dijkstra.findShortestPath(sourceCity);

				// Get the shortest path
				ArrayList<City> path = (ArrayList<City>) dijkstra.getShortestPath(sourceCity, targetCity);

				// Check if path exists
				if (path == null || path.isEmpty()) {
					pathTextArea.setText("No path found between the selected cities.");
					return;
				}

				// Display path and data
				StringBuilder pathString = new StringBuilder();
				double totalDistance = 0.0;
				double totalTime = 0.0;
				double totalCost = 0.0;

				for (int i = 0; i < path.size() - 1; i++) {
					City curr = path.get(i);
					City next = path.get(i + 1);
					drawArrow(curr, next);
					for (Road road : graph.getRoads()) {
						if (road.getSource().equals(curr) && road.getDestination().equals(next)) {
							totalDistance += road.getDistance();
							totalTime += road.getTime();
							totalCost += road.getCost();
							break;
						}
					}
					pathString.append(curr.getName()).append(" -> ");
				}
				pathString.append(path.get(path.size() - 1).getName());
				pathTextArea.setText(pathString.toString());

				distanceField.setText(String.format("%.2f km", totalDistance));
				timeField.setText(String.format("%.2f min", totalTime));
				costField.setText(String.format("%.2f$", totalCost));
			} else {
				showAlert("Selection Error", "Please select both source and target cities!");
			}
		});

		Scene scene = new Scene(bPane, 1000, 560);
		primaryStage.setMaximized(true);
		primaryStage.setTitle("World Map");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	private void loadCitiesFromFile(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read the first line: Number of cities and roads
			line = br.readLine();
			if (line == null || line.isEmpty()) {
				showAlert("File Error", "The file is empty or invalid.");
				return;
			}

			String[] counts = line.split(",");
			if (counts.length != 2) {
				showAlert("File Error", "Invalid format in the first line. Expected: <numCities>,<numRoads>");
				return;
			}

			int numCities = Integer.parseInt(counts[0].trim());
			int numRoads = Integer.parseInt(counts[1].trim());
			// Initialize the graph object
			graph = new Graph(cities, road, new ArrayList<>());

			// Loading cities
			int cityCounter = 0;
			while (cityCounter < numCities && (line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] parts = line.split(",");
				if (parts.length == 3) {
					String name = parts[0].trim();
					double latitude = Double.parseDouble(parts[1].trim());
					double longitude = Double.parseDouble(parts[2].trim());
					cityCounter++;
					City cc = new City(name, latitude, longitude);
					cities.add(cc); // Add city
					graph.addCity(cc);
					System.out.println(cityCounter + ") " + name + " x:" + longitude + " y:" + latitude);
				} else {
					System.out.println("Invalid city format: " + line);
				}
			}
			System.out.println("Loaded " + cityCounter + " cities.\n");

			// Loading roads
			int roadCounter = 0;
			while (roadCounter < numRoads && (line = br.readLine()) != null) {
				if (line.isEmpty())
					continue;
				String[] parts = line.split(",");
				if (parts.length == 4) {
					String sourceName = parts[0].trim();
					String targetName = parts[1].trim();
					double cost = Double.parseDouble(parts[2].replace("$", "").trim());
					double time = Double.parseDouble(parts[3].replace("min", "").trim());

					// Find City objects using names
					City sourceCity = findCityByName(sourceName);
					City targetCity = findCityByName(targetName);

					if (sourceCity != null && targetCity != null) {
						roadCounter++;
						Road newRoad = new Road(sourceCity, targetCity, cost, time);
						road.add(newRoad);
						graph.addRoad(sourceCity, targetCity, cost, time);
						System.out.println("Road: " + sourceName + " -> " + targetName + " | Cost: " + cost
								+ "$ | Time: " + time + "min");
					} else {
						System.out.println("Cities not found: " + sourceName + " or " + targetName);
					}
				} else {
					System.out.println("Invalid road format: " + line);
				}
			}
			System.out.println("Loaded " + roadCounter + " roads.");
		} catch (IOException | NumberFormatException e) {
			showAlert("File Error", "Error reading data: " + e.getMessage());
		}
	}

	private City findCityByName(String name) {
		for (City city : cities) {
			if (city.getName().trim().equalsIgnoreCase(name.trim())) {
				return city;
			}
		}
		System.out.println("City not found: " + name); // Print the city name if not found
		return null;
	}

	private Circle createCircle(City city) {
		double xCoordinate = (((city.getLongitude() + 180.0) / 360.0) * W);
		double yCoordinate = (((90.0 - city.getLatitude()) / 180.0) * H) - 8;

		Circle circle = new Circle(xCoordinate, yCoordinate, 3, Color.RED);
		circle.setOnMouseClicked(event -> handleCityClick(city.getName()));

		Label label = new Label(city.getName());
		label.setLayoutX(xCoordinate + 5);
		label.setLayoutY(yCoordinate - 10);
		label.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		label.setStyle("-fx-text-fill: #ecf7f9;-fx-cursor: hand;");
		label.setOnMouseEntered(
				e -> label.setStyle("-fx-text-fill: red;-fx-cursor: hand;-fx-font-weight: bold;-fx-font-size: 16px;"));
		label.setOnMouseExited(e -> label.setStyle("-fx-text-fill: #ecf7f9;-fx-cursor: hand;"));

		mapPane.getChildren().add(label);

		return circle;
	}

	private void resetSelection() {
		sourceCity = null;
		targetCity = null;
		mapPane.getChildren().removeIf(node -> node instanceof Line || node instanceof Polygon);

		for (javafx.scene.Node node : mapPane.getChildren()) {
			if (node instanceof Circle) {
				Circle circle = (Circle) node;
				circle.setFill(Color.RED); // Restore original color
			}
		}
		System.out.println("Selection reset.");
	}

	private void handleCityClick(String cityName) {
	    // Create a list to store circles
	    List<Circle> circles = new ArrayList<>();
	    for (javafx.scene.Node node : mapPane.getChildren()) {
	        if (node instanceof Circle) {
	            circles.add((Circle) node); // Add circle nodes to the list
	        }
	    }

	    for (Circle circle : circles) {
	        // Find the city using its name
	        for (City city : cities) { // List of cities
	            if (city.getName().equals(cityName)) {
	                // Calculate the coordinates of the city
	                double calculatedX = (((city.getLongitude() + 180.0) / 360.0) * W);
	                double calculatedY = (((90.0 - city.getLatitude()) / 180.0) * H) - 8;

	                // Check if the circle matches the city's coordinates
	                if (Math.abs(circle.getCenterX() - calculatedX) < 0.1
	                        && Math.abs(circle.getCenterY() - calculatedY) < 0.1) {

	                    if (sourceCity == null && (targetCity == null || !targetCity.getName().equals(cityName))) {
	                        // If source is not selected yet and the city is not already a target
	                        sourceCity = city;
	                        circle.setFill(Color.YELLOW); // Highlight the city as the source
	                        showAlert("Source city selected: " + city.getName());
	                        sourceComboBox.setValue(cityName);
	                    } else if (targetCity == null
	                            && (sourceCity == null || !sourceCity.getName().equals(cityName))) {
	                        // If target is not selected yet and the city is not already a source
	                        targetCity = city;
	                        circle.setFill(Color.BLUE); // Highlight the city as the target
	                        showAlert("Target city selected: " + city.getName());
	                        targetComboBox.setValue(cityName);
	                    }
	                }
	            }
	        }
	    }
	}

	private void drawArrow(City source, City target) {
	    // Calculate coordinates for source and target cities
	    double startX = (((source.getLongitude() + 180.0) / 360.0) * W);
	    double startY = (((90.0 - source.getLatitude()) / 180.0) * H) - 8;

	    double endX = (((target.getLongitude() + 180.0) / 360.0) * W);
	    double endY = (((90.0 - target.getLatitude()) / 180.0) * H) - 8;

	    // Create a line between the cities
	    Line line = new Line(startX, startY, endX, endY);
	    line.setStroke(Color.RED); // Set line color to red
	    line.setStrokeWidth(2); // Set line width

	    // Calculate the arrowhead dimensions
	    double arrowLength = 10; // Length of the arrowhead
	  //  double arrowWidth = 5;   // Width of the arrowhead

	    double angle = Math.atan2(endY - startY, endX - startX); // Calculate the angle of the arrow

	    // Calculate points for the arrowhead
	    double x1 = endX - arrowLength * Math.cos(angle - Math.PI / 6);
	    double y1 = endY - arrowLength * Math.sin(angle - Math.PI / 6);

	    double x2 = endX - arrowLength * Math.cos(angle + Math.PI / 6);
	    double y2 = endY - arrowLength * Math.sin(angle + Math.PI / 6);

	    // Create a triangle for the arrowhead
	    Polygon arrowHead = new Polygon();
	    arrowHead.getPoints().addAll(endX, endY, x1, y1, x2, y2);
	    arrowHead.setFill(Color.RED); // Set arrowhead color to red

	    // Add the line and arrowhead to the map pane
	    mapPane.getChildren().addAll(line, arrowHead);
	}

	private void showAlert(String ss) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(null);
		alert.setHeaderText(null);
		alert.setContentText(ss);
		alert.showAndWait();
	}

	private void showAlert(String title, String mass) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(mass);
		alert.showAndWait();
	}
}

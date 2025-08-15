package Jraffic;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrafficSimulation extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private Roads roads;
    private TrafficController trafficController;
    private List<Vehicle> allVehicles;
    private AnimationTimer gameLoop;
    private Random random;
    private long lastVehicleSpawn = 0;
    private final int MIN_SPAWN_DELAY = 500; // 0.5 seconds between spawns
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize components
        canvas = new Canvas(600, 500);
        gc = canvas.getGraphicsContext2D();
        roads = new Roads();
        trafficController = new TrafficController(roads);
        roads.setTrafficController(trafficController);
        allVehicles = new ArrayList<>();
        random = new Random();
        
        // Setup scene
        VBox root = new VBox();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 600, 600);
        
        // Setup keyboard controls
        setupKeyControls(scene);
        
        // Start animation loop
        startGameLoop();
        
        // Stage setup
        primaryStage.setTitle("Traffic Simulation - Jraffic");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> gameLoop.stop());
        primaryStage.show();
        
        // Focus on scene for key events
        scene.getRoot().requestFocus();
    }
    
    private void setupKeyControls(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            long currentTime = System.currentTimeMillis();
            
            // Prevent spam creation - enforce minimum delay
            if (currentTime - lastVehicleSpawn < MIN_SPAWN_DELAY) {
                return;
            }
            
            String startPosition = null;
            
            switch (code) {
                case UP:
                    startPosition = "south"; // Coming from south, moving north
                    break;
                case DOWN:
                    startPosition = "north"; // Coming from north, moving south
                    break;
                case LEFT:
                    startPosition = "east"; // Coming from east, moving west
                    break;
                case RIGHT:
                    startPosition = "west"; // Coming from west, moving east
                    break;
                case R:
                    String[] positions = {"north", "south", "east", "west"};
                    startPosition = positions[random.nextInt(positions.length)];
                    break;
                case ESCAPE:
                    gameLoop.stop();
                    System.exit(0);
                    break;
            }
            
            if (startPosition != null) {
                spawnVehicle(startPosition);
                lastVehicleSpawn = currentTime;
            }
        });
    }
    
    private void spawnVehicle(String startPosition) {
        Lane lane = roads.getLane(startPosition);
        
        if (lane != null && lane.canAddVehicle()) {
            // Random direction for the vehicle
            String[] directions = {"straight", "turn left", "turn right"};
            String direction = directions[random.nextInt(directions.length)];
            
            // Create vehicle with roads reference
            Vehicle vehicle = new Vehicle(startPosition, direction, 2, 20, 20, roads);
            
            // Add to lane and global list
            lane.addVehicle(vehicle);
            allVehicles.add(vehicle);
        }
    }
    
    private void startGameLoop() {
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                render();
            }
        };
        gameLoop.start();
    }
    
    private void update() {
        // Update traffic controller
        trafficController.update(allVehicles);
        
        // Update vehicles
        List<Vehicle> vehiclesToRemove = new ArrayList<>();
        
        for (Vehicle vehicle : allVehicles) {
            updateVehicle(vehicle);
            
            // Remove vehicles that have left the screen
            if (isVehicleOffScreen(vehicle)) {
                vehiclesToRemove.add(vehicle);
                // Remove from lane as well
                Lane lane = roads.getLane(vehicle.getStartPosition());
                if (lane != null) {
                    lane.removeVehicle(vehicle);
                }
            }
        }
        
        allVehicles.removeAll(vehiclesToRemove);
    }
    
    private void updateVehicle(Vehicle vehicle) {
        // Check if vehicle should stop at traffic light
        boolean shouldStop = shouldVehicleStop(vehicle);
        vehicle.setMoving(!shouldStop);
        
        // Check for collision with vehicle in front
        if (!shouldStop) {
            shouldStop = checkCollision(vehicle);
            vehicle.setMoving(!shouldStop);
        }
        
        // Move vehicle if it's not stopped
        if (!shouldStop) {
            vehicle.move();
        }
    }
    
    private boolean shouldVehicleStop(Vehicle vehicle) {
        // Don't stop vehicles that are already in the intersection
        if (vehicle.isInIntersection()) {
            return false;
        }
        
        // Check if vehicle is approaching intersection
        int x = vehicle.getX();
        int y = vehicle.getY();
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int laneWidth = roads.getLaneWidth();
        
        // Define stop line positions (just before intersection)
        boolean atStopLine = false;
        String startDirection = vehicle.getStartPosition();
        
        switch (startDirection) {
            case "north":
                // Vehicles coming from north, check if at stop line before intersection
                atStopLine = (y >= centerY - laneWidth - 15) && (y <= centerY - laneWidth + 5) && 
                            (x >= centerX - laneWidth/2 - 10) && (x <= centerX - laneWidth/2 + 10);
                break;
            case "south":
                // Vehicles coming from south, check if at stop line before intersection
                atStopLine = (y <= centerY + laneWidth + 15) && (y >= centerY + laneWidth - 5) && 
                            (x >= centerX + laneWidth/2 - 10) && (x <= centerX + laneWidth/2 + 10);
                break;
            case "east":
                // Vehicles coming from east, check if at stop line before intersection
                atStopLine = (x <= centerX + laneWidth + 15) && (x >= centerX + laneWidth - 5) && 
                            (y >= centerY - laneWidth/2 - 10) && (y <= centerY - laneWidth/2 + 10);
                break;
            case "west":
                // Vehicles coming from west, check if at stop line before intersection
                atStopLine = (x >= centerX - laneWidth - 15) && (x <= centerX - laneWidth + 5) && 
                            (y >= centerY + laneWidth/2 - 10) && (y <= centerY + laneWidth/2 + 10);
                break;
        }
        
        if (atStopLine) {
            // Additional check: if intersection is heavily congested, allow entry to prevent deadlock
            long vehiclesInIntersection = allVehicles.stream()
                .filter(Vehicle::isInIntersection)
                .count();
            
            // If too many vehicles in intersection, be more permissive
            if (vehiclesInIntersection > 6) {
                return false; // Allow entry to clear congestion
            }
            
            return !trafficController.canVehiclePass(startDirection);
        }
        
        return false;
    }
    
    private boolean checkCollision(Vehicle vehicle) {
        // Enhanced collision detection using bounding boxes and safe distances
        for (Vehicle other : allVehicles) {
            if (!vehicle.equals(other)) { // Use equals instead of != for object comparison
                // Calculate comprehensive collision using bounding boxes
                if (wouldCollideWithVehicle(vehicle, other)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean wouldCollideWithVehicle(Vehicle vehicle1, Vehicle vehicle2) {
        // If both vehicles are in the intersection, do a simple overlap check
        if (vehicle1.isInIntersection() && vehicle2.isInIntersection()) {
            int dx = Math.abs(vehicle1.getX() - vehicle2.getX());
            int dy = Math.abs(vehicle1.getY() - vehicle2.getY());
            double distance = Math.sqrt(dx * dx + dy * dy);
            // Use a small buffer (e.g., half vehicle width) to prevent visual overlap
            return distance < (15 / 2.0);
        }

        // Check for same-lane collision (not in intersection)
        if (vehicle1.getStartPosition().equals(vehicle2.getStartPosition())) {
            // Check if vehicle2 is in front of vehicle1
            if (isInFront(vehicle1, vehicle2)) {
                int dx = Math.abs(vehicle1.getX() - vehicle2.getX());
                int dy = Math.abs(vehicle1.getY() - vehicle2.getY());
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                // Predict if the NEXT move will violate the safe distance.
                // This prevents the car from getting one step too close.
                if (distance - vehicle1.getVelocity() < vehicle1.getSafeDistance()) {
                    return true;
                }
            }
            return false; // No collision if not in front or far enough away
        }

        // For vehicles in different lanes approaching the intersection, use bounding box check
        if (!vehicle1.isInIntersection() && !vehicle2.isInIntersection()) {
            int x1 = vehicle1.getX();
            int y1 = vehicle1.getY();
            int length1 = vehicle1.getLength();
            
            int x2 = vehicle2.getX();
            int y2 = vehicle2.getY();
            int length2 = vehicle2.getLength();
            
            int vehicleWidth = 15;
            int buffer = 5; // Small buffer for cross-traffic

            // Bounding boxes
            int v1Left = x1 - vehicleWidth/2 - buffer;
            int v1Right = x1 + vehicleWidth/2 + buffer;
            int v1Top = y1 - length1/2 - buffer;
            int v1Bottom = y1 + length1/2 + buffer;
            
            int v2Left = x2 - vehicleWidth/2;
            int v2Right = x2 + vehicleWidth/2;
            int v2Top = y2 - length2/2;
            int v2Bottom = y2 + length2/2;
            
            // Check for bounding box overlap if both are approaching
            if (areApproachingIntersection(vehicle1) && areApproachingIntersection(vehicle2)) {
                return v1Left < v2Right && v1Right > v2Left && v1Top < v2Bottom && v1Bottom > v2Top;
            }
        }
        
        return false;
    }
    
    private boolean areApproachingIntersection(Vehicle vehicle) {
        int x = vehicle.getX();
        int y = vehicle.getY();
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int laneWidth = roads.getLaneWidth();
        
        // Check if vehicle is close to intersection entrance
        String direction = vehicle.getDirectionNow();
        switch (direction) {
            case "south":
                return y >= centerY - laneWidth - 30 && y < centerY - laneWidth;
            case "north":
                return y <= centerY + laneWidth + 30 && y > centerY + laneWidth;
            case "west":
                return x <= centerX + laneWidth + 30 && x > centerX + laneWidth;
            case "east":
                return x >= centerX - laneWidth - 30 && x < centerX - laneWidth;
        }
        return false;
    }
    
    private boolean areMovingTowardsEachOther(Vehicle vehicle1, Vehicle vehicle2) {
        String dir1 = vehicle1.getDirectionNow();
        String dir2 = vehicle2.getDirectionNow();
        
        // Check if vehicles are moving in opposite directions on same axis
        return (dir1.equals("north") && dir2.equals("south")) ||
               (dir1.equals("south") && dir2.equals("north")) ||
               (dir1.equals("east") && dir2.equals("west")) ||
               (dir1.equals("west") && dir2.equals("east"));
    }
    
    private boolean isInFront(Vehicle vehicle, Vehicle other) {
        String currentDirection = vehicle.getDirectionNow();
        switch (currentDirection) {
            case "north":
                return other.getY() < vehicle.getY(); // Other vehicle is further north
            case "south":
                return other.getY() > vehicle.getY(); // Other vehicle is further south
            case "east":
                return other.getX() > vehicle.getX(); // Other vehicle is further east
            case "west":
                return other.getX() < vehicle.getX(); // Other vehicle is further west
        }
        return false;
    }
    
    private boolean isVehicleOffScreen(Vehicle vehicle) {
        // Remove vehicles when they reach their destination (exit points)
        int x = vehicle.getX();
        int y = vehicle.getY();
        
        // Check if vehicle has reached its exit point based on its current direction
        String currentDirection = vehicle.getDirectionNow();
        
        switch (currentDirection) {
            case "north":
                return y < 50; // Exited to the north
            case "south":
                return y > 450; // Exited to the south
            case "east":
                return x > 550; // Exited to the east
            case "west":
                return x < 50; // Exited to the west
            default:
                // Fallback - remove if way off screen
                return x < -100 || x > 700 || y < -100 || y > 600;
        }
    }
    
    private void render() {
        // Clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 600, 500);
        
        // Draw roads
        drawRoads();
        
        // Draw traffic lights
        drawTrafficLights();
        
        // Draw vehicles
        drawVehicles();
        

        // Print vehicle info to console
        printAllVehicleInfo();
    }
    
    private void drawRoads() {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int roadWidth = roads.getLaneWidth() * 2;
        int roadLength = roads.getRoadLength();
        
        // Draw vertical road
        gc.strokeRect(centerX - roadWidth/2, centerY - roadLength, roadWidth, roadLength * 2);
        
        // Draw horizontal road
        gc.strokeRect(centerX - roadLength, centerY - roadWidth/2, roadLength * 2, roadWidth);
        
        // Draw lane dividers
        gc.setLineWidth(1);
        gc.setStroke(Color.GRAY);
        
        // Vertical lane divider
        gc.strokeLine(centerX, centerY - roadLength, centerX, centerY - roadWidth/2);
        gc.strokeLine(centerX, centerY + roadWidth/2, centerX, centerY + roadLength);
        
        // Horizontal lane divider
        gc.strokeLine(centerX - roadLength, centerY, centerX - roadWidth/2, centerY);
        gc.strokeLine(centerX + roadWidth/2, centerY, centerX + roadLength, centerY);
        
        // Highlight intersection
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(centerX - roadWidth/2, centerY - roadWidth/2, roadWidth, roadWidth);
        
        // Draw directional arrows
        drawDirectionalArrows(centerX, centerY, roadWidth, roadLength);
    }
    
    private void drawDirectionalArrows(int centerX, int centerY, int roadWidth, int roadLength) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(3);
        gc.setFill(Color.BLUE);
        
        int arrowSize = 12;
        int laneWidth = roads.getLaneWidth();
        
        // From NORTH perspective (looking south)
        // Entry (right side from north perspective) - coming FROM north
        int northEntryX = centerX - laneWidth/2; // Right lane from north perspective
        int northEntryY = centerY - roadLength + 40; // Near the start of the path
        drawArrow(northEntryX, northEntryY, "south", arrowSize, "IN");
        
        // Exit (left side from north perspective) - going TO north  
        int northExitX = centerX + laneWidth/2; // Left lane from north perspective
        int northExitY = centerY - roadLength + 40; // Near the end of the path
        drawArrow(northExitX, northExitY, "north", arrowSize, "OUT");
        
        // From SOUTH perspective (looking north)
        // Entry (right side from south perspective) - coming FROM south
        int southEntryX = centerX + laneWidth/2; // Right lane from south perspective
        int southEntryY = centerY + roadLength - 40; // Near the start of the path
        drawArrow(southEntryX, southEntryY, "north", arrowSize, "IN");
        
        // Exit (left side from south perspective) - going TO south
        int southExitX = centerX - laneWidth/2; // Left lane from south perspective  
        int southExitY = centerY + roadLength - 40; // Near the end of the path
        drawArrow(southExitX, southExitY, "south", arrowSize, "OUT");
        
        // From EAST perspective (looking west)
        // Entry (right side from east perspective) - coming FROM east
        int eastEntryX = centerX + roadLength - 40; // Near the start of the path
        int eastEntryY = centerY - laneWidth/2; // Right lane from east perspective
        drawArrow(eastEntryX, eastEntryY, "west", arrowSize, "IN");
        
        // Exit (left side from east perspective) - going TO east
        int eastExitX = centerX + roadLength - 40; // Near the end of the path
        int eastExitY = centerY + laneWidth/2; // Left lane from east perspective
        drawArrow(eastExitX, eastExitY, "east", arrowSize, "OUT");
        
        // From WEST perspective (looking east)
        // Entry (right side from west perspective) - coming FROM west
        int westEntryX = centerX - roadLength + 40; // Near the start of the path
        int westEntryY = centerY + laneWidth/2; // Right lane from west perspective
        drawArrow(westEntryX, westEntryY, "east", arrowSize, "IN");
        
        // Exit (left side from west perspective) - going TO west
        int westExitX = centerX - roadLength + 40; // Near the end of the path
        int westExitY = centerY - laneWidth/2; // Left lane from west perspective
        drawArrow(westExitX, westExitY, "west", arrowSize, "OUT");
        
        // Draw direction labels at the ends of roads
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(16));
        
        // Cardinal direction labels at road ends
        gc.fillText("NORTH", centerX - 25, centerY - roadLength + 15);
        gc.fillText("SOUTH", centerX - 25, centerY + roadLength - 5);
        gc.fillText("EAST", centerX + roadLength - 35, centerY + 5);
        gc.fillText("WEST", centerX - roadLength + 5, centerY + 5);
    }
    
    private void drawArrow(int x, int y, String direction, int size, String type) {
        // Set color based on type
        if ("IN".equals(type)) {
            gc.setStroke(Color.GREEN);
            gc.setFill(Color.GREEN);
        } else {
            gc.setStroke(Color.RED);
            gc.setFill(Color.RED);
        }
        
        gc.setLineWidth(2);
        
        // Draw arrow based on direction
        switch (direction) {
            case "north":
                // Arrow pointing up
                gc.strokeLine(x, y, x, y - size);
                gc.fillPolygon(new double[]{x - size/3, x, x + size/3}, 
                              new double[]{y - size/2, y - size, y - size/2}, 3);
                break;
            case "south":
                // Arrow pointing down
                gc.strokeLine(x, y, x, y + size);
                gc.fillPolygon(new double[]{x - size/3, x, x + size/3}, 
                              new double[]{y + size/2, y + size, y + size/2}, 3);
                break;
            case "east":
                // Arrow pointing right
                gc.strokeLine(x, y, x + size, y);
                gc.fillPolygon(new double[]{x + size/2, x + size, x + size/2}, 
                              new double[]{y - size/3, y, y + size/3}, 3);
                break;
            case "west":
                // Arrow pointing left
                gc.strokeLine(x, y, x - size, y);
                gc.fillPolygon(new double[]{x - size/2, x - size, x - size/2}, 
                              new double[]{y - size/3, y, y + size/3}, 3);
                break;
        }
        
        // Draw label next to arrow
        gc.setFont(Font.font(8));
        gc.fillText(type, x - 8, y + 20);
    }
    
    private void drawTrafficLights() {
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int laneWidth = roads.getLaneWidth();
        
        // Draw traffic lights exactly at the intersection borders/corners
        // North approach - top-left corner of intersection
        drawLight(centerX - laneWidth - 20, centerY - laneWidth - 20, trafficController.getTrafficLight("north"));
        
        // South approach - bottom-right corner of intersection
        drawLight(centerX + laneWidth, centerY + laneWidth, trafficController.getTrafficLight("south"));
        
        // East approach - top-right corner of intersection  
        drawLight(centerX + laneWidth, centerY - laneWidth - 20, trafficController.getTrafficLight("east"));
        
        // West approach - bottom-left corner of intersection
        drawLight(centerX - laneWidth - 20, centerY + laneWidth, trafficController.getTrafficLight("west"));
    }
    
    private void drawLight(int x, int y, TrafficLights light) {
        if (light != null) {
            gc.setFill(Color.BLACK);
            gc.fillRect(x, y, 20, 20);
            
            // Draw light color
            gc.setFill(light.getColor());
            gc.fillOval(x + 2, y + 2, 16, 16);
        }
    }
    
// TrafficSimulation.java
private void drawVehicles() {
    for (Vehicle vehicle : allVehicles) {
        gc.setFill(vehicle.getColor());
        gc.fillRect(vehicle.getX() - 8, vehicle.getY() - 8, 16, 16);
        
        // Draw direction indicator
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font(10));
        
        String label = "S";
        if (vehicle.getDirection().equals("turn left")) {
            label = "L";
        } else if (vehicle.getDirection().equals("turn right")) {
            label = "R";
        }

        // No label for straight
        
        if (!label.isEmpty()) {
            gc.fillText(label, vehicle.getX() - 3, vehicle.getY() + 3);
        }
    }
}
    
    private void printAllVehicleInfo() {
        System.out.println("=== VEHICLE STATUS ===");
        for (Vehicle v : allVehicles) {
            System.out.printf(
                "ID: %d | Vehicle from %s (%s) at (%d,%d) | DirNow: %s | InIntersection: %s | IsMoving: %s | Grid: %s | TurnStep: %d%n",
                v.getId(),
                v.getStartPosition(),
                v.getDirection(),
                v.getX(),
                v.getY(),
                v.getDirectionNow(),
                v.isInIntersection(),
                v.isMoving(),
                getVehicleCurrentGrid(v),
                v.getTurnStep()
            );
        }
        System.out.println("======================");
    }

    // Helper to access currentGrid (if no getter, use reflection)
    private String getVehicleCurrentGrid(Vehicle v) {
        try {
            java.lang.reflect.Field f = v.getClass().getDeclaredField("currentGrid");
            f.setAccessible(true);
            Object val = f.get(v);
            return val == null ? "null" : val.toString();
        } catch (Exception e) {
            return "?";
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

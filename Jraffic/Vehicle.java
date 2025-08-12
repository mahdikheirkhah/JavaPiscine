package Jraffic;

import javafx.scene.paint.Color;


public class Vehicle {
    private final Color color;
    private final String startPosition;
    private String direction;
    private String directionNow;
    private final int velocity;
    private final int safeDistance;
    private boolean isMoving;
    private int length;
    private int x;
    private int y;
    private boolean isInIntersection;
    private int turnStep; // Track which step of the turn we're in
    private int waitCounter; // Counter to detect if vehicle is stuck waiting
    private static final int MAX_WAIT_TIME = 100; // Maximum wait before forcing movement
    private Roads roads; // Reference to roads for grid positions
    private String currentGrid; // Track which grid the vehicle is currently occupying

    public Vehicle(String startPosition, String direction, int velocity, int safeDistance, int length, Roads roads) {
        this.direction = direction;
        this.directionNow = "straight";
        this.velocity = velocity;
        this.safeDistance = safeDistance;
        this.startPosition = startPosition;
        this.length = length;
        this.isMoving = true; // Initialize moving state
        this.isInIntersection = false;
        this.turnStep = 0;
        this.waitCounter = 0;
        this.roads = roads;
        this.currentGrid = null; // Not occupying any grid initially
        
        // Assign color based on direction using proper string comparison
        if ("straight".equals(direction)) {
            this.color = Color.YELLOW;
        } else if ("turn left".equals(direction)) {
            this.color = Color.BLUE;
        } else if ("turn right".equals(direction)) {
            this.color = Color.PURPLE;
        } else {
            this.color = Color.GRAY; // Default color for unknown directions
        }
        
        // Initialize position based on start position
        initializePosition();
    }
    
    private void initializePosition() {
        // Set initial coordinates based on starting position - spawn at lane entrance
        switch (startPosition.toLowerCase()) {
            case "north":
                // Coming from north, spawn at top, center of right lane (going south)
                this.x = 285; // Center of right lane (going south)
                this.y = 100; // Top of screen
                this.directionNow = "south"; // Moving south
                break;
            case "south":
                // Coming from south, spawn at bottom, center of right lane (going north)
                this.x = 315; // Center of right lane (going north)
                this.y = 400; // Bottom of screen
                this.directionNow = "north"; // Moving north
                break;
            case "east":
                // Coming from east, spawn at right, center of right lane (going west)
                this.x = 450; // Right side
                this.y = 235; // Center of right lane (going west)
                this.directionNow = "west"; // Moving west
                break;
            case "west":
                // Coming from west, spawn at left, center of right lane (going east)
                this.x = 150; // Left side
                this.y = 265; // Center of right lane (going east)
                this.directionNow = "east"; // Moving east
                break;
            default:
                this.x = 300;
                this.y = 250; // Default to center
                this.directionNow = "south";
        }
    }
    
    // Getters
    public Color getColor() {
        return color;
    }
    
    public String getStartPosition() {
        return startPosition;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public int getVelocity() {
        return velocity;
    }
    
    public int getSafeDistance() {
        return safeDistance;
    }
    
    public boolean isMoving() {
        return isMoving;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getLength() {
        return length;
    }
    
    // Setters
    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Movement method
    public void move() {
        if (isMoving) {
            // Check if we're entering the intersection
            if (!isInIntersection && isEnteringIntersection()) {
                isInIntersection = true;
                turnStep = 0;
                System.out.println("Vehicle from " + startPosition + " (" + direction + ") entering intersection at (" + x + "," + y + ")");
            }
            
            // If in intersection, follow grid-based movement
            if (isInIntersection) {
                moveInIntersection();
                // Print grid occupation status after movement
                printGridOccupation();
            } else {
                moveForward();
            }
        }
    }
    
    private void updateGridOccupancy() {
        String gridAtCurrentPosition = getGridAtPosition(x, y);
        
        // If we moved to a different grid, update occupancy
        if (!java.util.Objects.equals(currentGrid, gridAtCurrentPosition)) {
            if (gridAtCurrentPosition != null) {
                // Try to occupy the new grid
                if (canMoveToGrid(gridAtCurrentPosition)) {
                    occupyGrid(gridAtCurrentPosition);
                }
            } else {
                // We're no longer at a grid position, release current grid
                releaseCurrentGrid();
            }
        }
        
        // If we've exited the intersection, release the grid
        if (!isInIntersection && currentGrid != null) {
            releaseCurrentGrid();
        }
    }
    
    // Method to check for collision with another vehicle
    public boolean wouldCollideWith(Vehicle other) {
        if (other == null || !other.isInIntersection() || !this.isInIntersection()) {
            return false;
        }
        
        // Calculate distance between vehicles
        int dx = Math.abs(this.x - other.getX());
        int dy = Math.abs(this.y - other.getY());
        
        // Vehicles collide if they're too close (within vehicle length + safe distance)
        int collisionDistance = this.length + this.safeDistance;
        
        return (dx < collisionDistance && dy < collisionDistance);
    }
    
    // Method to check if a position would cause collision
    public boolean wouldCollideAtPosition(int newX, int newY, java.util.List<Vehicle> otherVehicles) {
        for (Vehicle other : otherVehicles) {
            if (other != this && other.isInIntersection()) {
                int dx = Math.abs(newX - other.getX());
                int dy = Math.abs(newY - other.getY());
                int collisionDistance = this.length + this.safeDistance;
                
                if (dx < collisionDistance && dy < collisionDistance) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isEnteringIntersection() {
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int laneWidth = roads.getLaneWidth();
        
        // Check if vehicle is about to enter intersection based on direction
        switch (directionNow.toLowerCase()) {
            case "south":
                return y >= centerY - laneWidth && y < centerY - laneWidth + 5;
            case "north":
                return y <= centerY + laneWidth && y > centerY + laneWidth - 5;
            case "west":
                return x <= centerX + laneWidth && x > centerX + laneWidth - 5;
            case "east":
                return x >= centerX - laneWidth && x < centerX - laneWidth + 5;
        }
        return false;
    }
    
    private void moveInIntersection() {
        if (direction.equals("straight")) {
            // Just move straight through
            moveForward();
            // Check if exited intersection
            if (hasExitedIntersection()) {
                isInIntersection = false;
                releaseCurrentGrid(); // Release grid when exiting intersection
            }
        } else if (direction.equals("turn left")) {
            moveLeftTurn();
        } else if (direction.equals("turn right")) {
            moveRightTurn();
        }
    }
    
    private void moveLeftTurn() {
        switch (startPosition.toLowerCase()) {
            case "east": // Coming from east, turning left to north
                if (turnStep == 0) {
                    // Step 1: Move to north-east grid (first grid straight ahead)
                    if (x > roads.getNorthEastGridX()) {
                        moveForward();
                    } else {
                        x = roads.getNorthEastGridX();
                        releaseCurrentGrid(); // Release previous grid before occupying new one
                        occupyGrid("NE");
                        turnStep = 1;
                    }
                } else if (turnStep == 1) {
                    // Step 2: Move to north-west grid (second grid, still going straight west)
                    if (x > roads.getNorthWestGridX()) {
                        moveForward();
                    } else {
                        x = roads.getNorthWestGridX();
                        releaseCurrentGrid(); // Release NE grid before occupying NW
                        occupyGrid("NW");
                        turnStep = 2;
                    }
                } else if (turnStep == 2) {
                    // Step 3: Now turn left (south) towards south-west grid
                    if (y < roads.getSouthWestGridY()) {
                        y += velocity;
                        directionNow = "south";
                    } else {
                        y = roads.getSouthWestGridY();
                        releaseCurrentGrid(); // Release NW grid before occupying SW
                        occupyGrid("SW");
                        turnStep = 3;
                    }
                } else {
                    // Step 4: Exit intersection going south (which leads to north exit)
                    directionNow = "south";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        x = roads.getSouthWestGridX(); // Ensure proper lane
                    }
                }
                break;
                
            case "west": // Coming from west, turning left to south
                if (turnStep == 0) {
                    // Step 1: Move to south-west grid (first grid straight ahead)
                    if (x < roads.getSouthWestGridX()) {
                        moveForward();
                    } else {
                        x = roads.getSouthWestGridX();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("SW");
                        turnStep = 1;
                    }
                } else if (turnStep == 1) {
                    // Step 2: Move to south-east grid (second grid, still going straight east)
                    if (x < roads.getSouthEastGridX()) {
                        moveForward();
                    } else {
                        x = roads.getSouthEastGridX();
                        releaseCurrentGrid(); // Release SW grid
                        occupyGrid("SE");
                        turnStep = 2;
                    }
                } else if (turnStep == 2) {
                    // Step 3: Now turn left (north) towards north-east grid
                    if (y > roads.getNorthEastGridY()) {
                        y -= velocity;
                        directionNow = "north";
                    } else {
                        y = roads.getNorthEastGridY();
                        releaseCurrentGrid(); // Release SE grid
                        occupyGrid("NE");
                        turnStep = 3;
                    }
                } else {
                    // Step 4: Exit intersection going north (which leads to south exit)
                    directionNow = "north";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        x = roads.getNorthEastGridX(); // Ensure proper lane
                    }
                }
                break;
                
            case "north": // Coming from north, turning left to west
                if (turnStep == 0) {
                    // Step 1: Move to north-west grid (first grid straight ahead)
                    if (y < roads.getNorthWestGridY()) {
                        moveForward();
                    } else {
                        y = roads.getNorthWestGridY();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("NW");
                        turnStep = 1;
                    }
                } else if (turnStep == 1) {
                    // Step 2: Move to south-west grid (second grid, still going straight south)
                    if (y < roads.getSouthWestGridY()) {
                        moveForward();
                    } else {
                        y = roads.getSouthWestGridY();
                        releaseCurrentGrid(); // Release NW grid
                        occupyGrid("SW");
                        turnStep = 2;
                    }
                } else if (turnStep == 2) {
                    // Step 3: Now turn left (east) towards south-east grid
                    if (x < roads.getSouthEastGridX()) {
                        x += velocity;
                        directionNow = "east";
                    } else {
                        x = roads.getSouthEastGridX();
                        releaseCurrentGrid(); // Release SW grid
                        occupyGrid("SE");
                        turnStep = 3;
                    }
                } else {
                    // Step 4: Exit intersection going east (which leads to west exit)
                    directionNow = "east";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        y = roads.getSouthEastGridY(); // Ensure proper lane
                    }
                }
                break;
                
            case "south": // Coming from south, turning left to east
                if (turnStep == 0) {
                    // Step 1: Move to south-east grid (first grid straight ahead)
                    if (y > roads.getSouthEastGridY()) {
                        moveForward();
                    } else {
                        y = roads.getSouthEastGridY();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("SE");
                        turnStep = 1;
                    }
                } else if (turnStep == 1) {
                    // Step 2: Move to north-east grid (second grid, still going straight north)
                    if (y > roads.getNorthEastGridY()) {
                        moveForward();
                    } else {
                        y = roads.getNorthEastGridY();
                        releaseCurrentGrid(); // Release SE grid
                        occupyGrid("NE");
                        turnStep = 2;
                    }
                } else if (turnStep == 2) {
                    // Step 3: Now turn left (west) towards north-west grid  
                    if (x > roads.getNorthWestGridX()) {
                        x -= velocity;
                        directionNow = "west";
                    } else {
                        x = roads.getNorthWestGridX();
                        releaseCurrentGrid(); // Release NE grid
                        occupyGrid("NW");
                        turnStep = 3;
                    }
                } else {
                    // Step 4: Exit intersection going west (which leads to east exit)
                    directionNow = "west";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        y = roads.getNorthWestGridY(); // Ensure proper lane
                    }
                }
                break;
        }
    }
    
    private void moveRightTurn() {
        switch (startPosition.toLowerCase()) {
            case "east": // Coming from east, turning right to south
                if (turnStep == 0) {
                    // Step 1: Move to north-east grid (first grid straight)
                    if (x > roads.getNorthEastGridX()) {
                        moveForward();
                    } else {
                        x = roads.getNorthEastGridX();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("NE");
                        turnStep = 1;
                    }
                } else {
                    // Step 2: Now turn north and exit
                    directionNow = "north";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        x = roads.getNorthEastGridX(); // Stay in correct exit lane
                    }
                }
                break;
                
            case "west": // Coming from west, turning right to north
                if (turnStep == 0) {
                    // Step 1: Move to south-west grid (first grid straight)
                    if (x < roads.getSouthWestGridX()) {
                        moveForward();
                    } else {
                        x = roads.getSouthWestGridX();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("SW");
                        turnStep = 1;
                    }
                } else {
                    // Step 2: Now turn south and exit
                    directionNow = "south";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        x = roads.getSouthWestGridX(); // Stay in correct exit lane
                    }
                }
                break;
                
            case "north": // Coming from north, turning right to east
                if (turnStep == 0) {
                    // Step 1: Move to north-west grid (first grid straight)
                    if (y < roads.getNorthWestGridY()) {
                        moveForward();
                    } else {
                        y = roads.getNorthWestGridY();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("NW");
                        turnStep = 1;
                    }
                } else {
                    // Step 2: Now turn west and exit
                    directionNow = "west";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        y = roads.getNorthWestGridY(); // Stay in correct exit lane
                    }
                }
                break;
                
            case "south": // Coming from south, turning right to west
                if (turnStep == 0) {
                    // Step 1: Move to south-east grid (first grid straight)
                    if (y > roads.getSouthEastGridY()) {
                        moveForward();
                    } else {
                        y = roads.getSouthEastGridY();
                        releaseCurrentGrid(); // Release previous grid
                        occupyGrid("SE");
                        turnStep = 1;
                    }
                } else {
                    // Step 2: Now turn east and exit
                    directionNow = "east";
                    moveForward();
                    if (hasExitedIntersection()) {
                        isInIntersection = false;
                        releaseCurrentGrid(); // Release grid when exiting intersection
                        y = roads.getSouthEastGridY(); // Stay in correct exit lane
                    }
                }
                break;
        }
    }
    
    private boolean hasExitedIntersection() {
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int laneWidth = roads.getLaneWidth();
        
        // Check if vehicle has completely exited the intersection
        return (x < centerX - laneWidth || x > centerX + laneWidth ||
                y < centerY - laneWidth || y > centerY + laneWidth);
    }
    
    private void moveForward() {
        // Move based on current direction
        switch (directionNow.toLowerCase()) {
            case "north":
                y -= velocity;
                break;
            case "south":
                y += velocity; 
                break;
            case "east":
                x += velocity; 
                break;
            case "west":
                x -= velocity; 
                break;
        }
    }
    
    // Getter for current movement direction
    public String getDirectionNow() {
        return directionNow;
    }
    
    // Getter for intersection status
    public boolean isInIntersection() {
        return isInIntersection;
    }
    
    // Getter for turn step (for debugging)
    public int getTurnStep() {
        return turnStep;
    }
    
    // Grid occupancy management methods
    private boolean canMoveToGrid(String targetGrid) {
        if (targetGrid == null) return true; // Can move to non-grid positions
        return !roads.isGridOccupied(targetGrid);
    }
    
    private boolean occupyGrid(String gridName) {
        if (gridName != null) {
            // Simple atomic check and occupy operation
            if (roads.occupyGrid(gridName)) {
                // Successfully occupied the new grid
                currentGrid = gridName;
                waitCounter = 0; // Reset wait counter on successful movement
                System.out.println("Vehicle from " + startPosition + " (" + direction + ") OCCUPIED grid: " + gridName + " at (" + x + "," + y + ")");
                return true;
            } else {
                System.out.println("Vehicle from " + startPosition + " (" + direction + ") FAILED to occupy grid: " + gridName + " (already occupied)");
            }
        }
        return false; // Could not occupy the grid (it's occupied by another vehicle)
    }
    
    private void releaseCurrentGrid() {
        if (currentGrid != null) {
            System.out.println("Vehicle from " + startPosition + " (" + direction + ") RELEASED grid: " + currentGrid + " at (" + x + "," + y + ")");
            roads.releaseGrid(currentGrid);
            currentGrid = null;
        }
    }
    
    private String getGridAtPosition(int x, int y) {
        return roads.getGridAtPosition(x, y);
    }
    
    // Debug method to print grid occupation status
    private void printGridOccupation() {
        System.out.println("=== Grid Occupation Status ===");
        System.out.println("Vehicle from " + startPosition + " (" + direction + ") at (" + x + "," + y + ") - Step: " + turnStep + " - Current Grid: " + currentGrid);
        System.out.println("NW Grid: " + (roads.isGridOccupied("NW") ? "OCCUPIED" : "FREE"));
        System.out.println("NE Grid: " + (roads.isGridOccupied("NE") ? "OCCUPIED" : "FREE"));
        System.out.println("SW Grid: " + (roads.isGridOccupied("SW") ? "OCCUPIED" : "FREE"));
        System.out.println("SE Grid: " + (roads.isGridOccupied("SE") ? "OCCUPIED" : "FREE"));
        System.out.println("==============================");
    }
}

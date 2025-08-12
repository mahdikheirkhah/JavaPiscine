package Jraffic;

import java.util.HashMap;
import java.util.Map;

public class Roads {
    private Lane eastLane;
    private Lane westLane;
    private Lane northLane;
    private Lane southLane;
    private Map<String, Lane> lanes;
    private int[][] intersectionBounds; // [x1, y1, x2, y2] for intersection area
    private int centerX = 300;
    private int centerY = 250;
    private int laneWidth = 30;
    private int roadLength = 150;
    
    // Intersection grid positions (4 key points for turning)
    private int northWestGridX, northWestGridY;
    private int northEastGridX, northEastGridY;
    private int southWestGridX, southWestGridY;
    private int southEastGridX, southEastGridY;
    
    // Grid occupancy tracking with boolean fields
    private boolean nwGridOccupied = false;
    private boolean neGridOccupied = false; 
    private boolean swGridOccupied = false;
    private boolean seGridOccupied = false;
    
    private static final String NW_GRID = "NW";
    private static final String NE_GRID = "NE";
    private static final String SW_GRID = "SW";
    private static final String SE_GRID = "SE";
    
    public Roads() {
        initializeIntersectionGrids();
        initializeLanes();
        this.intersectionBounds = new int[][]{
            {centerX - laneWidth, centerY - laneWidth},
            {centerX + laneWidth, centerY + laneWidth}
        };
    }
    
    private void initializeIntersectionGrids() {
        // Define the 4 grid points in the intersection
        northWestGridX = centerX - laneWidth/2;
        northWestGridY = centerY - laneWidth/2;
        
        northEastGridX = centerX + laneWidth/2;
        northEastGridY = centerY - laneWidth/2;
        
        southWestGridX = centerX - laneWidth/2;
        southWestGridY = centerY + laneWidth/2;
        
        southEastGridX = centerX + laneWidth/2;
        southEastGridY = centerY + laneWidth/2;
    }
    
    private void initializeLanes() {
        lanes = new HashMap<>();
        
        // North lane (vehicles coming from north, going south)
        northLane = new Lane(
            centerX - laneWidth/2, centerY - roadLength,  // spawn
            centerX - laneWidth/2, centerY - laneWidth/2, // destination
            roadLength - laneWidth/2, laneWidth, "north"
        );
        
        // South lane (vehicles coming from south, going north)
        southLane = new Lane(
            centerX + laneWidth/2, centerY + roadLength,  // spawn
            centerX + laneWidth/2, centerY + laneWidth/2, // destination
            roadLength - laneWidth/2, laneWidth, "south"
        );
        
        // East lane (vehicles coming from east, going west)
        eastLane = new Lane(
            centerX + roadLength, centerY - laneWidth/2,  // spawn
            centerX + laneWidth/2, centerY - laneWidth/2, // destination
            roadLength - laneWidth/2, laneWidth, "east"
        );
        
        // West lane (vehicles coming from west, going east)
        westLane = new Lane(
            centerX - roadLength, centerY + laneWidth/2,  // spawn
            centerX - laneWidth/2, centerY + laneWidth/2, // destination
            roadLength - laneWidth/2, laneWidth, "west"
        );
        
        lanes.put("north", northLane);
        lanes.put("south", southLane);
        lanes.put("east", eastLane);
        lanes.put("west", westLane);
    }
    
    public Lane getLane(String direction) {
        return lanes.get(direction.toLowerCase());
    }
    
    public boolean isIntersection(int x, int y) {
        return x >= intersectionBounds[0][0] && x <= intersectionBounds[1][0] &&
               y >= intersectionBounds[0][1] && y <= intersectionBounds[1][1];
    }
    
    public Map<String, Lane> getAllLanes() {
        return new HashMap<>(lanes);
    }
    
    public double getOverallCongestion() {
        double totalCongestion = 0;
        for (Lane lane : lanes.values()) {
            totalCongestion += lane.getCongestionLevel();
        }
        return totalCongestion / lanes.size();
    }
    
    // Getters
    public int getCenterX() { return centerX; }
    public int getCenterY() { return centerY; }
    public int getLaneWidth() { return laneWidth; }
    public int getRoadLength() { return roadLength; }
    
    // Intersection grid getters
    public int getNorthWestGridX() { return northWestGridX; }
    public int getNorthWestGridY() { return northWestGridY; }
    public int getNorthEastGridX() { return northEastGridX; }
    public int getNorthEastGridY() { return northEastGridY; }
    public int getSouthWestGridX() { return southWestGridX; }
    public int getSouthWestGridY() { return southWestGridY; }
    public int getSouthEastGridX() { return southEastGridX; }
    public int getSouthEastGridY() { return southEastGridY; }
    
    // Grid occupancy management methods
    public boolean isGridOccupied(String gridName) {
        switch (gridName) {
            case NW_GRID: return nwGridOccupied;
            case NE_GRID: return neGridOccupied;
            case SW_GRID: return swGridOccupied;
            case SE_GRID: return seGridOccupied;
            default: return false;
        }
    }
    
    public boolean occupyGrid(String gridName) {
        if (!isGridOccupied(gridName)) {
            setGridOccupied(gridName, true);
            return true;
        }
        return false;
    }
    
    public void releaseGrid(String gridName) {
        setGridOccupied(gridName, false);
    }
    
    public void setGridOccupied(String gridName, boolean occupied) {
        switch (gridName) {
            case NW_GRID: nwGridOccupied = occupied; break;
            case NE_GRID: neGridOccupied = occupied; break;
            case SW_GRID: swGridOccupied = occupied; break;
            case SE_GRID: seGridOccupied = occupied; break;
        }
    }
    
    public String getGridAtPosition(int x, int y) {
        int tolerance = 10; // Allow some tolerance for position matching
        
        if (Math.abs(x - northWestGridX) <= tolerance && Math.abs(y - northWestGridY) <= tolerance) {
            return NW_GRID;
        } else if (Math.abs(x - northEastGridX) <= tolerance && Math.abs(y - northEastGridY) <= tolerance) {
            return NE_GRID;
        } else if (Math.abs(x - southWestGridX) <= tolerance && Math.abs(y - southWestGridY) <= tolerance) {
            return SW_GRID;
        } else if (Math.abs(x - southEastGridX) <= tolerance && Math.abs(y - southEastGridY) <= tolerance) {
            return SE_GRID;
        }
        return null; // Not at any specific grid
    }
}

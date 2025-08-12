package Jraffic;

import java.util.ArrayList;
import java.util.List;

public class Lane {
    private int spawnX;
    private int spawnY;
    private int destinationX;
    private int destinationY;
    private int length;
    private int width;
    private List<Vehicle> vehicles;
    private int maxCapacity;
    private String direction; // "north", "south", "east", "west"

    public Lane(int spawnX, int spawnY, int destinationX, int destinationY, int length, int width, String direction) {
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.length = length;
        this.width = width;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.direction = direction;
        this.vehicles = new ArrayList<>();
        
        // Calculate max capacity based on vehicle length and safe distance
        int vehicleLength = 20; // Default vehicle length
        int safeGap = 10; // Default safe gap
        this.maxCapacity = length / (vehicleLength + safeGap);
    }

    public boolean canAddVehicle() {
        return vehicles.size() < maxCapacity;
    }
    
    public void addVehicle(Vehicle vehicle) {
        if (canAddVehicle()) {
            vehicles.add(vehicle);
        }
    }
    
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }
    
    public int getCurrentLoad() {
        return vehicles.size();
    }
    
    public double getCongestionLevel() {
        return (double) vehicles.size() / maxCapacity;
    }

    // Getters
    public int getSpawnX() {
        return this.spawnX;
    }
    
    public int getSpawnY() {
        return this.spawnY;
    }
    
    public int getDestinationY() {
        return this.destinationY;
    }
    
    public int getDestinationX() {
        return this.destinationX;
    }
    
    public int getLength() {
        return this.length;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public String getDirection() {
        return direction;
    }

}

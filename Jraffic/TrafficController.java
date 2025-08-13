package Jraffic;

import java.util.*;

public class TrafficController {
    private Map<String, TrafficLights> trafficLights;
    private Roads roads;
    private PriorityQueue<LanePriority> lanePriorityQueue;
    private long lastUpdate;
    private final int UPDATE_INTERVAL = 1000; // 1 second
    private final double CONGESTION_THRESHOLD = 0.7; // 70% capacity
    private String currentGreenDirection; // Track current green direction

    public TrafficController(Roads roads) {
        this.roads = roads;
        this.trafficLights = new HashMap<>();
        this.lastUpdate = System.currentTimeMillis();
        
        initializeTrafficLights();
        initializePriorityQueue();
        currentGreenDirection = "east"; // Start with east as green
    }
    
    private void initializeTrafficLights() {
        trafficLights.put("north", new TrafficLights("north"));
        trafficLights.put("south", new TrafficLights("south"));
        trafficLights.put("east", new TrafficLights("east"));
        trafficLights.put("west", new TrafficLights("west"));
        
        // Start with only east green
        trafficLights.get("east").setCurrentState(TrafficLights.LightState.GREEN);
        trafficLights.get("north").setCurrentState(TrafficLights.LightState.RED);
        trafficLights.get("south").setCurrentState(TrafficLights.LightState.RED);
        trafficLights.get("west").setCurrentState(TrafficLights.LightState.RED);
    }
    
    private void initializePriorityQueue() {
        lanePriorityQueue = new PriorityQueue<>(
            (a, b) -> Double.compare(b.congestionLevel, a.congestionLevel)
        );
    }
    
    public void update(List<Vehicle> allVehicles) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate >= UPDATE_INTERVAL) {
            // Check if any vehicle is in intersection
            boolean intersectionOccupied = allVehicles.stream()
                .anyMatch(Vehicle::isInIntersection);
                
            if (!intersectionOccupied) {
                updatePriorityQueue();
                manageLights();
            }
            lastUpdate = currentTime;
        }
    }
    
    private void updatePriorityQueue() {
        lanePriorityQueue.clear();
        
        for (Map.Entry<String, Lane> entry : roads.getAllLanes().entrySet()) {
            String direction = entry.getKey();
            Lane lane = entry.getValue();
            double congestion = lane.getCongestionLevel();
            
            lanePriorityQueue.offer(new LanePriority(direction, congestion, lane.getCurrentLoad()));
        }
    }
    
    // ADD THIS NEW METHOD IN ITS PLACE:
private void manageLights() {
    // Priority 1: Handle high congestion
    LanePriority mostCongested = lanePriorityQueue.peek();
    if (mostCongested != null && mostCongested.congestionLevel > CONGESTION_THRESHOLD) {
        handleCongestion(mostCongested.direction);
        return; 
    }

    // Priority 2: Handle unnecessary waiting
    boolean isGreenLaneEmpty = roads.getLane(currentGreenDirection).getCurrentLoad() == 0;

    // Find a lane that has cars waiting at a red light
    java.util.Optional<LanePriority> waitingLane = lanePriorityQueue.stream()
            .filter(lp -> lp.vehicleCount > 0 && !lp.direction.equals(currentGreenDirection))
            .findFirst();

    // If the current green light is unused AND another lane has cars waiting...
    if (isGreenLaneEmpty && waitingLane.isPresent()) {
        // Switch to the waiting lane immediately, out of order.
        switchToDirection(waitingLane.get().direction);
    } else {
        // Otherwise, fall back to the timed cycle if the timer has expired.
        TrafficLights currentLight = trafficLights.get(currentGreenDirection);
        if (currentLight.shouldSwitch()) {
            findAndSwitchToNextOccupiedLane();
        }
    }
}
// ADD THIS HELPER METHOD:
private void findAndSwitchToNextOccupiedLane() {
    String[] sequence = {"north", "east", "south", "west"};
    int currentIndex = -1;
    for (int i = 0; i < sequence.length; i++) {
        if (sequence[i].equals(currentGreenDirection)) {
            currentIndex = i;
            break;
        }
    }

    // Starting from the current direction, check the next lanes in the sequence
    for (int i = 1; i <= sequence.length; i++) {
        String nextDirection = sequence[(currentIndex + i) % sequence.length];
        if (roads.getLane(nextDirection).getCurrentLoad() > 0) {
            switchToDirection(nextDirection);
            return; // Switched successfully
        }
    }

    // If no cars are waiting anywhere, the light will just remain green on the current lane
    // until a car appears elsewhere and the logic runs again.
}
    
    private void handleCongestion(String congestedDirection) {
        // Only switch if not already green and not same as current green
        if (!congestedDirection.equals(currentGreenDirection)) {
            switchToDirection(congestedDirection);
        } else {
            // Extend green time
            trafficLights.get(currentGreenDirection).extendGreenTime(1000);
        }
    }

    private void switchToDirection(String direction) {
        // Set all lights to red first
        trafficLights.values().forEach(light -> 
            light.setCurrentState(TrafficLights.LightState.RED));
        
        // Set new direction to green
        trafficLights.get(direction).setCurrentState(TrafficLights.LightState.GREEN);
        currentGreenDirection = direction;
        
        updateVehiclesAtStopLineAfterLightChange(direction);
    }
    
    private void updateVehiclesAtStopLineAfterLightChange(String direction) {
        Lane lane = roads.getLane(direction);
        if (lane == null) return;
        
        for (Vehicle v : lane.getVehicles()) {
            if (!v.isInIntersection() && isAtStopLine(v, direction)) {
                v.setMoving(true);
            }
        }
    }
    
    private boolean isAtStopLine(Vehicle v, String dir) {
        int x = v.getX();
        int y = v.getY();
        int centerX = roads.getCenterX();
        int centerY = roads.getCenterY();
        int laneWidth = roads.getLaneWidth();
        
        switch (dir) {
            case "north":
                return y >= centerY - laneWidth - 15 && y <= centerY - laneWidth + 5 &&
                       x >= centerX - laneWidth/2 - 10 && x <= centerX - laneWidth/2 + 10;
            case "south":
                return y <= centerY + laneWidth + 15 && y >= centerY + laneWidth - 5 &&
                       x >= centerX + laneWidth/2 - 10 && x <= centerX + laneWidth/2 + 10;
            case "east":
                return x <= centerX + laneWidth + 15 && x >= centerX + laneWidth - 5 &&
                       y >= centerY - laneWidth/2 - 10 && y <= centerY - laneWidth/2 + 10;
            case "west":
                return x >= centerX - laneWidth - 15 && x <= centerX - laneWidth + 5 &&
                       y >= centerY + laneWidth/2 - 10 && y <= centerY + laneWidth/2 + 10;
            default: return false;
        }
    }
    
    public boolean canVehiclePass(String direction) {
        TrafficLights light = trafficLights.get(direction);
        return light != null && light.getCurrentState() == TrafficLights.LightState.GREEN;
    }
    
    public TrafficLights getTrafficLight(String direction) {
        return trafficLights.get(direction);
    }
    
    public Map<String, TrafficLights> getAllTrafficLights() {
        return new HashMap<>(trafficLights);
    }
    
    private static class LanePriority {
        String direction;
        double congestionLevel;
        int vehicleCount;
        
        LanePriority(String direction, double congestionLevel, int vehicleCount) {
            this.direction = direction;
            this.congestionLevel = congestionLevel;
            this.vehicleCount = vehicleCount;
        }
    }
}
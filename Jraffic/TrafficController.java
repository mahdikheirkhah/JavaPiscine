package Jraffic;

import java.util.*;

public class TrafficController {
    private Map<String, TrafficLights> trafficLights;
    private Roads roads;
    private PriorityQueue<LanePriority> lanePriorityQueue;
    private long lastUpdate;
    private final int UPDATE_INTERVAL = 1000; // 1 second
    private final double CONGESTION_THRESHOLD = 0.7; // 70% capacity
    
    public TrafficController(Roads roads) {
        this.roads = roads;
        this.trafficLights = new HashMap<>();
        this.lastUpdate = System.currentTimeMillis();
        
        initializeTrafficLights();
        initializePriorityQueue();
    }
    
    private void initializeTrafficLights() {
        trafficLights.put("north", new TrafficLights("north"));
        trafficLights.put("south", new TrafficLights("south"));
        trafficLights.put("east", new TrafficLights("east"));
        trafficLights.put("west", new TrafficLights("west"));
        
        // Start with east-west green, north-south red
        trafficLights.get("east").setCurrentState(TrafficLights.LightState.GREEN);
        trafficLights.get("west").setCurrentState(TrafficLights.LightState.GREEN);
    }
    
    private void initializePriorityQueue() {
        // Priority queue that prioritizes lanes with higher congestion
        lanePriorityQueue = new PriorityQueue<>(
            (a, b) -> Double.compare(b.congestionLevel, a.congestionLevel)
        );
    }
    
    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate >= UPDATE_INTERVAL) {
            updatePriorityQueue();
            manageLights();
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
    
    private void manageLights() {
        // Get the most congested lane
        LanePriority mostCongested = lanePriorityQueue.peek();
        
        if (mostCongested != null && mostCongested.congestionLevel > CONGESTION_THRESHOLD) {
            handleCongestion(mostCongested.direction);
        } else {
            // Normal cycling
            normalLightCycle();
        }
    }
    
    private void handleCongestion(String congestedDirection) {
        TrafficLights congestedLight = trafficLights.get(congestedDirection);
        
        // If the congested lane's light is red, prioritize switching it to green
        if (congestedLight.getCurrentState() == TrafficLights.LightState.RED) {
            switchLightGroup(congestedDirection);
            // Extend green time for congested lane
            congestedLight.extendGreenTime(2000); // Add 2 seconds
        } else if (congestedLight.getCurrentState() == TrafficLights.LightState.GREEN) {
            // Extend current green time
            congestedLight.extendGreenTime(1000); // Add 1 second
        }
    }
    
    private void normalLightCycle() {
        // Check if any lights should switch based on their timers
        for (TrafficLights light : trafficLights.values()) {
            if (light.shouldSwitch()) {
                // Switch the entire group (north-south or east-west)
                if (light.getPosition().equals("north") || light.getPosition().equals("south")) {
                    switchLightGroup("north"); // This will handle both north and south
                } else {
                    switchLightGroup("east"); // This will handle both east and west
                }
                break; // Only switch one group at a time
            }
        }
    }
    
    private void switchLightGroup(String direction) {
        if (direction.equals("north") || direction.equals("south")) {
            // Switch north-south group
            TrafficLights northLight = trafficLights.get("north");
            TrafficLights southLight = trafficLights.get("south");
            TrafficLights eastLight = trafficLights.get("east");
            TrafficLights westLight = trafficLights.get("west");
            
            if (northLight.getCurrentState() == TrafficLights.LightState.RED) {
                // Switch to green for north-south, red for east-west
                northLight.setCurrentState(TrafficLights.LightState.GREEN);
                southLight.setCurrentState(TrafficLights.LightState.GREEN);
                eastLight.setCurrentState(TrafficLights.LightState.RED);
                westLight.setCurrentState(TrafficLights.LightState.RED);
            } else {
                // Switch to red for north-south, green for east-west
                northLight.setCurrentState(TrafficLights.LightState.RED);
                southLight.setCurrentState(TrafficLights.LightState.RED);
                eastLight.setCurrentState(TrafficLights.LightState.GREEN);
                westLight.setCurrentState(TrafficLights.LightState.GREEN);
            }
        } else {
            // Switch east-west group
            TrafficLights northLight = trafficLights.get("north");
            TrafficLights southLight = trafficLights.get("south");
            TrafficLights eastLight = trafficLights.get("east");
            TrafficLights westLight = trafficLights.get("west");
            
            if (eastLight.getCurrentState() == TrafficLights.LightState.RED) {
                // Switch to green for east-west, red for north-south
                eastLight.setCurrentState(TrafficLights.LightState.GREEN);
                westLight.setCurrentState(TrafficLights.LightState.GREEN);
                northLight.setCurrentState(TrafficLights.LightState.RED);
                southLight.setCurrentState(TrafficLights.LightState.RED);
            } else {
                // Switch to red for east-west, green for north-south
                eastLight.setCurrentState(TrafficLights.LightState.RED);
                westLight.setCurrentState(TrafficLights.LightState.RED);
                northLight.setCurrentState(TrafficLights.LightState.GREEN);
                southLight.setCurrentState(TrafficLights.LightState.GREEN);
            }
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
    
    // Inner class for priority queue
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

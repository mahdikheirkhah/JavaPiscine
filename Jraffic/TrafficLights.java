package Jraffic;

import javafx.scene.paint.Color;

public class TrafficLights {
    public enum LightState {
        RED, GREEN
    }
    
    private LightState currentState;
    private String position; // "north", "south", "east", "west"
    private long lastStateChange;
    private int greenDuration; // in milliseconds
    private int redDuration;
    
    public TrafficLights(String position) {
        this.position = position;
        this.currentState = LightState.RED;
        this.greenDuration = 5000; // 5 seconds default
        this.redDuration = 5000; // 5 seconds default
        this.lastStateChange = System.currentTimeMillis();
    }
    
    public void switchState() {
        currentState = (currentState == LightState.RED) ? LightState.GREEN : LightState.RED;
        lastStateChange = System.currentTimeMillis();
    }
    
    public boolean shouldSwitch() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastStateChange;
        
        if (currentState == LightState.GREEN && elapsed >= greenDuration) {
            return true;
        } else if (currentState == LightState.RED && elapsed >= redDuration) {
            return true;
        }
        return false;
    }
    
    public void extendGreenTime(int additionalTime) {
        if (currentState == LightState.GREEN) {
            this.greenDuration += additionalTime;
        }
    }
    
    // Getters and setters
    public LightState getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(LightState state) {
        this.currentState = state;
        this.lastStateChange = System.currentTimeMillis();
    }
    
    public String getPosition() {
        return position;
    }
    
    public Color getColor() {
        return currentState == LightState.GREEN ? Color.GREEN : Color.RED;
    }
    
    public int getGreenDuration() {
        return greenDuration;
    }
    
    public void setGreenDuration(int greenDuration) {
        this.greenDuration = greenDuration;
    }
    
    public int getRedDuration() {
        return redDuration;
    }
    
    public void setRedDuration(int redDuration) {
        this.redDuration = redDuration;
    }
}

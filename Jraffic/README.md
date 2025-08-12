## Jraffic

### Objectives

Traffic congestion can be a major problem in urban areas. Your task is to create a traffic control strategy and visualize it with a simulation. The choice of library and file system is up to you. However, you might find the JavaFX library useful for creating GUI applications.

### Setup and Installation

#### Prerequisites

- Java JDK 11 or higher
- JavaFX SDK (since JavaFX was removed from JDK starting with Java 11)

#### JavaFX Setup

1. **Download JavaFX SDK:**
   - Visit [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)
   - Download the appropriate version for your platform (e.g., `javafx-sdk-23.0.1`)
   - Extract the downloaded archive to your project directory

2. **Compile the application:**
   ```bash
   # Compile all Java files in the Jraffic package
   javac --module-path javafx-sdk-23.0.1/lib --add-modules javafx.controls,javafx.fxml Jraffic/*.java
   ```

3. **Run the traffic simulation:**
   ```bash
   # From the project root directory
   java --module-path javafx-sdk-23.0.1/lib --add-modules javafx.controls,javafx.fxml -cp . Jraffic.TrafficSimulation
   ```

#### Alternative Setup (if you have JavaFX installed system-wide)

If JavaFX is installed via a package manager:
```bash
# Compile
javac Jraffic/*.java

# Run
java Jraffic.TrafficSimulation
```

> **Note:** The module path and add-modules flags are required because JavaFX is no longer bundled with the JDK. Make sure the path to the JavaFX lib directory matches your installation.

### Vehicle Color Coding

In this implementation, vehicles are color-coded based on their intended route:
- **Yellow vehicles**: Going straight through the intersection
- **Blue vehicles**: Making a left turn
- **Purple vehicles**: Making a right turn

### Traffic Management Features

- **Priority Queue System**: Lanes with higher congestion get priority for green lights
- **Dynamic Light Timing**: Traffic lights adapt their timing based on real-time congestion
- **Collision Avoidance**: Vehicles maintain safe following distances
- **Anti-Spam Protection**: Minimum 2-second delay between vehicle spawns
- **Real-time Monitoring**: Live display of congestion levels for each lane

### Instructions

#### **Environment and Rules**

You should create an environment that includes the objects specified in this section. The representation of the objects is entirely up to you.

**1. Roads**

Create two intersecting roads, each with a single lane in both directions. Traffic approaching the intersection can choose between :

- going straight
- turning left
- turning right

Below is a simple representation:

```console
                        North
                    |  ↓  |  ↑  |
                    |  ↓  |  ↑  |
                    |     |     |
                    |     |     |
                    |     |     |
                    |     |     |
     _______________|     |     |_______________
     ← ←                                     ← ←
East ---------------             --------------- West
     → →                                     → →
     _______________             _______________
                    |     |     |
                    |     |     |
                    |     |     |
                    |     |     |
                    |     |     |
                    |  ↓  |  ↑  |
                    |  ↓  |  ↑  |
                        South
```

**2. Traffic Lights**

Position traffic lights at the points where each lane enters the intersection. Your traffic lights should only have two colors: red and green.

You can implement any algorithm you choose to control the traffic lights system, but bear in mind that **traffic congestion should remain below the lane’s maximum capacity**.

> **Dynamic Congestion Rule:**
> The maximum allowed queue length for each lane is calculated based on the lane’s physical length, vehicle length, and safety gap between vehicles:
>
> ```
> capacity = floor(lane_length / (vehicle_length + safety_gap))
> ```
>
> Where:
>
> - `lane_length`: Distance from the stop line to the vehicle spawn point
> - `vehicle_length`: Approximate car length in simulation units (e.g., pixels or meters)
> - `safety_gap`: Minimum safe distance between vehicles
>
> If the number of vehicles in a lane reaches this capacity, the traffic light logic should adjust (e.g., extend green time for that lane) to prevent overflow.

The primary function of your traffic light system is to avoid collisions between vehicles passing through the intersection, while dynamically adapting to congestion.

**3. Vehicles**

```
  ______
 /|_||_\`.__
=`-(_)--(_)-'
```

The vehicles traveling through your capital city's new junction must follow these rules:

- Vehicles must be painted in a color that illustrates the route they will follow. The colors are up to you to decide, and your choices will need to be made available during the audit of the raid. For example, all cars which make a right turn could be painted yellow. It's really up to you though.

- It is not possible for the vehicle to change its selected route.

- Each vehicle must have a fixed velocity.

- A safety distance from other vehicles must be maintained. If one vehicle stops, the following vehicle must also stop before it gets too close to the stationary vehicle in front.

- Vehicles must stop if the traffic light is red and proceed otherwise.

- There are no other vehicle types with special privileges. You can consider that there are no emergency vehicles in your capital city.

---

#### **Commands**

You will use your keyboard to spawn vehicles for your simulation. You will use the arrow keys to spawn a vehicle on the appropriate side of the road, and with a random route.

- **`↑` Up:** moves towards the intersection **from the south.**
- **`↓` Down:** moves towards the intersection **from the north.**
- **`→` Right:** moves towards the intersection **from the west.**
- **`←` Left:** moves towards the intersection **from the east.**
- **`r`:** moves towards the intersection **from a random direction.**
- **`Esc` Escape:** ends the simulation.

> It must not be possible to use the keyboard to spam the creation of vehicles; they must be created with a safe distance between them.

> A safe distance is any distance which enables the vehicles to avoid crashing into each other.

### Example

You can see an example for road_intersection [here](https://www.youtube.com/watch?v=6B0-ZBET6mo).

### Bonus

You can implement the following optional features:

- Vehicle and traffic light animations, and image rendering. You can find some cool assets here:
  - [limezu](https://limezu.itch.io/)
  - [finalbossblue](http://finalbossblues.com/timefantasy/free-graphics/).
  - [mobilegamegraphics](https://mobilegamegraphics.com/product-category/all_products/freestuff/).
  - [spriters-resource](https://www.spriters-resource.com/).

### Notions

- [JavaFX](https://openjfx.io/openjfx-docs/)
- [Java KeyEvents](https://docs.oracle.com/javase/tutorial/uiswing/events/keylistener.html)
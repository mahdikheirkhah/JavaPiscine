package Jaikin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

public class Main extends JPanel {
    private final List<Point> controlPoints = new ArrayList<>();
    private List<Point> currentPoints = new ArrayList<>();
    private int animationStep = -1; // -1 = not animating, 0-6 = current step
    private Timer animationTimer;
    private static final int POINT_RADIUS = 4;
    private static final int ANIMATION_DELAY = 1000; // ms between steps
    private static final int MAX_ITERATIONS = 6; // 7 total steps: 0-6
    
    // Dragging state
    private int draggedPointIndex = -1;
    private boolean isDragging = false;
    
    // Iteration control
    private int selectedIterations = 3; // Default number of iterations
    private boolean showLinearOnly = false;

    public Main() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // Check if clicking on an existing control point for dragging
                    for (int i = 0; i < controlPoints.size(); i++) {
                        Point p = controlPoints.get(i);
                        double distance = Math.sqrt(Math.pow(p.x - e.getX(), 2) + 
                                                  Math.pow(p.y - e.getY(), 2));
                        if (distance <= POINT_RADIUS + 5) { // Slightly larger hit area
                            draggedPointIndex = i;
                            isDragging = true;
                            return;
                        }
                    }
                    
                    // If not dragging and not animating, add new point
                    if (animationStep == -1) {
                        Point newPoint = new Point(e.getX(), e.getY());
                        
                        // Check if a point already exists very close to this location
                        boolean isDuplicate = false;
                        for (Point existing : controlPoints) {
                            double distance = Math.sqrt(Math.pow(existing.x - newPoint.x, 2) + 
                                                      Math.pow(existing.y - newPoint.y, 2));
                            if (distance < 15.0) { // If within 15 pixels, consider it a duplicate
                                isDuplicate = true;
                                break;
                            }
                        }
                        
                        if (!isDuplicate) {
                            controlPoints.add(newPoint);
                            currentPoints = new ArrayList<>(controlPoints);
                            updateCurveInRealTime();
                            repaint();
                        }
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isDragging = false;
                draggedPointIndex = -1;
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging && draggedPointIndex >= 0 && draggedPointIndex < controlPoints.size()) {
                    // Update the dragged point's position
                    Point newPoint = new Point(e.getX(), e.getY());
                    controlPoints.set(draggedPointIndex, newPoint);
                    currentPoints = new ArrayList<>(controlPoints);
                    
                    // Update curve in real-time if we have enough points
                    updateCurveInRealTime();
                    repaint();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (controlPoints.isEmpty()) {
                        JOptionPane.showMessageDialog(Main.this, 
                            "Please add some control points by clicking on the canvas first!", 
                            "No Points", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        startAnimation();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearCanvas();
                } else if (e.getKeyCode() == KeyEvent.VK_L) {
                    // Toggle linear mode
                    showLinearOnly = !showLinearOnly;
                    updateCurveInRealTime();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    // Press 1 for linear connections
                    showLinearOnly = true;
                    selectedIterations = 0;
                    updateCurveInRealTime();
                    repaint();
                } else if (e.getKeyCode() >= KeyEvent.VK_2 && e.getKeyCode() <= KeyEvent.VK_7) {
                    // Press 2-7 for Chaikin iterations (1-6 iterations)
                    selectedIterations = e.getKeyCode() - KeyEvent.VK_1; // 2->1, 3->2, ..., 7->6
                    showLinearOnly = false;
                    updateCurveInRealTime();
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_0) {
                    // Show original control points only
                    selectedIterations = 0;
                    showLinearOnly = false;
                    updateCurveInRealTime();
                    repaint();
                }
            }
        });
        
        animationTimer = new Timer(ANIMATION_DELAY, _ -> {
            if (animationStep < MAX_ITERATIONS) {
                animationStep++;
                if (controlPoints.size() > 1) {
                    if (animationStep != 0) {
                        // Steps 1-6: Apply Chaikin's algorithm
                        currentPoints = chaikinStep(currentPoints);
                    }
                }
                repaint();
            } else {
                // Animation complete, restart
                animationStep = -1;
                currentPoints = new ArrayList<>(controlPoints);
                repaint();
                animationTimer.stop();
            }
        });
    }

    private void startAnimation() {
        if (controlPoints.isEmpty()) return;
        
        animationStep = -1; // Start from -1 so first increment makes it 0
        currentPoints = new ArrayList<>(controlPoints);
        
        animationTimer.start();
    }

    private void clearCanvas() {
        controlPoints.clear();
        currentPoints.clear();
        animationStep = -1;
        animationTimer.stop();
        repaint();
    }
    
    private void updateCurveInRealTime() {
        if (showLinearOnly) {
            // Show linear connections only
            currentPoints = new ArrayList<>(controlPoints);
        } else if (selectedIterations == 0) {
            // Show original control points only
            currentPoints = new ArrayList<>(controlPoints);
        } else if (controlPoints.size() >= 3) {
            // Apply selected number of Chaikin iterations
            List<Point> previewPoints = new ArrayList<>(controlPoints);
            for (int i = 0; i < selectedIterations; i++) {
                previewPoints = chaikinStep(previewPoints);
                if (previewPoints.size() < 3) break;
            }
            currentPoints = previewPoints;
        } else {
            currentPoints = new ArrayList<>(controlPoints);
        }
    }

    private List<Point> chaikinStep(List<Point> points) {
        List<Point> newPoints = new ArrayList<>();
        
        if (points.size() < 3) {
            return new ArrayList<>(points);
        }
        
        // TRUE CHAIKIN'S ALGORITHM FOR OPEN CURVES:
        // For each interior vertex, replace it with two new points
        // Keep the first and last vertices unchanged for open curves
        
        // Keep the first point unchanged
        newPoints.add(new Point(points.get(0).x, points.get(0).y));
        
        // For each interior vertex (not the first or last)
        for (int i = 1; i < points.size() - 1; i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            Point next = points.get(i + 1);
            
            // Create two new points:
            // Q1 = 3/4 * prev + 1/4 * curr
            // Q2 = 1/4 * curr + 3/4 * next
            Point q1 = interpolatePoint(prev, curr, 0.75);  // Closer to current
            Point q2 = interpolatePoint(curr, next, 0.25);  // Closer to current
            
            newPoints.add(q1);
            newPoints.add(q2);
        }
        
        // Keep the last point unchanged
        newPoints.add(new Point(points.get(points.size() - 1).x, points.get(points.size() - 1).y));
        
        return newPoints;
    }

    private Point interpolatePoint(Point p1, Point p2, double t) {
        double x = p1.x * (1.0 - t) + p2.x * t;
        double y = p1.y * (1.0 - t) + p2.y * t;
        return new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw control points with circles
        for (int i = 0; i < controlPoints.size(); i++) {
            Point p = controlPoints.get(i);
            
            // Highlight the dragged point
            if (isDragging && i == draggedPointIndex) {
                g2d.setColor(Color.ORANGE);
                // Draw larger circle for dragged point
                g2d.fillOval((int)(p.x - POINT_RADIUS - 2), (int)(p.y - POINT_RADIUS - 2), 
                            (POINT_RADIUS + 2) * 2, (POINT_RADIUS + 2) * 2);
                g2d.setColor(Color.BLACK);
                g2d.drawOval((int)(p.x - POINT_RADIUS - 2), (int)(p.y - POINT_RADIUS - 2), 
                            (POINT_RADIUS + 2) * 2, (POINT_RADIUS + 2) * 2);
            } else {
                g2d.setColor(Color.RED);
                // Draw normal circle
                g2d.fillOval((int)(p.x - POINT_RADIUS), (int)(p.y - POINT_RADIUS), 
                            POINT_RADIUS * 2, POINT_RADIUS * 2);
                g2d.setColor(Color.BLACK);
                g2d.drawOval((int)(p.x - POINT_RADIUS), (int)(p.y - POINT_RADIUS), 
                            POINT_RADIUS * 2, POINT_RADIUS * 2);
            }
        }
        
        // Draw real-time curve preview (when not animating and have points)
        if (animationStep == -1 && !currentPoints.isEmpty()) {
            if (showLinearOnly) {
                // Draw linear connections in green
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(2.0f));
            } else if (selectedIterations == 0) {
                // Draw dots for control points only (no lines)
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{5}, 0));
            } else {
                // Draw smoothed curve in blue
                g2d.setColor(new Color(0, 0, 255, 180)); // Semi-transparent blue
                g2d.setStroke(new BasicStroke(2.0f));
            }
            
            if (currentPoints.size() >= 2 && (showLinearOnly || selectedIterations > 0)) {
                Path2D.Double path = new Path2D.Double();
                Point first = currentPoints.get(0);
                path.moveTo(first.x, first.y);
                
                for (int i = 1; i < currentPoints.size(); i++) {
                    Point p = currentPoints.get(i);
                    path.lineTo(p.x, p.y);
                }
                
                g2d.draw(path);
            }
        }
        
        // Draw animation curve
        if (animationStep >= 0 && !currentPoints.isEmpty()) {
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2.0f));
            
            if (currentPoints.size() >= 2) {
                Path2D.Double path = new Path2D.Double();
                Point first = currentPoints.get(0);
                path.moveTo(first.x, first.y);
                
                for (int i = 1; i < currentPoints.size(); i++) {
                    Point p = currentPoints.get(i);
                    path.lineTo(p.x, p.y);
                }
                
                g2d.draw(path);
            }
        }
        
        // Draw step information
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        if (animationStep == -1) {
            g2d.drawString("Click to add control points, drag to move them", 10, 25);
            g2d.drawString("Press 1 for linear, 2-7 for iterations, ENTER for animation", 10, 45);
            g2d.drawString("Press 0 for points only, C to clear, ESC to exit", 10, 65);
            
            // Show current mode
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            if (showLinearOnly) {
                g2d.setColor(Color.GREEN);
                g2d.drawString("Mode: 1 - Linear connections", 10, 85);
            } else if (selectedIterations == 0) {
                g2d.setColor(Color.GRAY);
                g2d.drawString("Mode: 0 - Control points only", 10, 85);
            } else {
                g2d.setColor(Color.BLUE);
                g2d.drawString("Mode: " + (selectedIterations + 1) + " - " + selectedIterations + " Chaikin iterations", 10, 85);
            }
        } else {
            g2d.drawString("Animation Step: " + animationStep + " / " + MAX_ITERATIONS, 10, 25);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chaikin's Algorithm Visualization");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            Main panel = new Main();
            frame.add(panel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    private static class Point {
        public final double x;
        public final double y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Point point = (Point) obj;
            return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
        }
        
        @Override
        public int hashCode() {
            return java.util.Objects.hash(x, y);
        }
    }
}

package Jaikin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ChaikinsAlgorithm extends JPanel {
    private final List<Point> controlPoints = new ArrayList<>();
    private List<Point> currentPoints = new ArrayList<>();
    private int animationStep = -1; // -1 = not animating, 0-7 = current step
    private Timer animationTimer;
    private static final int POINT_RADIUS = 2;
    private static final int ANIMATION_DELAY = 1000; // ms between steps

    public ChaikinsAlgorithm() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        
        // Mouse listener for adding points
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && animationStep == -1) {
                    controlPoints.add(new Point(e.getX(), e.getY()));
                    currentPoints = new ArrayList<>(controlPoints);
                    repaint();
                }
            }
        });

        // Keyboard controls
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !controlPoints.isEmpty()) {
                    startAnimation();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                } else if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearCanvas();
                }
            }
        });

        // Animation timer
        animationTimer = new Timer(ANIMATION_DELAY, _ -> {
            if (animationStep < 7) { // Changed to 7 for 6 iterations (steps 2-7)
                animationStep++;
                if (controlPoints.size() > 1 && animationStep > 1) {
                    // Only start applying Chaikin's algorithm from step 2 onwards
                    currentPoints = chaikinStep(currentPoints);
                }
                repaint();
            } else {
                // Animation finished, stop the timer
                animationTimer.stop();
            }
        });
    }

    private void startAnimation() {
        if (controlPoints.isEmpty()) return;
        
        animationStep = 0;
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

    private List<Point> chaikinStep(List<Point> points) {
        List<Point> newPoints = new ArrayList<>();
        
        // Need at least 2 points for Chaikin's algorithm
        if (points.size() < 2) {
            return new ArrayList<>(points);
        }
        
        // First, subdivide any very long segments
        List<Point> preprocessedPoints = subdivideLongSegments(points);
        
        if (preprocessedPoints.size() == 2) {
            // For just 2 points, create intermediate points without preserving endpoints
            Point p1 = preprocessedPoints.get(0);
            Point p2 = preprocessedPoints.get(1);
            
            newPoints.add(interpolatePoint(p1, p2, 0.25));
            newPoints.add(interpolatePoint(p1, p2, 0.75));
            
            return newPoints;
        }
        
        // For 3 or more points: standard Chaikin's algorithm without endpoint preservation
        // Process each segment with standard 0.25/0.75 ratio
        for (int i = 0; i < preprocessedPoints.size() - 1; i++) {
            Point p1 = preprocessedPoints.get(i);
            Point p2 = preprocessedPoints.get(i + 1);
            
            // Add standard Chaikin points
            newPoints.add(interpolatePoint(p1, p2, 0.25));
            newPoints.add(interpolatePoint(p1, p2, 0.75));
        }
        
        return newPoints;
    }

    /**
     * Subdivide segments that are too long by adding intermediate points
     * This helps Chaikin's algorithm work better on long distances
     */
    private List<Point> subdivideLongSegments(List<Point> points) {
        List<Point> result = new ArrayList<>();
        if (points.isEmpty()) return result;
        
        result.add(points.get(0));
        
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            double dist = distance(p1, p2);
            
            // If segment is very long, add intermediate points
            if (dist > 150) {
                // Add one intermediate point at the middle
                result.add(interpolatePoint(p1, p2, 0.5));
            }
            
            result.add(p2);
        }
        
        return result;
    }

    /**
     * Linear interpolation between two points
     * @param p1 First point
     * @param p2 Second point 
     * @param t Value between 0.0 and 1.0 (0.0 returns p1, 1.0 returns p2)
     * @return New point at position t along the line from p1 to p2
     */
    private Point interpolatePoint(Point p1, Point p2, double t) {
        double x = p1.x * (1.0 - t) + p2.x * t;
        double y = p1.y * (1.0 - t) + p2.y * t;
        return new Point(x, y);
    }

    /**
     * Calculate distance between two points
     */
    private double distance(Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw control points
        g2d.setColor(Color.RED);
        for (Point p : controlPoints) {
            g2d.fillOval((int)(p.x - POINT_RADIUS), (int)(p.y - POINT_RADIUS), 
                        POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
        
        // Draw current curve (during animation)
        if (animationStep >= 1 && !currentPoints.isEmpty()) {
            g2d.setColor(Color.BLUE);
            
            if (currentPoints.size() == 1) {
                // Single point
                Point p = currentPoints.get(0);
                g2d.fillOval((int)(p.x - POINT_RADIUS), (int)(p.y - POINT_RADIUS), 
                            POINT_RADIUS * 2, POINT_RADIUS * 2);
            } else {
                // Draw lines between consecutive points
                for (int i = 0; i < currentPoints.size() - 1; i++) {
                    Point p1 = currentPoints.get(i);
                    Point p2 = currentPoints.get(i + 1);
                    g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y);
                }
            }
        }
        
        // Display current step
        if (animationStep >= 0) {
            g2d.setColor(Color.BLACK);
            if (animationStep == 0) {
                g2d.drawString("Step: " + animationStep + " (Control points only)", 10, 20);
            } else if (animationStep == 1) {
                g2d.drawString("Step: " + animationStep + " (Original polygon)", 10, 20);
            } else {
                g2d.drawString("Step: " + animationStep + " (Chaikin iteration " + (animationStep - 1) + " of 6)", 10, 20);
            }
        } else if (controlPoints.isEmpty()) {
            g2d.setColor(Color.GRAY);
            g2d.drawString("Click to add points, then press Enter to animate", 10, 20);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chaikin's Algorithm Animation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            ChaikinsAlgorithm panel = new ChaikinsAlgorithm();
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
    }
}
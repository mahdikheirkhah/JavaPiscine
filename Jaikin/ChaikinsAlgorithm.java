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
    private static final int POINT_RADIUS = 5;
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
            if (animationStep < 7) {
                animationStep++;
                if (controlPoints.size() > 1) {
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
        
        // For each line segment, create two new points at 25% and 75%
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            
            // Point at 25% of the way from p1 to p2
            Point q1 = new Point(
                (int) (p1.x * 0.75 + p2.x * 0.25),
                (int) (p1.y * 0.75 + p2.y * 0.25)
            );
            
            // Point at 75% of the way from p1 to p2
            Point q2 = new Point(
                (int) (p1.x * 0.25 + p2.x * 0.75),
                (int) (p1.y * 0.25 + p2.y * 0.75)
            );
            
            newPoints.add(q1);
            newPoints.add(q2);
        }
        
        return newPoints;
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
            g2d.fillOval(p.x - POINT_RADIUS, p.y - POINT_RADIUS, 
                        POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
        
        // Draw current curve (only during animation)
        if (!currentPoints.isEmpty() && animationStep >= 0) {
            g2d.setColor(Color.BLUE);
            
            if (currentPoints.size() == 1) {
                // Single point
                Point p = currentPoints.get(0);
                g2d.fillOval(p.x - POINT_RADIUS, p.y - POINT_RADIUS, 
                            POINT_RADIUS * 2, POINT_RADIUS * 2);
            } else if (currentPoints.size() == 2) {
                // Straight line
                Point p1 = currentPoints.get(0);
                Point p2 = currentPoints.get(1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            } else {
                // Open curve - draw lines between consecutive points
                for (int i = 0; i < currentPoints.size() - 1; i++) {
                    Point p1 = currentPoints.get(i);
                    Point p2 = currentPoints.get(i + 1);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
        
        // Display current step
        if (animationStep >= 0) {
            g2d.setColor(Color.BLACK);
            g2d.drawString("Step: " + animationStep, 10, 20);
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
        public final int x;
        public final int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
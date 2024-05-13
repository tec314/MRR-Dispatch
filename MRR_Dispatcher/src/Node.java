import java.awt.*;

import javax.swing.JFrame;

public class Node {
    private float nodeXFactor;
    private float nodeYFactor;
    private float nodeX;
    private float nodeY;
    private boolean isEndPointForNext;
    private boolean isEndPointForPrev;
    private boolean occupied = false;
    private int occupiedPriority;
    private boolean endOfPath = false;
    private boolean isEndOfScreen;
    private String name;
    private Node nextNode;
    private Node previousNode;
    private Signal thisSignal;
    private Switch thisSwitch;
    private JFrame frame;

    // Constructor
    public Node(float x, float y, boolean n, boolean p, String nm, Signal sig, Switch sw, boolean s, JFrame frame) {
    	this.frame = frame;
    	nodeXFactor = frame.getWidth()/x;
    	nodeYFactor = frame.getHeight()/y;
    	
    	this.nodeX = x;
        this.nodeY = y;
        this.isEndPointForNext = n;
        this.isEndPointForPrev = p;
        this.name = nm;
        this.thisSignal = sig;
        this.thisSwitch = sw;
        this.isEndOfScreen = s;
    }

    // Update method
    public void update() {
        this.nodeX = frame.getWidth() / nodeXFactor;
        this.nodeY = frame.getHeight() / nodeYFactor;
    }

    // Render method for GUI
    public void renderGUI(Graphics g, Color c) {
        g.setColor(c);
        if (nextNode != null && !isEndOfScreen) {
        	g.drawLine((int) nodeX, (int) nodeY, (int) nextNode.getX(), (int) nextNode.getY());
        }
    }

    // Render method for connections
    public void renderConnections(Graphics g, Color c, String dir) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(4));

        if (nextNode != null && !isEndPointForNext && dir.equals("CW") && !endOfPath) {
            g2d.drawLine((int) nodeX, (int) nodeY, (int) nextNode.getX(), (int) nextNode.getY());
        }
        if (previousNode != null && !isEndPointForPrev && dir.equals("CCW") && !endOfPath) {
            g2d.drawLine((int) nodeX, (int) nodeY, (int) previousNode.getX(), (int) previousNode.getY());
        }
        if (isEndPointForPrev || isEndPointForNext) {
            g.setColor(c);
            g.fillOval((int) (nodeX - 5), (int) (nodeY - 5), 10, 10);
        }
        if (endOfPath) {
            g.setColor(Color.RED);
            g.drawLine((int) (nodeX + Math.cos(Math.toRadians(45)) * 10), (int) (nodeY + Math.sin(Math.toRadians(45)) * 10), (int) (nodeX + Math.cos(Math.toRadians(225)) * 10), (int) (nodeY + Math.sin(Math.toRadians(225)) * 10));
            g.drawLine((int) (nodeX + Math.cos(Math.toRadians(135)) * 10), (int) (nodeY + Math.sin(Math.toRadians(135)) * 10), (int) (nodeX + Math.cos(Math.toRadians(315)) * 10), (int) (nodeY + Math.sin(Math.toRadians(315)) * 10));
        }
    }

    // Debug render method
    public void renderDebug(Graphics g) {
        g.setColor(new Color(255, 100, 0));
        g.drawOval((int) (nodeX - 7.5), (int) (nodeY - 7.5), 15, 15);
        g.setColor(Color.RED);
        g.drawString(name, (int) nodeX, (int) (nodeY + 10));
    }

    void setNextAndPreviousNode(Node n) {
        nextNode = n;
        n.setPreviousNode(this);
      }
      
      void setNextNode(Node n) {
        nextNode = n;
      } 
      
      void setPreviousNode(Node n) {
        previousNode = n;
      }  
      
      float getX() {
        return nodeX;
      }
      
      float getY() {
        return nodeY;
      }
      
      Signal getSignal() {
    	  return thisSignal;
      }
      
      String getName() {
    	  return name;
      }
      
      void isEndOfPath(Boolean e) {
        endOfPath = e;
      }
      
      Boolean isOccupied() {
        return occupied;
      }
      
      int getOccupiedPriority() {
        return occupiedPriority;
      }
      
      void occupyNode(int p) {
        occupiedPriority = p;
        occupied = true;
      }
      
      void unoccupyNode() {
        occupiedPriority = 0;
        occupied = false;
      }
}

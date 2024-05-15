import java.awt.*;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
    private JPanel panel;

    // Constructor
    public Node(float x, float y, boolean n, boolean p, String nm, Signal sig, Switch sw, boolean s, JPanel panel) {
    	this.panel = panel;
    	int width = panel.getWidth();
		int height = panel.getHeight();
		
    	nodeXFactor = width/x;
    	nodeYFactor = height/y;
    	
    	this.nodeX = x;
        this.nodeY = y;
        this.isEndPointForNext = n;
        this.isEndPointForPrev = p;
        this.name = nm;
        this.thisSignal = sig;
        this.thisSwitch = sw;
        this.isEndOfScreen = s;
        
        MRRDispatchFrame.nodeArray.add(this);
    }

    // Update method
    public void update() {
        this.nodeX = panel.getWidth() / nodeXFactor;
        this.nodeY = panel.getHeight() / nodeYFactor;
    }

    // Render method for GUI
    public void renderGUI(Graphics2D g2d, Color c) {
        g2d.setColor(c);
        if (nextNode != null && !isEndOfScreen) {
        	g2d.draw(new Line2D.Double((float) nodeX, (float) nodeY, (float) nextNode.getX(), (float) nextNode.getY()));
        }
    }

    // Render method for connections
    public void renderConnections(Graphics g2d, Color c, String dir) {
        g2d.setColor(c);
        //g2d.setStroke(new BasicStroke(4));

        if (nextNode != null && !isEndPointForNext && dir.equals("CW") && !endOfPath) {
            g2d.drawLine((int) nodeX, (int) nodeY, (int) nextNode.getX(), (int) nextNode.getY());
        }
        if (previousNode != null && !isEndPointForPrev && dir.equals("CCW") && !endOfPath) {
            g2d.drawLine((int) nodeX, (int) nodeY, (int) previousNode.getX(), (int) previousNode.getY());
        }
        if (isEndPointForPrev || isEndPointForNext) {
        	g2d.setColor(c);
        	g2d.fillOval((int) (nodeX - 5), (int) (nodeY - 5), 10, 10);
        }
        if (endOfPath) {
            g2d.setColor(Color.RED);
            g2d.drawLine((int) (nodeX + Math.cos(Math.toRadians(45)) * 10), (int) (nodeY + Math.sin(Math.toRadians(45)) * 10), (int) (nodeX + Math.cos(Math.toRadians(225)) * 10), (int) (nodeY + Math.sin(Math.toRadians(225)) * 10));
            g2d.drawLine((int) (nodeX + Math.cos(Math.toRadians(135)) * 10), (int) (nodeY + Math.sin(Math.toRadians(135)) * 10), (int) (nodeX + Math.cos(Math.toRadians(315)) * 10), (int) (nodeY + Math.sin(Math.toRadians(315)) * 10));
        }
    }

    // Debug render method
    public void renderDebug(Graphics g2D) {
        g2D.setColor(new Color(255, 100, 0));
        g2D.drawOval((int) (nodeX - 7.5), (int) (nodeY - 7.5), 15, 15);
        g2D.setColor(Color.RED);
        g2D.drawString(name, (int) (nodeX + 20), (int) (nodeY + 20));
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

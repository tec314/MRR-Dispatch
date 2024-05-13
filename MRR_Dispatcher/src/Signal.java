import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Signal {
	private float signalXFactor;
	private float signalYFactor;
    private float signalX;
    private float signalY;
    private String signalType;
    private String signalDirection;
    private String signalName;
    private Color topHeadColor;
    private Color bottomHeadColor;
    private String signalState = "";
    private String signalFlow = "";
    private JFrame frame;
    
    Node signalNode;
    Switch signalSwitch;

    // Constructor
    public Signal(float x, float y, String name, String type, String direction, String flow, Switch sw, JFrame frame) {
    	this.frame = frame;
    	int width = frame.getWidth();
    	int height = frame.getHeight();
    	this.signalXFactor = width/x;
    	this.signalYFactor = height/y;
    	
        this.signalName = name;
        this.signalType = type;
        this.signalDirection = direction;
        this.signalFlow = flow;
        this.topHeadColor = Color.BLACK; // Default color
        this.bottomHeadColor = Color.BLACK; // Default color
        this.signalSwitch = sw;
        
        this.signalNode = new Node(x, y, false, false, "S", this, null, false, frame);
    }

    // Updates signal position
    public void update() {
        signalX = frame.getWidth() / signalXFactor;
        if (signalDirection.equals("<--")) {
            signalY = (frame.getHeight() / signalYFactor) - (frame.getHeight() / 35);
        } else {
            signalY = (frame.getHeight() / signalYFactor) + (frame.getHeight() / 35);
        }
        signalNode.update();
    }

    // Renders signal on screen
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // DOUBLE_HEAD Signal Graphic
        if (signalType.equals("DOUBLE_HEAD")) {
            g2d.setColor(topHeadColor);
            g2d.fillOval((int) signalX - 8, (int) signalY - 8, 16, 16);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval((int) signalX - 8, (int) signalY - 8, 16, 16);
            
            if (signalDirection.equals("<--")) {
                g2d.setColor(bottomHeadColor);
                g2d.fillOval((int) signalX + 17, (int) signalY - 8, 16, 16);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval((int) signalX + 17, (int) signalY - 8, 16, 16);
                g2d.drawLine((int) signalX + 8, (int) signalY, (int) signalX + 18, (int) signalY);
                g2d.drawLine((int) signalX + 33, (int) signalY, (int) signalX + 55, (int) signalY);
                g2d.drawLine((int) signalX + 55, (int) signalY + 5, (int) signalX + 55, (int) signalY - 5);
                
                // Draw signal name
                g2d.setColor(Color.YELLOW);
                g2d.drawString(signalName, (int) signalX + 70, (int) signalY + 5);
            } else {
                g2d.setColor(bottomHeadColor);
                g2d.fillOval((int) signalX - 33, (int) signalY - 8, 16, 16);
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval((int) signalX - 33, (int) signalY - 8, 16, 16);
                g2d.drawLine((int) signalX - 8, (int) signalY, (int) signalX - 18, (int) signalY);
                g2d.drawLine((int) signalX - 33, (int) signalY, (int) signalX - 55, (int) signalY);
                g2d.drawLine((int) signalX - 55, (int) signalY + 5, (int) signalX - 55, (int) signalY - 5);
                
                // Draw signal name
                g2d.setColor(Color.YELLOW);
                g2d.drawString(signalName, (int) signalX - 120, (int) signalY + 5);
            }

        // SINGLE_HEAD Signal Graphic
        } else {
            g2d.setColor(topHeadColor);
            g2d.fillOval((int) signalX - 8, (int) signalY - 8, 16, 16);
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval((int) signalX - 8, (int) signalY - 8, 16, 16);
            if (signalDirection.equals("<--")) {
                g2d.drawLine((int) signalX + 10, (int) signalY, (int) signalX + 40, (int) signalY);
                g2d.drawLine((int) signalX + 40, (int) signalY + 5, (int) signalX + 40, (int) signalY - 5);
                
                // Draw signal name
                g2d.setColor(Color.YELLOW);
                g2d.drawString(signalName, (int) signalX + 70, (int) signalY + 5);
            } else {
                g2d.drawLine((int) signalX - 10, (int) signalY, (int) signalX - 40, (int) signalY);
                g2d.drawLine((int) signalX - 40, (int) signalY + 5, (int) signalX - 40, (int) signalY - 5);
                
             // Draw signal name
                g2d.setColor(Color.YELLOW);
                g2d.drawString(signalName, (int) signalX - 120, (int) signalY + 5);
            }
        }

    }
    /*
    // Changes the signal's aspect
    public void signalState(String s) {
        signalState = s;
        if (signalState.equals("STOP")) {
            topHeadColor = new Color(255, 0, 0); // Red
            bottomHeadColor = new Color(0, 0, 0); // Black
            sendMessage(signalName + "_STOP"); // Sends a message for Arduino with signal to update
        } else if (signalState.equals("APPROACH")) {
            if (signalSwitch != null && signalSwitch.isThrown()) {
                topHeadColor = new Color(255, 0, 0); // Red
                bottomHeadColor = new Color(255, 220, 0); // Orange
                sendMessage(signalName + "_SLOW-APPR");
            } else {
                topHeadColor = new Color(255, 220, 0); // Orange
                bottomHeadColor = new Color(0, 0, 0); // Black
                sendMessage(signalName + "_APPROACH");
            }
        } else if (signalState.equals("CLEAR")) {
            if (signalSwitch != null && signalSwitch.isThrown()) {
                topHeadColor = new Color(255, 0, 0); // Red
                bottomHeadColor = new Color(0, 255, 0); // Green
                sendMessage(signalName + "_MED-CLEAR");
            } else {
                topHeadColor = new Color(0, 255, 0); // Green
                bottomHeadColor = new Color(0, 0, 0); // Black
                sendMessage(signalName + "_CLEAR");
            }
        } else {
            topHeadColor = new Color(0, 0, 0); // Black
            bottomHeadColor = new Color(0, 0, 0); // Black
        }
    }
        // Add your logic to change signal colors based on state
    }*/
    
    public String getName() {
    	return signalName;
    }
}


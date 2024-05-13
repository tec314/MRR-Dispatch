import java.awt.*;

import javax.swing.JFrame;

public class Switch {
    private float switchXFactor;
    private float switchYFactor;
    private float switchX;
    private float switchY;
    private int thrownAngle;
    private int closedAngle;
    private boolean thrown = true;
    private String entryDirection;
    private String name;
    private JFrame frame;
    private int width;
    private int height;
    
    Node entryNode;
    Node closedNode;
    Node thrownNode;

    // Constructor
    public Switch(float x, float y, int t, int c, String ed, String nm, JFrame frame) {
    	this.frame = frame;
    	width = frame.getWidth();
    	height = frame.getHeight();
        switchXFactor = width/x;
        switchYFactor = height/y;
    	
    	
    	this.switchX = x;
        this.switchY = y;
        this.thrownAngle = t;
        this.closedAngle = c;
        this.entryDirection = ed;
        this.name = nm;
        
        entryNode = new Node(x, y, false, false, "E ", null, this, false, frame);
        Main.nodeArray.add(entryNode);
        closedNode = new Node((float) (x + Math.cos(Math.toRadians(closedAngle))*(width/96)), (float) (y + Math.sin(Math.toRadians(closedAngle))*(height/48)), false, false, "C ", null, this, false, frame);
        Main.nodeArray.add(closedNode);
        thrownNode = new Node((float) (x + Math.cos(Math.toRadians(thrownAngle))*(width/96)), (float) (y + Math.sin(Math.toRadians(thrownAngle))*(height/48)), false, false, "T " + name, null, this, false, frame);
        Main.nodeArray.add(thrownNode);
    }

    // Rest of the Switch class methods...
 // Updates switch position using current frame width and height
    public void update() {
        width = frame.getWidth();
        height = frame.getHeight();
        switchX = width / switchXFactor;
        switchY = height / switchYFactor;

        entryNode.update();
        closedNode.update();
        thrownNode.update();
    }
    
    // Render switch position on screen
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(5)); // Set the stroke width

        if (!thrown) {
            g2d.setColor(Color.BLACK); // Set color for closed position
            g2d.drawLine((int) switchX, (int) switchY, (int) (switchX + Math.cos(Math.toRadians(thrownAngle)) * (width / 96)), (int) (switchY + Math.sin(Math.toRadians(thrownAngle)) * (height / 48)));

            g2d.setColor(Color.WHITE); // Set color for thrown position
            g2d.setStroke(new BasicStroke(2)); // Set the stroke width
            g2d.drawLine((int) switchX, (int) switchY, (int) (switchX + Math.cos(Math.toRadians(closedAngle)) * (width / 96)), (int) (switchY + Math.sin(Math.toRadians(closedAngle)) * (height / 48)));
        } else {
            g2d.setColor(Color.BLACK); // Set color for closed position
            g2d.drawLine((int) switchX, (int) switchY, (int) (switchX + Math.cos(Math.toRadians(closedAngle)) * (width / 96)), (int) (switchY + Math.sin(Math.toRadians(closedAngle)) * (height / 48)));

            g2d.setColor(Color.WHITE); // Set color for thrown position
            g2d.setStroke(new BasicStroke(2)); // Set the stroke width
            g2d.drawLine((int) switchX, (int) switchY, (int) (switchX + Math.cos(Math.toRadians(thrownAngle)) * (width / 96)), (int) (switchY + Math.sin(Math.toRadians(thrownAngle)) * (height / 48)));
        }
    }
    
    // Toggle switch position
    public void toggleSwitch() {
        thrown = !thrown;

        // SEND A PULSE TO CHILD ARDUINOS TELLING THIS SWITCH TO FLIP

        if (thrown) {
        	SendRecieve.sendMessage(name + "_" + "THROWN");

            if (entryDirection.equals("CW")) {
                entryNode.setNextNode(thrownNode);
                thrownNode.setPreviousNode(entryNode);
                closedNode.setPreviousNode(null);
            } else {
                thrownNode.setNextNode(entryNode);
                entryNode.setPreviousNode(thrownNode);
                closedNode.setNextNode(null);
            }
        } else {
        	SendRecieve.sendMessage(name + "_" + "OPEN");

            if (entryDirection.equals("CW")) {
                entryNode.setNextNode(closedNode);
                closedNode.setPreviousNode(entryNode);
                thrownNode.setPreviousNode(null);
            } else {
                closedNode.setNextNode(entryNode);
                entryNode.setPreviousNode(closedNode);
                thrownNode.setNextNode(null);
            }
        }
    }

    // Check if the switch is thrown
    public boolean isThrown() {
        return thrown;
    }
}

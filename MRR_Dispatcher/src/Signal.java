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
	private Boolean isBuffer;
	private String signalState = "";
	private String signalFlow = "";
	private JPanel panel;

	Node signalNode;
	Switch signalSwitch;

	// Constructor
	public Signal(float x, float y, String name, String type, String direction, String flow, Switch sw, Boolean buffer,
			JPanel panel) {
		this.panel = panel;
		int width = panel.getWidth();
		int height = panel.getHeight();
		
		this.signalXFactor = width/x;
		this.signalYFactor = height/y;

		this.signalName = name;
		this.signalType = type;
		this.signalDirection = direction;
		this.signalFlow = flow;
		this.topHeadColor = Color.BLACK; // Default color
		this.bottomHeadColor = Color.BLACK; // Default color
		this.isBuffer = buffer;
		this.signalSwitch = sw;

		this.signalNode = new Node(x, y, false, false, "S", this, null, false, panel);
		MRRDispatchFrame.signalArray.add(this);
		
	}

	// Updates signal position
	public void update() {
		signalX = panel.getWidth() / signalXFactor;
		if (signalDirection.equals("<--")) {
			signalY = (panel.getHeight() / signalYFactor) - (panel.getHeight() / 35);
		} else {
			signalY = (panel.getHeight() / signalYFactor) + (panel.getHeight() / 35);
		}
		signalNode.update();
	}

	// Renders signal on screen
	public void render(Graphics2D g2d) {
		if (!isBuffer) {
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
	}
	/*
	 * // Changes the signal's aspect public void signalState(String s) {
	 * signalState = s; if (signalState.equals("STOP")) { topHeadColor = new
	 * Color(255, 0, 0); // Red bottomHeadColor = new Color(0, 0, 0); // Black
	 * sendMessage(signalName + "_STOP"); // Sends a message for Arduino with signal
	 * to update } else if (signalState.equals("APPROACH")) { if (signalSwitch !=
	 * null && signalSwitch.isThrown()) { topHeadColor = new Color(255, 0, 0); //
	 * Red bottomHeadColor = new Color(255, 220, 0); // Orange
	 * sendMessage(signalName + "_SLOW-APPR"); } else { topHeadColor = new
	 * Color(255, 220, 0); // Orange bottomHeadColor = new Color(0, 0, 0); // Black
	 * sendMessage(signalName + "_APPROACH"); } } else if
	 * (signalState.equals("CLEAR")) { if (signalSwitch != null &&
	 * signalSwitch.isThrown()) { topHeadColor = new Color(255, 0, 0); // Red
	 * bottomHeadColor = new Color(0, 255, 0); // Green sendMessage(signalName +
	 * "_MED-CLEAR"); } else { topHeadColor = new Color(0, 255, 0); // Green
	 * bottomHeadColor = new Color(0, 0, 0); // Black sendMessage(signalName +
	 * "_CLEAR"); } } else { topHeadColor = new Color(0, 0, 0); // Black
	 * bottomHeadColor = new Color(0, 0, 0); // Black } } // Add your logic to
	 * change signal colors based on state }
	 */
	
	void signalState(String s) {
        if (s.equals("STOP")) {
        	topHeadColor = Color.RED;
            bottomHeadColor = Color.BLACK;
            SendRecieve.sendMessage(signalName + "_STOP"); // Sends a message for Arduino with signal to update
        } else if (s.equals("APPROACH")) {
        	if(signalSwitch != null && signalSwitch.isThrown()) {
        		topHeadColor = Color.RED;
                bottomHeadColor = Color.ORANGE;
                SendRecieve.sendMessage(signalName + "_SLOW-APPR");
        	} else {
        		topHeadColor = Color.ORANGE;
                bottomHeadColor = Color.BLACK;
                SendRecieve.sendMessage(signalName + "_APPROACH");
        	}
        } else if (s.equals("CLEAR")) {
        	if(signalSwitch != null && signalSwitch.isThrown()) {
        		topHeadColor = Color.RED;
                bottomHeadColor = Color.GREEN;
                SendRecieve.sendMessage(signalName + "_MED-CLEAR");
        	} else {
        		topHeadColor = Color.GREEN;
                bottomHeadColor = Color.BLACK;
                SendRecieve.sendMessage(signalName + "_CLEAR");
        	}
        } else {
        	topHeadColor = Color.BLACK;
            bottomHeadColor = Color.BLACK;
        }
    }
	
	String getName() {
		return signalName;
	}
	
	String getSignalFlow() {
		return signalFlow;
	} 
}

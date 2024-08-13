import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Switch {
	private float switchXFactor;
	private float switchYFactor;
	private float switchX;
	private float switchY;
	private int thrownAngle;
	private int closedAngle;
	private boolean thrown = true;
	private String entryDirection;
	private String buttonLocation;
	private String name;
	private JPanel panel;
	private int width;
	private int height;

	Node entryNode;
	Node closedNode;
	Node thrownNode;
	SerialComm writer;

	JButton throwSwitchButton;
	SerialComm serial;

	// Constructor
	public Switch(SerialComm s, float x, float y, int t, int c, String ed, String bl, String nm, JPanel panel) {
		this.serial = s;
		this.panel = panel;
		width = panel.getWidth();
		height = panel.getHeight();

		switchXFactor = width / x;
		switchYFactor = height / y;

		this.switchX = x;
		this.switchY = y;
		this.thrownAngle = t;
		this.closedAngle = c;
		this.entryDirection = ed;
		this.buttonLocation = bl;
		this.name = nm;

		entryNode = new Node(x, y, false, false, "E ", null, this, false, panel);
		MRRDispatchFrame.nodeArray.add(entryNode);
		closedNode = new Node((float) (x + Math.cos(Math.toRadians(closedAngle)) * (width / 96)),
				(float) (y + Math.sin(Math.toRadians(closedAngle)) * (height / 48)), false, false, "C ", null, this,
				false, panel);
		MRRDispatchFrame.nodeArray.add(closedNode);
		thrownNode = new Node((float) (x + Math.cos(Math.toRadians(thrownAngle)) * (width / 96)),
				(float) (y + Math.sin(Math.toRadians(thrownAngle)) * (height / 48)), false, false, "T " + name, null,
				this, false, panel);
		MRRDispatchFrame.nodeArray.add(thrownNode);

		//this.toggleSwitch();   // "Wake up" switch for use (UNCOMMENT AFTER DEBUGGING)
		
		throwSwitchButton = new JButton(name);
		panel.add(throwSwitchButton);
		throwSwitchButton.addActionListener(e -> {
			this.toggleSwitch();
			MRRDispatchFrame.updateTrains();
		});

		MRRDispatchFrame.switchArray.add(this);
	}

	// Rest of the Switch class methods...
	// Updates switch position using current frame width and height
	public void update() {
		width = panel.getWidth();
		height = panel.getHeight();
		switchX = width / switchXFactor;
		switchY = height / switchYFactor;
		if (buttonLocation.equals("NE")) {
			throwSwitchButton.setBounds((int) switchX + 10, (int) switchY - 40, 65, 25);
		} else if (buttonLocation.equals("SE")) {
			throwSwitchButton.setBounds((int) switchX + 10, (int) switchY + 15, 65, 25);
		} else if (buttonLocation.equals("SW")) {
			throwSwitchButton.setBounds((int) switchX - 75, (int) switchY + 15, 65, 25);
		} else if (buttonLocation.equals("NW")) {
			throwSwitchButton.setBounds((int) switchX - 75, (int) switchY - 40, 65, 25);
		} else {
			throwSwitchButton.setBounds((int) switchX, (int) switchY, 65, 25); // this should never be called
		}
		

		entryNode.update();
		closedNode.update();
		thrownNode.update();
	}

	// Toggle switch position
	public void toggleSwitch() {
		thrown = !thrown;

		// SEND A PULSE TO CHILD ARDUINOS TELLING THIS SWITCH TO FLIP

		if (thrown) {
			serial.writeData(name + "_" + "THROWN");

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
			serial.writeData(name + "_" + "OPEN");

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

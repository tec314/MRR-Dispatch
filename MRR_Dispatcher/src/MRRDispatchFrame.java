import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class MRRDispatchFrame extends JFrame {
	public static Switch SW6;
	public static Switch SW5;
	public static Switch SW4;
	public static Switch SW3;
	public static Switch SW2;
	public static Switch SW1;

	public static Signal SIG_201A;
	public static Signal SIG_201B;
	public static Signal SIG_202A;
	public static Signal SIG_202B;
	public static Signal SIG_203A;
	public static Signal SIG_203B;
	public static Signal SIG_204A; 
	public static Signal SIG_204B;
	public static Signal SIG_205A;
	public static Signal SIG_205B;
	public static Signal SIG_206A;
	public static Signal SIG_206B;
	public static Signal SIG_207A;
	public static Signal SIG_208A;
	public static Signal buffer1;
	public static Signal buffer2;

	public static Node node1;
	public static Node node2;
	public static Node node3;
	public static Node node4;
	public static Node node5;
	public static Node node6;
	public static Node node7;
	public static Node node8;
	public static Node node9;
	public static Node node10;
	public static Node node11;

	public static ArrayList<Signal> signalArray = new ArrayList<Signal>();
	public static ArrayList<Switch> switchArray = new ArrayList<Switch>();
	public static ArrayList<Node> nodeArray = new ArrayList<Node>();
	
	public static Boolean debug = false;
	
	public JPanel optionsPanel = new JPanel(new GridLayout()); // 6 rows, 2 columns
	public JPanel mapPanel = new DrawPanel();
	public JLabel coordLabel = new JLabel();
	
	public MRRDispatchFrame() {
		super("MRR Dispatcher");
		
		setLayout(new BorderLayout());
		optionsPanel.setBackground(Color.WHITE);
		mapPanel.setBackground(Color.BLACK);
		mapPanel.setLayout(null);
		
		mapPanel.setSize(1904, 1018);

		// Create a menu bar
		JMenuBar menuBar = new JMenuBar();
		// menuBar.setBackground(new Color(100, 100, 100));

		// Create actionButton menu
		JMenu actionDropdown = new JMenu("Actions");
		JMenuItem addTrainButton = new JMenuItem("Add Train");
		JMenuItem removeTrainButton = new JMenuItem("Remove Train");
		JMenuItem setTrainDirectionButton = new JMenuItem("Set Train Direction");

		// Add action items to actions drop-down
		actionDropdown.add(addTrainButton);
		actionDropdown.add(removeTrainButton);
		actionDropdown.add(setTrainDirectionButton);

		// Add actionButton menu to menu bar
		menuBar.add(actionDropdown);

		// Create toolsButton menu
		JMenu toolsDropdown = new JMenu("Tools");
		JMenuItem debugToggleButton = new JMenuItem("Toggle Debug Mode");

		// Add tool items to tools drop-down
		toolsDropdown.add(debugToggleButton);

		// Add toolsButton menu to menu bar
		menuBar.add(toolsDropdown);

		JMenu helpDropdown = new JMenu("Help");
		
		menuBar.add(helpDropdown);
		
		// Add menus to the menu bar
		menuBar.add(actionDropdown);
		menuBar.add(toolsDropdown);
		menuBar.add(helpDropdown);
		
		// Set the menu bar for the frame
		optionsPanel.add(menuBar);

		// Action listeners for add trains
		addTrainButton.addActionListener(e -> {
			// Add action for Action 1
			System.out.println("Action 1 selected");
			JPanel windowPanel = new JPanel(new GridLayout(6, 2)); // 6 rows, 2 columns
			windowPanel.setSize(800, 800);
			ImageIcon customIcon = new ImageIcon(Main.class.getResource("/icon/septa_icon.png").getPath());
			Image image = customIcon.getImage(); // transform it
			Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
			customIcon = new ImageIcon(newimg); // transform it back

			// Add labels and text fields for each input
			JTextField trainNameField = new JTextField();
			windowPanel.add(new JLabel("Train Name:"));
			windowPanel.add(trainNameField);

			// Create radio buttons for train type
			JRadioButton passengerButton = new JRadioButton("Passenger");
			JRadioButton freightButton = new JRadioButton("Freight");
			JRadioButton mowButton = new JRadioButton("MOW");
			JPanel typeButtons = new JPanel(new GridLayout(1, 3)); // 6 rows, 2 columns

			ButtonGroup trainTypeGroup = new ButtonGroup();
			trainTypeGroup.add(passengerButton);
			trainTypeGroup.add(freightButton);
			trainTypeGroup.add(mowButton);
			windowPanel.add(new JLabel("Train Type:"));
			typeButtons.add(passengerButton);
			typeButtons.add(freightButton);
			typeButtons.add(mowButton);
			windowPanel.add(typeButtons);

			JTextField equipmentField = new JTextField();
			windowPanel.add(new JLabel("Number of Equipment:"));
			windowPanel.add(equipmentField);

			// Prompt user for priority (using a slider)
			JSlider prioritySlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5); // Min = 0, Max = 10, Default = 5
			prioritySlider.setMajorTickSpacing(10);
			prioritySlider.setMinorTickSpacing(1);
			// prioritySlider.setPaintTicks(true);
			prioritySlider.setPaintLabels(true);
			windowPanel.add(new JLabel("Select Priority:"));
			windowPanel.add(prioritySlider);

			JRadioButton clockwiseButton = new JRadioButton("CW");
			JRadioButton counterclockwiseButton = new JRadioButton("CCW");
			JPanel directionButtons = new JPanel(new GridLayout(1, 2)); // 6 rows, 2 columns

			ButtonGroup trainDirectionGroup = new ButtonGroup();
			trainDirectionGroup.add(clockwiseButton);
			trainDirectionGroup.add(counterclockwiseButton);
			windowPanel.add(new JLabel("Train Direction:"));
			directionButtons.add(clockwiseButton);
			directionButtons.add(counterclockwiseButton);
			windowPanel.add(directionButtons);

			JComboBox<String> signalDropdown = new JComboBox<>();
			for (Signal signal : signalArray) {
				signalDropdown.addItem(signal.getName());
			}
			windowPanel.add(new JLabel("Starting Signal:"));
			windowPanel.add(signalDropdown);

			// Show the input dialog
			int result = JOptionPane.showConfirmDialog(optionsPanel, windowPanel, "Add Train", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE, customIcon);

			// If the user clicks OK, retrieve the values from the text fields
			if (result == JOptionPane.OK_OPTION) {
				String trainName = trainNameField.getText();
				String trainType = "";
				if (passengerButton.isSelected()) {
					trainType = "Passenger";
				} else if (freightButton.isSelected()) {
					trainType = "Freight";
				} else if (mowButton.isSelected()) {
					trainType = "MOW";
				}
				int numberOfEquipment = Integer.parseInt(equipmentField.getText());
				int priority = prioritySlider.getValue();
				// String direction = directionField.getText();
				// String startingSignal = startingSignalField.getText();

				// Use the inputs as needed (you can print them for now)
				System.out.println("Train Name: " + trainName);
				System.out.println("Train Type: " + trainType);
				System.out.println("Number of Equipment: " + numberOfEquipment);
				System.out.println("Priority: " + priority);
				// System.out.println("Direction of Travel: " + direction);
				// System.out.println("Starting Signal: " + startingSignal);
			}
		});

		removeTrainButton.addActionListener(e -> {
			// Add action for Action 2
			System.out.println("Action 2 selected");
		});

		setTrainDirectionButton.addActionListener(e -> {
			// Add action for Action 3
			System.out.println("Action 3 selected");
		});

		debugToggleButton.addActionListener(e -> {
			debug = !debug;
		});
		
		add(optionsPanel, BorderLayout.NORTH);
		add(mapPanel, BorderLayout.CENTER);
		add(coordLabel, BorderLayout.SOUTH);
		
		initialization(mapPanel);
		setNodeConnections();
		
		System.out.println("DIM: " + mapPanel.getWidth() + ", " + mapPanel.getHeight());
	}

	void initialization(JPanel panel) {
		int width = panel.getWidth();
		int height = panel.getHeight();
		
		// Switch Initializations
		SW6 = new Switch((float) (width / 3.78), (float) (height / 1.78), 330, 0, "CCW", "NW", "SW6", panel);
		SW5 = new Switch((float) (width / 3), (float) (height / 4), 30, 0, "CW", "NW", "SW5", panel);
		SW4 = new Switch((float) (width / 3 + Math.cos(Math.toRadians(30)) * width / 16),
				(float) (height / 3.2), 210, 180, "CCW", "SE", "SW4", panel);
		SW3 = new Switch((float) (width - width / 3.5), (float) (height / 1.6), 150, 180, "CW", "SE", "SW3", panel);
		SW2 = new Switch((float) (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 16),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 8), 0, 30, "CCW", "SW", "SW2", panel);
		SW1 = new Switch((float) (width / 3.5), (float) (height / 1.6), 30, 0, "CCW", "NE", "SW1", panel);
		
		// Signal Initializations
		SIG_201A = new Signal((float) (510), (float) (height / 4), "SIG-201A", "DOUBLE_HEAD", "-->", "CW", SW5, false, panel);
		signalArray.add(SIG_201A);
		SIG_201B = new Signal((float) (510), (float) (height / 3.2), "SIG-201B", "SINGLE_HEAD", "-->", "CW", null,
				false, panel);
		signalArray.add(SIG_201B);
		SIG_202A = new Signal((float) (910), (float) (height / 4), "SIG-202A", "SINGLE_HEAD", "<--", "CCW", null,
				false, panel);
		signalArray.add(SIG_202A);
		SIG_202B = new Signal((float) (910), (float) (height / 3.2), "SIG-202B", "DOUBLE_HEAD", "<--", "CCW", SW4,
				false, panel);
		signalArray.add(SIG_202B);
		SIG_203A = new Signal((float) (1480), (float) (height / 4), "SIG-203A", "DOUBLE_HEAD", "-->", "CW", SW3, false, panel);
		signalArray.add(SIG_203A);
		SIG_203B = new Signal((float) (1480), (float) (height / 3.2), "SIG-203B", "SINGLE_HEAD", "-->", "CW", null,
				false, panel);
		signalArray.add(SIG_203B);
		SIG_204A = new Signal((float) (450), (float) (height / 1.6), "SIG-204A", "DOUBLE_HEAD", "-->", "CCW", SW1,
				false, panel);
		signalArray.add(SIG_204A);
		SIG_204B = new Signal((float) (450), (float) (height / 1.78), "SIG-204B", "SINGLE_HEAD", "-->", "CCW", null,
				false, panel);
		signalArray.add(SIG_204B);
		SIG_205A = new Signal((float) (730), (float) (height / 1.6), "SIG-205A", "SINGLE_HEAD", "<--", "CW", null,
				false, panel);
		signalArray.add(SIG_205A);
		SIG_205B = new Signal((float) (730), (float) (height / 1.78), "SIG-205B", "SINGLE_HEAD", "<--", "CW", null,
				false, panel);
		signalArray.add(SIG_205B);
		SIG_206A = new Signal((float) (1200), (float) (height / 1.6), "SIG-206A", "SINGLE_HEAD", "-->", "CCW", null,
				false, panel);
		signalArray.add(SIG_206A);
		SIG_206B = new Signal((float) (1200), (float) (height / 1.78), "SIG-206B", "SINGLE_HEAD", "-->", "CCW", null,
				false, panel);
		signalArray.add(SIG_206B);
		SIG_207A = new Signal((float) (790), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4),
				"SIG-207A", "SINGLE_HEAD", "<--", "CW", null, false, panel);
		signalArray.add(SIG_207A);
		SIG_208A = new Signal((float) (1130), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4),
				"SIG-208A", "SINGLE_HEAD", "-->", "CCW", null, false, panel);
		signalArray.add(SIG_208A);
		buffer1 = new Signal((float) (width / 2), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 8),
				"buffer1", "SINGLE_HEAD", "-->", "CCW", null, true, panel); // FOR END OF FREIGHT STORAGE TRACK
		buffer2 = new Signal((float) (width / 2.7), (float) (height / 2.5 + Math.sin(Math.toRadians(30)) * height / 8),
				"buffer2", "SINGLE_HEAD", "-->", "CCW", null, true, panel);
		
		// Standalone Node Initializations
		node1 = new Node(width / 7, height / 4, false, true, "1", null, null, false, panel);
		node2 = new Node((float) width / 7, (float) (height / 3.2), false, true, "2", null, null, false, panel);
		node3 = new Node(width - width / 7, height / 4, true, false, "3", null, null, true, panel);
		node4 = new Node(width - width / 7, (float) (height / 3.2), true, false, "4", null, null, true, panel);
		node5 = new Node(width / 7, (float) (height / 1.78), true, false, "5", null, null, true, panel);
		node6 = new Node(width / 7, (float) (height / 1.6), true, false, "6", null, null, true, panel);
		node7 = new Node(width - width / 7, (float) (height / 1.78), false, true, "7", null, null, false, panel);
		node8 = new Node(width - width / 7, (float) (height / 1.6), false, true, "8", null, null, false, panel);
		node9 = new Node((float) (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 8),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4), false, false, "9", null, null,
				false, panel);
		node10 = new Node((float) (width - (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 8)),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4), false, false, "10", null, null,
				false, panel);
		
	}

	void setNodeConnections() {
		// Bleh, node connection hard code >:(

		// setNextAndPreviousNode = next node going clockwise & prev node going counterclockwise
		node1.setNextAndPreviousNode(SIG_201A.signalNode);
		node2.setNextAndPreviousNode(SIG_201B.signalNode);
		SIG_201A.signalNode.setNextAndPreviousNode(SW5.entryNode);
		SIG_201B.signalNode.setNextAndPreviousNode(SW4.closedNode);
		SW5.thrownNode.setNextAndPreviousNode(SW4.thrownNode);
		SW5.closedNode.setNextAndPreviousNode(SIG_202A.signalNode);
		SW4.entryNode.setNextAndPreviousNode(SIG_202B.signalNode);
		SIG_202A.signalNode.setNextAndPreviousNode(SIG_203A.signalNode);
		SIG_202B.signalNode.setNextAndPreviousNode(SIG_203B.signalNode);
		SIG_203A.signalNode.setNextAndPreviousNode(node3);
		SIG_203B.signalNode.setNextAndPreviousNode(node4);
		node3.setNextAndPreviousNode(node8);
		node4.setNextAndPreviousNode(node7);
		node7.setNextAndPreviousNode(SIG_206B.signalNode);
		SIG_206B.signalNode.setNextAndPreviousNode(SIG_205B.signalNode);
		SIG_205B.signalNode.setNextAndPreviousNode(SW6.closedNode);
		SW6.entryNode.setNextAndPreviousNode(SIG_204B.signalNode);
		SIG_204B.signalNode.setNextAndPreviousNode(node5);
		node5.setNextAndPreviousNode(node2);
		node8.setNextAndPreviousNode(SW3.entryNode);
		SW3.closedNode.setNextAndPreviousNode(SIG_206A.signalNode);
		SIG_206A.signalNode.setNextAndPreviousNode(SIG_205A.signalNode);
		SIG_205A.signalNode.setNextAndPreviousNode(SW1.closedNode);
		SW1.entryNode.setNextAndPreviousNode(SIG_204A.signalNode);
		SIG_204A.signalNode.setNextAndPreviousNode(node6);
		node6.setNextAndPreviousNode(node1);
		SW3.thrownNode.setNextAndPreviousNode(node10);
		node10.setNextAndPreviousNode(SIG_208A.signalNode);
		SIG_208A.signalNode.setNextAndPreviousNode(SIG_207A.signalNode);
		SIG_207A.signalNode.setNextAndPreviousNode(node9);
		node9.setNextAndPreviousNode(SW2.closedNode);
		SW2.entryNode.setNextAndPreviousNode(SW1.thrownNode);
		buffer1.signalNode.setNextAndPreviousNode(SW2.thrownNode);
		buffer2.signalNode.setNextAndPreviousNode(SW6.thrownNode);
		
		repaint();
	}
	
	private class DrawPanel extends JPanel {
		MouseHandler mouse = new MouseHandler();
		
		public DrawPanel() {
			this.setBackground(Color.BLACK);
			this.addMouseListener(mouse);
			this.addMouseMotionListener(mouse);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			
			// Curved dashed lines
			float dashLength[] = new float[1];
			dashLength[0] = 10;
			BasicStroke stroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 15, dashLength, 0);
			
			g2d.setColor(Color.WHITE);
			g2d.setStroke(stroke);
			int arcWidth1 = getWidth() / 5;
			int arcHeight1 = ((int) (getHeight() / 1.6) - (getHeight() / 4));
			int arcWidth2 = getWidth() / 8;
			int arcHeight2 = ((int) (getHeight() / 1.78) - (int) (getHeight() / 3.2));
			
			g2d.drawArc((getWidth() / 7) - (arcWidth1/2), (getHeight() / 4), arcWidth1, arcHeight1, 90, 180);
			g2d.drawArc((getWidth() / 7) - (arcWidth2/2), (int) (getHeight() / 3.2), arcWidth2, arcHeight2, 90, 180);
			
			g2d.drawArc((getWidth() - getWidth() / 7) - (arcWidth1/2), (getHeight() / 4), arcWidth1, arcHeight1, -90, 180);
			g2d.drawArc((getWidth() - getWidth() / 7) - (arcWidth2/2), (int) (getHeight() / 3.2), arcWidth2, arcHeight2, -90, 180);
			
			for (Switch sw : switchArray) {
				sw.update();
				sw.render(g2d);
				repaint();
			}
			
			for (Signal sig : signalArray) {
				sig.update();
				sig.render(g2d);
				repaint();
			}
			
			for (Node node : nodeArray) {
				node.update();
				node.renderGUI(g2d, Color.WHITE);
				if(debug) {
					node.renderDebug(g2d);
				}
				repaint();
			}
		}
	}
	
	private class MouseHandler extends MouseAdapter implements MouseMotionListener {
		public void mousePressed(MouseEvent event) {}
		public void mouseReleased(MouseEvent event) {}
		@Override
		public void mouseDragged(MouseEvent event) {}
		@Override
		public void mouseMoved(MouseEvent event) {
			if(debug) {
				coordLabel.setText("  " + Integer.toString(event.getX()) + ", " + Integer.toString(event.getY()));
			} else {
				coordLabel.setText("");
			}
		}
	}
}


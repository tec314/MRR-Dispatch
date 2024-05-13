import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Main {
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
	public static ArrayList<Node> nodeArray = new ArrayList<Node>();

	public static void main(String[] args) {
		// Create a new JFrame
		System.out.println("Loop?");
		JFrame frame = new JFrame("Model Railroad Dispatcher");

		// Set the size of the frame
		frame.setSize(1920, 1080);
		frame.getContentPane().setBackground(Color.BLACK);

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

		// Set the menu bar for the frame
		frame.setJMenuBar(menuBar);

		// Set default close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Make the frame visible
		frame.setVisible(true);

		// Action listeners for add trains
		addTrainButton.addActionListener(e -> {
			// Add action for Action 1
			System.out.println("Action 1 selected");
			JPanel panel = new JPanel(new GridLayout(6, 2)); // 6 rows, 2 columns
			panel.setSize(800, 800);
			ImageIcon customIcon = new ImageIcon(Main.class.getResource("/icon/septa_icon.png").getPath());
			Image image = customIcon.getImage(); // transform it
			Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
			customIcon = new ImageIcon(newimg); // transform it back

			// Add labels and text fields for each input
			JTextField trainNameField = new JTextField();
			panel.add(new JLabel("Train Name:"));
			panel.add(trainNameField);

			// Create radio buttons for train type
			JRadioButton passengerButton = new JRadioButton("Passenger");
			JRadioButton freightButton = new JRadioButton("Freight");
			JRadioButton mowButton = new JRadioButton("MOW");
			JPanel typeButtons = new JPanel(new GridLayout(1, 3)); // 6 rows, 2 columns

			ButtonGroup trainTypeGroup = new ButtonGroup();
			trainTypeGroup.add(passengerButton);
			trainTypeGroup.add(freightButton);
			trainTypeGroup.add(mowButton);
			panel.add(new JLabel("Train Type:"));
			typeButtons.add(passengerButton);
			typeButtons.add(freightButton);
			typeButtons.add(mowButton);
			panel.add(typeButtons);

			JTextField equipmentField = new JTextField();
			panel.add(new JLabel("Number of Equipment:"));
			panel.add(equipmentField);

			// Prompt user for priority (using a slider)
			JSlider prioritySlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 5); // Min = 0, Max = 10, Default = 5
			prioritySlider.setMajorTickSpacing(10);
			prioritySlider.setMinorTickSpacing(1);
			// prioritySlider.setPaintTicks(true);
			prioritySlider.setPaintLabels(true);
			panel.add(new JLabel("Select Priority:"));
			panel.add(prioritySlider);

			JRadioButton clockwiseButton = new JRadioButton("CW");
			JRadioButton counterclockwiseButton = new JRadioButton("CCW");
			JPanel directionButtons = new JPanel(new GridLayout(1, 2)); // 6 rows, 2 columns

			ButtonGroup trainDirectionGroup = new ButtonGroup();
			trainDirectionGroup.add(clockwiseButton);
			trainDirectionGroup.add(counterclockwiseButton);
			panel.add(new JLabel("Train Direction:"));
			directionButtons.add(clockwiseButton);
			directionButtons.add(counterclockwiseButton);
			panel.add(directionButtons);

			JComboBox<String> signalDropdown = new JComboBox<>();
			for (Signal signal : signalArray) {
				signalDropdown.addItem(signal.getName());
			}
			panel.add(new JLabel("Starting Signal:"));
			panel.add(signalDropdown);

			// Show the input dialog
			int result = JOptionPane.showConfirmDialog(frame, panel, "Add Train", JOptionPane.OK_CANCEL_OPTION,
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
			// Add action for Tool 1
			System.out.println("Tool 1 selected");
		});

		// Add menus to the menu bar
		menuBar.add(actionDropdown);
		menuBar.add(toolsDropdown);

		initialization(frame);
		setNodeConnections();

		// Create a separate thread for continuous updating
		Thread updateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					// Insert a small delay to prevent high CPU usage
					try {
						Thread.sleep(10); // Adjust this value as needed

						SIG_201A.update();
						SIG_201A.render(frame.getGraphics());
						SIG_201B.update();
						SIG_201B.render(frame.getGraphics());
						SIG_202A.update();
						SIG_202A.render(frame.getGraphics());
						SIG_202B.update();
						SIG_202B.render(frame.getGraphics());
						SIG_203A.update();
						SIG_203A.render(frame.getGraphics());
						SIG_203B.update();
						SIG_203B.render(frame.getGraphics());
						SIG_204A.update();
						SIG_204A.render(frame.getGraphics());
						SIG_204B.update();
						SIG_204B.render(frame.getGraphics());
						SIG_205A.update();
						SIG_205A.render(frame.getGraphics());
						SIG_205B.update();
						SIG_205B.render(frame.getGraphics());
						SIG_206A.update();
						SIG_206A.render(frame.getGraphics());
						SIG_206B.update();
						SIG_206B.render(frame.getGraphics());
						SIG_207A.update();
						SIG_207A.render(frame.getGraphics());
						SIG_208A.update();
						SIG_208A.render(frame.getGraphics());
						buffer1.update();
						buffer2.update();

						SW1.update();
						SW1.render(frame.getGraphics());
						SW2.update();
						SW2.render(frame.getGraphics());
						SW3.update();
						SW3.render(frame.getGraphics());
						SW4.update();
						SW4.render(frame.getGraphics());
						SW5.update();
						SW5.render(frame.getGraphics());
						SW6.update();
						SW6.render(frame.getGraphics());
						
						for(Node node : nodeArray) {
							node.update();
							node.renderGUI(frame.getGraphics(), Color.WHITE);
						}

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		updateThread.start();
	}

	static void initialization(JFrame frame) {
		int width = frame.getWidth();
		int height = frame.getHeight();

		SW6 = new Switch((float) (width / 3.78), (float) (height / 1.78), 330, 0, "CCW", "SW6", frame);
		SW5 = new Switch((float) (width / 3), (float) (height / 4), 30, 0, "CW", "SW5", frame);
		SW4 = new Switch((float) (width / 3 + Math.cos(Math.toRadians(30)) * width / 16),
				(float) (height / 4 + Math.sin(Math.toRadians(30)) * height / 8), 210, 180, "CCW", "SW4", frame);
		SW3 = new Switch((float) (width - width / 3.5), (float) (height / 1.6), 150, 180, "CW", "SW3", frame);
		SW2 = new Switch((float) (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 16),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 8), 0, 30, "CCW", "SW2", frame);
		SW1 = new Switch((float) (width / 3.5), (float) (height / 1.6), 30, 0, "CCW", "SW1", frame);

		SIG_201A = new Signal((float) (510), (float) (height / 4), "SIG-201A", "DOUBLE_HEAD", "-->", "CW", SW5, frame);
		signalArray.add(SIG_201A);
		SIG_201B = new Signal((float) (510), (float) (height / 3.2), "SIG-201B", "SINGLE_HEAD", "-->", "CW", null,
				frame);
		signalArray.add(SIG_201B);
		SIG_202A = new Signal((float) (910), (float) (height / 4), "SIG-202A", "SINGLE_HEAD", "<--", "CCW", null,
				frame);
		signalArray.add(SIG_202A);
		SIG_202B = new Signal((float) (910), (float) (height / 3.2), "SIG-202B", "DOUBLE_HEAD", "<--", "CCW", SW4,
				frame);
		signalArray.add(SIG_202B);
		SIG_203A = new Signal((float) (1480), (float) (height / 4), "SIG-203A", "DOUBLE_HEAD", "-->", "CW", SW3, frame);
		signalArray.add(SIG_203A);
		SIG_203B = new Signal((float) (1480), (float) (height / 3.2), "SIG-203B", "SINGLE_HEAD", "-->", "CW", null,
				frame);
		signalArray.add(SIG_203B);
		SIG_204A = new Signal((float) (450), (float) (height / 1.6), "SIG-204A", "DOUBLE_HEAD", "-->", "CCW", SW1,
				frame);
		signalArray.add(SIG_204A);
		SIG_204B = new Signal((float) (450), (float) (height / 1.78), "SIG-204B", "SINGLE_HEAD", "-->", "CCW", null,
				frame);
		signalArray.add(SIG_204B);
		SIG_205A = new Signal((float) (730), (float) (height / 1.6), "SIG-205A", "SINGLE_HEAD", "<--", "CW", null,
				frame);
		signalArray.add(SIG_205A);
		SIG_205B = new Signal((float) (730), (float) (height / 1.78), "SIG-205B", "SINGLE_HEAD", "<--", "CW", null,
				frame);
		signalArray.add(SIG_205B);
		SIG_206A = new Signal((float) (1200), (float) (height / 1.6), "SIG-206A", "SINGLE_HEAD", "-->", "CCW", null,
				frame);
		signalArray.add(SIG_206A);
		SIG_206B = new Signal((float) (1200), (float) (height / 1.78), "SIG-206B", "SINGLE_HEAD", "-->", "CCW", null,
				frame);
		signalArray.add(SIG_206B);
		SIG_207A = new Signal((float) (790), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4),
				"SIG-207A", "SINGLE_HEAD", "<--", "CW", null, frame);
		signalArray.add(SIG_207A);
		SIG_208A = new Signal((float) (1130), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4),
				"SIG-208A", "SINGLE_HEAD", "-->", "CCW", null, frame);
		signalArray.add(SIG_208A);
		buffer1 = new Signal((float) (width / 2), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 8),
				"buffer1", "SINGLE_HEAD", "-->", "CCW", null, frame); // FOR END OF FREIGHT STORAGE TRACK
		buffer2 = new Signal((float) (width / 2.7), (float) (height / 2.5 + Math.sin(Math.toRadians(30)) * height / 8),
				"buffer2", "SINGLE_HEAD", "-->", "CCW", null, frame);

		node1 = new Node(width / 7, height / 4, false, true, "1", null, null, false, frame);
		nodeArray.add(node1);
		node2 = new Node((float) width / 7, (float) (height / 3.2), false, true, "2", null, null, false, frame);
		nodeArray.add(node2);
		node3 = new Node(width - width / 7, height / 4, true, false, "3", null, null, true, frame);
		nodeArray.add(node3);
		node4 = new Node(width - width / 7, (float) (height / 3.2), true, false, "4", null, null, true, frame);
		nodeArray.add(node4);
		node5 = new Node(width / 7, (float) (height / 1.78), true, false, "5", null, null, true, frame);
		nodeArray.add(node5);
		node6 = new Node(width / 7, (float) (height / 1.6), true, false, "6", null, null, true, frame);
		nodeArray.add(node6);
		node7 = new Node(width - width / 7, (float) (height / 1.78), false, true, "7", null, null, false, frame);
		nodeArray.add(node7);
		node8 = new Node(width - width / 7, (float) (height / 1.6), false, true, "8", null, null, false, frame);
		nodeArray.add(node8);
		node9 = new Node((float) (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 8),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4), false, false, "9", null, null,
				false, frame);
		nodeArray.add(node9);
		node10 = new Node((float) (width - (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 8)),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4), false, false, "10", null, null,
				false, frame);
		nodeArray.add(node10);
	}

	static void setNodeConnections() {
		// Bleh, node connection hard code >:(

		// setNextAndPreviousNode = next node going clockwise
		// setPreviousNode = prev node going counterclockwise
		node1.setNextAndPreviousNode(SIG_201A.signalNode);
		// node1.setPreviousNode(node6);
		node2.setNextAndPreviousNode(SIG_201B.signalNode);
		// node2.setPreviousNode(node5);
		SIG_201A.signalNode.setNextAndPreviousNode(SW5.entryNode);
		// SIG_201A.signalNode.setPreviousNode(node1);
		// SW5.entryNode.setPreviousNode(SIG_201A.signalNode);
		SIG_201B.signalNode.setNextAndPreviousNode(SW4.closedNode);
		// SIG_201B.signalNode.setPreviousNode(node2);
		// SW4.closedNode.setPreviousNode(SIG_201B.signalNode);
		SW5.thrownNode.setNextAndPreviousNode(SW4.thrownNode);
		SW5.closedNode.setNextAndPreviousNode(SIG_202A.signalNode);
		// SIG_202A.signalNode.setPreviousNode(SW5.closedNode);
		SW4.entryNode.setNextAndPreviousNode(SIG_202B.signalNode);
		// SW4.thrownNode.setPreviousNode(SW5.thrownNode);
		// SIG_202B.signalNode.setPreviousNode(SW4.entryNode);
		SIG_202A.signalNode.setNextAndPreviousNode(SIG_203A.signalNode);
		// SIG_203A.signalNode.setPreviousNode(SIG_202A.signalNode);
		SIG_202B.signalNode.setNextAndPreviousNode(SIG_203B.signalNode);
		// SIG_203B.signalNode.setPreviousNode(SIG_202B.signalNode);
		SIG_203A.signalNode.setNextAndPreviousNode(node3);
		// node3.setPreviousNode(SIG_203A.signalNode);
		SIG_203B.signalNode.setNextAndPreviousNode(node4);
		// node4.setPreviousNode(SIG_203B.signalNode);
		node3.setNextAndPreviousNode(node8);
		// node8.setPreviousNode(node3);
		node4.setNextAndPreviousNode(node7);
		// node7.setPreviousNode(node4);
		node7.setNextAndPreviousNode(SIG_206B.signalNode);
		// SIG_206B.signalNode.setPreviousNode(node7);
		SIG_206B.signalNode.setNextAndPreviousNode(SIG_205B.signalNode);
		// SIG_205B.signalNode.setPreviousNode(SIG_206B.signalNode);
		SIG_205B.signalNode.setNextAndPreviousNode(SW6.closedNode);
		// SIG_204B.signalNode.setPreviousNode(SW6.entryNode);
		SW6.entryNode.setNextAndPreviousNode(SIG_204B.signalNode);
		// SW6.closedNode.setPreviousNode(SIG_205B.signalNode);
		SIG_204B.signalNode.setNextAndPreviousNode(node5);
		// node5.setPreviousNode(SIG_204B.signalNode);
		node5.setNextAndPreviousNode(node2);
		node8.setNextAndPreviousNode(SW3.entryNode);
		// SW3.entryNode.setPreviousNode(node8);
		SW3.closedNode.setNextAndPreviousNode(SIG_206A.signalNode);
		// SIG_206A.signalNode.setPreviousNode(SW3.closedNode);
		SIG_206A.signalNode.setNextAndPreviousNode(SIG_205A.signalNode);
		// SIG_205A.signalNode.setPreviousNode(SIG_206A.signalNode);
		SIG_205A.signalNode.setNextAndPreviousNode(SW1.closedNode);
		// SW1.closedNode.setPreviousNode(SIG_205A.signalNode);
		SW1.entryNode.setNextAndPreviousNode(SIG_204A.signalNode);
		// SIG_204A.signalNode.setPreviousNode(SW1.entryNode);
		SIG_204A.signalNode.setNextAndPreviousNode(node6);
		// node6.setPreviousNode(SIG_204A.signalNode);
		node6.setNextAndPreviousNode(node1);
		SW3.thrownNode.setNextAndPreviousNode(node10);
		// node10.setPreviousNode(SW3.thrownNode);
		node10.setNextAndPreviousNode(SIG_208A.signalNode);
		// SIG_208A.signalNode.setPreviousNode(node10);
		SIG_208A.signalNode.setNextAndPreviousNode(SIG_207A.signalNode);
		// SIG_207A.signalNode.setPreviousNode(SIG_208A.signalNode);
		SIG_207A.signalNode.setNextAndPreviousNode(node9);
		// node9.setPreviousNode(SIG_207A.signalNode);
		node9.setNextAndPreviousNode(SW2.closedNode);
		// SW2.closedNode.setPreviousNode(node9);
		SW2.entryNode.setNextAndPreviousNode(SW1.thrownNode);
		// SW1.thrownNode.setPreviousNode(SW2.entryNode);
		// SW2.thrownNode.setPreviousNode(buffer1.signalNode);
		buffer1.signalNode.setNextAndPreviousNode(SW2.thrownNode);
		// buffer1.signalNode.setPreviousNode(null);
		buffer2.signalNode.setNextAndPreviousNode(SW6.thrownNode);
		// buffer2.signalNode.setPreviousNode(null);
	}
	
	//git testing
}
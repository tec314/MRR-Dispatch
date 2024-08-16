import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class MRRDispatchFrame extends JFrame {
	public static Switch SW7;
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
	public static Signal buffer3;

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
	public static ArrayList<Train> trainArray = new ArrayList<Train>();
	
	public static Boolean debug = false;
	public static Boolean isFullscreen = false;
	private GraphicsDevice device;
	private Rectangle windowedBounds;
	
	public JPanel optionsPanel = new JPanel(new GridLayout()); // 6 rows, 2 columns
	public JPanel mapPanel = new DrawPanel();
	public JLabel coordLabel = new JLabel();
	
	public static JTable terminalOut = new JTable();
	public static DefaultTableModel model = new DefaultTableModel();
	
	public static SerialComm serial; // Serial object
	
	public MRRDispatchFrame() {
		super("MRR Dispatcher");
		serial = new SerialComm("COM6"); // Main serial object for Arduino communication, set to COM7 by default 
		// Change this so you can change the COM port if needed (no static value)!
		
		//serial.startReading();
		
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
		JMenuItem toggleLightingButton = new JMenuItem("Toggle Lighting");

		// Add action items to actions drop-down
		actionDropdown.add(addTrainButton);
		actionDropdown.add(removeTrainButton);
		actionDropdown.add(setTrainDirectionButton);
		actionDropdown.add(toggleLightingButton);

		// Add actionButton menu to menu bar
		menuBar.add(actionDropdown);

		// Create toolsButton menu
		JMenu toolsDropdown = new JMenu("Tools");
		JMenuItem debugToggleButton = new JMenuItem("Toggle Debug Mode");
		JMenuItem setPortButton = new JMenuItem("Set Master Arduino Port");
		JMenuItem testPingButton = new JMenuItem("Test Ping Arduinos");

		// Add tool items to tools drop-down
		toolsDropdown.add(debugToggleButton);
		toolsDropdown.add(setPortButton);
		toolsDropdown.add(testPingButton);
		
		// Create toolsButton menu
		JMenu windowDropdown = new JMenu("Window");
		JMenuItem fullscreenToggleButton = new JMenuItem("Toggle Fullscreen");

		// Add tool items to tools drop-down
		windowDropdown.add(fullscreenToggleButton);

		JMenu helpDropdown = new JMenu("Help");
		JMenuItem aboutButton = new JMenuItem("About");
		JMenuItem githubButton = new JMenuItem("Visit GitHub Page");
		
		helpDropdown.add(aboutButton);
		helpDropdown.add(githubButton);
		
		menuBar.add(helpDropdown);
		
		// Add menus to the menu bar
		menuBar.add(actionDropdown);
		menuBar.add(toolsDropdown);
		menuBar.add(windowDropdown);
		menuBar.add(helpDropdown);
		
		// Set the menu bar for the frame
		optionsPanel.add(menuBar);

		// Setting a new port for the Master Arduino connection
		setPortButton.addActionListener(e -> {
			JPanel windowPanel = new JPanel(new BorderLayout()); // 6 rows, 2 columns
			windowPanel.setSize(800, 800);

			windowPanel.add(new JLabel("Select New COM Port"), BorderLayout.NORTH);
			
			JComboBox<String> portOptions = new JComboBox<>();
			
			// Change this so that it detects what COM ports are available on the host's computer?
			portOptions.addItem("COM1");
			portOptions.addItem("COM2");
			portOptions.addItem("COM3");
			portOptions.addItem("COM4");
			portOptions.addItem("COM5");
			portOptions.addItem("COM6");
			portOptions.addItem("COM7");
			portOptions.addItem("COM8");
			portOptions.addItem("COM9");
			portOptions.addItem("COM10");
			
			portOptions.setSelectedIndex(0);

			windowPanel.add(portOptions, BorderLayout.CENTER);
	        
			int result = JOptionPane.showConfirmDialog(optionsPanel, windowPanel, "Select New Port", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			
			if (result == JOptionPane.OK_OPTION) {
				serial = new SerialComm(portOptions.getSelectedItem().toString());
			}
		});
			
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

			SpinnerModel model = new SpinnerNumberModel(1, 1, 100, 1);     
			JSpinner numEquipmentSpinner = new JSpinner(model);
			windowPanel.add(new JLabel("Number of Equipment:"));
			windowPanel.add(numEquipmentSpinner);

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
			JPanel directionButtons = new JPanel(new GridLayout(1, 2)); 

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

			// If the user clicks OK, retrieve the values from the fields
			if (result == JOptionPane.OK_OPTION) {
				String trainName = trainNameField.getText();
				String trainType = null;
				String direction = null;
				Node startingSignal = null;
				for (Node node : nodeArray) {
					if(node.getSignal() != null && String.valueOf(signalDropdown.getSelectedItem()).equals(node.getSignal().getName())) {
						startingSignal = node;
					}
				}
				int numberOfEquipment = (int) numEquipmentSpinner.getValue();
				int priority = prioritySlider.getValue();
				
				if (passengerButton.isSelected()) {
					trainType = "Passenger";
				} else if (freightButton.isSelected()) {
					trainType = "Freight";
				} else if (mowButton.isSelected()) {
					trainType = "MOW";
				}
				
				if (clockwiseButton.isSelected()) {
					direction = "CW";
				} else if (counterclockwiseButton.isSelected()) {
					direction = "CCW";
				} 
				
				// Starting node is already occupied by the pathTracer for another train
				if(startingSignal.isOccupied()) {
					JOptionPane.showMessageDialog(null, "ERROR\nAlready occupied by another train's routing!", "Error", JOptionPane.ERROR_MESSAGE);
				} 
				// Correct entry, create new train for entered inputs
				else if(trainName != null && trainType != null && direction != null && startingSignal != null && numberOfEquipment > 0 && priority > 0) {
					trainArray.add(new Train (trainName, trainType, numberOfEquipment, priority, direction, startingSignal, mapPanel));
					updateTrains();
				} 
				// Error in entry, try again
				else {
					JOptionPane.showMessageDialog(null, "INVALID ENTRY\nPlease fill in all fields correctly.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		removeTrainButton.addActionListener(e -> {
			if(!trainArray.isEmpty()) {
				// Add action for Action 1
				System.out.println("Action 1 selected");
				JPanel windowPanel = new JPanel(new GridLayout(6, 2)); // 6 rows, 2 columns
				windowPanel.setSize(800, 800);
				ImageIcon customIcon = new ImageIcon(Main.class.getResource("/icon/septa_icon.png").getPath());
				Image image = customIcon.getImage(); // transform it
				Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				customIcon = new ImageIcon(newimg); // transform it back
	
				JComboBox<String> trainDropdown = new JComboBox<>();
				for (Train train : trainArray) {
					trainDropdown.addItem(train.getName());
				}
				windowPanel.add(new JLabel("Select Train:"));
				windowPanel.add(trainDropdown);
	
				// Show the input dialog
				int result = JOptionPane.showConfirmDialog(optionsPanel, windowPanel, "Remove Train", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, customIcon);
	
				// If the user clicks OK, retrieve the values from the fields
				if (result == JOptionPane.OK_OPTION) {
					for (int i = 0; i < trainArray.size(); i++) {
						if(String.valueOf(trainDropdown.getSelectedItem()).equals(trainArray.get(i).getName())) {
							trainArray.get(i).prepareForRemoval();
							trainArray.remove(i);
						}
					}
					
					if(trainDropdown.getSelectedItem() != null) {
						// Correct entry, create new train for entered inputs
						updateTrains();
					} else {
						// Error in entry, try again
						JOptionPane.showMessageDialog(null, "INVALID ENTRY\nPlease select a train.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "ERROR\nNo trains to remove.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		setTrainDirectionButton.addActionListener(e -> {
			// Add action for Action 3
			System.out.println("Action 3 selected");
			if(!trainArray.isEmpty()) {
				// Add action for Action 1
				System.out.println("Action 1 selected");
				JPanel windowPanel = new JPanel(new GridLayout(6, 2)); // 6 rows, 2 columns
				windowPanel.setSize(800, 800);
				ImageIcon customIcon = new ImageIcon(Main.class.getResource("/icon/septa_icon.png").getPath());
				Image image = customIcon.getImage(); // transform it
				Image newimg = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
				customIcon = new ImageIcon(newimg); // transform it back
	
				JComboBox<String> trainDropdown = new JComboBox<>();
				for (Train train : trainArray) {
					trainDropdown.addItem(train.getName());
				}
				windowPanel.add(new JLabel("Select Train:"));
				windowPanel.add(trainDropdown);
				
				JRadioButton clockwiseButton = new JRadioButton("CW");
				JRadioButton counterclockwiseButton = new JRadioButton("CCW");
				JPanel directionButtons = new JPanel(new GridLayout(1, 2)); 
				windowPanel.add(new JLabel("New Train Direction:"));
				directionButtons.add(clockwiseButton);
				directionButtons.add(counterclockwiseButton);
				windowPanel.add(directionButtons);
	
				// Show the input dialog
				int result = JOptionPane.showConfirmDialog(optionsPanel, windowPanel, "Remove Train", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, customIcon);
	
				// If the user clicks OK, retrieve the values from the fields
				if (result == JOptionPane.OK_OPTION) {
					String direction = null;
					Train selectedTrain = null;
					for (int i = 0; i < trainArray.size(); i++) {
						if(String.valueOf(trainDropdown.getSelectedItem()).equals(trainArray.get(i).getName())) {
							selectedTrain = trainArray.get(i);
						}
					}
					
					if (clockwiseButton.isSelected()) {
						direction = "CW";
					} else if (counterclockwiseButton.isSelected()) {
						direction = "CCW";
					} 
					
					if(trainDropdown.getSelectedItem() != null && direction != null) {
						// Correct entry, create new train for entered inputs
						selectedTrain.updateDirection(direction);
						updateTrains();
					} else {
						// Error in entry, try again
						JOptionPane.showMessageDialog(null, "INVALID ENTRY\nPlease select a train.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(null, "ERROR\nNo trains to update direction.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		// Turn on lighting to all Arduino's containing lighting accessories
		toggleLightingButton.addActionListener(e -> {
			JPanel windowPanel = new JPanel(new GridLayout(2, 2)); // 6 rows, 2 columns
			windowPanel.add(new JLabel("Turn ON"));
			JButton onButton = new JButton("ON");
			windowPanel.add(onButton);
			
			windowPanel.add(new JLabel("Turn OFF"));
			JButton offButton = new JButton("OFF");
			windowPanel.add(offButton);
			
			onButton.addActionListener(f -> {
				serial.writeData("LIGHTS_ON");
			});
			
			offButton.addActionListener(g -> {
				serial.writeData("LIGHTS_OFF");
			});
			
			JOptionPane.showConfirmDialog(optionsPanel, windowPanel, "Toggle Lighting", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
		});

		debugToggleButton.addActionListener(e -> {
			debug = !debug;
		});
		
		githubButton.addActionListener(e -> {
		    // Specify the URL you want to redirect the user to
		    String redirectURL = "https://github.com/tec314/MRR-Dispatch";
		    
		    // Get the default desktop browser and open the URL
		    try {
		        Desktop.getDesktop().browse(new URI(redirectURL));
		    } catch (IOException ex) {
		        ex.printStackTrace();
		        // Handle IO exceptions here
		    } catch (URISyntaxException ex) {
		        ex.printStackTrace();
		        // Handle URI syntax exceptions here
		    }
		});
		
		testPingButton.addActionListener(e -> {
			JPanel windowPanel = new JPanel(new BorderLayout()); // 6 rows, 2 columns
			windowPanel.setSize(800, 800);

			//windowPanel.add(new JLabel("Arduino Ping Test"));
			
			JButton startPing = new JButton("Start Ping");
			startPing.addActionListener(f -> {
				model.setRowCount(0);
				serial.writeData("TEST_GETNAME");
			});
			
			windowPanel.add(startPing, BorderLayout.SOUTH);
			
			terminalOut.setDefaultEditor(Object.class, null); // Makes table not editable
			Object col[] = {"ArduinoName", "TT", "Success?"};
			model.setColumnIdentifiers(col);
			terminalOut.setModel(model);

	        JScrollPane scrollPane = new JScrollPane(terminalOut);
	        
	        windowPanel.add(scrollPane, BorderLayout.CENTER);
			int result = JOptionPane.showConfirmDialog(optionsPanel, windowPanel, "Test Ping", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);
		});
		
		device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		fullscreenToggleButton.addActionListener(e -> {
	        if (isFullscreen) {
	            // Exit fullscreen
	            device.setFullScreenWindow(null);  // Exit fullscreen
	            setVisible(false);  // Hide the frame temporarily
	            dispose();  // Make the frame non-displayable
	            setUndecorated(false);  // Restore window decorations
	            setBounds(windowedBounds);  // Restore the previous window bounds
	            setVisible(true);  // Make the frame visible again
	        } else {
	            // Enter fullscreen
	            windowedBounds = getBounds();  // Store the current window bounds
	            setVisible(false);  // Hide the frame temporarily
	            dispose();  // Make the frame non-displayable
	            setUndecorated(true);  // Remove window decorations
	            setVisible(true);  // Make the frame visible again
	            device.setFullScreenWindow(this);  // Enter fullscreen
	        }
	        isFullscreen = !isFullscreen;
	        revalidate();
	        repaint();
		});
		
		add(optionsPanel, BorderLayout.NORTH);
		add(mapPanel, BorderLayout.CENTER);
		add(coordLabel, BorderLayout.SOUTH);
		
		initialization(mapPanel); // Initializes all objects
		setNodeConnections(); // Connects all nodes together
		
		System.out.println("DIM: " + mapPanel.getWidth() + ", " + mapPanel.getHeight());
	}
	
	// Initialize major map objects (signals, switches, nodes)
	void initialization(JPanel panel) {
		int width = panel.getWidth();
		int height = panel.getHeight();
		
		// Switch Initializations
		SW7 = new Switch(serial, (float) (width / 3.12), (float) (height / 2), 0, 330, "CCW", "NW", "SW7", panel);
		SW6 = new Switch(serial, (float) (width / 3.78), (float) (height / 1.78), 330, 0, "CCW", "NW", "SW6", panel);
		SW5 = new Switch(serial, (float) (width / 3), (float) (height / 4), 30, 0, "CW", "NW", "SW5", panel);
		SW4 = new Switch(serial, (float) (width / 3 + Math.cos(Math.toRadians(30)) * width / 16),
				(float) (height / 3.2), 210, 180, "CCW", "SE", "SW4", panel);
		SW3 = new Switch(serial, (float) (width - width / 3.5), (float) (height / 1.6), 150, 180, "CW", "SE", "SW3", panel);
		SW2 = new Switch(serial, (float) (width / 3.5 + Math.cos(Math.toRadians(30)) * width / 16),
				(float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 8), 0, 30, "CCW", "SW", "SW2", panel);
		SW1 = new Switch(serial, (float) (width / 3.5), (float) (height / 1.6), 30, 0, "CCW", "NE", "SW1", panel);
		
		// Signal Initializations
		SIG_201A = new Signal(serial, (float) (510), (float) (height / 4), "SIG-201A", "DOUBLE_HEAD", "-->", "CW", SW5, false, panel);
		SIG_201B = new Signal(serial, (float) (510), (float) (height / 3.2), "SIG-201B", "SINGLE_HEAD", "-->", "CW", null,
				false, panel);
		SIG_202A = new Signal(serial, (float) (910), (float) (height / 4), "SIG-202A", "SINGLE_HEAD", "<--", "CCW", null,
				false, panel);
		SIG_202B = new Signal(serial, (float) (910), (float) (height / 3.2), "SIG-202B", "DOUBLE_HEAD", "<--", "CCW", SW4,
				false, panel);
		SIG_203A = new Signal(serial, (float) (1480), (float) (height / 4), "SIG-203A", "DOUBLE_HEAD", "-->", "CW", SW3, false, panel);
		SIG_203B = new Signal(serial, (float) (1480), (float) (height / 3.2), "SIG-203B", "SINGLE_HEAD", "-->", "CW", null,
				false, panel);
		SIG_204A = new Signal(serial, (float) (450), (float) (height / 1.6), "SIG-204A", "DOUBLE_HEAD", "-->", "CCW", SW1,
				false, panel);
		SIG_204B = new Signal(serial, (float) (450), (float) (height / 1.78), "SIG-204B", "SINGLE_HEAD", "-->", "CCW", null,
				false, panel);
		SIG_205A = new Signal(serial, (float) (730), (float) (height / 1.6), "SIG-205A", "SINGLE_HEAD", "<--", "CW", null,
				false, panel);
		SIG_205B = new Signal(serial, (float) (730), (float) (height / 1.78), "SIG-205B", "SINGLE_HEAD", "<--", "CW", null,
				false, panel);
		SIG_206A = new Signal(serial, (float) (1200), (float) (height / 1.6), "SIG-206A", "SINGLE_HEAD", "-->", "CCW", null,
				false, panel);
		SIG_206B = new Signal(serial, (float) (1200), (float) (height / 1.78), "SIG-206B", "SINGLE_HEAD", "-->", "CCW", null,
				false, panel);
		SIG_207A = new Signal(serial, (float) (790), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4),
				"SIG-207A", "SINGLE_HEAD", "<--", "CW", null, false, panel);
		SIG_208A = new Signal(serial, (float) (1130), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 4),
				"SIG-208A", "SINGLE_HEAD", "-->", "CCW", null, false, panel);
		buffer1 = new Signal(serial, (float) (width / 2), (float) (height / 1.6 + Math.sin(Math.toRadians(30)) * height / 8),
				"buffer1", "SINGLE_HEAD", "-->", "CCW", null, true, panel); // FOR END OF FREIGHT STORAGE TRACK
		buffer2 = new Signal(serial, (float) (width / 2), (float) (height / 2),
				"buffer2", "SINGLE_HEAD", "-->", "CCW", null, true, panel);
		buffer3 = new Signal(serial, (float) (width / 2.64), (float) (height / 2.3),
				"buffer3", "SINGLE_HEAD", "-->", "CCW", null, true, panel);
		
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
	
	// Create links between nodes on map (hard coding)
	void setNodeConnections() {
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
		SW7.entryNode.setNextAndPreviousNode(SW6.thrownNode);
		buffer2.signalNode.setNextAndPreviousNode(SW7.thrownNode);
		buffer3.signalNode.setNextAndPreviousNode(SW7.closedNode);
		
		repaint();
	}
	
	// Paint refreshing code
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
			}
			
			for(Train train : trainArray) {
				//train.update();
				train.render();
				repaint();
			}
			
			activeTrainLabel(g2d);
			mapLabeling(g2d);
			mapImagery(g2d);
			debugTerminal(g2d);
		}
	}
	
	// Current active train status
	void activeTrainLabel(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.drawString("Active Trains", getWidth() - 750, 20);
		g2d.drawString("Name", getWidth() - 600, 20);
		g2d.drawString("Type", getWidth() - 500, 20);
		g2d.drawString("Direction", getWidth() - 400, 20);
		g2d.drawString("Is Loop?", getWidth() - 300, 20);
		g2d.drawString("________________________________________________________________________", getWidth() - 750, 24);

		// Updates current train info to be displayed
		for (int i = 0; i < trainArray.size(); i++) {
			g2d.setColor(trainArray.get(i).getGeneratedPath().getPathColor());
			g2d.drawString("—", getWidth() - 660, 40 + i * 20); 
			 
			g2d.setColor(Color.WHITE);
	        g2d.drawString(trainArray.get(i).getName(), getWidth() - 600, 40 + i * 20); 
	        
	        if(trainArray.get(i).getType().equals("Passenger")) {
	        	g2d.setColor(new Color(43, 163, 255));
			} else if(trainArray.get(i).getType().equals("Freight")) {
				g2d.setColor(Color.ORANGE);
			} else {
				g2d.setColor(Color.RED);
			}
	        g2d.drawString(trainArray.get(i).getType(), getWidth() - 500, 40 + i * 20); 
	        
	        g2d.setColor(Color.MAGENTA);
	        if(trainArray.get(i).getDirection().equals("CW")) {
	        	g2d.drawString(trainArray.get(i).getDirection() + "    ⭮", getWidth() - 400, 40 + i * 20);
			} else {
				g2d.drawString(trainArray.get(i).getDirection() + "    ⭯", getWidth() - 400, 40 + i * 20);
			}
	        
	        if(trainArray.get(i).getGeneratedPath().isLoop()) {
	        	g2d.setColor(Color.GREEN);
	        	g2d.drawString("LOOP", getWidth() - 300, 40 + i * 20);
			} else {
				g2d.setColor(Color.RED);
				g2d.drawString("NO LOOP", getWidth() - 300, 40 + i * 20);
			}
	    }	
	}
	
	// Map labeling (interlockings, storage tracks, information, etc.)
	void mapLabeling(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.drawString("Model Railroad Dispatching Software", 10, 20);
		g2d.drawString("2024 | Pre-alpha", 10, 35);
		
		//g2d.drawString("CRID INTERLOCKING", getWidth()/3, getHeight()/6); 
		//g2d.drawString("ALB INTERLOCKING", getWidth()/5, 2*getHeight()/3); 
		//g2d.drawString("BUD INTERLOCKING", 7*getWidth()/11, 7*getHeight()/15); 
		//g2d.drawString("____ STATION", getWidth()/2, 10*getHeight()/21); 
		//g2d.drawString("EAST PENN CUTOFF", 13*getWidth()/30, 3*getHeight()/4);
		g2d.drawString("TRK1", getWidth()/7, 7*getHeight()/33); 
		g2d.drawString("TRK2", getWidth()/7, 14*getHeight()/51); 
		g2d.drawString("STORAGE1", 13*getWidth()/30, 5*getHeight()/8); 
		g2d.drawString("STORAGE2", 5*getWidth()/11, 13*getHeight()/29); 
		g2d.drawString("STORAGE3", 5*getWidth()/13, 12*getHeight()/30); 
	}	
	
	// Graphics found throughout program
	void mapImagery(Graphics2D g2d) {
		Image image;
		ImageIcon amtrakLogoIcon = new ImageIcon(Main.class.getResource("/icon/amtrak_logo.png").getPath());
		image = amtrakLogoIcon.getImage(); // transform it

        AffineTransform transform = new AffineTransform();
        transform.translate(30, getHeight() - 130);
        transform.scale(.035, .035);
        
		g2d.drawImage(image, transform, this);
		
		ImageIcon csxIcon = new ImageIcon(Main.class.getResource("/icon/csx_logo.png").getPath());
		image = csxIcon.getImage(); // transform it

        transform = new AffineTransform();
        transform.translate(130, getHeight() - 125);
        transform.scale(.2, .2);
        
		g2d.drawImage(image, transform, this);
		
		ImageIcon mrrdIcon = new ImageIcon(Main.class.getResource("/mrrd_logo.png").getPath());
		image = mrrdIcon.getImage(); // transform it

        transform = new AffineTransform();
        transform.translate(20, getHeight() - 260);
        transform.scale(.12, .12);
        
		g2d.drawImage(image, transform, this);
	}
	
	// Debug Terminal (displays information sent and received)
	void debugTerminal(Graphics2D g2d) {
		g2d.setColor(Color.WHITE);
		g2d.drawString("Communication Terminal", getWidth() - 500, 830);
		g2d.drawString("_________________________________________________________", getWidth() - 500, 834);
		for (int i = 0; i < serial.getDebugArray().size(); i++) {
			g2d.setColor(Color.GREEN);
			g2d.drawString(">>", getWidth() - 520, 850);
			g2d.setColor(Color.WHITE);
			g2d.drawString(serial.getDebugArray().get(i).substring(0, 25), getWidth() - 500, 850 + i * 20);
			g2d.drawString(serial.getDebugArray().get(i).substring(28), getWidth() - 280, 850 + i * 20);
			String d = serial.getDebugArray().get(i).substring(25, 28);
			if(d.equals("OUT")) {
				g2d.setColor(Color.ORANGE);
			} else {
				g2d.setColor(Color.CYAN);
			}
			g2d.drawString(d, getWidth() - 330, 850 + i * 20);
		}
	}
	
	// Mouse events
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
	
	public static void updateTrains() {
		for(Train train : trainArray) {
			train.update();
		}
	}
}


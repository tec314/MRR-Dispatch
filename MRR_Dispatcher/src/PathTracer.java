// Used for creating a track path for a particular train

import java.awt.*;
import java.util.*;

import javax.swing.JPanel;

class pathTracer {

	Node startNode;
	Node thisCurrentNode;
	Node thisFollowingNode;

	Boolean restrictedAccess = false;
	int priorityOfPath; // ranked on scale from 1 (lowest) to 10 (highest) and compared to other train
						// paths to see which can occupy first

	// Drawing path on screen
	ArrayList<Node> nodePath = new ArrayList<Node>();
	ArrayList<Node> currentBlockPath = new ArrayList<Node>();
	ArrayList<Node> forwSignalPath = new ArrayList<Node>();
	ArrayList<Node> oppSignalPath = new ArrayList<Node>();

	Boolean completeLoop = false;
	Boolean cannotCreatePath = false;
	String name;
	Color pathColor;
	String pathDirection = "CW";

	private JPanel panel;

	pathTracer(Node start, int p, String n, Color c, JPanel panel) {
		this.startNode = start; // initial starting point
		this.priorityOfPath = p;

		this.name = n;
		this.pathColor = c;
		this.panel = panel;
	}

	// Update the current starting point of the path
	void setNewStart(Node start) {
		startNode = start;
	}

	void resetPath() {
		for (Node node : nodePath) {
			node.setPathColor(Color.WHITE);
			node.isEndOfPath(false);
			node.unoccupyNode();
		}
		
		// Resets all existing signals to STOP !!!!!! THIS MAY BE CAUSING ISSUES FOR
		// MULTI TRAIN + ISSUES FOR SIGNAL DISPLAY!!!!!!!!!!
		for (Node node : forwSignalPath) {
			if (node.getSignal() != null) {
				node.getSignal().signalState("STOP");
			}
		}

		for (Node node : oppSignalPath) {
			if (node.getSignal() != null) {
				node.getSignal().signalState("STOP");
			}
		}

		nodePath.clear(); // clears nodes from previous path for new path
		forwSignalPath.clear(); // clears signals from previous path for new path
		oppSignalPath.clear(); // clears signals behind train
	}

	void tracePath() {
		// Resets current path and is ready for next
		resetPath();

		// println("START NODE: " + str(startNode.getOccupiedPriority()));
		// println("THIS PATH: " + str(priorityOfPath));
		if (startNode.isOccupied() && startNode.getOccupiedPriority() > priorityOfPath) { // !!!!! START NODE IS 0 FOR
																							// SOME REASON
			cannotCreatePath = true;
		} else {
			cannotCreatePath = false;
			thisCurrentNode = startNode;
			restrictedAccess = false;
			startNode.occupyNode(priorityOfPath); // <-- maybe? nope lmao

			if (pathDirection.equals("CW")) {
				thisFollowingNode = startNode.getNextNode();
			} else {
				thisFollowingNode = startNode.getPreviousNode();
			}

			// Creates path using linked list !!!!!! FIGURE OUT SYSTEM FOR IF OCCUPIED FOR
			// MULTIPLE TRAINS
			while (thisFollowingNode != null && thisFollowingNode != startNode && !restrictedAccess) {
				// println(name + " -> Occ Node Priority: " +
				// str(thisFollowingNode.getOccupiedPriority()) + " This Path Priority: " +
				// str(priorityOfPath));
				// println("Direction: " + pathDirection);
				if (thisFollowingNode.isOccupied() && thisFollowingNode.getOccupiedPriority() > priorityOfPath) {
					restrictedAccess = true;
				} else {
					nodePath.add(thisCurrentNode);
					thisCurrentNode.occupyNode(priorityOfPath);
					if (thisCurrentNode.getSignal() != null
							&& thisCurrentNode.getSignal().getSignalFlow().equals(pathDirection)) {
						forwSignalPath.add(thisCurrentNode);
					}
					thisCurrentNode = thisFollowingNode;
					if (pathDirection.equals("CW")) {
						thisFollowingNode = thisCurrentNode.getNextNode();
					} else {
						thisFollowingNode = thisCurrentNode.getPreviousNode();
					}
				}
			}

			if (thisFollowingNode == startNode) {
				nodePath.add(thisCurrentNode);
				if (thisCurrentNode.getSignal() != null
						&& thisCurrentNode.getSignal().getSignalFlow().equals(pathDirection)) {
					oppSignalPath.add(thisCurrentNode);
				}
				thisCurrentNode = thisFollowingNode;
				if (pathDirection.equals("CW")) {
					thisFollowingNode = thisCurrentNode.getNextNode();
				} else {
					thisFollowingNode = thisCurrentNode.getPreviousNode();
				}
				completeLoop = true;

				for (int i = 0; i < forwSignalPath.size(); i++) {
					forwSignalPath.get(i).getSignal().signalState("CLEAR");
				}
			}

			// Hit dead end
			if (thisFollowingNode == null || restrictedAccess) {
				nodePath.add(thisCurrentNode);
				if (thisCurrentNode.getSignal() != null
						&& thisCurrentNode.getSignal().getSignalFlow().equals(pathDirection)) {
					forwSignalPath.add(thisCurrentNode);
				}
				thisCurrentNode.isEndOfPath(true);
				completeLoop = false;

				// Set signals ahead of starting node for trains to prepare to stop at a
				// possible endpoint
				if (forwSignalPath.size() > 0)
					forwSignalPath.get(forwSignalPath.size() - 1).getSignal().signalState("STOP");
				if (forwSignalPath.size() > 1)
					forwSignalPath.get(forwSignalPath.size() - 2).getSignal().signalState("APPROACH");
				if (forwSignalPath.size() > 2)
					for (int i = forwSignalPath.size() - 3; i >= 0; i--) {
						forwSignalPath.get(i).getSignal().signalState("CLEAR");
					}
			}
			setSignalsBehind();
		}
	}

	// Set signals behind of starting nodes for other trains behind occupied block
	void setSignalsBehind() {
		String oppDirection;

		thisCurrentNode = startNode;
		if (pathDirection.equals("CW")) {
			oppDirection = "CCW";
			thisFollowingNode = startNode.getPreviousNode();
		} else {
			oppDirection = "CW";
			thisFollowingNode = startNode.getNextNode();
		}

		while (thisFollowingNode != null && thisFollowingNode != startNode) {
			if (thisCurrentNode.getSignal() != null
					&& thisCurrentNode.getSignal().getSignalFlow().equals(pathDirection)) {
				oppSignalPath.add(thisCurrentNode);
			}
			thisCurrentNode = thisFollowingNode;
			if (oppDirection.equals("CW")) {
				thisFollowingNode = thisCurrentNode.getNextNode();
			} else {
				thisFollowingNode = thisCurrentNode.getPreviousNode();
			}
		}

		if (thisFollowingNode == startNode) {
			if (thisCurrentNode.getSignal() != null
					&& thisCurrentNode.getSignal().getSignalFlow().equals(pathDirection)) {
				oppSignalPath.add(thisCurrentNode);
			}
			thisCurrentNode = thisFollowingNode;
			if (oppDirection.equals("CW")) {
				thisFollowingNode = thisCurrentNode.getNextNode();
			} else {
				thisFollowingNode = thisCurrentNode.getPreviousNode();
			}
		}

		if (oppSignalPath.size() > 0)
			oppSignalPath.get(0).getSignal().signalState("STOP");
		if (oppSignalPath.size() > 1)
			oppSignalPath.get(1).getSignal().signalState("APPROACH");
	}

	// Complete possible path for train to take (renders first)
	void renderPath() {
		for (Node node : nodePath) {
			node.update();
			node.renderConnections(panel.getGraphics(), pathColor, pathDirection);
		}
	}

	// Second render of only the block portion of the path, that is the current spot
	// of the train relative to signals (as these are the places where IR sensors
	// can detect where a train is)
	void renderBlock() {
		Boolean findBlockEnd = true;
		for (Node node : currentBlockPath) {
			// Current block of the train is colored
			node.isArrowLocation(false);
		}
		currentBlockPath.clear();

		for (Node node : nodePath) {
			if (findBlockEnd && node.getSignal() != null && node != startNode) {
				currentBlockPath.add(node); // This last entry is the ending signal of the current block

				findBlockEnd = false;
			} else if (findBlockEnd) {
				currentBlockPath.add(node);
			}
		}
		for (int i = 0; i < currentBlockPath.size() - 1; i++) {
			// Current block of the train is colored
			currentBlockPath.get(i).update();
			currentBlockPath.get(i).renderConnections(panel.getGraphics(), Color.RED, pathDirection);
		}
		
		int i = currentBlockPath.size() - 1;
		currentBlockPath.get(i).isArrowLocation(true);
		currentBlockPath.get(i).setPathDirection(pathDirection);
	}

	Boolean isLoop() {
		return completeLoop;
	}

	Boolean cannotCreate() {
		return cannotCreatePath;
	}
	
	Color getPathColor() {
		return pathColor;
	}

	void setPathDirection(String dir) {
		pathDirection = dir;
	}
}
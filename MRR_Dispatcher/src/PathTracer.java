// Used for creating a track path for a particular train

import java.awt.*;
import java.util.*;

class pathTracer {

  Node startNode;
  Node thisCurrentNode;
  Node thisFollowingNode;
  
  Boolean restrictedAccess = false;
  int priorityOfPath; // ranked on scale from 1 (lowest) to 10 (highest) and compared to other train paths to see which can occupy first
  
  // Drawing path on screen
  ArrayList<Node> nodePath = new ArrayList<Node>();
  ArrayList<Node> blockPath = new ArrayList<Node>();
  ArrayList<Node> forwSignalPath = new ArrayList<Node>();
  ArrayList<Node> oppSignalPath = new ArrayList<Node>();

  Boolean completeLoop = false;
  Boolean cannotCreatePath = false;
  String name;
  Color pathColor;
  String pathDirection = "CW";

  pathTracer(Node start, int p, String n, Color c) {
    startNode = start; // initial starting point
    priorityOfPath = p;
    
    name = n;
    pathColor = c;
  }
  
  // Update the current starting point of the path
  void setNewStart(Node start) {
    startNode = start;
  }
  
  void tracePath() {
    // Resets current path and is ready for next
    for(Node node : nodePath) {
       node.isEndOfPath(false);    
       node.unoccupyNode();
    }
    
    // Resets all existing signals to STOP       !!!!!! THIS MAY BE CAUSING ISSUES FOR MULTI TRAIN + ISSUES FOR SIGNAL DISPLAY!!!!!!!!!!
    for(Node node : forwSignalPath) {
       if(node.getSignal() != null) {
         //forwSignalPath.get(i).thisSignal.signalState("STOP");
       }
    }
    
    for(Node node : oppSignalPath) {
       if(node.getSignal() != null) {
         //oppSignalPath.get(i).thisSignal.signalState("STOP");
       }
    }
    
    nodePath.clear(); // clears nodes from previous path for new path
    forwSignalPath.clear(); // clears signals from previous path for new path
    oppSignalPath.clear(); // clears signals behind train
    
    //println("START NODE: " + str(startNode.getOccupiedPriority()));
    //println("THIS PATH: " + str(priorityOfPath));
    if(startNode.isOccupied() && startNode.getOccupiedPriority() > priorityOfPath) {  // !!!!! START NODE IS 0 FOR SOME REASON
      cannotCreatePath = true;
    } else {
      cannotCreatePath = false;
      thisCurrentNode = startNode;
      restrictedAccess = false;
      startNode.occupyNode(priorityOfPath); // <-- maybe? nope lmao
      
      if(pathDirection.equals("CW")) {
        thisFollowingNode = startNode.nextNode;
      } else {
        thisFollowingNode = startNode.previousNode;
      }
      
      // Creates path using linked list            !!!!!! FIGURE OUT SYSTEM FOR IF OCCUPIED FOR MULTIPLE TRAINS
      while(thisFollowingNode != null && thisFollowingNode != startNode && !restrictedAccess) {
        //println(name + " -> Occ Node Priority: " + str(thisFollowingNode.getOccupiedPriority()) + " This Path Priority: " + str(priorityOfPath));
        //println("Direction: " + pathDirection);
        if(thisFollowingNode.isOccupied() && thisFollowingNode.getOccupiedPriority() > priorityOfPath) {
          restrictedAccess = true;
        } else {
          nodePath.add(thisCurrentNode);
          thisCurrentNode.occupyNode(priorityOfPath);
          if(thisCurrentNode.thisSignal != null && thisCurrentNode.thisSignal.signalFlow.equals(pathDirection)) {
            forwSignalPath.add(thisCurrentNode);
          }
          thisCurrentNode = thisFollowingNode;
          if(pathDirection.equals("CW")) {
            thisFollowingNode = thisCurrentNode.nextNode;
          } else {
            thisFollowingNode = thisCurrentNode.previousNode;
          }
        }
      }
      
      if(thisFollowingNode == startNode) {
        nodePath.add(thisCurrentNode);
        if(thisCurrentNode.thisSignal != null && thisCurrentNode.thisSignal.signalFlow.equals(pathDirection)) {
          oppSignalPath.add(thisCurrentNode);
        }
        thisCurrentNode = thisFollowingNode;
        if(pathDirection.equals("CW")) {
          thisFollowingNode = thisCurrentNode.nextNode;
        } else {
          thisFollowingNode = thisCurrentNode.previousNode;
        }
        completeLoop = true;
        
        for(int i = 0; i < forwSignalPath.size(); i++) {
          forwSignalPath.get(i).thisSignal.signalState("CLEAR");
        }
      }
      
      // Hit dead end
      if(thisFollowingNode == null || restrictedAccess) {
        nodePath.add(thisCurrentNode);
        if(thisCurrentNode.thisSignal != null && thisCurrentNode.thisSignal.signalFlow.equals(pathDirection)) {
          forwSignalPath.add(thisCurrentNode);
        }
        thisCurrentNode.isEndOfPath(true);
        completeLoop = false;
        
        // Set signals ahead of starting node for trains to prepare to stop at a possible endpoint
        if(forwSignalPath.size() > 0)
          forwSignalPath.get(forwSignalPath.size() - 1).thisSignal.signalState("STOP");
        if(forwSignalPath.size() > 1)
          forwSignalPath.get(forwSignalPath.size() - 2).thisSignal.signalState("APPROACH");
        if(forwSignalPath.size() > 2)
          for(int i = forwSignalPath.size() - 3; i >= 0; i--) {
            forwSignalPath.get(i).thisSignal.signalState("CLEAR");
          }       
      }
      setSignalsBehind();
    }
  }
  
  
  // Set signals behind of starting nodes for other trains behind occupied block
  void setSignalsBehind() {
    String oppDirection;
    
    thisCurrentNode = startNode;
    if(pathDirection.equals("CW")) {
      oppDirection = "CCW";
      thisFollowingNode = startNode.previousNode;
    } else {
      oppDirection = "CW";
      thisFollowingNode = startNode.nextNode;
    }

    while(thisFollowingNode != null && thisFollowingNode != startNode) {
      if(thisCurrentNode.thisSignal != null && thisCurrentNode.thisSignal.signalFlow.equals(pathDirection)) {
        oppSignalPath.add(thisCurrentNode);
      }
      thisCurrentNode = thisFollowingNode;
      if(oppDirection.equals("CW")) {
        thisFollowingNode = thisCurrentNode.nextNode;
      } else {
        thisFollowingNode = thisCurrentNode.previousNode;
      }
    }
    
    if(thisFollowingNode == startNode) {
      if(thisCurrentNode.thisSignal != null && thisCurrentNode.thisSignal.signalFlow.equals(pathDirection)  ) {
        oppSignalPath.add(thisCurrentNode);
      }
      thisCurrentNode = thisFollowingNode;
      if(oppDirection.equals("CW")) {
        thisFollowingNode = thisCurrentNode.nextNode;
      } else {
        thisFollowingNode = thisCurrentNode.previousNode;
      }
    }
    
    if(oppSignalPath.size() > 0)
      oppSignalPath.get(0).thisSignal.signalState("STOP");
    if(oppSignalPath.size() > 1)
      oppSignalPath.get(1).thisSignal.signalState("APPROACH");
  }
  
  // Complete possible path for train to take (renders first)
  void renderPath() {
    for(int i = 0; i < nodePath.size(); i++) {
      nodePath.get(i).update();
      nodePath.get(i).renderConnections(pathColor, pathDirection);
    }
  }
  
  // Second render of only the block portion of the path, that is the current spot of the train relative to signals (as these are the places where IR sensors can detect where a train is)
  void renderBlock() {
    Boolean findBlockEnd = true;
    blockPath.clear();
    
    for(int i = 0; i < nodePath.size(); i++) {
      if(findBlockEnd && nodePath.get(i).thisSignal != null && nodePath.get(i) != startNode) {
        blockPath.add(nodePath.get(i)); // This last entry is the ending signal of the current block

import java.awt.Color;

findBlockEnd = false;
      } else if(findBlockEnd) {
        blockPath.add(nodePath.get(i));
      }
    }
    
    for(int i = 0; i < blockPath.size() - 1; i++) {
      blockPath.get(i).update();
      blockPath.get(i).renderConnections(color(0,255,0), pathDirection);
    }
  }
  
  String isLoop() {
    if(completeLoop) {
      fill(0,255,0);
      return "LOOP";
    } else {
      fill(255,0,0);
      return "NO LOOP";
    }
  }
  
  Boolean cannotCreate() {
    return cannotCreatePath;
  }
  
  void setPathDirection(String dir) {
    pathDirection = dir;
  }
}
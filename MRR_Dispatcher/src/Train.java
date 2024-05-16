import java.awt.Color;

import javax.swing.JPanel;

class Train {
  String name;
  String type;
  String direction;
  int numEquipment;
  int priority; // ranked on scale from 1 (lowest) to 10 (highest)
  private JPanel panel;
  
  pathTracer path;
  
  Train(String n, String t, int num, int p, String d, Node start, JPanel panel) {
	this.name = n;
    this.type = t;
    this.numEquipment = num;
    this.priority = p;
    this.direction = d;
    this.panel = panel;
    
    path = new pathTracer(start, priority, name, Color.BLUE, panel);
    path.setPathDirection(direction);
  }
  
  void render() {
    path.renderPath();
    path.renderBlock();
  }
 
  void update() {
    path.tracePath();
  }
  
  void updatePosition(Node start) {
    path.setNewStart(start);
    path.tracePath();
  }
  
  void updateDirection(String d) {
    direction = d;
    path.setPathDirection(direction);
    path.tracePath();
  }
  
  String getName() {
    return name;
  }
  
  String getType() {
    if(type.equals("F")) {
      return "F";
    } else if(type.equals("P")){
      return "P";
    } else {
      return type;
    }
  }
  
  String getDirection() {
    if(direction.equals("CW")) {
      return "CW";
    } else {
      return "CCW";
    }
  }
  
  void prepareForRemoval() {
	  path.resetPath();
  }
}
class Train {
  String name;
  String type;
  String direction;
  int numEquipment;
  int priority; // ranked on scale from 1 (lowest) to 10 (highest)
  
  pathTracer path;
  
  Train(String n, String t, int num, int p, String d, Node start) {
    name = n;
    type = t;
    numEquipment = num;
    priority = p;
    direction = d;
    
    path = new pathTracer(start, priority, name, color(random(0,255),random(0,255),random(0,255)));
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
      fill(245, 224, 39);
      return "F";
    } else if(type.equals("P")){
      fill(57, 73, 247);
      return "P";
    } else {
      fill(255, 70, 69);
      return type;
    }
  }
  
  String getDirection() {
    textSize(18);
    if(direction.equals("CW")) {
      fill(0,200,255);
      return "CW";
    } else {
      fill(0,100,255);
      return "CCW";
    }
  }
}
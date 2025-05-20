// DOES NOT NEED CHANGING

#include "Switch.h"

Switch::Switch(String sn, int sp, int sa, bool ao) {
  switchName = sn;
  
  switchPin = sp;
  servoAngle = sa;
  angleOrder = ao;
}

void Switch::initialize() {
  pinMode(switchPin, OUTPUT);

  switchState = "OPEN";
}

void Switch::writeSwitch() {

  servo.attach(switchPin);
  if(angleOrder) {
    if(switchState.equals("OPEN")) {
      servo.write(0);
    }
    if(switchState.equals("THROWN")) {
      servo.write(servoAngle);
    }
  } else {
    if(switchState.equals("OPEN")) {
      servo.write(servoAngle);
    }
    if(switchState.equals("THROWN")) {
      servo.write(0);
    }
  }  
  delay(500);
  servo.detach();
}

void Switch::setSwitchState(String ss) {
  switchState = ss;
}

String Switch::getName() {
  return switchName;
}

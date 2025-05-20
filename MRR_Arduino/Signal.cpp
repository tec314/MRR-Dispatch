// DOES NOT NEED CHANGING

#include "Signal.h"

Signal::Signal(String sn, String st, int trp, int typ, int tgp, int byp, int bgp, bool usr, int dp, int lp, int cp) {
  signalName = sn;
  signalType = st;
  
  topRedPin = trp;
  topYellowPin = typ;
  topGreenPin = tgp;

  bottomYellowPin = byp;
  bottomGreenPin = bgp;

  useShiftRegister = usr;

  dataPin = dp;
  latchPin = lp;
  clockPin = cp;
}

void Signal::initialize() {
  signalState = "STOP";
  leds = 0;

  // With shift register (initialize data, latch, clock pins)
  if(useShiftRegister) {
    pinMode(dataPin, OUTPUT);
    pinMode(latchPin, OUTPUT);  
    pinMode(clockPin, OUTPUT);
  // Without shift register (initialize direct pin connections)
  } else {
    pinMode(topRedPin, OUTPUT);
    pinMode(topYellowPin, OUTPUT);
    pinMode(topGreenPin, OUTPUT);
  
    if(signalType.equals("DOUBLE_HEAD")) {
      pinMode(bottomYellowPin, OUTPUT);
      pinMode(bottomGreenPin, OUTPUT);
    }
  }
}

void Signal::signalLightUp() {
  // With shift register (use data, latch, clock pins)
  if(useShiftRegister) {
    if(signalState.equals("STOP")) {
      this->updateSequence(topRedPin);
    } 
    else if(signalState.equals("APPROACH")) {
      this->updateSequence(topYellowPin);
    }
    else if(signalState.equals("CLEAR")) {
      this->updateSequence(topGreenPin);
    }
  
    else if(signalState.equals("SLOW-APPR")) {
      this->updateSequence(topRedPin);
      this->updateSequence(bottomYellowPin);
    }
    else if(signalState.equals("MED-CLEAR")) {
      this->updateSequence(topRedPin);
      this->updateSequence(bottomGreenPin);
    }
  // Without shift register (use direct pin connections)
  } else {
    if(signalState.equals("STOP")) {
      digitalWrite(topRedPin, HIGH);
      delay(2);
      digitalWrite(topRedPin, LOW);
    } 
    else if(signalState.equals("APPROACH")) {
      digitalWrite(topYellowPin, HIGH);
      delay(2);
      digitalWrite(topYellowPin, LOW);
    }
    else if(signalState.equals("CLEAR")) {
      digitalWrite(topGreenPin, HIGH);
      delay(2);
      digitalWrite(topGreenPin, LOW);
    }
  
    else if(signalState.equals("SLOW-APPR")) {
      digitalWrite(topRedPin, HIGH);
      digitalWrite(bottomYellowPin, HIGH);
      delay(2);
      digitalWrite(topRedPin, LOW);
      digitalWrite(bottomYellowPin, LOW);
    }
    else if(signalState.equals("MED-CLEAR")) {
      digitalWrite(topRedPin, HIGH);
      digitalWrite(bottomGreenPin, HIGH);
      delay(2);
      digitalWrite(topRedPin, LOW);
      digitalWrite(bottomGreenPin, LOW);
    }  
  }
}

void Signal::setSignalState(String ss) {
  signalState = ss;
}

String Signal::getName() {
  return signalName;
}

void Signal::updateSequence(int aspect) { // Aspect is the int representation of what pair of LEDs to turn on
  bitSet(leds, aspect);
  this->updateShiftRegister();
  delay(2);
  bitClear(leds, aspect);
  this->updateShiftRegister();
}

void Signal::updateShiftRegister() {
   digitalWrite(latchPin, LOW);
   shiftOut(dataPin, clockPin, LSBFIRST, leds);
   digitalWrite(latchPin, HIGH);
}

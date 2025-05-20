// DOES NOT NEED CHANGING

#ifndef Signal_h
#define Signal_h

#include <Arduino.h>

class Signal {
  public:
    Signal(String sn, String st, int trp, int typ, int tgp, int byp, int bgp, bool usr, int dp, int lp, int cp);   
    void initialize();
    void signalLightUp();
    void setSignalState(String ss);
    String getName();
    void updateSequence(int aspect);
    void updateShiftRegister();
  private:
    String signalName;
    String signalState;
    String signalType;
    byte leds;
    bool useShiftRegister;
    int topRedPin, topYellowPin, topGreenPin, bottomYellowPin, bottomGreenPin;
    int dataPin, latchPin, clockPin;
};

#endif

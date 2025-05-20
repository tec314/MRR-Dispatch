// DOES NOT NEED CHANGING

#ifndef Switch_h
#define Switch_h

#include <Arduino.h>
#include <Servo.h>

class Switch {
  public:
    Switch(String sn, int sp, int sa, bool ao);   
    void initialize();
    void writeSwitch();
    void setSwitchState(String ss);
    String getName();
  private:
    String switchName;
    String switchState;
    int switchPin;
    int servoAngle;
    bool angleOrder;
    Servo servo;
};

#endif

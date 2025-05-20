#include "Signal.h"
#include "Switch.h"

#include <Servo.h>
#include <ezButton.h>

// ----------------------------------------------------
// Start of code that needs changing

// CHANGE TO MATCH NAME OF FILE (ex: "ARDUINO2")
String arduinoName = "ARDUINOX";

/* 
 * All signal initializations
 * --- FORMAT ---
 * Signal sigXXXZ(sn, st, trp, typ, tgp, byp, bgp, usr, dp, lp, cp);
 * sigXXXZ  : signal name (ex: sig202A)
 * sn       : [String] signal name string (ex: "SIG-202A") 
 * st       : [String] signal head type ("SINGLE_HEAD" or "DOUBLE_HEAD" only)
 * trp      : [int] top head red LED pin        (use NULL value if not using)
 * typ      : [int] top head yellow LED pin     (use NULL value if not using)
 * tgp      : [int] top head green LED pin      (use NULL value if not using)
 * byp      : [int] bottom head yellow LED pin  (use NULL value if not using)
 * bgp      : [int] bottom head green LED pin   (use NULL value if not using)
 * usr      : [bool] using shift registers? (true for yes, false for no)
 * dp       : [int] data pin of shift register  (use NULL value if not using
 * lp       : [int] latch pin of shift register (use NULL value if not using
 * cp       : [int] clock pin of shift register (use NULL value if not using
 */

// Example Template:
Signal sigXXXZ("SIG-XXXZ", "SINGLE_HEAD", 4, 5, 6, 7, 8, false, NULL, NULL, NULL);
// Add more if necessary...

/* 
 * All switch initializations
 * --- FORMAT ---
 * Switch swX(sn, sp, sa, ao);  
 * swX      : switch name (ex: sw2)
 * sn       : [String] switch name string (ex: "SW2") 
 * sp       : [int] pin of switch motor
 * sa       : [int] angle (degree) value servo turns to switch track
 * ao       : [bool] determines when to apply angle to switch
 *            (true for applying angle when switch is set to THROWN)
 *            (false for applying angle when switch is set to OPEN)
 */

// Example Template:
Switch swX("SWX", A1, 45, true); 
// Add more if necessary...

// List of switches assigned to this Arduino (CHANGE TO MATCH TOTAL NUMBER OF SWITCHES INITIALIZED)
#define swArrSize 1 

// (ADD ALL INITIALIZED SWITCHES HERE)
Switch switches[swArrSize] {
  swX                         // sw1, sw2, sw3, etc...
};

// List of signals assigned to this Arduino (CHANGE TO MATCH TOTAL NUMBER OF SIGNALS INITIALIZED)
#define sigArrSize 1

// (ADD ALL INITIALIZED SIGNALS HERE)
Signal signals[sigArrSize] {
  sigXXXZ                     // sig201A, sig201B, sig202A, sig202B, etc...
};

// All code below does not need to be changed
// ----------------------------------------------------

#define serialDelayTime 0

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(50);
  
  delay(100);

  for(int i = 0; i < sigArrSize; i++) {
    signals[i].initialize();
  }

  for(int i = 0; i < swArrSize; i++) {
    switches[i].initialize();
  }

  IRSetup();
}

void loop() {
  recieveMessage();
  IRLoop();

  for(int i = 0; i < sigArrSize; i++) {
    signals[i].signalLightUp();
  }
}

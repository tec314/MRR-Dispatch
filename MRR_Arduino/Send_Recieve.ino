// DOES NOT NEED CHANGING

bool searchForUnderscore = true;
String serialMessage;
String serialAddress;

void recieveMessage() {
  bool gotMatch = false;
  
  if(Serial.available() > 0) {
    char serialChar = Serial.read();
    
    // Grabs the address of the message and checks if it matches any switch assigned to this Arduino
    if(serialChar == '_') {
      searchForUnderscore = false;  
    }
    
    if(searchForUnderscore) {
      serialAddress += serialChar;  
    }
    
    if(!searchForUnderscore && serialChar != '_' && serialChar != '.') {
      serialMessage += serialChar;
    }

    if(serialChar == '.') {
      searchForUnderscore = true;

      for(int i = 0; i < swArrSize; i++) {
        if(serialAddress.equals(switches[i].getName())) {
          switches[i].setSwitchState(serialMessage);
          gotMatch = true;
          switches[i].writeSwitch();
        }
      }

      for(int i = 0; i < sigArrSize; i++) {
        if(serialAddress.equals(signals[i].getName())) {
          signals[i].setSignalState(serialMessage);
          gotMatch = true;
        }
      }

      // For test pings sent by dispatch
      if(serialAddress.equals("TEST") && serialMessage.equals("GETNAME")) {
        Serial.print("PING_" + arduinoName + ".");
        delay(1000); // Add a little delay to space out data written on Serial
        // No gotMatch flag flip (this message is for all Arduino's)
      }

      // If no addresses matches, pass it on
      if(!gotMatch) { 
        Serial.print(serialAddress + "_" + serialMessage + ".");
        delay(serialDelayTime);
      }
    
      serialMessage = "";
      serialAddress = "";
    } 

  } 
}

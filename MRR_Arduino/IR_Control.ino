// Add/Edit all IR sensing pins here (change code to reflect adjustment made):

// !!! WILL ADJUST CODE LATER TO BE INCLUDED IN SIGNAL INITIALIZATIONS !!!

#define IRPin1 A6
#define IRPin2 A7
#define IRPin3 A5
#define IRPin4 A4
// etc...

int SIG_203A_STATUS;
int SIG_203B_STATUS;
int SIG_202A_STATUS;
int SIG_202B_STATUS;
// etc...

unsigned long currentMillis;

unsigned long IR1_Millis = -3000;
unsigned long IR2_Millis = -3000;
unsigned long IR3_Millis = -3000;
unsigned long IR4_Millis = -3000;
// etc...

void IRSetup() {
  pinMode(IRPin1, INPUT);
  pinMode(IRPin2, INPUT);
  pinMode(IRPin3, INPUT);
  pinMode(IRPin4, INPUT);
  // etc...
}

void IRLoop() {
  currentMillis = millis();
  
  SIG_203A_STATUS = analogRead(IRPin1); 
  SIG_203B_STATUS = analogRead(IRPin2); 
  SIG_202A_STATUS = analogRead(IRPin3); 
  SIG_202B_STATUS = analogRead(IRPin4); 
  // etc...

  checkIRTrip();
  /*
  Serial.print("IR_Counter: ");
  Serial.println(IR_Counter);
  Serial.print("lightsCounter: ");
  Serial.println(lightsCounter);
  Serial.print("waitForIR1 ");
  Serial.println(waitForIR1);
  Serial.print("waitForIR2 ");
  Serial.println(waitForIR2);
  Serial.print("currentMillis: ");
  Serial.println(currentMillis);
  */
}

void checkIRTrip() {
  if(SIG_203A_STATUS < 500 && currentMillis - IR1_Millis >= 3000) {
    Serial.print("DISPATCH_SIG-203A.");
    IR1_Millis = currentMillis;
    //delay(300);
  }
  if(SIG_203B_STATUS < 500 && currentMillis - IR2_Millis >= 3000) {
    Serial.print("DISPATCH_SIG-203B.");
    IR2_Millis = currentMillis;
    //delay(300);
  }
  if(SIG_202A_STATUS < 500 && currentMillis - IR3_Millis >= 3000) {
    Serial.print("DISPATCH_SIG-202A.");
    IR3_Millis = currentMillis;
    //delay(300);
  }
  if(SIG_202B_STATUS < 500 && currentMillis - IR4_Millis >= 3000) {
    //Serial.print("DISPATCH_SIG-202B.");
    //IR4_Millis = currentMillis;
    //delay(300);
  }
  // etc...
}

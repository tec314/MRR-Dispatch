import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.InputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SerialComm {
    private SerialPort sp;
    
    private String readString = "";
    private String serialAddress = "";
    private String serialMessage = "";
    
    public static ArrayList<String> debugArray = new ArrayList<String>();

    SerialComm(String portName) {
    	// https://stackoverflow.com/questions/69704119/jserialcomm-dynamically-detect-available-serial-ports-hotplug-device
    	// look into this?
    	
    	sp = SerialPort.getCommPort(portName);
    	sp.setComPortParameters(9600, 8, 2, 0);
    	sp.setComPortTimeouts(0, 0, 0);
        if(!sp.openPort()) {
        	
        	System.out.println("COM port NOT available!"); 
        	return;
        }
        
        // Listener for reading any incoming data on SerialPort
        sp.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                //System.out.println("GOT SOMETHING TO READ!");
                byte[] newData = new byte[sp.bytesAvailable()];
                sp.readBytes(newData, newData.length);
                for(byte b : newData) {
                	char c = (char) b;
                	if(c == '_') {
                		serialAddress = readString;
                		readString = "";
                	}
                	else if(c == '.') {
                		serialMessage = readString;
                		// Get the current time
                        LocalDateTime now = LocalDateTime.now();
                        // Define the format for the timestamp
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                        // Format the current time
                        String timestamp = now.format(formatter);
                        debugArray.add(0, "[" + timestamp + "]" + " IN" + " msg: " + serialAddress + "_" + serialMessage);
                        if (debugArray.size() > 6) {
                            debugArray.remove(6);
                        }
                		readString = "";
                		
                		// Ping test table filling
                		if(serialAddress.equals("PING")) {
                        	Object[] columnData = new Object[3];
                        	columnData[0] = serialMessage;
                        	columnData[1] = "XX";
                        	columnData[2] = "SUCCESS";
                        	MRRDispatchFrame.model.addRow(columnData);
                        }              		
                	}
                	else {
                		readString += c;	
                	}
                }

                System.out.println(serialAddress);
                System.out.println(serialMessage);
                
                // CHECK TRAINS FOR POSITION UPDATE
                for(Train train : MRRDispatchFrame.trainArray) {
                	if(serialMessage.equals(train.getGeneratedPath().getEndOfBlock().getSignal().getName())) {
                		train.updatePosition(train.getGeneratedPath().getEndOfBlock());
                	}
                }
                
                serialAddress = "";
        		serialMessage = "";
            }
        });
        
    }

    // Writer function for sending data on SerialPort
    public void writeData(String data) {
        try {
        	for(char c : data.toCharArray()) {
        		sp.getOutputStream().write(c);
        	}
        	sp.getOutputStream().write('.');
        	
        	// Get the current time
            LocalDateTime now = LocalDateTime.now();
            // Define the format for the timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            // Format the current time
            String timestamp = now.format(formatter);
        	debugArray.add(0, "[" + timestamp + "]" + "OUT" + "msg:   " + data);
            if(debugArray.size() > 6) {
            	debugArray.remove(6);
            }
            
            try {
				Thread.sleep(200); // Delay to allow packets to propagate through Arduino network
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "ERROR\nInterruped!", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "ERROR\nNo port detected.", "Error", JOptionPane.ERROR_MESSAGE);
		}
    }
    
    public ArrayList<String> getDebugArray() {
    	return debugArray;
    }
}

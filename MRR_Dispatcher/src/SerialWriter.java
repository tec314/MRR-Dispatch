import java.io.IOException;
import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.InputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SerialWriter {
    private SerialPort sp;
    
    private String readString;
    
    public static ArrayList<String> debugArray = new ArrayList<String>();

    SerialWriter(String portName) {
    	// https://stackoverflow.com/questions/69704119/jserialcomm-dynamically-detect-available-serial-ports-hotplug-device
    	// look into this?
    	
    	sp = SerialPort.getCommPort(portName);
    	sp.setComPortParameters(9600, 8, 2, 0);
    	sp.setComPortTimeouts(0, 0, 0);
        if(!sp.openPort()) {
        	
        	System.out.println("COM port NOT available!"); 
        	return;
        }
        
        sp.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[sp.bytesAvailable()];
                int numRead = sp.readBytes(newData, newData.length);
                for(byte b : newData) {
                	char c = (char) b;
                	if(c == '.') {
                		// Get the current time
                        LocalDateTime now = LocalDateTime.now();
                        // Define the format for the timestamp
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                        // Format the current time
                        String timestamp = now.format(formatter);
                        debugArray.add(0, "[" + timestamp + "]" + " IN" + " msg: " + readString);
                        if (debugArray.size() > 6) {
                            debugArray.remove(6);
                        }
                		readString = "";
                	}
                	else {
                		readString += c;	
                	}
                }
                
            }
        });
        
    }

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
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*
    public void startReading() {
        new Thread(() -> {
            try {
                byte[] readBuffer = new byte[1024];
                while (true) {
                    int numBytes = sp.getInputStream().read(readBuffer);
                    if (numBytes > 0) {
                        String receivedData = new String(readBuffer, 0, numBytes);
                        
                        // Get the current time
                        LocalDateTime now = LocalDateTime.now();
                        // Define the format for the timestamp
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                        // Format the current time
                        String timestamp = now.format(formatter);
                        
                        synchronized (debugArray) {
                            debugArray.add(0, "[" + timestamp + "]" + " IN" + " msg: " + receivedData);
                            if (debugArray.size() > 6) {
                                debugArray.remove(6);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    */
    
    public ArrayList<String> getDebugArray() {
    	return debugArray;
    }
}

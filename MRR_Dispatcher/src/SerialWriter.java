import java.io.IOException;
import java.util.ArrayList;

import com.fazecast.jSerialComm.SerialPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SerialWriter {
    private SerialPort sp;
    
    public static ArrayList<String> debugArray = new ArrayList<String>();

    SerialWriter(String portName) {
    	// https://stackoverflow.com/questions/69704119/jserialcomm-dynamically-detect-available-serial-ports-hotplug-device
    	// look into this?
    	
    	sp = SerialPort.getCommPort(portName);
    	sp.setComPortParameters(9600, 8, 1, 0);
    	sp.setComPortTimeouts(0, 0, 0);
        if(!sp.openPort()) {
        	
        	System.out.println("COM port NOT available!"); 
        	return;
        }
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
    
    public ArrayList<String> getDebugArray() {
    	return debugArray;
    }
}

import java.io.IOException;

import com.fazecast.jSerialComm.SerialPort;

public class SerialWriter {
    private SerialPort sp;

    SerialWriter(String portName) {
    	sp = SerialPort.getCommPort(portName);
    	sp.setComPortParameters(9600, 8, 1, 0);
    	sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

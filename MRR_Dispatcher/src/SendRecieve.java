import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SendRecieve extends JFrame {
    private static final long serialVersionUID = 1L;

    private static String message = "";
    private static int serialDelayTime = 300;
    private static Node detectedSignal;
    
    public static ArrayList<String> debugTerminal = new ArrayList<String>();

    // Constructor
    public SendRecieve() {

    }

    // Simulating send message function
    public static void sendMessage(String m) {
    	// Message sent through serial
        System.out.println("SENDING MESSAGE: " + m);
        
        
        // Get the current time
        LocalDateTime now = LocalDateTime.now();
        // Define the format for the timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        // Format the current time
        String timestamp = now.format(formatter);
        debugTerminal.add(0, "[" + timestamp + "]" + "OUT" + "msg:   " + m);
        if(debugTerminal.size() > 6) {
        	debugTerminal.remove(6);
        }
        // Simulate delay
        try {
            Thread.sleep(serialDelayTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Simulating receive message function
    public static void receiveMessage(ArrayList<Node> nodeArray, ArrayList<Train> trainArray) {
        // Simulate receiving messages
        // In real scenario, messages will be received from some source
        message = "MASTER-SIGNALNAME_Trip.";

        if (message != null) {
            System.out.println("GOT MESSAGE: " + message);
            String temp;
            // Simulating nodeArray
            for (Node node : nodeArray) {
                if (node.getSignal().getName() != null) {
                    temp = "MASTER-" + node.getSignal().getName() + "_Trip.";
                    System.out.println("TEMP: " + temp);
                    // If a match is found, set that as a marker
                    if (temp.equals(message.trim())) {
                        System.out.println("Got Signal!!!");
                        detectedSignal = node;
                    }
                }
            }

            // Simulating trainArray
            for (Train train : trainArray) {
                System.out.println("UPDATED " + train.getName());
                train.updatePosition(detectedSignal);
            }
        }
    }
}

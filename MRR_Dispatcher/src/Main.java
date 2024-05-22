import java.util.concurrent.TimeUnit;

import javax.swing.*;

public class Main {
    public static void main(String[] args)
    {
    	/*
    	TitleScreenFrame titleFrame = new TitleScreenFrame();
    	titleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	titleFrame.setSize(1920,1080);
    	titleFrame.setVisible(true);
    	
        try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        titleFrame.setVisible(false);
        */
        MRRDispatchFrame applicationFrame = new MRRDispatchFrame();
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        applicationFrame.setSize(1920,1080);
        applicationFrame.setVisible(true);
    }
}

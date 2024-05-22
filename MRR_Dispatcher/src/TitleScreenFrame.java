import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TitleScreenFrame extends JFrame {
    public TitleScreenFrame() {
    	super("MRR Dispatcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel to hold the components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.BLACK);
        // Create a label for the title
        JLabel titleLabel = new JLabel("MRR Dispatching", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);

        // Create a button to proceed to the main frame
        JButton startButton = new JButton("Start");
        //startButton.setFont(new Font("Serif", Font.BOLD, 24));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the title screen and open the main frame
                dispose();
                MRRDispatchFrame mainFrame = new MRRDispatchFrame();
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(1920, 1080);
                mainFrame.setVisible(true);
            }
        });
        panel.add(startButton, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
    }
}

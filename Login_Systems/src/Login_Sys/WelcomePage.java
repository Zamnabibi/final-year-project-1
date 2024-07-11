package Login_Sys;

import javax.swing.*;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel animationLabel;
    private Timer animationTimer;
    private int xCoordinate = 0;
    private int direction = 1;

    public WelcomePage() {
        setTitle("Welcome Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        setBounds(100, 100, 1000, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Creating a panel to hold the animation
        JPanel contentPane = new JPanel();

        // Creating a label for animation
        animationLabel = new JLabel();
        animationLabel.setBounds(0, 0, 710, 360);
        ImageIcon animationIcon = new ImageIcon(getClass().getResource("/animation.gif")); // Replace with your animation gif path
        contentPane.setLayout(null);
        animationLabel.setIcon(animationIcon);
        contentPane.add(animationLabel);

        // Timer for animation movement
        animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateImage();
            }
        });
        animationTimer.start();

        setContentPane(contentPane);
        
        JButton btnNewButton = new JButton("Sign up");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new SignUpForm().setVisible(true);
        	}
        });
        btnNewButton.setBounds(798, 54, 89, 23);
        contentPane.add(btnNewButton);
        setVisible(true);
        JButton btnNewButton_1 = new JButton("Sign in");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		new Login_S().setVisible(true);
        	}
        });
        btnNewButton_1.setBounds(798, 117, 89, 23);
        contentPane.add(btnNewButton_1);
        setVisible(true);
    }

    // Method to animate the image
    private void animateImage() {
        xCoordinate += direction;
        animationLabel.setLocation(xCoordinate, 0);

        // Reverse direction when image reaches the edge of the frame
        if (xCoordinate >= getWidth() - animationLabel.getWidth() || xCoordinate <= 0) {
            direction *= -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomePage();
            }
        });
    }
}

package Login_Sys;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpForm extends JFrame {
    private static final long serialVersionUID = 1L;

    public SignUpForm() {
        setTitle("Two Buttons UI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel();
        panel.setLayout(null);
        getContentPane().add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        JLabel label = new JLabel("Choose an option:");
        label.setBounds(10, 20, 150, 25);
        panel.add(label);

        JButton button1 = new JButton("User");
        button1.setBounds(50, 60, 100, 25);
        panel.add(button1);

        JButton button2 = new JButton("Donor");
        button2.setBounds(160, 60, 100, 25);
        panel.add(button2);

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserSignUpUI().setVisible(true);
                //dispose(); // Close the current frame
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpUI().setVisible(true);
                //dispose(); // Close the current frame
            }
        });
    }

    public static void main(String[] args) {
        new SignUpForm();
    }
}

package Login_Sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class SignUpUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField mobileNoField;

    public SignUpUI() {
        setTitle("Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 34));
        titleLabel.setBounds(420, 20, 159, 47);
        contentPane.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setBounds(300, 100, 100, 25);
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBounds(420, 100, 203, 25);
        contentPane.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(300, 150, 100, 20);
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(420, 150, 203, 25);
        contentPane.add(passwordField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setBounds(300, 200, 100, 20);
        contentPane.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(420, 200, 203, 25);
        contentPane.add(emailField);

        JLabel mobileNoLabel = new JLabel("Mobile No:");
        mobileNoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mobileNoLabel.setBounds(300, 250, 100, 20);
        contentPane.add(mobileNoLabel);

        mobileNoField = new JTextField();
        mobileNoField.setFont(new Font("Arial", Font.PLAIN, 14));
        mobileNoField.setBounds(420, 250, 203, 25);
        contentPane.add(mobileNoField);

        JButton signUpButton = new JButton("Sign Up");
        Image img = new ImageIcon(this.getClass().getResource("/OK-icon.png")).getImage();
        signUpButton.setIcon(new ImageIcon(img));
        signUpButton.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpButton.setBounds(320, 300, 106, 30);
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String email = emailField.getText();
                String mobileNo = mobileNoField.getText();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || mobileNo.isEmpty()) {
                    JOptionPane.showMessageDialog(SignUpUI.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Replace with actual sign up logic (e.g., database insertion)
                    JOptionPane.showMessageDialog(SignUpUI.this, "Sign Up Successful!");
                    usernameField.setText("");
                    passwordField.setText("");
                    emailField.setText("");
                    mobileNoField.setText("");
                    // Optionally, open a new window or switch to another view
                    new home().setVisible(true);
                }
            }
        });
        contentPane.add(signUpButton);

        JButton resetButton = new JButton("Reset");
        Image img3 = new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
        resetButton.setIcon(new ImageIcon(img3));
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        resetButton.setBounds(440, 300, 100, 30);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
                emailField.setText("");
                mobileNoField.setText("");
            }
        });
        contentPane.add(resetButton);

        setContentPane(contentPane);

        JLabel backgroundLabel = new JLabel("");
        Image img16 = new ImageIcon(this.getClass().getResource("/animation.jpg")).getImage();
        backgroundLabel.setIcon(new ImageIcon(img16));
        backgroundLabel.setBounds(-130, 0, 1114, 372);
        contentPane.add(backgroundLabel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SignUpUI();
            }
        });
    }
}

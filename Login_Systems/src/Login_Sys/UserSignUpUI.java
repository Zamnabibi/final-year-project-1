package Login_Sys;

import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserSignUpUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField mobileNoField;
    private JTextField addressField;

    public UserSignUpUI() {
        setTitle("User Sign Up");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500); // Adjusted size to fit all components
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 34));
        titleLabel.setBounds(320, 20, 159, 47); // Adjusted position
        contentPane.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setBounds(79, 100, 100, 25); // Adjusted position
        contentPane.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setBounds(300, 100, 203, 25); // Adjusted position
        contentPane.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(79, 152, 100, 20); // Adjusted position
        contentPane.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBounds(300, 150, 203, 25); // Adjusted position
        contentPane.add(passwordField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setBounds(79, 202, 100, 20); // Adjusted position
        contentPane.add(emailLabel);

        emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBounds(300, 200, 203, 25); // Adjusted position
        contentPane.add(emailField);

        JLabel mobileNoLabel = new JLabel("Mobile No:");
        mobileNoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mobileNoLabel.setBounds(79, 252, 100, 20); // Adjusted position
        contentPane.add(mobileNoLabel);

        mobileNoField = new JTextField();
        mobileNoField.setFont(new Font("Arial", Font.PLAIN, 14));
        mobileNoField.setBounds(300, 250, 203, 25); // Adjusted position
        contentPane.add(mobileNoField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 14));
        addressLabel.setBounds(79, 302, 100, 20); // Adjusted position
        contentPane.add(addressLabel);

        addressField = new JTextField();
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        addressField.setBounds(300, 300, 203, 25); // Adjusted position
        contentPane.add(addressField);

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBounds(320, 350, 100, 30); // Adjusted position
        contentPane.add(submitButton);
        
        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(0, 0, 784, 461);
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel.setIcon(new ImageIcon(img4));
        contentPane.add(lblNewLabel);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String email = emailField.getText();
                String mobileNo = mobileNoField.getText();
                String address = addressField.getText();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || mobileNo.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(UserSignUpUI.this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Replace with actual sign up logic (e.g., database insertion)
                    JOptionPane.showMessageDialog(UserSignUpUI.this, "Sign Up Successful!\nUsername: " + username + "\nEmail: " + email);
                    usernameField.setText("");
                    passwordField.setText("");
                    emailField.setText("");
                    mobileNoField.setText("");
                    addressField.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UserSignUpUI().setVisible(true);
            }
        });
    }
}

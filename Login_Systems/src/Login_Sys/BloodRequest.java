package Login_Sys;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

@SuppressWarnings("serial")
public class BloodRequest extends JFrame {
    private JTextField usernameField, emailField, fullNameField;
    private JPasswordField passwordField;
    private JButton submitButton;
    private JLabel timeLabel;
    private JLabel userTypeLabel;

    public BloodRequest() {
        // Frame settings
        setTitle("Blood Request");
        setSize(846, 593);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Add time label
        timeLabel = new JLabel();
        timeLabel.setBounds(640, 10, 184, 20);
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        getContentPane().add(timeLabel);

        // Set the timer to update the JLabel every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        // Initial time update
        updateTime();

        // Form fields
        JLabel label = new JLabel("Username:");
        label.setBounds(29, 61, 278, 73);
        getContentPane().add(label);
        usernameField = new JTextField();
        usernameField.setBounds(307, 61, 307, 73);
        getContentPane().add(usernameField);

        JLabel label_1 = new JLabel("Password:");
        label_1.setBounds(29, 133, 278, 73);
        getContentPane().add(label_1);
        passwordField = new JPasswordField();
        passwordField.setBounds(307, 133, 307, 73);
        getContentPane().add(passwordField);

        JLabel label_2 = new JLabel("Email:");
        label_2.setBounds(29, 205, 278, 73);
        getContentPane().add(label_2);
        emailField = new JTextField();
        emailField.setBounds(307, 205, 307, 73);
        getContentPane().add(emailField);

        JLabel label_3 = new JLabel("Full Name:");
        label_3.setBounds(29, 276, 278, 73);
        getContentPane().add(label_3);
        fullNameField = new JTextField();
        fullNameField.setBounds(307, 276, 307, 73);
        getContentPane().add(fullNameField);


        JLabel label_4 = new JLabel("User Type:");
        label_4.setBounds(29, 349, 278, 73);
        getContentPane().add(label_4);
        
        // User Type Label
        userTypeLabel = new JLabel("Patient");
        userTypeLabel.setBounds(307, 349, 307, 73);
        getContentPane().add(userTypeLabel);

        submitButton = new JButton("Submit");
        submitButton.setBounds(307, 420, 307, 73);
        getContentPane().add(submitButton);

        // Button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
                new UserSignUpUI().setVisible(true);
            }
        });
        
        JLabel lblRequest = new JLabel("Request");
        lblRequest.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblRequest.setBounds(29, 0, 278, 59);
        getContentPane().add(lblRequest);
        JLabel lblNewLabel = new JLabel("Already Account");
        lblNewLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		new UserSignUpUI().setVisible(true);
        	}
        });
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(694, 387, 130, 25);
        getContentPane().add(lblNewLabel);
        
        // Add footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 504, 830, 50); // Adjust size and position as needed
        getContentPane().add(footerPanel);

        // Background Image
        JLabel lblBackground = new JLabel("");
        Image img1 = new ImageIcon(getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img1));
        lblBackground.setBounds(0, 0, 830, 517); // Adjusted bounds
        getContentPane().add(lblBackground);
        
        

        setVisible(true);
    }

    private void handleSubmit() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        String userType = userTypeLabel.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty() || userType.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save to database
        saveToDatabase(username, password, email, fullName, userType);
    }

    private void saveToDatabase(String username, String password, String email, String fullName, String userType) {
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String pass = "zamna0";

        String query = "INSERT INTO Users (UserName, Password, Email, FullName, Type, CreatedAt, UpdatedAt) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, fullName);
            pst.setString(5, userType);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Request submitted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BloodRequest());
    }
}

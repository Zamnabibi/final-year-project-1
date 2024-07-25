package Login_Sys;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class BloodDonationRequest extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JComboBox<String> typeComboBox;
    private JButton sendButton;
    private JButton btnClose;
    private JLabel lblBackground;
    private JLabel lblNewLabel;

    public BloodDonationRequest() {
        setTitle("Blood Donation Request");
        setBounds(100, 100, 850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Change to DISPOSE_ON_CLOSE
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        // Create form elements
        usernameField = new JTextField(20);
        usernameField.setBounds(422, 0, 412, 63);
        passwordField = new JPasswordField(20);
        passwordField.setBounds(422, 73, 412, 63);
        emailField = new JTextField(20);
        emailField.setBounds(422, 146, 412, 63);
        fullNameField = new JTextField(20);
        fullNameField.setBounds(422, 219, 412, 63);
        typeComboBox = new JComboBox<>(new String[]{"Donor", "Patient"});
        typeComboBox.setBounds(422, 292, 412, 63);

        // Create form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        JLabel label = new JLabel("Username:");
        label.setBounds(0, 0, 412, 63);
        formPanel.add(label);
        formPanel.add(usernameField);
        JLabel label_1 = new JLabel("Password:");
        label_1.setBounds(0, 73, 412, 63);
        formPanel.add(label_1);
        formPanel.add(passwordField);
        JLabel label_2 = new JLabel("Email:");
        label_2.setBounds(0, 146, 412, 63);
        formPanel.add(label_2);
        formPanel.add(emailField);
        JLabel label_3 = new JLabel("Full Name:");
        label_3.setBounds(0, 219, 412, 63);
        formPanel.add(label_3);
        formPanel.add(fullNameField);
        JLabel label_4 = new JLabel("Type:");
        label_4.setBounds(0, 292, 412, 63);
        formPanel.add(label_4);
        formPanel.add(typeComboBox);

        // Create background panel
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        lblBackground = new JLabel(new ImageIcon(getClass().getResource("/back.jpg")));
        backgroundPanel.add(lblBackground, BorderLayout.CENTER);
        backgroundPanel.add(formPanel, BorderLayout.CENTER);
        sendButton = new JButton("Send Request");
        btnClose = new JButton("Close");
        
                // Add buttons
                JPanel buttonPanel = new JPanel();
                buttonPanel.setBounds(422, 366, 201, 33);
                formPanel.add(buttonPanel);
                buttonPanel.add(sendButton);
                buttonPanel.add(btnClose);
                
                lblNewLabel = new JLabel("");
                Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
                lblNewLabel.setIcon(new ImageIcon(img4));
                lblNewLabel.setBounds(0, 0, 834, 461);
                formPanel.add(lblNewLabel);
                
                        // Add close button action
                        btnClose.addActionListener(e -> dispose());
                        
                                // Add action listener to the send button
                                sendButton.addActionListener(e -> sendDonationRequest());

        getContentPane().add(backgroundPanel);
    }

    private void sendDonationRequest() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        String type = (String) typeComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save the request to the database
        boolean success = saveRequestToDatabase(username, password, email, fullName, type);

        if (success) {
            JOptionPane.showMessageDialog(this, "Request sent successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            new UserSignUpUI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send request", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean saveRequestToDatabase(String username, String password, String email, String fullName, String type) {
        String url = "jdbc:mysql://localhost:3306/bbms";
        String user = "root";
        String pass = "zamna0";
        String query = "INSERT INTO users (Username, Password, Email, FullName, Type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, fullName);
            stmt.setString(5, type);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BloodDonationRequest().setVisible(true));
    }
}

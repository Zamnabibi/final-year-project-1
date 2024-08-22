package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@SuppressWarnings("serial")
public class EmailFrame extends JFrame {

    private JComboBox<String> donorComboBox;
    private JComboBox<String> patientComboBox;
    private JTextField subjectField;
    private JTextArea messageArea;
    private JTextField senderEmailField;
    private JPasswordField senderPasswordField;
    private JButton sendButton;
    private JButton generatePasswordButton;

    private Connection connection;
    private final String placeholderText = "Enter App-Specific Password";

    public EmailFrame() {
    	 setTitle("Email Sender");
    	    setSize(500, 500);
    	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	    setLocationRelativeTo(null);
    	    getContentPane().setLayout(null); // Use absolute layout
    	    
    	    // Set background color to pink
    	    getContentPane().setBackground(Color.PINK);
        // Establish database connection
        establishConnection();
        
        // Donor and Patient selection
        donorComboBox = new JComboBox<>(fetchUserNames("Donor"));
        patientComboBox = new JComboBox<>(fetchUserNames("Patient"));

        // Sender Email and Password fields
        senderEmailField = new JTextField();
        senderPasswordField = new JPasswordField(placeholderText.length());
        addPlaceholderStyle(senderPasswordField);  // Set initial placeholder style

        // Subject and Message fields
        subjectField = new JTextField();
        messageArea = new JTextArea(5, 20);
        messageArea.setLineWrap(true); // Wrap lines
        messageArea.setWrapStyleWord(true); // Wrap at word boundaries

        // Scroll Pane for JTextArea
        JScrollPane messageScrollPane = new JScrollPane(messageArea);
        messageScrollPane.setBounds(150, 235, 300, 100);

        // Send button
        sendButton = new JButton("Send Email");
        try {
            ImageIcon imagIcon = new ImageIcon(getClass().getResource("/mail.png"));
            sendButton.setIcon(imagIcon);
        } catch (Exception e) {
            System.out.println("Icon not found: " + e.getMessage());
        }
        // Initialize Generate Password Button and set icon
        generatePasswordButton = new JButton("");
        try {
            ImageIcon generateIcon = new ImageIcon(getClass().getResource("/info.png"));
            generatePasswordButton.setIcon(generateIcon);
        } catch (Exception e) {
            System.out.println("Icon not found: " + e.getMessage());
        }

        // Setting bounds for components
        senderEmailField.setBounds(150, 58, 300, 25);
        senderPasswordField.setBounds(150, 94, 300, 25);
        generatePasswordButton.setBounds(451, 94, 23, 25);
        donorComboBox.setBounds(150, 130, 300, 25);
        patientComboBox.setBounds(150, 165, 300, 25);
        subjectField.setBounds(150, 200, 300, 25);
        messageScrollPane.setBounds(150, 235, 300, 100);
        sendButton.setBounds(150, 340, 150, 25);

        // Adding components to the frame
        getContentPane().add(new JLabel("Sender Email:")).setBounds(20, 60, 120, 25);
        getContentPane().add(senderEmailField);
        getContentPane().add(new JLabel("Sender Password:")).setBounds(20, 95, 120, 25);
        getContentPane().add(senderPasswordField);
        getContentPane().add(generatePasswordButton);
        getContentPane().add(new JLabel("Select Donor:")).setBounds(20, 130, 120, 25);
        getContentPane().add(donorComboBox);
        getContentPane().add(new JLabel("Select Patient:")).setBounds(20, 165, 120, 25);
        getContentPane().add(patientComboBox);
        getContentPane().add(new JLabel("Subject:")).setBounds(20, 200, 120, 25);
        getContentPane().add(subjectField);
        getContentPane().add(new JLabel("Message:")).setBounds(20, 235, 120, 25);
        getContentPane().add(messageScrollPane); // Add scroll pane for message area
        getContentPane().add(sendButton);

        // Set up placeholder for the password field
        setupPasswordFieldPlaceholder(senderPasswordField, placeholderText);
        
        JButton btnNewButton = new JButton("Close");
        Image img13 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnNewButton.setIcon(new ImageIcon(img13));
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        	}
        });
        btnNewButton.setBounds(310, 341, 140, 25);
        getContentPane().add(btnNewButton);

        // Action Listener for Generate Password Button
        generatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWebPage("https://myaccount.google.com/apppasswords");
            }
        });

        // Send Button Action Listener
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String senderEmail = senderEmailField.getText();
                String senderPassword = new String(senderPasswordField.getPassword());
                String selectedDonor = donorComboBox.getSelectedItem().toString();
                String selectedPatient = patientComboBox.getSelectedItem().toString();
                String subject = subjectField.getText();
                String message = messageArea.getText();

                // Logic to send email based on selected options
                if (!subject.isEmpty() && !message.isEmpty() && !senderPassword.equals(placeholderText)) {
                    sendEmail(senderEmail, senderPassword, selectedDonor, selectedPatient, subject, message);
                } else {
                    JOptionPane.showMessageDialog(null, "Subject, Message, and Password cannot be empty.");
                }
            }
        });

        // Close connection on frame close
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                closeConnection();
            }
        });
    }

    // Setup placeholder functionality for JPasswordField
    private void setupPasswordFieldPlaceholder(JPasswordField passwordField, String placeholder) {
        passwordField.setText(placeholder);
        passwordField.setEchoChar((char) 0); // Show text instead of masking it

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals(placeholder)) {
                    passwordField.setText("");
                    passwordField.setEchoChar('•'); // Set masking character
                    passwordField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getPassword().length == 0) {
                    passwordField.setText(placeholder);
                    passwordField.setEchoChar((char) 0); // Show text instead of masking it
                    addPlaceholderStyle(passwordField);
                }
            }
        });
    }

    // Add placeholder style (optional, you can customize this)
    private void addPlaceholderStyle(JPasswordField passwordField) {
        passwordField.setForeground(Color.GRAY);
        passwordField.setFont(passwordField.getFont().deriveFont(Font.ITALIC));
    }

    // Establish JDBC connection
    private void establishConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/bloods";
            String user = "root";
            String password = "zamna0";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to the database.");
        }
    }

    // Close JDBC connection
    private void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch user names based on UserType
    private String[] fetchUserNames(String userType) {
        ArrayList<String> userNames = new ArrayList<>();
        String query = "SELECT Name FROM User WHERE UserType = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userType);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userNames.add(resultSet.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userNames.toArray(new String[0]);
    }

    // Method to send email using JavaMail API
    private void sendEmail(String senderEmail, String appSpecificPassword, String donor, String patient, String subject, String messageBody) {
        String donorEmail = fetchUserEmail(donor);
        String patientEmail = fetchUserEmail(patient);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, appSpecificPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(donorEmail + "," + patientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            JOptionPane.showMessageDialog(this, "Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send email.");
        }
    }

    // Fetch user email based on the name
    private String fetchUserEmail(String userName) {
        String email = "";
        String query = "SELECT Email FROM User WHERE Name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("Email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    // Open a web page
    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmailFrame frame = new EmailFrame();
            frame.setVisible(true);
        });
    }
}

package com.bbms.ui;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

@SuppressWarnings("serial")
public class ForgetPasswordFrame extends JFrame {
    private JTextField emailField;
    private JButton submitButton;

    public ForgetPasswordFrame() {
        // Frame setup
        setTitle("Forgot Password");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 30, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 30, 200, 25);
        add(emailField);

        // Submit Button
        submitButton = new JButton("Submit");
        submitButton.setBounds(150, 80, 100, 30);
        add(submitButton);

        // Submit Button Action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleForgotPassword();
            }
        });

        setVisible(true);
    }

    private void handleForgotPassword() {
        String email = emailField.getText().trim(); // Trim to remove any extra spaces

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Debug statement to check email value
        System.out.println("Entered email: " + email);

        // Simulate database interaction
        boolean emailExists = simulateDatabaseCheck(email);

        if (emailExists) {
            String resetToken = generateResetToken();
            sendResetEmail(email, resetToken);
        } else {
            JOptionPane.showMessageDialog(this, "Email address not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean simulateDatabaseCheck(String email) {
        // Simulate checking the database for the email address
        // In a real application, replace this with actual database logic
        String mockEmail = "zamnabibi40@gmail.com"; // Simulate that this email exists in the database

        // Debug statement to check comparison
        System.out.println("Simulated email: " + mockEmail);
        System.out.println("Checking email: " + email);

        return email.equalsIgnoreCase(mockEmail); // Case-insensitive comparison
    }

    private String generateResetToken() {
        // Simple token generation (for demo purposes)
        // In a real application, use a more secure method
        return Long.toHexString(System.currentTimeMillis());
    }

    private void sendResetEmail(String email, String resetToken) {
        // Email server setup
        final String username = "zamnabibi40@gmail.com"; // Your email
        final String password = "knwi ycyn hjzu fuci"; // Your email password

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset Request");
            message.setText("To reset your password, please click the link below:\n\n" +
                            "http://your-website.com/reset-password?token=" + resetToken);

            Transport.send(message);
            JOptionPane.showMessageDialog(this, "Password reset link has been sent to your email.");
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send email.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new ForgetPasswordFrame();
    }
}

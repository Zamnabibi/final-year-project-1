package com.bbms.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@SuppressWarnings("serial")
public class BlodRequestFrame extends JFrame {
    private JTextField patientNameField;
    private JTextField bloodGroupField;
    private JTextField quantityField;
    private JTextField donorEmailField;
    private JButton submitButton;

    public BlodRequestFrame() {
        // Frame setup
        setTitle("Send Blood Request");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Patient Name Field
        JLabel patientNameLabel = new JLabel("Patient Name:");
        patientNameLabel.setBounds(30, 30, 100, 25);
        add(patientNameLabel);

        patientNameField = new JTextField();
        patientNameField.setBounds(150, 30, 200, 25);
        add(patientNameField);

        // Blood Group Field
        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        bloodGroupLabel.setBounds(30, 70, 100, 25);
        add(bloodGroupLabel);

        bloodGroupField = new JTextField();
        bloodGroupField.setBounds(150, 70, 200, 25);
        add(bloodGroupField);

        // Quantity Field
        JLabel quantityLabel = new JLabel("Quantity (Units):");
        quantityLabel.setBounds(30, 110, 120, 25);
        add(quantityLabel);

        quantityField = new JTextField();
        quantityField.setBounds(150, 110, 200, 25);
        add(quantityField);

        // Donor Email Field
        JLabel donorEmailLabel = new JLabel("Donor Email:");
        donorEmailLabel.setBounds(30, 150, 100, 25);
        add(donorEmailLabel);

        donorEmailField = new JTextField();
        donorEmailField.setBounds(150, 150, 200, 25);
        add(donorEmailField);

        // Submit Button
        submitButton = new JButton("Send Request");
        submitButton.setBounds(150, 200, 150, 30);
        add(submitButton);

        // Submit Button Action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendRequest();
            }
        });

        setVisible(true);
    }

    private void handleSendRequest() {
        String patientName = patientNameField.getText().trim();
        String bloodGroup = bloodGroupField.getText().trim();
        String quantity = quantityField.getText().trim();
        String donorEmail = donorEmailField.getText().trim();

        if (patientName.isEmpty() || bloodGroup.isEmpty() || quantity.isEmpty() || donorEmail.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simulate sending request
        boolean emailSent = sendRequestEmail(donorEmail, patientName, bloodGroup, quantity);

        if (emailSent) {
            JOptionPane.showMessageDialog(this, "Request sent successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Failed to send request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean sendRequestEmail(String donorEmail, String patientName, String bloodGroup, String quantity) {
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(donorEmail));
            message.setSubject("Blood Donation Request");
            message.setText("Dear Donor,\n\n" +
                            "A blood donation request has been made by the following patient:\n\n" +
                            "Patient Name: " + patientName + "\n" +
                            "Blood Group: " + bloodGroup + "\n" +
                            "Quantity: " + quantity + " units\n\n" +
                            "Please respond to this request if you are able to donate.\n\n" +
                            "Thank you.");

            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        new BlodRequestFrame();
    }
}

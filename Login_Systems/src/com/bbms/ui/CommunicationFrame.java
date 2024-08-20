package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class CommunicationFrame extends JFrame {

    private JTextArea chatArea;
    private JTextField messageField;
    private JComboBox<String> recipientComboBox;
    private JButton sendButton;

    public CommunicationFrame() {
        setTitle("Donor-Patient Communication");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Donor-Patient Communication"));
        add(headerPanel, BorderLayout.NORTH);

        // Navigation Panel
        JPanel navigationPanel = new JPanel();
        JButton donorViewButton = new JButton("Donor View");
        JButton patientViewButton = new JButton("Patient View");
        navigationPanel.add(donorViewButton);
        navigationPanel.add(patientViewButton);
        add(navigationPanel, BorderLayout.WEST);

        // Main Area
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        recipientComboBox = new JComboBox<>(new String[] {"Select Recipient", "Donor1", "Donor2", "Patient1", "Patient2"});
        messageField = new JTextField(30);
        sendButton = new JButton("Send");

        inputPanel.add(recipientComboBox);
        inputPanel.add(messageField);
        inputPanel.add(sendButton);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.add(new JLabel("Status/Notifications"));
        add(footerPanel, BorderLayout.SOUTH);

        // Action Listeners
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String recipient = (String) recipientComboBox.getSelectedItem();
        String message = messageField.getText();
        if (recipient != null && !message.isEmpty()) {
            chatArea.append("To " + recipient + ": " + message + "\n");
            messageField.setText("");
            // Here you would add code to actually send the message to the selected recipient
        } else {
            JOptionPane.showMessageDialog(this, "Please select a recipient and enter a message.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CommunicationFrame().setVisible(true);
        });
    }
}


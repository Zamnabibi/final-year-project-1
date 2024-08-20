package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class BlooodRequestFrame extends JFrame {

    private JComboBox<String> bloodGroupComboBox;
    private JTextArea requestTextArea;
    private JButton sendRequestButton;
    private JLabel statusLabel;

    public BlooodRequestFrame() {
        setTitle("Send Blood Request");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Send Blood Request"));
        add(headerPanel, BorderLayout.NORTH);

        // Main Area
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));

        JPanel bloodGroupPanel = new JPanel();
        bloodGroupPanel.add(new JLabel("Select Blood Group:"));
        bloodGroupComboBox = new JComboBox<>(loadBloodGroups());
        bloodGroupPanel.add(bloodGroupComboBox);
        mainPanel.add(bloodGroupPanel);

        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new BorderLayout());
        requestTextArea = new JTextArea(10, 40);
        requestTextArea.setLineWrap(true);
        requestTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(requestTextArea);
        requestPanel.add(new JLabel("Request Message:"), BorderLayout.NORTH);
        requestPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(requestPanel);

        sendRequestButton = new JButton("Send Request");
        mainPanel.add(sendRequestButton);

        add(mainPanel, BorderLayout.CENTER);

        // Footer
        statusLabel = new JLabel(" ");
        add(statusLabel, BorderLayout.SOUTH);

        // Action Listeners
        sendRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendRequest();
            }
        });
    }

    private String[] loadBloodGroups() {
        // Dummy implementation, replace with actual database code
        return new String[]{"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
    }

    private void sendRequest() {
        String selectedBloodGroup = (String) bloodGroupComboBox.getSelectedItem();
        String requestMessage = requestTextArea.getText();

        if (selectedBloodGroup != null && !requestMessage.trim().isEmpty()) {
            ArrayList<String> matchingDonors = getMatchingDonors(selectedBloodGroup);

            if (!matchingDonors.isEmpty()) {
                // Simulate sending request to matching donors
                for (String donor : matchingDonors) {
                    // Here you would send the request to each donor
                    System.out.println("Sending request to donor: " + donor);
                }
                statusLabel.setText("Request sent to " + matchingDonors.size() + " donors.");
            } else {
                statusLabel.setText("No donors found with blood group " + selectedBloodGroup);
            }
        } else {
            statusLabel.setText("Please select a blood group and enter a request message.");
        }
    }

    private ArrayList<String> getMatchingDonors(String bloodGroup) {
        ArrayList<String> donors = new ArrayList<>();
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/blood", "root", "zamna0");

            // Retrieve BloodGroupId based on selected blood group
            String bloodGroupIdQuery = "SELECT BloodGroupId FROM BloodGroup WHERE GroupName = ?";
            PreparedStatement bloodGroupStmt = conn.prepareStatement(bloodGroupIdQuery);
            bloodGroupStmt.setString(1, bloodGroup);
            ResultSet bloodGroupRs = bloodGroupStmt.executeQuery();

            if (bloodGroupRs.next()) {
                int bloodGroupId = bloodGroupRs.getInt("BloodGroupId");
                System.out.println("BloodGroupId for " + bloodGroup + ": " + bloodGroupId);

                // Retrieve DonorIds for the matching blood group
                String donorIdQuery = "SELECT DonorId FROM Donor WHERE BloodGroupId = ?";
                PreparedStatement donorStmt = conn.prepareStatement(donorIdQuery);
                donorStmt.setInt(1, bloodGroupId);
                ResultSet donorRs = donorStmt.executeQuery();

                while (donorRs.next()) {
                    int donorId = donorRs.getInt("DonorId");
                    System.out.println("Found DonorId: " + donorId);

                    // Fetch names from the User table
                    String userQuery = "SELECT Name FROM User WHERE UserId = ? AND UserType = 'Donor'";
                    PreparedStatement userStmt = conn.prepareStatement(userQuery);
                    userStmt.setInt(1, donorId);
                    ResultSet userRs = userStmt.executeQuery();

                    if (userRs.next()) {
                        donors.add(userRs.getString("Name"));
                        System.out.println("Added donor: " + userRs.getString("Name"));
                    }
                    userRs.close();
                    userStmt.close();
                }
                donorRs.close();
                donorStmt.close();
            } else {
                System.out.println("No BloodGroupId found for " + bloodGroup);
            }
            bloodGroupRs.close();
            bloodGroupStmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return donors;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BlooodRequestFrame().setVisible(true);
        });
    }
}

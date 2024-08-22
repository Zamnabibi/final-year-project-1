package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class UpdateDonorFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> amountComboBox;
    private JComboBox<String> groupNameComboBox;
    private JTextField contactNoField;
    private JLabel nameLabel;
    private JTextField textField; // BloodUnit field
    
    private JButton updateButton;
    private JButton closeButton;
    
    private Map<String, String> bloodGroupMap = new HashMap<>();
    private Map<String, String> groupNameMap = new HashMap<>();
    private JPanel footerPanel;
    private Component footerLabel;

    public UpdateDonorFrame() {
        setTitle("Update Donor");
        setSize(800, 586);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        getContentPane().setBackground(Color.PINK);

        // Create and configure form panel
        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.PINK);
        formPanel.setBounds(10, 10, 760, 293);
        getContentPane().add(formPanel);

        // Form setup
        JLabel userLabel = new JLabel("UserId:");
        userLabel.setBounds(167, 10, 100, 25);
        formPanel.add(userLabel);

        userComboBox = new JComboBox<>();
        userComboBox.setBounds(356, 10, 150, 25);
        formPanel.add(userComboBox);

        JLabel nameLabelLabel = new JLabel("Name:");
        nameLabelLabel.setBounds(167, 45, 100, 25);
        formPanel.add(nameLabelLabel);

        nameLabel = new JLabel();
        nameLabel.setBounds(356, 45, 150, 25);
        formPanel.add(nameLabel);

        JLabel bloodGroupLabel = new JLabel("BloodGroupId:");
        bloodGroupLabel.setBounds(167, 81, 100, 25);
        formPanel.add(bloodGroupLabel);

        bloodGroupComboBox = new JComboBox<>();
        bloodGroupComboBox.setBounds(356, 81, 150, 25);
        formPanel.add(bloodGroupComboBox);

        JLabel groupNameLabel = new JLabel("GroupName:");
        groupNameLabel.setBounds(167, 115, 100, 25);
        formPanel.add(groupNameLabel);

        groupNameComboBox = new JComboBox<>();
        groupNameComboBox.setBounds(356, 115, 150, 25);
        formPanel.add(groupNameComboBox);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(167, 150, 100, 25);
        formPanel.add(amountLabel);

        amountComboBox = new JComboBox<>();
        amountComboBox.setBounds(356, 150, 150, 25);
        formPanel.add(amountComboBox);

        JLabel contactNoLabel = new JLabel("ContactNo:");
        contactNoLabel.setBounds(167, 186, 100, 25);
        formPanel.add(contactNoLabel);

        contactNoField = new JTextField();
        contactNoField.setBounds(356, 186, 150, 25);
        formPanel.add(contactNoField);
        
        JLabel lblBloodunit = new JLabel("BloodUnit:");
        lblBloodunit.setBounds(167, 222, 100, 25);
        formPanel.add(lblBloodunit);
        
        textField = new JTextField();
        textField.setBounds(356, 222, 150, 25);
        formPanel.add(textField);
        
        closeButton = new JButton("Close");
        closeButton.setBounds(522, 252, 150, 30);
        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }
        formPanel.add(closeButton);
        
        updateButton = new JButton("Update Donor");
        updateButton.setBounds(230, 252, 150, 30);
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
            updateButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }
        formPanel.add(updateButton);
        
        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);
        footerPanel.setBounds(10, 492, 800, 55);
        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);
        footerPanel.add(footerLabel, BorderLayout.NORTH);
        getContentPane().add(footerPanel);

        loadComboBoxes();
       

        userComboBox.addActionListener(e -> updateUserDetails());
        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());
        closeButton.addActionListener(e -> dispose());
        updateButton.addActionListener(e -> updateDonorDetails());

    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load UserIds for Donors
            String query = "SELECT UserId FROM User WHERE UserType = 'Donor'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userComboBox.addItem(resultSet.getString("UserId"));
            }

            // Load BloodGroupId and GroupName
            query = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String bloodGroupId = resultSet.getString("BloodGroupId");
                String groupName = resultSet.getString("GroupName");

                bloodGroupComboBox.addItem(bloodGroupId);
                groupNameComboBox.addItem(groupName);
                bloodGroupMap.put(bloodGroupId, groupName);
                groupNameMap.put(groupName, bloodGroupId);
            }

            // Load unique amounts
            query = "SELECT DISTINCT Amount FROM User WHERE Amount IS NOT NULL";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BigDecimal amount = resultSet.getBigDecimal("Amount");
                amountComboBox.addItem(amount.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateUserDetails() {
        String selectedUserId = (String) userComboBox.getSelectedItem();
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Query to get user details
            String query = "SELECT Name, ContactNo FROM User WHERE UserId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedUserId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nameLabel.setText(resultSet.getString("Name"));
                contactNoField.setText(resultSet.getString("ContactNo"));
            }

            // Query to get donor details
            query = "SELECT BloodGroupId, BloodUnit FROM Donor WHERE UserId = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, selectedUserId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String bloodGroupId = resultSet.getString("BloodGroupId");
                BigDecimal bloodUnit = resultSet.getBigDecimal("BloodUnit");

                bloodGroupComboBox.setSelectedItem(bloodGroupId);
                textField.setText(bloodUnit != null ? bloodUnit.toString() : ""); // Ensure textField is updated correctly

                // Set the selected GroupName based on BloodGroupId
                groupNameComboBox.setSelectedItem(bloodGroupMap.get(bloodGroupId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGroupName() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String groupName = bloodGroupMap.get(selectedBloodGroupId);
        groupNameComboBox.setSelectedItem(groupName);
    }

    private void updateBloodGroupId() {
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
        String bloodGroupId = groupNameMap.get(selectedGroupName);
        bloodGroupComboBox.setSelectedItem(bloodGroupId);
    }
    
    private void updateDonorDetails() {
        // Prompt user for confirmation
        int confirmResult = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to update the donor details?", 
            "Confirm Update", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );

        // If the user confirms, proceed with the update
        if (confirmResult == JOptionPane.YES_OPTION) {
            String selectedUserId = (String) userComboBox.getSelectedItem();
            String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
            
            // Check if bloodUnit field is empty or not a valid number
            BigDecimal bloodUnit;
            try {
                bloodUnit = new BigDecimal(textField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number for Blood Unit.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Update Donor table
                String query = "UPDATE Donor SET BloodGroupId = ?, BloodUnit = ? WHERE UserId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedBloodGroupId);
                statement.setBigDecimal(2, bloodUnit);
                statement.setString(3, selectedUserId);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Donor details updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No donor found with the selected UserId.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating donor details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UpdateDonorFrame frame = new UpdateDonorFrame();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

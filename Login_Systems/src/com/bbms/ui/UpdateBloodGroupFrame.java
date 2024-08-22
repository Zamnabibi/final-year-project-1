package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class UpdateBloodGroupFrame extends JFrame {
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JTextField groupNameField;
    private JButton updateButton, closeButton;
    
    private JPanel footerPanel;
    private JLabel footerLabel;

    public UpdateBloodGroupFrame() {
        setTitle("Update Blood Group");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(Color.PINK);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.setOpaque(false);

        formPanel.add(new JLabel("Blood Group ID:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("Group Name:"));
        groupNameField = new JTextField();
        formPanel.add(groupNameField);

        // Initialize buttons
        updateButton = new JButton("Update Blood Group");
        closeButton = new JButton("Close");

        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
            updateButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }

        // Add buttons to form panel
        formPanel.add(updateButton);
        formPanel.add(closeButton);

        add(formPanel, BorderLayout.NORTH);

        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Load BloodGroup IDs
        loadBloodGroupIds();

        // Event Listeners
        bloodGroupIdComboBox.addActionListener(e -> loadGroupName());
        updateButton.addActionListener(e -> updateBloodGroup());
        closeButton.addActionListener(e -> dispose());
        
        setVisible(true);
    }

    // Method to load Blood Group IDs into the ComboBox
    private void loadBloodGroupIds() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bloodGroupIdComboBox.addItem(resultSet.getInt("BloodGroupId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood group IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to load Group Name based on selected BloodGroupId
    private void loadGroupName() {
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        if (bloodGroupId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, bloodGroupId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    groupNameField.setText(resultSet.getString("GroupName"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

 // Method to update Blood Group details
    private void updateBloodGroup() {
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = groupNameField.getText();

        // Validate input
        if (bloodGroupId == null || groupName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a blood group ID and enter a group name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to update the blood group?",
            "Confirm Update",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        // Proceed if user confirms
        if (confirmation == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE BloodGroup SET GroupName = ? WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, groupName);
                statement.setInt(2, bloodGroupId);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Blood group updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "No blood group found with the given ID.", "Warning", JOptionPane.WARNING_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating blood group.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // User chose not to update
            JOptionPane.showMessageDialog(this, "Update canceled.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UpdateBloodGroupFrame::new);
    }
}

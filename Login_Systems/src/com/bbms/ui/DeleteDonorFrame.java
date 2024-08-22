package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class DeleteDonorFrame extends JFrame {
    private JComboBox<String> donorIdComboBox;
    private JTextField userIdField;
    private JTextField bloodGroupField;
    private JTextField bloodUnitField;
    private JButton deleteButton;
    private JButton closeButton;

    private JLabel donorIdLabel;
    private JLabel userIdLabel;
    private JLabel bloodGroupLabel;
    private JLabel bloodUnitLabel;

    public DeleteDonorFrame() {
        setTitle("Delete Donor");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.PINK);

        // Initialize components
        donorIdComboBox = new JComboBox<>();
        userIdField = new JTextField();
        bloodGroupField = new JTextField();
        bloodUnitField = new JTextField();
        deleteButton = new JButton("Delete");
        closeButton = new JButton("Close");

        // Initialize labels
        donorIdLabel = new JLabel("Donor ID:");
        userIdLabel = new JLabel("User ID:");
        bloodGroupLabel = new JLabel("Blood Group:");
        bloodUnitLabel = new JLabel("Blood Unit:");

        // Set text fields to be non-editable
        userIdField.setEditable(false);
        bloodGroupField.setEditable(false);
        bloodUnitField.setEditable(false);

        // Set bounds for components
        donorIdComboBox.setBounds(150, 20, 200, 30);
        donorIdLabel.setBounds(50, 20, 100, 30);
        userIdField.setBounds(150, 60, 200, 30);
        userIdLabel.setBounds(50, 60, 100, 30);
        bloodGroupField.setBounds(150, 100, 200, 30);
        bloodGroupLabel.setBounds(50, 100, 100, 30);
        bloodUnitField.setBounds(150, 140, 200, 30);
        bloodUnitLabel.setBounds(50, 140, 100, 30);
        deleteButton.setBounds(150, 180, 150, 30);
        closeButton.setBounds(320, 180, 150, 30);

        // Add components to frame
        getContentPane().add(donorIdComboBox);
        getContentPane().add(donorIdLabel);
        getContentPane().add(userIdField);
        getContentPane().add(userIdLabel);
        getContentPane().add(bloodGroupField);
        getContentPane().add(bloodGroupLabel);
        getContentPane().add(bloodUnitField);
        getContentPane().add(bloodUnitLabel);
        getContentPane().add(deleteButton);
        getContentPane().add(closeButton);

        // Create and add footer panel
        JPanel footerPanel = new JPanel(null);
        footerPanel.setBounds(0, 413, 700, 67);
        footerPanel.setBackground(Color.PINK);

        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.BLACK);
        footerLabel.setBounds(10, 10, getWidth() - 20, 20);
        footerPanel.add(footerLabel);

        getContentPane().add(footerPanel);

        // Add action listeners
        donorIdComboBox.addActionListener(e -> loadDonorDetails());
        deleteButton.addActionListener(e -> deleteDonor());
        closeButton.addActionListener(e -> dispose());

        // Load data
        loadDonorIds();
        loadIcons();
    }
    
    private void loadIcons() {
        try {
            Image imgDelete = new ImageIcon(getClass().getResource("/delete.png")).getImage();
            deleteButton.setIcon(new ImageIcon(imgDelete));
        } catch (Exception e) {
            System.err.println("Delete icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.err.println("Close icon not found.");
        }
    }


    private void loadDonorIds() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT DonorId FROM Donor";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            donorIdComboBox.removeAllItems();
            while (resultSet.next()) {
                donorIdComboBox.addItem(resultSet.getString("DonorId"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading donor IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDonorDetails() {
        String selectedDonorId = (String) donorIdComboBox.getSelectedItem();
        if (selectedDonorId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT d.UserId, bg.GroupName, d.BloodUnit " +
                        "FROM Donor d " +
                        "JOIN BloodGroup bg ON d.BloodGroupId = bg.BloodGroupId " +
                        "WHERE d.DonorId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedDonorId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    userIdField.setText(resultSet.getString("UserId"));
                    bloodGroupField.setText(resultSet.getString("GroupName"));
                    bloodUnitField.setText(resultSet.getString("BloodUnit"));
                } else {
                    // Clear fields if no result is found
                    userIdField.setText("");
                    bloodGroupField.setText("");
                    bloodUnitField.setText("");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading donor details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteDonor() {
        String selectedDonorId = (String) donorIdComboBox.getSelectedItem();
        if (selectedDonorId == null) {
            JOptionPane.showMessageDialog(this, "Please select a donor to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this donor?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                // Move donor details to history table
                String historyQuery = "INSERT INTO History (UserId, BloodGroupId, BloodUnit) " +
                        "SELECT d.UserId, d.BloodGroupId, d.BloodUnit " +
                        "FROM Donor d WHERE d.DonorId = ?";
                PreparedStatement historyStatement = connection.prepareStatement(historyQuery);
                historyStatement.setString(1, selectedDonorId);
                int rowsInserted = historyStatement.executeUpdate();

                // Delete donor from donor table
                String deleteQuery = "DELETE FROM Donor WHERE DonorId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setString(1, selectedDonorId);
                int rowsDeleted = deleteStatement.executeUpdate();

                if (rowsInserted > 0 && rowsDeleted > 0) {
                    // Clear fields and update UI
                    donorIdComboBox.removeItem(selectedDonorId);
                    userIdField.setText("");
                    bloodGroupField.setText("");
                    bloodUnitField.setText("");
                    JOptionPane.showMessageDialog(this, "Donor deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Donor could not be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting donor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteDonorFrame().setVisible(true));
    }
}

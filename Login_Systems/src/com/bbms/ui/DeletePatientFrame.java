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
public class DeletePatientFrame extends JFrame {
    private JComboBox<String> patientComboBox;
    private JButton deleteButton;
    private JButton closeButton;

    private JTextField userIdField;
    private JTextField bloodGroupIdField;
    private JTextField hospitalNameField;
    private JTextField bloodUnitField;
    private JPanel footerPanel;
    private JLabel footerLabel;

    public DeletePatientFrame() {
        setTitle("Delete Patient");
        setSize(702, 586);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PINK); // Set background color to pink

        // Initialize components
        patientComboBox = new JComboBox<>();
        deleteButton = new JButton("Delete Patient");
        closeButton = new JButton("Close");

        userIdField = new JTextField();
        bloodGroupIdField = new JTextField();
        hospitalNameField = new JTextField();
        bloodUnitField = new JTextField();

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 2)); // Adjusted to include relevant fields
        controlPanel.setBackground(Color.PINK); // Set background color to pink
        controlPanel.add(new JLabel("Select Patient:"));
        controlPanel.add(patientComboBox);
        controlPanel.add(new JLabel("User ID:"));
        controlPanel.add(userIdField);
        controlPanel.add(new JLabel("Blood Group ID:"));
        controlPanel.add(bloodGroupIdField);
        controlPanel.add(new JLabel("Hospital Name:"));
        controlPanel.add(hospitalNameField);
        controlPanel.add(new JLabel("Blood Unit:"));
        controlPanel.add(bloodUnitField);
        controlPanel.add(deleteButton);
        controlPanel.add(closeButton);

        // Add components to frame
        getContentPane().add(controlPanel, BorderLayout.NORTH);

        // Create footer panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Add the footer panel to the bottom of the frame
        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Add action listeners
        deleteButton.addActionListener(e -> deletePatient());
        closeButton.addActionListener(e -> dispose());
        patientComboBox.addActionListener(e -> updatePatientDetails());

        // Load data
        loadComboBoxes();
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


    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load UserIds
            String userQuery = "SELECT UserId FROM User WHERE UserType = 'Patient'";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            ResultSet userResultSet = userStatement.executeQuery();
            patientComboBox.removeAllItems();
            while (userResultSet.next()) {
                patientComboBox.addItem(userResultSet.getString("UserId"));
            }

            // Load BloodGroupIds if needed
            // Currently not used but available for future use
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePatientDetails() {
        String selectedPatientId = (String) patientComboBox.getSelectedItem();
        if (selectedPatientId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT UserId, BloodGroupId, HospitalName, BloodUnit " +
                        "FROM Patient " +
                        "WHERE UserId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedPatientId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    userIdField.setText(resultSet.getString("UserId"));
                    bloodGroupIdField.setText(resultSet.getString("BloodGroupId"));
                    hospitalNameField.setText(resultSet.getString("HospitalName"));
                    bloodUnitField.setText(resultSet.getString("BloodUnit"));
                } else {
                    clearPatientDetails();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error loading patient details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearPatientDetails() {
        userIdField.setText("");
        bloodGroupIdField.setText("");
        hospitalNameField.setText("");
        bloodUnitField.setText("");
    }

    private void deletePatient() {
        String selectedPatientId = (String) patientComboBox.getSelectedItem();
        if (selectedPatientId != null) {
            // Show confirmation dialog
            int confirmResult = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete the selected patient?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE);
            
            if (confirmResult == JOptionPane.YES_OPTION) {
                try (Connection connection = DatabaseConnection.getConnection()) {
                    // Move patient data to history table
                    String insertQuery = "INSERT INTO History (UserId, BloodGroupId, HistoryBloodUnit, HistoryHospitalName, DeletedAt) " +
                            "SELECT UserId, BloodGroupId, BloodUnit, HospitalName, NOW() " +
                            "FROM Patient " +
                            "WHERE UserId = ?";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, selectedPatientId);
                    int rowsInserted = insertStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        // Delete patient records from the Patient table
                        String deleteQuery = "DELETE FROM Patient WHERE UserId = ?";
                        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, selectedPatientId);
                        int rowsDeleted = deleteStatement.executeUpdate();

                        if (rowsDeleted > 0) {
                            JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
                            clearPatientDetails();
                            loadComboBoxes(); // Reload combo boxes to reflect the deletion
                        } else {
                            JOptionPane.showMessageDialog(this, "Failed to delete the patient.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to move patient data to history.");
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Deletion cancelled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "No patient selected for deletion.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeletePatientFrame().setVisible(true));
    }
}

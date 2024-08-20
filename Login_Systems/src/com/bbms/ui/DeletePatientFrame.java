package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
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
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> bloodGroupComboBox; // Ensure it's not null
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
        

       

        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
            deleteButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }
        patientTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"PatientId", "UserId", "BloodGroupId", "GroupName", "HospitalName", "BloodUnit", "CreatedAt", "UpdatedAt"},
                0
        );
        patientTable.setModel(tableModel);

        bloodGroupComboBox = new JComboBox<>(); // Initialize it

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2)); // Adjusted to include bloodGroupComboBox
        controlPanel.setBackground(Color.PINK); // Set background color to pink
        controlPanel.add(new JLabel("Select Patient:"));
        controlPanel.add(patientComboBox);
        controlPanel.add(new JLabel("Select Blood Group:")); // Label for bloodGroupComboBox
        controlPanel.add(bloodGroupComboBox); // Add bloodGroupComboBox to panel
        controlPanel.add(deleteButton);
        controlPanel.add(closeButton);

        // Add components to frame
        getContentPane().add(controlPanel, BorderLayout.NORTH);

        // Add scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
        scrollPane.getViewport().setBackground(Color.PINK); // Set background color of viewport

        getContentPane().add(scrollPane, BorderLayout.CENTER); // Add scroll pane to center

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

        // Load data
        loadPatientData();
        loadComboBoxes();
    }

    private void loadPatientData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT Patient.PatientId, Patient.UserId, Patient.BloodGroupId, BloodGroup.GroupName, " +
                    "Patient.HospitalName, Patient.BloodUnit, Patient.CreatedAt, Patient.UpdatedAt " +
                    "FROM Patient " +
                    "INNER JOIN BloodGroup ON Patient.BloodGroupId = BloodGroup.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("PatientId"),
                        resultSet.getInt("UserId"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName"),
                        resultSet.getString("HospitalName"),
                        resultSet.getInt("BloodUnit"),
                        resultSet.getTimestamp("CreatedAt"),
                        resultSet.getTimestamp("UpdatedAt")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

            // Load BloodGroupIds
            String bloodGroupQuery = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            bloodGroupComboBox.removeAllItems(); // Make sure it's not null
            while (bloodGroupResultSet.next()) {
                bloodGroupComboBox.addItem(bloodGroupResultSet.getString("GroupName"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            int patientId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Log the PatientId for debugging
                System.out.println("Attempting to delete PatientId: " + patientId);

                // Move patient data to history table
                String insertQuery = "INSERT INTO History (PatientId, UserId, BloodGroupId, GroupName, BloodUnit, HospitalName, DeletedAt) " +
                        "SELECT Patient.PatientId, Patient.UserId, Patient.BloodGroupId, BloodGroup.GroupName, Patient.BloodUnit, Patient.HospitalName, NOW() " +
                        "FROM Patient " +
                        "INNER JOIN BloodGroup ON Patient.BloodGroupId = BloodGroup.BloodGroupId " +
                        "WHERE Patient.PatientId = ?";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, patientId);
                int rowsInserted = insertStatement.executeUpdate();

                if (rowsInserted > 0) {
                    // Delete patient record from the Patient table
                    String deleteQuery = "DELETE FROM Patient WHERE PatientId = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, patientId);
                    int rowsDeleted = deleteStatement.executeUpdate();

                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete the patient.");
                    }

                    // Refresh the patient data table
                    loadPatientData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to move patient data to history.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No patient selected for deletion.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeletePatientFrame().setVisible(true));
    }
}

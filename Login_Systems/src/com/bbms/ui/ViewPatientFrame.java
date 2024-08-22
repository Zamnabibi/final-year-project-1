package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.bbms.util.DatabaseConnection;
import java.awt.*;
import java.sql.*;

@SuppressWarnings("serial")
public class ViewPatientFrame extends JFrame {

    private JTable patientTable;
    private DefaultTableModel patientTableModel;
    private JPanel footerPanel;

    public ViewPatientFrame() {
        setTitle("View Patient Frame");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize the table model with column names
        patientTableModel = new DefaultTableModel(new Object[]{
            "Patient ID", "User ID", "Blood Group ID", "Name", "Email", "Contact No", "Blood Unit", "Group Name"
        }, 0);

        // Initialize the table with the model
        patientTable = new JTable(patientTableModel);

        // Set table and header customization
        customizeTable();
        
        // Wrap the table in a JScrollPane
        JScrollPane tableScrollPane = new JScrollPane(patientTable);

        // Set viewport background color
        tableScrollPane.getViewport().setBackground(Color.PINK);

        // Add the JScrollPane to the frame
        add(tableScrollPane, BorderLayout.CENTER);

        // Create footer panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK); // Set footer background color to pink

        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);
        
        // Add footer panel to the SOUTH position of BorderLayout
        add(footerPanel, BorderLayout.SOUTH);

        // Load patient data into the table
        loadPatients();
        
        createFooter();
    }

    private void loadPatients() {
        // Clear any existing rows
        patientTableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT p.PatientId, p.UserId, p.BloodGroupId, u.Name, u.Email, u.ContactNo, p.BloodUnit, bg.GroupName " +
                           "FROM Patient p " +
                           "JOIN User u ON p.UserId = u.UserId " +
                           "JOIN BloodGroup bg ON p.BloodGroupId = bg.BloodGroupId";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    patientTableModel.addRow(new Object[]{
                        resultSet.getInt("PatientId"),
                        resultSet.getString("UserId"),
                        resultSet.getString("BloodGroupId"),
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getString("ContactNo"),
                        resultSet.getInt("BloodUnit"),
                        resultSet.getString("GroupName")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void customizeTable() {
        JTableHeader header = patientTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        patientTable.setFont(new Font("Arial", Font.PLAIN, 12));
        patientTable.setRowHeight(25); // Optional: Set row height for better visibility
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Set single selection mode
        patientTable.setGridColor(Color.BLACK); // Optional: Set grid color for the table
    }
    
    private void createFooter() {
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK); // Set footer background color to pink

        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Add footer panel to the SOUTH position of BorderLayout
        add(footerPanel, BorderLayout.SOUTH);
    }


    public static void main(String[] args) {
        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
            null,
            "Do you want to view the patient details?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> new ViewPatientFrame().setVisible(true));
        }
    }
}

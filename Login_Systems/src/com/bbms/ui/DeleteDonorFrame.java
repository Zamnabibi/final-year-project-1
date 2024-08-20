package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class DeleteDonorFrame extends JFrame {
    private JComboBox<String> donorComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable donorTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> bloodGroupComboBox;
    private JPanel footerPanel; // Footer panel
    private JLabel footerLabel; // Footer label

    public DeleteDonorFrame() {
        setTitle("Delete Donor");
        setSize(702, 586);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PINK); // Set background color to pink

        // Initialize components
        donorComboBox = new JComboBox<>();
        deleteButton = new JButton("Delete Donor");
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
        donorTable = new JTable();
        tableModel = new DefaultTableModel(
                new Object[]{"DonorId", "UserId", "name", "BloodGroupId", "GroupName", "Amount", "BloodUnit", "ContactNo"},
                0
        );
        donorTable.setModel(tableModel);

        bloodGroupComboBox = new JComboBox<>();

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2));
        controlPanel.setBackground(Color.PINK); // Set background color to pink
        controlPanel.add(new JLabel("Select Donor:"));
        controlPanel.add(donorComboBox);
        controlPanel.add(new JLabel("Select Blood Group:")); // Added label for blood group selection
        controlPanel.add(bloodGroupComboBox); // Added blood group combo box
        controlPanel.add(deleteButton);
        controlPanel.add(closeButton);

        // Add components to frame
        getContentPane().add(controlPanel, BorderLayout.NORTH);

        // Add JScrollPane with pink background
        JScrollPane scrollPane = new JScrollPane(donorTable);
        scrollPane.getViewport().setBackground(Color.PINK); // Set background color of viewport
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create footer panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        footerPanel.setBackground(Color.PINK); // Set background color to pink

        // Create footer label
        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.BLACK);

        // Add footer label to footer panel
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Add footer panel to the bottom of the frame
        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Add action listeners
        deleteButton.addActionListener(e -> deleteDonor());
        closeButton.addActionListener(e -> dispose());

        // Load data
        loadDonorData();
        loadComboBoxes();
    }

    private void loadDonorData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT Donor.*,name, BloodGroup.GroupName FROM Donor INNER JOIN BloodGroup ON Donor.BloodGroupId = BloodGroup.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("DonorId"),
                        resultSet.getInt("UserId"),
                        resultSet.getString("Name"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName"),
                        resultSet.getBigDecimal("Amount") != null ? "$" + resultSet.getBigDecimal("Amount").toPlainString() : "",
                        resultSet.getInt("BloodUnit"),
                        resultSet.getString("ContactNo"), // Fixed to getString
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading donor data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load UserIds
            String userQuery = "SELECT UserId FROM User WHERE UserType = 'Donor'";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            ResultSet userResultSet = userStatement.executeQuery();
            donorComboBox.removeAllItems();
            while (userResultSet.next()) {
                donorComboBox.addItem(userResultSet.getString("UserId"));
            }

            // Load BloodGroupIds
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            bloodGroupComboBox.removeAllItems();
            while (bloodGroupResultSet.next()) {
                bloodGroupComboBox.addItem(bloodGroupResultSet.getString("BloodGroupId"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow != -1) {
            int donorId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Move donor data to history table
                String insertQuery = "INSERT INTO History (DonorId, UserId,Name, BloodGroupId, GroupName, Amount, BloodUnit, ContactNo, DeletedAt) " +
                        "SELECT DonorId, UserId,Name, BloodGroupId, GroupName, Amount, BloodUnit, ContactNo, NOW() " +
                        "FROM Donor WHERE DonorId = ?";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, donorId);
                insertStatement.executeUpdate();

                // Delete donor record from the Donor table
                String deleteQuery = "DELETE FROM Donor WHERE DonorId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, donorId);
                deleteStatement.executeUpdate();

                // Refresh the donor data table
                loadDonorData();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting donor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No donor selected for deletion.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteDonorFrame().setVisible(true));
    }
}

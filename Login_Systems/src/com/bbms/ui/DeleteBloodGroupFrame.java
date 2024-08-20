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
public class DeleteBloodGroupFrame extends JFrame {
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable bloodGroupTable;
    private DefaultTableModel tableModel;
    private JPanel buttonPanel;
    private JPanel footerPanel;
    private JLabel footerLabel;

    public DeleteBloodGroupFrame() {
        setTitle("Delete Blood Group");
        setSize(600, 568);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Set the background color to pink
        getContentPane().setBackground(Color.PINK);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.setBackground(Color.PINK);
        formPanel.add(new JLabel("Blood Group ID:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);
        getContentPane().add(formPanel, BorderLayout.NORTH);

        // Buttons Panel
        buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.PINK);
        deleteButton = new JButton("Delete Blood Group");
        closeButton = new JButton("Close");
        

        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

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
        // Create footer panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Add buttonPanel and footerPanel to the SOUTH region of the frame
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH); // Place buttonPanel at the top
        southPanel.add(footerPanel, BorderLayout.SOUTH); // Place footerPanel at the bottom
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        // Table setup
        tableModel = new DefaultTableModel();
        bloodGroupTable = new JTable(tableModel);
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName");
        getContentPane().add(new JScrollPane(bloodGroupTable), BorderLayout.CENTER);

        // Load data
        loadBloodGroupIds();
        loadBloodGroupData();

        // Add Action Listeners
        deleteButton.addActionListener(e -> deleteBloodGroup());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadBloodGroupIds() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            bloodGroupIdComboBox.removeAllItems(); // Clear existing items
            while (resultSet.next()) {
                bloodGroupIdComboBox.addItem(resultSet.getInt("BloodGroupId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood group IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBloodGroupData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood groups.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBloodGroup() {
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        if (bloodGroupId == null) {
            JOptionPane.showMessageDialog(this, "Please select a blood group ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Retrieve data from the BloodGroup table
            String selectQuery = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, bloodGroupId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String groupName = resultSet.getString("GroupName");

                // Insert data into the History table
                String insertQuery = "INSERT INTO History (BloodGroupId, GroupName, DeletedAt) VALUES (?, ?, NOW())";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, bloodGroupId);
                insertStatement.setString(2, groupName);
                insertStatement.executeUpdate();

                // Delete the record from the BloodGroup table
                String deleteQuery = "DELETE FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, bloodGroupId);
                deleteStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Blood group deleted successfully.");
                loadBloodGroupIds(); // Reload the BloodGroup IDs
                loadBloodGroupData(); // Refresh the table with updated data
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteBloodGroupFrame().setVisible(true));
    }
}

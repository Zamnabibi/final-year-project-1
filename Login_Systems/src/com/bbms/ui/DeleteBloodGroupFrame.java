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
public class DeleteBloodGroupFrame extends JFrame {
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JLabel groupNameLabel;
    private JButton deleteButton;
    private JButton closeButton;
   
    private JPanel buttonPanel;
    private JPanel footerPanel;
    private JLabel footerLabel;

    public DeleteBloodGroupFrame() {
        setTitle("Delete Blood Group");
        setSize(600, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Set the background color to pink
        getContentPane().setBackground(Color.PINK);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.setBackground(Color.PINK);
        
        formPanel.add(new JLabel("Blood Group ID:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("Group Name:"));
        groupNameLabel = new JLabel();
        formPanel.add(groupNameLabel);

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
            Image imgDelete = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
            deleteButton.setIcon(new ImageIcon(imgDelete));
        } catch (Exception e) {
            System.out.println("Delete icon not found.");
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

        // Load data
        loadBloodGroupIds();
        
        // Add Action Listeners
        bloodGroupIdComboBox.addActionListener(e -> loadGroupName());
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

    private void loadGroupName() {
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        if (bloodGroupId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, bloodGroupId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    groupNameLabel.setText(resultSet.getString("GroupName"));
                } else {
                    groupNameLabel.setText("Group Name not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading group name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBloodGroup() {
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        if (bloodGroupId == null) {
            JOptionPane.showMessageDialog(this, "Please select a blood group ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this blood group?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return; // If user clicks No, exit the method
        }

        Connection connection = null;
        PreparedStatement selectStatement = null;
        PreparedStatement insertStatement = null;
        PreparedStatement deleteStatement = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Retrieve data from the BloodGroup table
            String selectQuery = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
            selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, bloodGroupId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String groupName = resultSet.getString("GroupName");

                // Insert data into the History table
                String insertQuery = "INSERT INTO History (BloodGroupId, HistoryGroupName, DeletedAt) VALUES (?, ?, NOW())";
                insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, bloodGroupId);
                insertStatement.setString(2, groupName);

                int rowsInserted = insertStatement.executeUpdate();
                System.out.println("Rows inserted into History: " + rowsInserted);

                // Now delete the record from the BloodGroup table
                String deleteQuery = "DELETE FROM BloodGroup WHERE BloodGroupId = ?";
                deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, bloodGroupId);

                int rowsDeleted = deleteStatement.executeUpdate();
                System.out.println("Rows deleted from BloodGroup: " + rowsDeleted);

                if (rowsInserted > 0 && rowsDeleted > 0) {
                    connection.commit(); // Commit transaction
                    JOptionPane.showMessageDialog(this, "Blood group deleted successfully.");
                    loadBloodGroupIds(); // Reload the BloodGroup IDs
                    groupNameLabel.setText(""); // Clear the group name label
                } else {
                    connection.rollback(); // Rollback transaction
                    JOptionPane.showMessageDialog(this, "Error deleting record. No rows affected.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record. Ensure there are no constraints blocking the deletion.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Clean up resources
            try {
                if (selectStatement != null) selectStatement.close();
                if (insertStatement != null) insertStatement.close();
                if (deleteStatement != null) deleteStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteBloodGroupFrame().setVisible(true));
    }
}

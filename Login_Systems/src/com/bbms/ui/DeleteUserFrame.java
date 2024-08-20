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
public class DeleteUserFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;
    private JLabel footerLabel;

    public DeleteUserFrame() {
        setTitle("Delete User");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PINK); // Set background color to pink

        // Initialize components
        userComboBox = new JComboBox<>();
        deleteButton = new JButton("Delete User");
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
        userTable = new JTable();
        
        // Update table model with new columns
        tableModel = new DefaultTableModel(
            new Object[]{"UserId", "UserType", "Name", "FatherName", "MotherName", "DOB", "ContactNo", "Gender", "Email", "Amount", "Address", "CreatedAt", "UpdatedAt"},
            0
        );
        userTable.setModel(tableModel);

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 2));
        controlPanel.setBackground(Color.PINK); // Set background color to pink
        controlPanel.add(new JLabel("Select User:"));
        controlPanel.add(userComboBox);
        controlPanel.add(deleteButton);
        controlPanel.add(closeButton);

        // Add components to frame
        getContentPane().add(controlPanel, BorderLayout.NORTH);

        // Add JScrollPane with pink background
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.getViewport().setBackground(Color.PINK); // Set background color of viewport
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Initialize the status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.RED); // You can set the color as needed

        // Add footer panel with statusLabel and footerLabel
        JPanel footerPanel = new JPanel(new GridLayout(2, 1)); // Using GridLayout to place status and footer labels
        footerPanel.setBackground(Color.PINK); // Set background color to pink
        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);
        footerPanel.add(statusLabel);  // Add status label first
        footerPanel.add(footerLabel);  // Add footer label second
        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Add action listeners
        deleteButton.addActionListener(e -> deleteUser());
        closeButton.addActionListener(e -> dispose());

        // Load data
        loadUserData();
        loadUserComboBox();
    }

    private void loadUserData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("UserId"),
                    resultSet.getString("UserType"),
                    resultSet.getString("Name"),
                    resultSet.getString("FatherName"),
                    resultSet.getString("MotherName"),
                    resultSet.getDate("DOB"),
                    resultSet.getString("ContactNo"),
                    resultSet.getString("Gender"),
                    resultSet.getString("Email"),
                    resultSet.getBigDecimal("Amount") != null ? "$" + resultSet.getBigDecimal("Amount").toPlainString() : "", // Format Amount with $
                    resultSet.getString("Address"), // Retrieve Address
                    resultSet.getTimestamp("CreatedAt"),
                    resultSet.getTimestamp("UpdatedAt")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserComboBox() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT UserId FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            userComboBox.removeAllItems();
            while (resultSet.next()) {
                userComboBox.addItem(resultSet.getString("UserId"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user combo box data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedUserId = Integer.parseInt((String) userComboBox.getSelectedItem());
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Move user data to history table
            String insertQuery = "INSERT INTO History (UserId, UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email, Amount, Address, DeletedAt) " +
                    "SELECT UserId, UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email, Amount, Address, NOW() " +
                    "FROM User WHERE UserId = ?";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, selectedUserId);
            insertStatement.executeUpdate();

            // Delete user record from the User table
            String deleteQuery = "DELETE FROM User WHERE UserId = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, selectedUserId);
            deleteStatement.executeUpdate();

            // Refresh the user data table
            loadUserData();
            loadUserComboBox();

            // Update footer status
            statusLabel.setText("User deleted successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Error deleting user.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteUserFrame().setVisible(true));
    }
}

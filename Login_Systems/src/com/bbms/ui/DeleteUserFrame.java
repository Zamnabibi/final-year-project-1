package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class DeleteUserFrame extends JFrame {
    private JComboBox<Integer> userComboBox;
    private JButton deleteButton;
    private JButton closeButton;
    private JLabel statusLabel;
    private JLabel footerLabel;

    // Text fields for user details
    private JTextField userTypeField;
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JTextField emailField;
    private JTextField contactNoField;
    private JTextField dobField;
    private JTextField genderField;
    private JTextField amountField;
    private JTextField addressField;

    public DeleteUserFrame() {
        setTitle("Delete User");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PINK);

        // Initialize components
        userComboBox = new JComboBox<>();
        deleteButton = new JButton("Delete User");
        closeButton = new JButton("Close");

        // Initialize text fields for user details
        userTypeField = new JTextField();
        nameField = new JTextField();
        fatherNameField = new JTextField();
        motherNameField = new JTextField();
        emailField = new JTextField();
        contactNoField = new JTextField();
        dobField = new JTextField();
        genderField = new JTextField();
        amountField = new JTextField();
        addressField = new JTextField();

        // Set text fields to non-editable
        setTextFieldsEditable(false);

        // Load icons
        loadIcons();

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(12, 2));
        controlPanel.setBackground(Color.PINK);
        addControlsToPanel(controlPanel);

        // Add components to frame
        getContentPane().add(controlPanel, BorderLayout.CENTER);

        // Initialize the status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.RED);

        // Add footer panel with statusLabel and footerLabel
        JPanel footerPanel = new JPanel(new GridLayout(2, 1));
        footerPanel.setBackground(Color.PINK);
        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.BLACK);
        footerPanel.add(statusLabel);
        footerPanel.add(footerLabel);
        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Add action listeners
        userComboBox.addActionListener(e -> loadUserDetails());
        deleteButton.addActionListener(e -> deleteUser());
        closeButton.addActionListener(e -> dispose());

        // Load data
        loadUserComboBox();
        clearUserDetails(); // Clear fields initially
    }

    private void setTextFieldsEditable(boolean editable) {
        userTypeField.setEditable(editable);
        nameField.setEditable(editable);
        fatherNameField.setEditable(editable);
        motherNameField.setEditable(editable);
        emailField.setEditable(editable);
        contactNoField.setEditable(editable);
        dobField.setEditable(editable);
        genderField.setEditable(editable);
        amountField.setEditable(editable);
        addressField.setEditable(editable);
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

    private void addControlsToPanel(JPanel panel) {
        panel.add(new JLabel("Select User:"));
        panel.add(userComboBox);
        panel.add(new JLabel("UserType:"));
        panel.add(userTypeField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Father Name:"));
        panel.add(fatherNameField);
        panel.add(new JLabel("Mother Name:"));
        panel.add(motherNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Contact No:"));
        panel.add(contactNoField);
        panel.add(new JLabel("DOB:"));
        panel.add(dobField);
        panel.add(new JLabel("Gender:"));
        panel.add(genderField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(deleteButton);
        panel.add(closeButton);
    }

    private void loadUserComboBox() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT UserId FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            userComboBox.removeAllItems();
            while (resultSet.next()) {
                userComboBox.addItem(resultSet.getInt("UserId"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user combo box data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserDetails() {
        Integer selectedUserId = (Integer) userComboBox.getSelectedItem();
        if (selectedUserId == null) {
            clearUserDetails();
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User WHERE UserId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedUserId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userTypeField.setText(resultSet.getString("UserType"));
                nameField.setText(resultSet.getString("Name"));
                fatherNameField.setText(resultSet.getString("FatherName"));
                motherNameField.setText(resultSet.getString("MotherName"));
                emailField.setText(resultSet.getString("Email"));
                contactNoField.setText(resultSet.getString("ContactNo"));
                dobField.setText(resultSet.getDate("DOB").toString());
                genderField.setText(resultSet.getString("Gender"));

                // Handle amount correctly and add dollar sign
                BigDecimal amount = resultSet.getBigDecimal("Amount");
                if (amount != null) {
                    amountField.setText("$" + amount.toString());
                } else {
                    amountField.setText("");
                }
                addressField.setText(resultSet.getString("Address"));
            } else {
                clearUserDetails();
                JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearUserDetails() {
        userTypeField.setText("");
        nameField.setText("");
        fatherNameField.setText("");
        motherNameField.setText("");
        emailField.setText("");
        contactNoField.setText("");
        dobField.setText("");
        genderField.setText("");
        amountField.setText("");
        addressField.setText("");
    }

    private void deleteUser() {
        Integer selectedUserId = (Integer) userComboBox.getSelectedItem();
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(this, "Please select a User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return; // Exit the method if the user does not confirm
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Move user data to history table
            String insertQuery = "INSERT INTO History (UserId, HistoryUserType, HistoryName, HistoryFatherName, HistoryMotherName, HistoryDOB, HistoryContactNo, HistoryGender, HistoryEmail, HistoryAmount, HistoryAddress, DeletedAt) " +
                    "SELECT UserId, UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email, CONCAT('$', Amount), Address, NOW() " +
                    "FROM User WHERE UserId = ?";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setInt(1, selectedUserId);
            insertStatement.executeUpdate();

            // Delete user from User table
            String deleteQuery = "DELETE FROM User WHERE UserId = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, selectedUserId);
            deleteStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadUserComboBox(); // Refresh the user combo box
            clearUserDetails(); // Clear fields after deletion
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteUserFrame().setVisible(true));
    }
}

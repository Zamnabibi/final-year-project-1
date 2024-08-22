package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class UpdateUserFrame extends JFrame {
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JTextField emailField;
    private JTextField contactNoField;
    private JTextField dobField;
    private JTextField amountField; // New field for Amount
    private JTextField addressField; // New field for Address
    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> genderComboBox;
    private JButton updateButton;
    private JButton closeButton;

    private JComboBox<Integer> userIdComboBox; // New JComboBox for UserId
    private Integer selectedUserId; // Instance variable to store selected userId
    private JPanel footerPanel;
    private Component footerLabel;

    public UpdateUserFrame() {
        // Initialize the frame
        setTitle("Update User");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(Color.PINK);

        // Create the form panel using GridLayout
        JPanel formPanel = new JPanel(new GridLayout(11, 2, 10, 10)); // 11 rows, 2 columns, spacing of 10
        formPanel.setBackground(Color.PINK); // Set the background of the form panel

        userIdComboBox = new JComboBox<>();
        nameField = new JTextField();
        fatherNameField = new JTextField();
        motherNameField = new JTextField();
        emailField = new JTextField();
        contactNoField = new JTextField();
        dobField = new JTextField();
        amountField = new JTextField(); // Initialize Amount field
        addressField = new JTextField(); // Initialize Address field
        userTypeComboBox = new JComboBox<>(new String[]{"Donor", "Patient"});
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});

        // Add labels and fields to the form panel
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIdComboBox);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Father Name:"));
        formPanel.add(fatherNameField);
        formPanel.add(new JLabel("Mother Name:"));
        formPanel.add(motherNameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Contact No:"));
        formPanel.add(contactNoField);
        formPanel.add(new JLabel("DOB (YYYY-MM-DD):"));
        formPanel.add(dobField);
        formPanel.add(new JLabel("User Type:"));
        formPanel.add(userTypeComboBox);
        formPanel.add(new JLabel("Gender:"));
        formPanel.add(genderComboBox);
        formPanel.add(new JLabel("Amount:")); // Label for Amount
        formPanel.add(amountField);
        formPanel.add(new JLabel("Address:")); // Label for Address
        formPanel.add(addressField);

        // Add form panel to the top of the frame
        add(formPanel, BorderLayout.NORTH);

        // Create a panel for buttons using FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.PINK); // Set the background of the button panel
        updateButton = new JButton("Update User");
        closeButton = new JButton("Close");

        buttonPanel.add(updateButton);
        buttonPanel.add(closeButton);

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

        // Create footer panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Create a panel for south region
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.PINK);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add southPanel to the frame
        add(southPanel, BorderLayout.SOUTH);

       
        // Load user data into the table
       
        // Populate userIdComboBox with user IDs
        populateUserIdComboBox();

        // Add action listener to update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        // Add action listener to close button
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });

        // Add action listener to userIdComboBox
        userIdComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedUserId = (Integer) userIdComboBox.getSelectedItem();
                if (selectedUserId != null) {
                    loadUserDetails(selectedUserId);
                }
            }
        });

        // Add listener to userTypeComboBox to enable or disable amountField based on userType
        userTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) userTypeComboBox.getSelectedItem();
                if (selectedType.equals("Donor")) {
                    amountField.setEnabled(true);
                } else {
                    amountField.setEnabled(false);
                }
            }
        });
    }

   
    private void populateUserIdComboBox() {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT UserId FROM user")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                userIdComboBox.addItem(rs.getInt("UserId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadUserDetails(int userId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user WHERE UserId = ?")) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("Name"));
                fatherNameField.setText(rs.getString("FatherName"));
                motherNameField.setText(rs.getString("MotherName"));
                emailField.setText(rs.getString("Email"));
                contactNoField.setText(rs.getString("ContactNo"));
                dobField.setText(rs.getString("DOB"));
                userTypeComboBox.setSelectedItem(rs.getString("UserType"));
                genderComboBox.setSelectedItem(rs.getString("Gender"));
                amountField.setText(rs.getBigDecimal("Amount") != null ? rs.getBigDecimal("Amount").toPlainString() : ""); // Format amount as needed
                addressField.setText(rs.getString("Address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        if (selectedUserId == null) {
            JOptionPane.showMessageDialog(this, "Please select a User ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = nameField.getText();
        String fatherName = fatherNameField.getText();
        String motherName = motherNameField.getText();
        String email = emailField.getText();
        String contactNo = contactNoField.getText();
        String dob = dobField.getText();
        String userType = (String) userTypeComboBox.getSelectedItem();
        String gender = (String) genderComboBox.getSelectedItem();
        String amountText = amountField.getText();
        String address = addressField.getText();

        // Remove the '$' sign if it is present
        BigDecimal amount = null;
        if (amountText != null && !amountText.isEmpty()) {
            try {
                // Remove '$' if present
                amountText = amountText.replace("$", "").trim();
                amount = new BigDecimal(amountText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Confirm update action
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to update this user?",
            "Confirm Update",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            // Proceed with the update
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "UPDATE user SET Name = ?, FatherName = ?, MotherName = ?, Email = ?, ContactNo = ?, DOB = ?, UserType = ?, Gender = ?, Amount = ?, Address = ? WHERE UserId = ?")) {

                stmt.setString(1, name);
                stmt.setString(2, fatherName);
                stmt.setString(3, motherName);
                stmt.setString(4, email);
                stmt.setString(5, contactNo);
                stmt.setString(6, dob);
                stmt.setString(7, userType);
                stmt.setString(8, gender);
                stmt.setBigDecimal(9, amount);
                stmt.setString(10, address);
                stmt.setInt(11, selectedUserId);

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "User updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "User update failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // User chose not to update
            JOptionPane.showMessageDialog(this, "Update canceled.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdateUserFrame updateUserFrame = new UpdateUserFrame();
            updateUserFrame.setVisible(true);
        });
    }
}

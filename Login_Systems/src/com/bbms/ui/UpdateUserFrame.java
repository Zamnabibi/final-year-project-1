package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private JTable userTable;
    private Integer userId; // Instance variable to store userId
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
        JPanel formPanel = new JPanel(new GridLayout(10, 2, 10, 10)); // 10 rows, 2 columns, spacing of 10
        formPanel.setBackground(Color.PINK); // Set the background of the form panel

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

        // Create table for displaying user data
        userTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.getViewport().setBackground(Color.PINK); // Set viewport background color
        add(tableScrollPane, BorderLayout.CENTER);

        // Load user data into the table
        loadUserData();

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

        // Add mouse listener to table for row selection
        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = userTable.getSelectedRow();

                if (selectedRow >= 0) {
                    String userType = (userTable.getValueAt(selectedRow, 7) != null) ? userTable.getValueAt(selectedRow, 7).toString() : "";
                    String name = (userTable.getValueAt(selectedRow, 1) != null) ? userTable.getValueAt(selectedRow, 1).toString() : "";
                    String fatherName = (userTable.getValueAt(selectedRow, 2) != null) ? userTable.getValueAt(selectedRow, 2).toString() : "";
                    String motherName = (userTable.getValueAt(selectedRow, 3) != null) ? userTable.getValueAt(selectedRow, 3).toString() : "";
                    String dob = (userTable.getValueAt(selectedRow, 6) != null) ? userTable.getValueAt(selectedRow, 6).toString() : "";
                    String contactNo = (userTable.getValueAt(selectedRow, 5) != null) ? userTable.getValueAt(selectedRow, 5).toString() : "";
                    String gender = (userTable.getValueAt(selectedRow, 8) != null) ? userTable.getValueAt(selectedRow, 8).toString() : "";
                    String email = (userTable.getValueAt(selectedRow, 4) != null) ? userTable.getValueAt(selectedRow, 4).toString() : "";
                    String amount = (userTable.getValueAt(selectedRow, 9) != null) ? userTable.getValueAt(selectedRow, 9).toString() : "";
                    String address = (userTable.getValueAt(selectedRow, 10) != null) ? userTable.getValueAt(selectedRow, 10).toString() : "";

                    userTypeComboBox.setSelectedItem(userType);
                    nameField.setText(name);
                    fatherNameField.setText(fatherName);
                    motherNameField.setText(motherName);
                    dobField.setText(dob);
                    contactNoField.setText(contactNo);
                    genderComboBox.setSelectedItem(gender);
                    emailField.setText(email);
                    amountField.setText(amount);
                    addressField.setText(address);
                    userId = (Integer) userTable.getValueAt(selectedRow, 0); // Store the selected userId
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

    private void loadUserData() {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{
            "UserId", "Name", "Father Name", "Mother Name", "Email", "Contact No", "DOB", "User Type", "Gender", "Amount", "Address"
        }, 0);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM user")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BigDecimal amount = rs.getBigDecimal("Amount");
                String formattedAmount = amount != null ? "$" + amount.toPlainString() : "";

                tableModel.addRow(new Object[]{
                    rs.getInt("UserId"),
                    rs.getString("Name"),
                    rs.getString("FatherName"),
                    rs.getString("MotherName"),
                    rs.getString("Email"),
                    rs.getString("ContactNo"),
                    rs.getString("DOB"),
                    rs.getString("UserType"),
                    rs.getString("Gender"),
                    formattedAmount, // Display the amount with $
                    rs.getString("Address")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userTable.setModel(tableModel);
    }

    private void updateUser() {
        if (userId == null) {
            JOptionPane.showMessageDialog(this, "No user selected.", "Error", JOptionPane.ERROR_MESSAGE);
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

        BigDecimal amount = null;
        if (userType.equals("Donor")) {
            try {
                // Debugging statement to check the amountText value
                System.out.println("Amount Text: " + amountText);
                
                // Attempt to parse the amount
                amount = new BigDecimal(amountText);
            } catch (NumberFormatException e) {
                // Provide detailed error message
                JOptionPane.showMessageDialog(this, "Invalid amount format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE user SET Name=?, FatherName=?, MotherName=?, Email=?, ContactNo=?, DOB=?, UserType=?, Gender=?, Amount=?, Address=? WHERE UserId=?")) {

            stmt.setString(1, name);
            stmt.setString(2, fatherName);
            stmt.setString(3, motherName);
            stmt.setString(4, email);
            stmt.setString(5, contactNo);
            stmt.setString(6, dob);
            stmt.setString(7, userType);
            stmt.setString(8, gender);
            if (amount != null) {
                stmt.setBigDecimal(9, amount);
            } else {
                stmt.setNull(9, java.sql.Types.DECIMAL);
            }
            stmt.setString(10, address);
            stmt.setInt(11, userId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserData(); // Reload user data
            } else {
                JOptionPane.showMessageDialog(this, "Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while updating the user. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdateUserFrame updateUserFrame = new UpdateUserFrame();
            updateUserFrame.setVisible(true);
        });
    }
}

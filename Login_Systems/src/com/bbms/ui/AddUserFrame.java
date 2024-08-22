package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class AddUserFrame extends JFrame {
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JTextField emailField;
    private JTextField contactNoField;
    private JTextField dobField;
    private JTextField addressField;
    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> amountComboBox;
    private JButton addButton;
    private JButton closeButton;
   
    private JPanel formPanel;
    private JLabel footerLabel;
    private JPanel footerPanel;

    public AddUserFrame() {
        setTitle("Add User");
        setSize(800, 651);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Set pink background for the frame
        getContentPane().setBackground(Color.PINK);

        formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Dimension textFieldSize = new Dimension(300, 30);

        // Form Fields
        nameField = new JTextField();
        fatherNameField = new JTextField();
        motherNameField = new JTextField();
        emailField = new JTextField();
        contactNoField = new JTextField();
        dobField = new JTextField();
        addressField = new JTextField();

        nameField.setPreferredSize(textFieldSize);
        fatherNameField.setPreferredSize(textFieldSize);
        motherNameField.setPreferredSize(textFieldSize);
        emailField.setPreferredSize(textFieldSize);
        contactNoField.setPreferredSize(textFieldSize);
        dobField.setPreferredSize(textFieldSize);
        addressField.setPreferredSize(textFieldSize);

        userTypeComboBox = new JComboBox<>(new String[]{"Donor", "Patient"});
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        amountComboBox = new JComboBox<>(new String[]{"$5", "$7", "$0"});

        addFormField("Name:", nameField);
        addFormField("Father Name:", fatherNameField);
        addFormField("Mother Name:", motherNameField);
        addFormField("Email:", emailField);
        addFormField("Contact No:", contactNoField);
        addFormField("DOB (YYYY-MM-DD):", dobField);
        addFormField("User Type:", userTypeComboBox);
        addFormField("Gender:", genderComboBox);
        addFormField("Amount:", amountComboBox);
        addFormField("Address:", addressField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Add User");
        closeButton = new JButton("Close");
        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);

        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
            addButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Add icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }

        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buttonPanel);

        getContentPane().add(formPanel, BorderLayout.NORTH);

        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        userTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) userTypeComboBox.getSelectedItem();
                amountComboBox.setEnabled("Donor".equals(selectedType));
                if ("Patient".equals(selectedType)) {
                    amountComboBox.setSelectedItem(null); // Clear the selection for patients
                }
            }
        });

        // Initialize amountComboBox state based on default userTypeComboBox selection
        amountComboBox.setEnabled("Donor".equals(userTypeComboBox.getSelectedItem()));
    }

    private void addFormField(String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new BorderLayout(10, 10));
        fieldPanel.setOpaque(false);
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(150, 30));
        field.setPreferredSize(new Dimension(300, 30));

        fieldPanel.add(jLabel, BorderLayout.WEST);
        fieldPanel.add(field, BorderLayout.CENTER);

        formPanel.add(fieldPanel);
        formPanel.add(Box.createVerticalStrut(10));
    }

    private void addUser() {
        String selectedAmount = (String) amountComboBox.getSelectedItem();
        BigDecimal amount = BigDecimal.ZERO; // Default to zero if not applicable

        if (selectedAmount != null && !selectedAmount.isEmpty()) {
            try {
                // Remove the dollar sign and parse as BigDecimal
                amount = new BigDecimal(selectedAmount.replace("$", ""));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
        }

        // Show confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to add this user?",
            "Confirm Addition",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return; // If user clicks No, exit the method
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO user (Name, FatherName, MotherName, Email, ContactNo, DOB, UserType, Gender, Amount, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nameField.getText());
                stmt.setString(2, fatherNameField.getText());
                stmt.setString(3, motherNameField.getText());
                stmt.setString(4, emailField.getText());
                stmt.setString(5, contactNoField.getText());
                stmt.setString(6, dobField.getText());
                stmt.setString(7, (String) userTypeComboBox.getSelectedItem());
                stmt.setString(8, (String) genderComboBox.getSelectedItem());
                stmt.setBigDecimal(9, amount);
                stmt.setString(10, addressField.getText());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, "User added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "User addition failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddUserFrame().setVisible(true));
    }
}

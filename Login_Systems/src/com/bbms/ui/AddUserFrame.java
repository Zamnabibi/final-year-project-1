package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JPanel formPanel;
	private JLabel footerLabel;
	private JPanel footerPanel;

    public AddUserFrame() {
        setTitle("Add User");
        setSize(800, 697);
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
            System.out.println("Update icon not found.");
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

        String[] columnNames = {"UserId", "UserType", "Name", "FatherName", "MotherName", "DOB", "ContactNo", "Gender", "Email", "Amount", "Address"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);

        userTable.setBackground(Color.WHITE);
        userTable.setForeground(Color.BLACK);
        userTable.setGridColor(Color.BLACK);

        JScrollPane tableScrollPane = new JScrollPane(userTable);
        tableScrollPane.setBackground(Color.PINK);
        tableScrollPane.getViewport().setBackground(Color.PINK);

        JTableHeader tableHeader = userTable.getTableHeader();
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setForeground(Color.BLACK);

        getContentPane().add(tableScrollPane, BorderLayout.CENTER);
        
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
                if ("Patient".equals(selectedType)) {
                    amountComboBox.setEnabled(false);
                    amountComboBox.setSelectedItem(null); // Clear the selection for patients
                } else {
                    amountComboBox.setEnabled(true);
                }
            }
        });

        // Add a listener to enable/disable amountComboBox based on userTypeComboBox selection
        userTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String) userTypeComboBox.getSelectedItem();
                amountComboBox.setEnabled("Donor".equals(selectedType));
            }
        });

        // Initialize amountComboBox state based on default userTypeComboBox selection
        amountComboBox.setEnabled("Donor".equals(userTypeComboBox.getSelectedItem()));

        loadUserData();
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
        try {
            String userType = (String) userTypeComboBox.getSelectedItem();
            String name = nameField.getText().trim();
            String fatherName = fatherNameField.getText().trim();
            String motherName = motherNameField.getText().trim();
            String email = emailField.getText().trim();
            String contactNo = contactNoField.getText().trim();
            String dob = dobField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            int amount = 0; // Default amount for non-donors

            if ("Donor".equals(userType)) {
                String amountString = (String) amountComboBox.getSelectedItem();
                if (amountString != null && amountString.startsWith("$")) {
                    amount = Integer.parseInt(amountString.substring(1));
                }
            }

            String address = addressField.getText().trim();

            Connection connection = DatabaseConnection.getConnection();
            String query = "INSERT INTO User (UserType, Name, FatherName, MotherName, Email, ContactNo, DOB, Gender, Amount, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userType);
            statement.setString(2, name);
            statement.setString(3, fatherName);
            statement.setString(4, motherName);
            statement.setString(5, email);
            statement.setString(6, contactNo);
            statement.setString(7, dob);
            statement.setString(8, gender);
            statement.setInt(9, amount);
            statement.setString(10, address);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadUserData();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                Object[] row = new Object[]{
                    resultSet.getInt("UserId"),
                    resultSet.getString("UserType"),
                    resultSet.getString("Name"),
                    resultSet.getString("FatherName"),
                    resultSet.getString("MotherName"),
                    resultSet.getDate("DOB"),
                    resultSet.getString("ContactNo"),
                    resultSet.getString("Gender"),
                    resultSet.getString("Email"),
                    resultSet.getInt("Amount"),
                    resultSet.getString("Address")
                };
                tableModel.addRow(row);
            }

            userTable.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (value instanceof Integer) {
                        setText("$" + value);
                    }
                    return c;
                }
            });

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading user data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddUserFrame().setVisible(true));
    }
}

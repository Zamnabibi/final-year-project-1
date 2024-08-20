package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.HashMap;

@SuppressWarnings("serial")
public class AddDonorFrame extends JFrame {
    private JTextField bloodUnitField;
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> groupNameComboBox;
    private JComboBox<String> amountComboBox;
    private JButton addButton;
    private JButton closeButton;
    private JTable donorTable;
    private DefaultTableModel tableModel;

    // Map to store the relationship between BloodGroupId and GroupName
    private HashMap<String, String> bloodGroupMap = new HashMap<>();

    // Label to display name and contact number
    private JLabel nameLabel;
    private JLabel contactNoLabel;
    private JPanel footerPanel;
    private JLabel footerLabel;

    public AddDonorFrame() {
        setTitle("Add Donor");
        setSize(880, 700); // Increased height to accommodate new label
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PINK); // Set background color for the frame

        JPanel formPanel = new JPanel(new GridLayout(10, 2)); // Adjust grid layout to fit the new label
        formPanel.setBackground(Color.PINK); // Set background color for the form panel
        getContentPane().add(formPanel, BorderLayout.NORTH);

        // Form setup
        formPanel.add(new JLabel("UserId:"));
        userComboBox = new JComboBox<>();
        formPanel.add(userComboBox);

        formPanel.add(new JLabel("Name:"));
        nameLabel = new JLabel(" ");  // Name Label to display the name from User table
        formPanel.add(nameLabel);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("Amount:"));
        amountComboBox = new JComboBox<>();
        formPanel.add(amountComboBox);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        // Contact Number Label
        formPanel.add(new JLabel("Contact No:"));
        contactNoLabel = new JLabel(" ");
        formPanel.add(contactNoLabel);

        addButton = new JButton("Add Donor");
        formPanel.add(addButton);

        // Close Button
        closeButton = new JButton("Close");
        formPanel.add(closeButton);

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

        // Load ComboBoxes
        loadComboBoxes();

        // ComboBox Listeners
        userComboBox.addActionListener(e -> updateUserDetails());
        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());

        addButton.addActionListener(e -> addDonor());
        closeButton.addActionListener(e -> dispose()); // Dispose of the frame when clicked

        // Table setup
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"DonorId", "UserId", "Name", "BloodGroupId", "GroupName", "Amount", "BloodUnit", "ContactNo"});
        donorTable = new JTable(tableModel);

        // Set table and header background color
        donorTable.setBackground(Color.WHITE); // Set background color for the table
        donorTable.setForeground(Color.BLACK); // Optional: Set text color
        donorTable.setGridColor(Color.BLACK); // Optional: Set grid color
        JScrollPane tableScrollPane = new JScrollPane(donorTable);

        // Set viewport background color
        tableScrollPane.getViewport().setBackground(Color.PINK);

        getContentPane().add(tableScrollPane, BorderLayout.CENTER);

        JTableHeader tableHeader = donorTable.getTableHeader();
        tableHeader.setBackground(Color.white); // Set a different background color for the header
        tableHeader.setForeground(Color.BLACK); // Optional: Set header text color

        // Load Table Data
        loadTableData();

        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load User IDs where UserType is 'Donor'
            String query = "SELECT UserId FROM User WHERE UserType = 'Donor'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userComboBox.addItem(resultSet.getString("UserId"));
            }

            // Load Blood Group IDs and Group Names
            query = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String bloodGroupId = resultSet.getString("BloodGroupId");
                String groupName = resultSet.getString("GroupName");

                bloodGroupComboBox.addItem(bloodGroupId);
                groupNameComboBox.addItem(groupName);

                bloodGroupMap.put(bloodGroupId, groupName);
            }

            // Load Amounts from User table where UserType is 'Donor'
            query = "SELECT Amount FROM User WHERE UserType = 'Donor'";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BigDecimal amount = resultSet.getBigDecimal("Amount");
                if (amount != null) {
                    amountComboBox.addItem("$" + amount.toString());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load data into comboboxes.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to update both Name and Contact No labels based on selected UserId
    private void updateUserDetails() {
        String selectedUserId = (String) userComboBox.getSelectedItem();
        if (selectedUserId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT Name, ContactNo FROM User WHERE UserId = ? AND UserType = 'Donor'";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedUserId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String contactNo = resultSet.getString("ContactNo");
                    nameLabel.setText(name != null ? name : "No name available");
                    contactNoLabel.setText(contactNo != null ? contactNo : "No contact number available");
                } else {
                    nameLabel.setText("No name available");
                    contactNoLabel.setText("No contact number available");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to fetch user details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateGroupName() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        if (selectedBloodGroupId != null) {
            String groupName = bloodGroupMap.get(selectedBloodGroupId);
            groupNameComboBox.setSelectedItem(groupName);
        }
    }

    private void updateBloodGroupId() {
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
        if (selectedGroupName != null) {
            for (String bloodGroupId : bloodGroupMap.keySet()) {
                if (bloodGroupMap.get(bloodGroupId).equals(selectedGroupName)) {
                    bloodGroupComboBox.setSelectedItem(bloodGroupId);
                    break;
                }
            }
        }
    }

    private void addDonor() {
        String selectedUserId = (String) userComboBox.getSelectedItem();
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
        String selectedAmount = (String) amountComboBox.getSelectedItem();
        String bloodUnit = bloodUnitField.getText();
        String contactNo = contactNoLabel.getText();
        String name = nameLabel.getText();

        if (selectedUserId == null || selectedBloodGroupId == null || selectedGroupName == null ||
            selectedAmount == null || bloodUnit.isEmpty() || contactNo.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Donor (UserId, Name, BloodGroupId, GroupName, Amount, BloodUnit, ContactNo) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedUserId);
            statement.setString(2, name);
            statement.setString(3, selectedBloodGroupId);
            statement.setString(4, selectedGroupName);
            statement.setBigDecimal(5, new BigDecimal(selectedAmount.replace("$", ""))); // Remove $ symbol before converting to BigDecimal
            statement.setInt(6, Integer.parseInt(bloodUnit));
            statement.setString(7, contactNo);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Donor added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadTableData(); // Reload the table data to reflect the new donor
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add donor.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred while adding donor.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Blood Unit.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
        	String query = "SELECT DonorId, UserId,Name, BloodGroupId, GroupName, Amount, BloodUnit, ContactNo FROM Donor";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            tableModel.setRowCount(0); // Clear existing data
            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getString("DonorId"),
                    resultSet.getString("UserId"),
                    resultSet.getString("Name"),
                    resultSet.getString("BloodGroupId"),
                    resultSet.getString("GroupName"),
                    resultSet.getBigDecimal("Amount"),
                    resultSet.getInt("BloodUnit"),
                    resultSet.getString("ContactNo")
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load table data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddDonorFrame frame = new AddDonorFrame();
            frame.setVisible(true);
        });
    }
}

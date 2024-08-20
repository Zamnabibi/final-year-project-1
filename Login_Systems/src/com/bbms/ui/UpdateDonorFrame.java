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
import java.util.Map;

@SuppressWarnings("serial")
public class UpdateDonorFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> amountComboBox;
    private JComboBox<String> groupNameComboBox;
    private JTextField contactNoField;
    private JLabel nameLabel;
    
    private JButton updateButton;
    private JButton closeButton;
    private JTable donorTable;
    private DefaultTableModel tableModel;
    private Map<String, String> bloodGroupMap = new HashMap<>();
    private Map<String, String> groupNameMap = new HashMap<>();
    private JPanel footerPanel;
    private Component footerLabel;
    private JTextField textField;

    public UpdateDonorFrame() {
        setTitle("Update Donor");
        setSize(800, 586);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null); // Set layout to null for absolute positioning

        // Set the background color of the content pane to pink
        getContentPane().setBackground(Color.PINK);

        // Create and configure form panel
        JPanel formPanel = new JPanel(null); // Use absolute layout for form panel
        formPanel.setBackground(Color.PINK);
        formPanel.setBounds(10, 10, 760, 293); // Set bounds for the form panel
        getContentPane().add(formPanel);

        // Form setup
        JLabel userLabel = new JLabel("UserId:");
        userLabel.setBounds(167, 10, 100, 25);
        formPanel.add(userLabel);

        userComboBox = new JComboBox<>();
        userComboBox.setBounds(356, 10, 150, 25);
        formPanel.add(userComboBox);

        JLabel nameLabelLabel = new JLabel("Name:");
        nameLabelLabel.setBounds(167, 45, 100, 25);
        formPanel.add(nameLabelLabel);

        nameLabel = new JLabel();
        nameLabel.setBounds(356, 45, 150, 25);
        formPanel.add(nameLabel);

        JLabel bloodGroupLabel = new JLabel("BloodGroupId:");
        bloodGroupLabel.setBounds(167, 81, 100, 25);
        formPanel.add(bloodGroupLabel);

        bloodGroupComboBox = new JComboBox<>();
        bloodGroupComboBox.setBounds(356, 81, 150, 25);
        formPanel.add(bloodGroupComboBox);

        JLabel groupNameLabel = new JLabel("GroupName:");
        groupNameLabel.setBounds(167, 115, 100, 25);
        formPanel.add(groupNameLabel);

        groupNameComboBox = new JComboBox<>();
        groupNameComboBox.setBounds(356, 115, 150, 25);
        formPanel.add(groupNameComboBox);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(167, 150, 100, 25);
        formPanel.add(amountLabel);

        amountComboBox = new JComboBox<>();
        amountComboBox.setBounds(356, 150, 150, 25);
        formPanel.add(amountComboBox);

        JLabel contactNoLabel = new JLabel("ContactNo:");
        contactNoLabel.setBounds(167, 186, 100, 25);
        formPanel.add(contactNoLabel);

        contactNoField = new JTextField();
        contactNoField.setBounds(356, 186, 150, 25);
        formPanel.add(contactNoField);
        closeButton = new JButton("Close");
        closeButton.setBounds(522, 252, 150, 30);

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }
        formPanel.add(closeButton);
        
                // Initialize buttons
                updateButton = new JButton("Update Donor");
                updateButton.setBounds(230, 252, 150, 30);
                // Load icons
                try {
                    Image imgUpdate = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
                    updateButton.setIcon(new ImageIcon(imgUpdate));
                } catch (Exception e) {
                    System.out.println("Update icon not found.");
                }
                formPanel.add(updateButton);
               
                
                JLabel lblBloodunit = new JLabel("BloodUnit:");
                lblBloodunit.setBounds(167, 222, 100, 25);
                formPanel.add(lblBloodunit);
                
                textField = new JTextField();
                textField.setBounds(356, 222, 150, 25);
                formPanel.add(textField);
                updateButton.addActionListener(e -> updateDonor());

        // Initialize table and load data
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"DonorId", "UserId", "Name", "BloodGroupId", "GroupName", "Amount", "ContactNo", "BloodUnit"});
        donorTable = new JTable(tableModel);

        donorTable.setBackground(Color.WHITE);
        donorTable.setSelectionBackground(Color.LIGHT_GRAY);

        JTableHeader tableHeader = donorTable.getTableHeader();
        tableHeader.setBackground(Color.LIGHT_GRAY);

        JScrollPane tableScrollPane = new JScrollPane(donorTable);
        tableScrollPane.setBounds(54, 314, 694, 167); // Adjust bounds for table scroll pane
        getContentPane().add(tableScrollPane);

        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);
        footerPanel.setBounds(10, 492, 800, 55); // Set bounds for footer panel

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.NORTH);
        getContentPane().add(footerPanel);

        // Load data into the table and combo boxes
        loadComboBoxes();
        loadTableData();

        // Add action listeners
        userComboBox.addActionListener(e -> updateAmountField());
        userComboBox.addActionListener(e -> updateNameLabel());
        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());
        closeButton.addActionListener(e -> dispose());

        donorTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedDonor();
            }
        });
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT UserId FROM User WHERE UserType = 'Donor'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userComboBox.addItem(resultSet.getString("UserId"));
            }

            query = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String bloodGroupId = resultSet.getString("BloodGroupId");
                String groupName = resultSet.getString("GroupName");

                bloodGroupComboBox.addItem(bloodGroupId);
                groupNameComboBox.addItem(groupName);
                bloodGroupMap.put(bloodGroupId, groupName);
                groupNameMap.put(groupName, bloodGroupId);
            }

            // Load unique amounts from the Donor table
            query = "SELECT DISTINCT Amount FROM Donor WHERE Amount IS NOT NULL";
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
        }
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT DonorId, UserId, Name, BloodGroupId, GroupName, Amount, ContactNo, BloodUnit FROM Donor";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("DonorId"),
                    resultSet.getString("UserId"),
                    resultSet.getString("Name"),
                    resultSet.getString("BloodGroupId"),
                    resultSet.getString("GroupName"),
                    resultSet.getBigDecimal("Amount") != null ? "$" + resultSet.getBigDecimal("Amount").toPlainString() : "",
                    resultSet.getString("ContactNo"),
                    resultSet.getString("BloodUnit"), 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            String bloodGroupId = groupNameMap.get(selectedGroupName);
            bloodGroupComboBox.setSelectedItem(bloodGroupId);
        }
    }

    private void updateAmountField() {
        String selectedUserId = (String) userComboBox.getSelectedItem();
        if (selectedUserId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT Amount FROM Donor WHERE UserId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedUserId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    BigDecimal amount = resultSet.getBigDecimal("Amount");
                    if (amount != null) {
                        amountComboBox.setSelectedItem("$" + amount.toPlainString());
                    } else {
                        amountComboBox.setSelectedIndex(-1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateNameLabel() {
        String selectedUserId = (String) userComboBox.getSelectedItem();
        if (selectedUserId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT Name FROM User WHERE UserId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedUserId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    nameLabel.setText(resultSet.getString("Name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    


    private void updateDonor() {
        String selectedUserId = (String) userComboBox.getSelectedItem();
        if (selectedUserId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String amountText = (String) amountComboBox.getSelectedItem();
                BigDecimal amount = amountText != null ? new BigDecimal(amountText.replace("$", "")) : null;
                String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
                String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
                String contactNo = contactNoField.getText();
                String bloodUnit = textField.getText();

                String query = "UPDATE Donor SET BloodGroupId = ?, GroupName = ?, Amount = ?, ContactNo = ?, BloodUnit = ? WHERE UserId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedBloodGroupId);
                statement.setString(2, selectedGroupName);
                statement.setBigDecimal(3, amount);
                statement.setString(4, contactNo);
                statement.setString(5, bloodUnit);
                statement.setString(6, selectedUserId);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Donor updated successfully!");
                    loadTableData(); // Refresh the table data
                } else {
                    JOptionPane.showMessageDialog(this, "Donor update failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSelectedDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String userId = (String) donorTable.getValueAt(selectedRow, 1);
            userComboBox.setSelectedItem(userId);
            nameLabel.setText((String) donorTable.getValueAt(selectedRow, 2));
            bloodGroupComboBox.setSelectedItem(donorTable.getValueAt(selectedRow, 3));
            groupNameComboBox.setSelectedItem(donorTable.getValueAt(selectedRow, 4));
            amountComboBox.setSelectedItem(donorTable.getValueAt(selectedRow, 5));
            contactNoField.setText((String) donorTable.getValueAt(selectedRow, 6));
            textField.setText((String) donorTable.getValueAt(selectedRow,7));
            
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdateDonorFrame frame = new UpdateDonorFrame();
            frame.setVisible(true);
        });
    }
}

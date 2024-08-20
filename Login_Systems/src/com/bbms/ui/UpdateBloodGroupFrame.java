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
public class UpdateBloodGroupFrame extends JFrame {
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JTextField groupNameField;
    private JButton updateButton, closeButton;
    private JTable bloodGroupTable;
    private DefaultTableModel tableModel;
    private JPanel footerPanel;
    private JLabel footerLabel;

    public UpdateBloodGroupFrame() {
        setTitle("Update Blood Group");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(Color.PINK);

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.setOpaque(false);

        formPanel.add(new JLabel("Blood Group ID:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("Group Name:"));
        groupNameField = new JTextField();
        formPanel.add(groupNameField);

        // Initialize buttons
        updateButton = new JButton("Update Blood Group");
        closeButton = new JButton("Close");

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

        // Add buttons to form panel
        formPanel.add(updateButton);
        formPanel.add(closeButton);

        add(formPanel, BorderLayout.NORTH);

        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        // Table setup
        tableModel = new DefaultTableModel();
        bloodGroupTable = new JTable(tableModel);
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName");
        loadBloodGroupData();
        add(new JScrollPane(bloodGroupTable), BorderLayout.CENTER);

        // Load BloodGroup IDs
        loadBloodGroupIds();

        // Event Listeners
        bloodGroupIdComboBox.addActionListener(e -> loadGroupName());
        updateButton.addActionListener(e -> updateBloodGroup());
        closeButton.addActionListener(e -> dispose());
        bloodGroupTable.getSelectionModel().addListSelectionListener(e -> fillFormFromTable());

        setVisible(true);
    }

    private void loadBloodGroupIds() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
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
                    groupNameField.setText(resultSet.getString("GroupName"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    private void fillFormFromTable() {
        int selectedRow = bloodGroupTable.getSelectedRow();
        if (selectedRow >= 0) {
            Integer bloodGroupId = (Integer) tableModel.getValueAt(selectedRow, 0);
            String groupName = (String) tableModel.getValueAt(selectedRow, 1);

            bloodGroupIdComboBox.setSelectedItem(bloodGroupId);
            groupNameField.setText(groupName);
        }
    }

    private void updateBloodGroup() {
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = groupNameField.getText();

        if (bloodGroupId == null || groupName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a blood group ID and enter a group name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE BloodGroup SET GroupName = ? WHERE BloodGroupId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, groupName);
            statement.setInt(2, bloodGroupId);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood group updated successfully.");
            loadBloodGroupData(); // Refresh table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating blood group.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UpdateBloodGroupFrame::new);
    }
}

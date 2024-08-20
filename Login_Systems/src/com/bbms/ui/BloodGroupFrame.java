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
public class BloodGroupFrame extends JFrame {
    private JTable bloodGroupTable;
    private DefaultTableModel tableModel;
    private JTextField groupNameField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public BloodGroupFrame() {
        setTitle("Blood Group Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        bloodGroupTable = new JTable(tableModel);
        add(new JScrollPane(bloodGroupTable), BorderLayout.CENTER);

        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2));
        formPanel.add(new JLabel("GroupName:"));
        groupNameField = new JTextField();
        formPanel.add(groupNameField);

        add(formPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Load Data
        loadBloodGroupData();

        // Add Action Listeners
        addButton.addActionListener(e -> addBloodGroup());
        updateButton.addActionListener(e -> updateBloodGroup());
        deleteButton.addActionListener(e -> deleteBloodGroup());

        // Table Selection Listener
        bloodGroupTable.getSelectionModel().addListSelectionListener(e -> loadSelectedBloodGroup());
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
        }
    }

    private void loadSelectedBloodGroup() {
        int selectedRow = bloodGroupTable.getSelectedRow();
        if (selectedRow != -1) {
            groupNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
        }
    }

    private void addBloodGroup() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO BloodGroup (GroupName) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, groupNameField.getText());

            statement.executeUpdate();
            loadBloodGroupData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBloodGroup() {
        int selectedRow = bloodGroupTable.getSelectedRow();
        if (selectedRow != -1) {
            int bloodGroupId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE BloodGroup SET GroupName = ? WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, groupNameField.getText());
                statement.setInt(2, bloodGroupId);

                statement.executeUpdate();
                loadBloodGroupData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteBloodGroup() {
        int selectedRow = bloodGroupTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bloodGroupId = (Integer) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Retrieve data from the BloodGroup table
            String selectQuery = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, bloodGroupId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                String groupName = resultSet.getString("GroupName");

                // Insert data into the History table
                String insertQuery = "INSERT INTO History (BloodGroupId, GroupName, DeletedAt) VALUES (?, ?, NOW())";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, bloodGroupId);
                insertStatement.setString(2, groupName);
                insertStatement.executeUpdate();

                // Delete the record from the BloodGroup table
                String deleteQuery = "DELETE FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, bloodGroupId);
                deleteStatement.executeUpdate();

                // Refresh the table
                loadBloodGroupData();
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BloodGroupFrame frame = new BloodGroupFrame();
            frame.setVisible(true);
        });
    }
}

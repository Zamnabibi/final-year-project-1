package com.bbms.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class UpdateBloodStockFrame extends JFrame {
    private JTextField stockIdField;
    private JTextField bloodUnitField;
    private JComboBox<String> bloodGroupComboBox;
    private JButton updateButton;
    private JButton closeButton;
    private JLabel groupNameLabel;
    private JTable bloodStockTable;
    private DefaultTableModel tableModel;

    public UpdateBloodStockFrame() {
        setTitle("Update Blood Stock");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2));
        formPanel.setBackground(Color.PINK);

        formPanel.add(new JLabel("StockId:"));
        stockIdField = new JTextField();
        formPanel.add(stockIdField);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:")); // Label for GroupName
        groupNameLabel = new JLabel(""); // Initially empty
        formPanel.add(groupNameLabel);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        updateButton = new JButton("Update");
        formPanel.add(updateButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);

        add(formPanel, BorderLayout.NORTH);

        // Table for displaying blood stock
        String[] columnNames = {"StockId", "BloodGroupId", "GroupName", "BloodUnit"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bloodStockTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bloodStockTable);
        add(scrollPane, BorderLayout.CENTER);

        updateButton.addActionListener(e -> updateBloodStock());
        closeButton.addActionListener(e -> dispose());

        bloodGroupComboBox.addActionListener(e -> updateGroupNameLabel());

        bloodStockTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = bloodStockTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int stockId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    String bloodGroupId = (String) tableModel.getValueAt(selectedRow, 1);
                    String groupName = (String) tableModel.getValueAt(selectedRow, 2);
                    int bloodUnit = (Integer) tableModel.getValueAt(selectedRow, 3);

                    stockIdField.setText(String.valueOf(stockId));
                    bloodGroupComboBox.setSelectedItem(bloodGroupId);
                    groupNameLabel.setText(groupName);
                    bloodUnitField.setText(String.valueOf(bloodUnit));
                }
            }
        });

        loadComboBoxes();
        loadBloodStockData();
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bloodGroupComboBox.addItem(resultSet.getString("BloodGroupId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGroupNameLabel() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();

        if (selectedBloodGroupId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, selectedBloodGroupId); // Use setString for String parameter
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String groupName = resultSet.getString("GroupName");
                    groupNameLabel.setText(groupName);
                } else {
                    groupNameLabel.setText("GroupName not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                groupNameLabel.setText("Error retrieving GroupName");
            }
        }
    }

    private void updateBloodStock() {
        String stockIdText = stockIdField.getText();
        String bloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String bloodUnitText = bloodUnitField.getText();
        String groupName = groupNameLabel.getText(); // Get the GroupName from JLabel

        if (stockIdText.isEmpty() || bloodGroupId == null || bloodUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        int stockId;
        int bloodUnit;
        try {
            stockId = Integer.parseInt(stockIdText);
            bloodUnit = Integer.parseInt(bloodUnitText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "StockId and BloodUnit must be numbers.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Update BloodStock without GroupName
            String query = "UPDATE BloodStock SET BloodGroupId = ?, GroupName = ?, BloodUnit = ? WHERE StockId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, bloodGroupId); // Use setString for String parameter
            statement.setString(2, groupName);
            statement.setInt(3, bloodUnit);
            statement.setInt(4, stockId);
            statement.executeUpdate();
            
            // Optionally update BloodGroup if needed (but typically not required for BloodStock update)
            // Ensure this aligns with your database design
            // String groupNameUpdateQuery = "UPDATE BloodGroup SET GroupName = ? WHERE BloodGroupId = ?";
            // PreparedStatement groupNameStatement = connection.prepareStatement(groupNameUpdateQuery);
            // groupNameStatement.setString(1, groupName);
            // groupNameStatement.setString(2, bloodGroupId);
            // groupNameStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Blood stock updated successfully.");
            loadBloodStockData(); // Refresh table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating blood stock.");
        }
    }


    private void loadBloodStockData() {
        // Clear the table
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT bs.StockId, bs.BloodGroupId, bg.GroupName, bs.BloodUnit " +
                           "FROM BloodStock bs " +
                           "JOIN BloodGroup bg ON bs.BloodGroupId = bg.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int stockId = resultSet.getInt("StockId");
                String bloodGroupId = resultSet.getString("BloodGroupId");
                String groupName = resultSet.getString("GroupName");
                int bloodUnit = resultSet.getInt("BloodUnit");

                tableModel.addRow(new Object[]{stockId, bloodGroupId, groupName, bloodUnit});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood stock data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateBloodStockFrame().setVisible(true));
    }
}

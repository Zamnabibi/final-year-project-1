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
public class DeleteBloodStockFrame extends JFrame {
    private JTextField stockIdField;
    private JButton deleteButton;
    private JButton closeButton;
    private JTable bloodStockTable;
    private DefaultTableModel tableModel;

    public DeleteBloodStockFrame() {
        setTitle("Delete Blood Stock");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color for the main content pane
        getContentPane().setBackground(Color.PINK);

        // Panel for the form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2));
        formPanel.setBackground(Color.PINK);  // Set the background color for the form panel

        formPanel.add(new JLabel("StockId:"));
        stockIdField = new JTextField();
        formPanel.add(stockIdField);

        deleteButton = new JButton("Delete");
        formPanel.add(deleteButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);

        add(formPanel, BorderLayout.NORTH);

        // Table for displaying blood stock
        String[] columnNames = {"StockId", "BloodGroupId", "BloodUnit"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bloodStockTable = new JTable(tableModel);
        bloodStockTable.setBackground(Color.PINK); // Set background color for the table
        bloodStockTable.setForeground(Color.BLACK); // Set text color for the table

        JScrollPane scrollPane = new JScrollPane(bloodStockTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button actions
        deleteButton.addActionListener(e -> deleteBloodStock());
        closeButton.addActionListener(e -> dispose());

        // Load initial data into the table
        loadBloodStockData();
    }

    private void deleteBloodStock() {
        String stockIdText = stockIdField.getText();

        if (stockIdText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a StockId.");
            return;
        }

        int stockId;
        try {
            stockId = Integer.parseInt(stockIdText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "StockId must be a number.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Retrieve data from the BloodStock table
            String selectQuery = "SELECT BloodGroupId, BloodUnit FROM BloodStock WHERE StockId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, stockId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Blood stock not found.");
                return;
            }

            int bloodGroupId = resultSet.getInt("BloodGroupId");
            int bloodUnit = resultSet.getInt("BloodUnit");

            // Move data to the History table
            String historyQuery = "INSERT INTO History (UserId, UserType, BloodGroupId, BloodUnit, Action, DeletedAt) VALUES (?, 'BloodStock', ?, ?, 'DELETE', NOW())";
            PreparedStatement historyStatement = connection.prepareStatement(historyQuery);
            historyStatement.setInt(1, stockId);
            historyStatement.setInt(2, bloodGroupId);
            historyStatement.setInt(3, bloodUnit);
            historyStatement.executeUpdate();

            // Delete from the BloodStock table
            String deleteQuery = "DELETE FROM BloodStock WHERE StockId = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, stockId);
            deleteStatement.executeUpdate();

            JOptionPane.showMessageDialog(this, "Blood stock deleted successfully.");
            loadBloodStockData(); // Refresh table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting blood stock.");
        }
    }

    private void loadBloodStockData() {
        // Clear the table
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT StockId, BloodGroupId, BloodUnit FROM BloodStock";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int stockId = resultSet.getInt("StockId");
                int bloodGroupId = resultSet.getInt("BloodGroupId");
                int bloodUnit = resultSet.getInt("BloodUnit");

                tableModel.addRow(new Object[]{stockId, bloodGroupId, bloodUnit});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood stock data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DeleteBloodStockFrame().setVisible(true));
    }
}

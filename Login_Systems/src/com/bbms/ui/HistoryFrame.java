package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class HistoryFrame extends JFrame {
    private JTable historyTable;
    private DefaultTableModel tableModel;

    public HistoryFrame() {
        setTitle("History Management");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background color to pink
        getContentPane().setBackground(Color.PINK);

        // Table setup
        tableModel = new DefaultTableModel();
        historyTable = new JTable(tableModel);
        historyTable.setBackground(Color.white); // Set table background to white
        add(new JScrollPane(historyTable), BorderLayout.CENTER);

        // Footer setup
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.PINK); // Set footer panel background to pink
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System");
        footerPanel.add(footerLabel);
        add(footerPanel, BorderLayout.SOUTH);

        // Button panel setup
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.PINK); // Set button panel background to pink
        add(buttonPanel, BorderLayout.NORTH);

        JButton deleteButton = new JButton("Delete");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        // Add action listeners
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedRow();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });

        // Load initial data
        loadHistoryData();
    }

    private void loadHistoryData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM history";
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows
            tableModel.setColumnCount(0); // Clear existing columns

            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(resultSet.getMetaData().getColumnName(i));
            }

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = historyTable.getSelectedRow();
        if (selectedRow != -1) {
            // Get the primary key (or unique identifier) of the selected row
            int id = (Integer) historyTable.getValueAt(selectedRow, 0); // Assuming the first column is the ID

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Delete the row from the database
                String query = "DELETE FROM history WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Remove the row from the table
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting record", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HistoryFrame frame = new HistoryFrame();
            frame.setVisible(true);
        });
    }
}

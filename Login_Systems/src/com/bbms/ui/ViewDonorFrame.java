package com.bbms.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.bbms.util.DatabaseConnection;

public class ViewDonorFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable donorTable;
    private DefaultTableModel tableModel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
            null,
            "Do you want to view the Donor details?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> new ViewDonorFrame().setVisible(true));
        }
    }
       
    

    /**
     * Create the frame.
     */
    public ViewDonorFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        // Initialize table and load data
        initializeTable();
        loadTableData();

        // Add footer panel
        addFooter();
    }

    private void initializeTable() {
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

        contentPane.add(tableScrollPane, BorderLayout.CENTER);

        JTableHeader tableHeader = donorTable.getTableHeader();
        tableHeader.setBackground(Color.WHITE); // Set a different background color for the header
        tableHeader.setForeground(Color.BLACK); // Optional: Set header text color
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Query to join Donor, User, and BloodGroup tables
            String query = "SELECT d.DonorId, d.UserId, u.Name, d.BloodGroupId, bg.GroupName, u.Amount, d.BloodUnit, u.ContactNo " +
                           "FROM Donor d " +
                           "JOIN User u ON d.UserId = u.UserId " +
                           "JOIN BloodGroup bg ON d.BloodGroupId = bg.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            
            tableModel.setRowCount(0); // Clear existing data
            
            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getString("DonorId"),
                    resultSet.getString("UserId"),
                    resultSet.getString("Name"),            // Name from User table
                    resultSet.getString("BloodGroupId"),
                    resultSet.getString("GroupName"),       // GroupName from BloodGroup table
                    resultSet.getBigDecimal("Amount") != null ? "$" + resultSet.getBigDecimal("Amount").toPlainString() : "",
                    resultSet.getInt("BloodUnit"),
                    resultSet.getString("ContactNo")        // ContactNo from User table
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load table data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFooter() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.PINK);
        footerPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); // Add padding
        footerPanel.setLayout(new BorderLayout());

        // Create and add footer label
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.");
        footerLabel.setHorizontalAlignment(JLabel.CENTER);
        footerPanel.add(footerLabel, BorderLayout.CENTER);

        contentPane.add(footerPanel, BorderLayout.SOUTH);
    }
}

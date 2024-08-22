package com.bbms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.bbms.util.DatabaseConnection;

public class ViewBloodGroupFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private DefaultTableModel tableModel;
    private JTable bloodGroupTable;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ViewBloodGroupFrame frame = new ViewBloodGroupFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ViewBloodGroupFrame() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0)); // Set layout to BorderLayout

        setContentPane(contentPane);

        // Initialize table and table model
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"BloodGroupId", "GroupName"});
        bloodGroupTable = new JTable(tableModel);

        // Set table and header background color
        bloodGroupTable.setBackground(Color.WHITE); // Set background color for the table
        bloodGroupTable.setForeground(Color.BLACK); // Optional: Set text color
        bloodGroupTable.setGridColor(Color.BLACK); // Optional: Set grid color
        JScrollPane tableScrollPane = new JScrollPane(bloodGroupTable);

        // Set viewport background color
        tableScrollPane.getViewport().setBackground(Color.PINK);

        contentPane.add(tableScrollPane, BorderLayout.CENTER);

        JTableHeader tableHeader = bloodGroupTable.getTableHeader();
        tableHeader.setBackground(Color.WHITE); // Set a different background color for the header
        tableHeader.setForeground(Color.BLACK); // Optional: Set header text color

        loadBloodGroupData(); // Load data into the table

        // Add footer
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.");
        footerLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(footerLabel, BorderLayout.SOUTH);
    }

    // Method to load blood group data from the database
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
}

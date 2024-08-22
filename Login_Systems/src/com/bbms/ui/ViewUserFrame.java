package com.bbms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.bbms.util.DatabaseConnection;

public class ViewUserFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable userTable;
    private DefaultTableModel tableModel;
	private JPanel footerPanel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        // Show confirmation dialog
        int response = JOptionPane.showConfirmDialog(
            null,
            "Do you want to view the User details?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION
        );

        if (response == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> new ViewUserFrame().setVisible(true));
        }
    }

    /**
     * Create the frame.
     */
    public ViewUserFrame() {
        setTitle("View Users");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        // Initialize table model and JTable
        tableModel = new DefaultTableModel();
        userTable = new JTable(tableModel);
        
        // Define table columns
        tableModel.addColumn("UserId");
        tableModel.addColumn("UserType");
        tableModel.addColumn("Name");
        tableModel.addColumn("FatherName");
        tableModel.addColumn("MotherName");
        tableModel.addColumn("Email");
        tableModel.addColumn("ContactNo");
        tableModel.addColumn("DOB");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Address");

        // Set the background color of the table
        userTable.setBackground(Color.WHITE); // Set background color for the table
        userTable.setForeground(Color.BLACK); // Optional: Set text color
        userTable.setGridColor(Color.BLACK); // Optional: Set grid color
        
        // Set header background color
        JTableHeader tableHeader = userTable.getTableHeader();
        tableHeader.setBackground(Color.WHITE); // Set background color for the header
        tableHeader.setForeground(Color.BLACK); // Optional: Set header text color

        // Add table to JScrollPane and add to content pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.getViewport().setBackground(Color.PINK); // Set viewport background color
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Load user data into table
        loadUserData();
        createFooter();
    }

    private void loadUserData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT UserId, UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email, Amount, Address FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear existing rows
            tableModel.setRowCount(0);

            // Populate table with user data
            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("UserId"),
                    resultSet.getString("UserType"),
                    resultSet.getString("Name"),
                    resultSet.getString("FatherName"),
                    resultSet.getString("MotherName"),
                    resultSet.getString("Email"),
                    resultSet.getString("ContactNo"),
                    resultSet.getDate("DOB"),
                    resultSet.getString("Gender"),
                    resultSet.getBigDecimal("Amount") != null ? "$" + resultSet.getBigDecimal("Amount").toPlainString() : "",
                    resultSet.getString("Address")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void createFooter() {
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK); // Set footer background color to pink

        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        // Add footer panel to the SOUTH position of BorderLayout
        add(footerPanel, BorderLayout.SOUTH);
    }
}

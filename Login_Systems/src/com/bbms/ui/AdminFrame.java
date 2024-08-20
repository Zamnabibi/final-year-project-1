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
public class AdminFrame extends JFrame {
    private JTable adminTable;
    private DefaultTableModel tableModel;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> typeComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    
    public AdminFrame() {
        setTitle("Admin Table");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Table setup
        tableModel = new DefaultTableModel();
        adminTable = new JTable(tableModel);
        add(new JScrollPane(adminTable), BorderLayout.CENTER);

        tableModel.addColumn("AdminId");
        tableModel.addColumn("UserName");
        tableModel.addColumn("Password");
        tableModel.addColumn("Email");
        tableModel.addColumn("FullName");
        tableModel.addColumn("CreatedAt");
        tableModel.addColumn("UpdatedAt");
        tableModel.addColumn("Status");
        tableModel.addColumn("Type");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.add(new JLabel("UserName:"));
        userNameField = new JTextField();
        formPanel.add(userNameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("FullName:"));
        fullNameField = new JTextField();
        formPanel.add(fullNameField);

        formPanel.add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"Accepted", "Rejected", "Pending"});
        formPanel.add(statusComboBox);

        formPanel.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"Donor", "Patient", "Admin"});
        formPanel.add(typeComboBox);
        
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
        loadAdminData();

        // Add Action Listeners
        addButton.addActionListener(e -> addAdmin());
        updateButton.addActionListener(e -> updateAdmin());
        deleteButton.addActionListener(e -> deleteAdmin());
        
        // Table Selection Listener
        adminTable.getSelectionModel().addListSelectionListener(e -> loadSelectedAdmin());
    }

    private void loadAdminData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Admin";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("AdminId"),
                        resultSet.getString("UserName"),
                        resultSet.getString("Password"),
                        resultSet.getString("Email"),
                        resultSet.getString("FullName"),
                        resultSet.getTimestamp("CreatedAt"),
                        resultSet.getTimestamp("UpdatedAt"),
                        resultSet.getString("Status"),
                        resultSet.getString("Type")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedAdmin() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1) {
            userNameField.setText((String) tableModel.getValueAt(selectedRow, 1));
            passwordField.setText((String) tableModel.getValueAt(selectedRow, 2));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 3));
            fullNameField.setText((String) tableModel.getValueAt(selectedRow, 4));
            statusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 7));
            typeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 8));
        }
    }

    private void addAdmin() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Admin (UserName, Password, Email, FullName, Status, Type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userNameField.getText());
            statement.setString(2, new String(passwordField.getPassword()));
            statement.setString(3, emailField.getText());
            statement.setString(4, fullNameField.getText());
            statement.setString(5, (String) statusComboBox.getSelectedItem());
            statement.setString(6, (String) typeComboBox.getSelectedItem());

            statement.executeUpdate();
            loadAdminData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAdmin() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1) {
            int adminId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE Admin SET UserName = ?, Password = ?, Email = ?, FullName = ?, Status = ?, Type = ? WHERE AdminId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, userNameField.getText());
                statement.setString(2, new String(passwordField.getPassword()));
                statement.setString(3, emailField.getText());
                statement.setString(4, fullNameField.getText());
                statement.setString(5, (String) statusComboBox.getSelectedItem());
                statement.setString(6, (String) typeComboBox.getSelectedItem());
                statement.setInt(7, adminId);

                statement.executeUpdate();
                loadAdminData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteAdmin() {
        int selectedRow = adminTable.getSelectedRow();
        if (selectedRow != -1) {
            int adminId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "DELETE FROM Admin WHERE AdminId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, adminId);

                statement.executeUpdate();
                loadAdminData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminFrame frame = new AdminFrame();
            frame.setVisible(true);
        });
    }
}

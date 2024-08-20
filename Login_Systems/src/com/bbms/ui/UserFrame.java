package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import java.sql.Date;
@SuppressWarnings("serial")
public class UserFrame extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField nameField;
    private JTextField fatherNameField;
    private JTextField motherNameField;
    private JTextField emailField;
    private JTextField contactNoField;
    private JTextField dobField;
    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> genderComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public UserFrame() {
        setTitle("User Management");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER); // Corrected to BorderLayout.CENTER

        tableModel.addColumn("UserId");
        tableModel.addColumn("UserType");
        tableModel.addColumn("Name");
        tableModel.addColumn("FatherName");
        tableModel.addColumn("MotherName");
        tableModel.addColumn("DOB");
        tableModel.addColumn("ContactNo");
        tableModel.addColumn("Gender");
        tableModel.addColumn("Email");
        tableModel.addColumn("CreatedAt");
        tableModel.addColumn("UpdatedAt");

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Define preferred sizes
        Dimension textFieldSize = new Dimension(200, 30);
        Dimension labelSize = new Dimension(150, 30);

        nameField = new JTextField();
        fatherNameField = new JTextField();
        motherNameField = new JTextField();
        emailField = new JTextField();
        contactNoField = new JTextField();
        dobField = new JTextField();
        
        nameField.setPreferredSize(textFieldSize);
        fatherNameField.setPreferredSize(textFieldSize);
        motherNameField.setPreferredSize(textFieldSize);
        emailField.setPreferredSize(textFieldSize);
        contactNoField.setPreferredSize(textFieldSize);
        dobField.setPreferredSize(textFieldSize);

        // Update the form field method to handle preferred sizes
        addFormField(formPanel, gbc, "Name:", nameField, 0, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "FatherName:", fatherNameField, 1, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "MotherName:", motherNameField, 2, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "Email:", emailField, 3, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "ContactNo:", contactNoField, 4, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "DOB (YYYY-MM-DD):", dobField, 5, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "UserType:", userTypeComboBox = new JComboBox<>(new String[]{"Donor", "Patient"}), 6, labelSize, textFieldSize);
        addFormField(formPanel, gbc, "Gender:", genderComboBox = new JComboBox<>(new String[]{"Male", "Female"}), 7, labelSize, textFieldSize);

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
        loadUserData();

        // Add Action Listeners
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());

        // Table Selection Listener
        userTable.getSelectionModel().addListSelectionListener(e -> loadSelectedUser());
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y, Dimension labelSize, Dimension fieldSize) {
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(labelSize);
        field.setPreferredSize(fieldSize);

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(jLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = y;
        panel.add(field, gbc);
    }

    private void loadUserData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM User";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("UserId"),
                        resultSet.getString("UserType"),
                        resultSet.getString("Name"),
                        resultSet.getString("FatherName"),
                        resultSet.getString("MotherName"),
                        resultSet.getDate("DOB"),
                        resultSet.getString("ContactNo"),
                        resultSet.getString("Gender"),
                        resultSet.getString("Email"),
                        resultSet.getTimestamp("CreatedAt"),
                        resultSet.getTimestamp("UpdatedAt")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            nameField.setText((String) tableModel.getValueAt(selectedRow, 2));
            fatherNameField.setText((String) tableModel.getValueAt(selectedRow, 3));
            motherNameField.setText((String) tableModel.getValueAt(selectedRow, 4));
            emailField.setText((String) tableModel.getValueAt(selectedRow, 8));
            contactNoField.setText((String) tableModel.getValueAt(selectedRow, 6));
            dobField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            userTypeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
            genderComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 7));
        }
    }

    private void addUser() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO User (UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, (String) userTypeComboBox.getSelectedItem());
            statement.setString(2, nameField.getText());
            statement.setString(3, fatherNameField.getText());
            statement.setString(4, motherNameField.getText());
            statement.setDate(5, java.sql.Date.valueOf(dobField.getText()));
            statement.setString(6, contactNoField.getText());
            statement.setString(7, (String) genderComboBox.getSelectedItem());
            statement.setString(8, emailField.getText());

            statement.executeUpdate();
            loadUserData();
          setVisible(false);
          new UserFrame().setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE User SET UserType = ?, Name = ?, FatherName = ?, MotherName = ?, DOB = ?, ContactNo = ?, Gender = ?, Email = ? WHERE UserId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, (String) userTypeComboBox.getSelectedItem());
                statement.setString(2, nameField.getText());
                statement.setString(3, fatherNameField.getText());
                statement.setString(4, motherNameField.getText());
                statement.setDate(5, java.sql.Date.valueOf(dobField.getText()));
                statement.setString(6, contactNoField.getText());
                statement.setString(7, (String) genderComboBox.getSelectedItem());
                statement.setString(8, emailField.getText());
                statement.setInt(9, userId);

                statement.executeUpdate();
                loadUserData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        Connection connection = null;

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false); // Start transaction

            // Fetch user details
            String selectQuery = "SELECT UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email FROM User WHERE UserId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, userId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Prepare the user details for history table
                String userType = resultSet.getString("UserType");
                String name = resultSet.getString("Name");
                String fatherName = resultSet.getString("FatherName");
                String motherName = resultSet.getString("MotherName");
                Date dob = resultSet.getDate("DOB");
                String contactNo = resultSet.getString("ContactNo");
                String gender = resultSet.getString("Gender");
                String email = resultSet.getString("Email");

                // Check if the record already exists in the history table
                String checkHistoryQuery = "SELECT COUNT(*) FROM History WHERE UserId = ?";
                PreparedStatement checkHistoryStatement = connection.prepareStatement(checkHistoryQuery);
                checkHistoryStatement.setInt(1, userId);
                ResultSet checkResult = checkHistoryStatement.executeQuery();

                if (checkResult.next() && checkResult.getInt(1) > 0) {
                    // Update the existing record in history table
                    String updateHistoryQuery = "UPDATE History SET UserType = ?, Name = ?, FatherName = ?, MotherName = ?, DOB = ?, ContactNo = ?, Gender = ?, Email = ?, DeletedAt = ? " +
                                                "WHERE UserId = ?";
                    try (PreparedStatement updateHistoryStatement = connection.prepareStatement(updateHistoryQuery)) {
                        updateHistoryStatement.setString(1, userType);
                        updateHistoryStatement.setString(2, name);
                        updateHistoryStatement.setString(3, fatherName);
                        updateHistoryStatement.setString(4, motherName);
                        updateHistoryStatement.setDate(5, dob);
                        updateHistoryStatement.setString(6, contactNo);
                        updateHistoryStatement.setString(7, gender);
                        updateHistoryStatement.setString(8, email);
                        updateHistoryStatement.setTimestamp(9, new java.sql.Timestamp(System.currentTimeMillis())); // Use current timestamp
                        updateHistoryStatement.setInt(10, userId);

                        updateHistoryStatement.executeUpdate();
                    }
                } else {
                    // Insert into history table
                    String insertHistoryQuery = "INSERT INTO History (UserId, UserType, Name, FatherName, MotherName, DOB, ContactNo, Gender, Email, DeletedAt) " +
                                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertHistoryStatement = connection.prepareStatement(insertHistoryQuery)) {
                        insertHistoryStatement.setInt(1, userId);
                        insertHistoryStatement.setString(2, userType);
                        insertHistoryStatement.setString(3, name);
                        insertHistoryStatement.setString(4, fatherName);
                        insertHistoryStatement.setString(5, motherName);
                        insertHistoryStatement.setDate(6, dob);
                        insertHistoryStatement.setString(7, contactNo);
                        insertHistoryStatement.setString(8, gender);
                        insertHistoryStatement.setString(9, email);
                        insertHistoryStatement.setTimestamp(10, new java.sql.Timestamp(System.currentTimeMillis())); // Use current timestamp

                        insertHistoryStatement.executeUpdate();
                    }
                }
            }

            // Delete related records in BloodDonation
            String deleteBloodDonationQuery = "DELETE FROM BloodDonation WHERE DonorId = ?";
            try (PreparedStatement deleteBloodDonationStatement = connection.prepareStatement(deleteBloodDonationQuery)) {
                deleteBloodDonationStatement.setInt(1, userId);
                deleteBloodDonationStatement.executeUpdate();
            }

            // Delete the user
            String deleteUserQuery = "DELETE FROM User WHERE UserId = ?";
            try (PreparedStatement deleteUserStatement = connection.prepareStatement(deleteUserQuery)) {
                deleteUserStatement.setInt(1, userId);
                deleteUserStatement.executeUpdate();
            }

            connection.commit(); // Commit transaction
            loadUserData();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting user record.", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Reset auto-commit
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserFrame userFrame = new UserFrame();
            userFrame.setVisible(true);
        });
    }
}

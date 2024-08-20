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
public class BloodDonationFrame extends JFrame {
    private JTable bloodDonationTable;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> donorIdComboBox;
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JComboBox<String> groupNameComboBox; // New JComboBox for GroupName
    private JTextField donationDateField;
    private JComboBox<String> donationStatusComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public BloodDonationFrame() {
        setTitle("Blood Donation Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        bloodDonationTable = new JTable(tableModel);
        add(new JScrollPane(bloodDonationTable), BorderLayout.CENTER);

        tableModel.addColumn("BloodDonationId");
        tableModel.addColumn("DonorId");
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName"); // New column for GroupName
        tableModel.addColumn("DonationDate");
        tableModel.addColumn("DonationStatus");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("DonorId:"));
        donorIdComboBox = new JComboBox<>();
        formPanel.add(donorIdComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("GroupName:")); // New JLabel for GroupName
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("DonationDate (YYYY-MM-DD):"));
        donationDateField = new JTextField();
        formPanel.add(donationDateField);

        formPanel.add(new JLabel("DonationStatus:"));
        donationStatusComboBox = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        formPanel.add(donationStatusComboBox);

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
        loadBloodDonationData();
        loadComboBoxData();

        // Add Action Listeners
        addButton.addActionListener(e -> addBloodDonation());
        updateButton.addActionListener(e -> updateBloodDonation());
        deleteButton.addActionListener(e -> deleteBloodDonation());

        // Table Selection Listener
        bloodDonationTable.getSelectionModel().addListSelectionListener(e -> loadSelectedBloodDonation());
    }

    private void loadBloodDonationData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodDonation";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("BloodDonationId"),
                        resultSet.getInt("DonorId"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName"),
                        resultSet.getDate("DonationDate"),
                        resultSet.getString("DonationStatus")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load DonorId data
            String donorQuery = "SELECT DonorId FROM Donor";
            PreparedStatement donorStatement = connection.prepareStatement(donorQuery);
            ResultSet donorResultSet = donorStatement.executeQuery();
            while (donorResultSet.next()) {
                donorIdComboBox.addItem(donorResultSet.getInt("DonorId"));
            }

            // Load BloodGroupId data
            String bloodGroupQuery = "SELECT BloodGroupId FROM donor";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupIdComboBox.addItem(bloodGroupResultSet.getInt("BloodGroupId"));
            }

            // Load GroupName data
            String groupNameQuery = "SELECT GroupName FROM donor";
            PreparedStatement groupNameStatement = connection.prepareStatement(groupNameQuery);
            ResultSet groupNameResultSet = groupNameStatement.executeQuery();
            while (groupNameResultSet.next()) {
                groupNameComboBox.addItem(groupNameResultSet.getString("GroupName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSelectedBloodDonation() {
        int selectedRow = bloodDonationTable.getSelectedRow();
        if (selectedRow != -1) {
            donorIdComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
            bloodGroupIdComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            groupNameComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
            donationDateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            donationStatusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void addBloodDonation() {
        Integer donorId = (Integer) donorIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem(); // Get selected GroupName
        String donationDateText = donationDateField.getText();

        if (donorId == null || bloodGroupId == null || groupName == null || donationDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (!isValidDonorId(donorId)) {
                JOptionPane.showMessageDialog(this, "Invalid DonorId. Please select a valid DonorId.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidBloodGroupId(bloodGroupId)) {
                JOptionPane.showMessageDialog(this, "Invalid BloodGroupId. Please select a valid BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO BloodDonation (DonorId, BloodGroupId, GroupName, DonationDate, DonationStatus) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, donorId);
                statement.setInt(2, bloodGroupId);
                statement.setString(3, groupName);
                statement.setDate(4, java.sql.Date.valueOf(donationDateText));
                statement.setString(5, (String) donationStatusComboBox.getSelectedItem());

                statement.executeUpdate();
                loadBloodDonationData();
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for DonorId and BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidDonorId(int donorId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM Donor WHERE DonorId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, donorId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isValidBloodGroupId(int bloodGroupId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM BloodGroup WHERE BloodGroupId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, bloodGroupId);
            ResultSet resultSet = statement.executeQuery();
            
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateBloodDonation() {
        int selectedRow = bloodDonationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer donorId = (Integer) donorIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem();
        String donationDateText = donationDateField.getText();

        if (donorId == null || bloodGroupId == null || groupName == null || donationDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (!isValidDonorId(donorId)) {
                JOptionPane.showMessageDialog(this, "Invalid DonorId. Please select a valid DonorId.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidBloodGroupId(bloodGroupId)) {
                JOptionPane.showMessageDialog(this, "Invalid BloodGroupId. Please select a valid BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int bloodDonationId = (Integer) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE BloodDonation SET DonorId = ?, BloodGroupId = ?, GroupName = ?, DonationDate = ?, DonationStatus = ? WHERE BloodDonationId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, donorId);
                statement.setInt(2, bloodGroupId);
                statement.setString(3, groupName);
                statement.setDate(4, java.sql.Date.valueOf(donationDateText));
                statement.setString(5, (String) donationStatusComboBox.getSelectedItem());
                statement.setInt(6, bloodDonationId);

                statement.executeUpdate();
                loadBloodDonationData();
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for DonorId and BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBloodDonation() {
        int selectedRow = bloodDonationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a record to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bloodDonationId = (Integer) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Retrieve data from the BloodDonation table
            String selectQuery = "SELECT DonorId, BloodGroupId, DonationDate, DonationStatus FROM BloodDonation WHERE BloodDonationId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, bloodDonationId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int donorId = resultSet.getInt("DonorId");
                int bloodGroupId = resultSet.getInt("BloodGroupId");
                String donationDate = resultSet.getString("DonationDate");
                String donationStatus = resultSet.getString("DonationStatus");

                // Insert data into the History table
                String insertQuery = "INSERT INTO History (BloodDonationId, DonorId, BloodGroupId, DonationDate, DonationStatus, DeletedAt) " +
                                     "VALUES (?, ?, ?, ?, ?, NOW())";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, bloodDonationId);
                insertStatement.setInt(2, donorId);
                insertStatement.setInt(3, bloodGroupId);
                insertStatement.setString(4, donationDate);
                insertStatement.setString(5, donationStatus);
                insertStatement.executeUpdate();

                // Delete the record from the BloodDonation table
                String deleteQuery = "DELETE FROM BloodDonation WHERE BloodDonationId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, bloodDonationId);
                deleteStatement.executeUpdate();

                // Refresh the table and clear the form
                loadBloodDonationData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void clearForm() {
        donorIdComboBox.setSelectedIndex(-1);
        bloodGroupIdComboBox.setSelectedIndex(-1);
        groupNameComboBox.setSelectedIndex(-1);
        donationDateField.setText("");
        donationStatusComboBox.setSelectedIndex(0);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BloodDonationFrame frame = new BloodDonationFrame();
            frame.setVisible(true);
        });
    }
}

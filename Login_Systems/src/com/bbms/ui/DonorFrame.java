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
public class DonorFrame extends JFrame {
    private JTable donorTable;
    private DefaultTableModel tableModel;
    private JTextField bloodUnitField;
    private JComboBox<String> cityComboBox;
    private JComboBox<String> addressComboBox;
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> hospitalComboBox;
    private JLabel groupNameLabel;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public DonorFrame() {
        setTitle("Donor Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        donorTable = new JTable(tableModel);
        add(new JScrollPane(donorTable), BorderLayout.CENTER);

        tableModel.addColumn("DonorId");
        tableModel.addColumn("UserId");
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName"); // New column for GroupName
        tableModel.addColumn("HospitalId");
        tableModel.addColumn("BloodUnit");
        tableModel.addColumn("City");
        tableModel.addColumn("Address");
        tableModel.addColumn("CreatedAt");
        tableModel.addColumn("UpdatedAt");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(8, 2)); // Updated to 8 rows
        formPanel.add(new JLabel("UserId:"));
        userComboBox = new JComboBox<>();
        formPanel.add(userComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameLabel = new JLabel();
        formPanel.add(groupNameLabel);

        formPanel.add(new JLabel("HospitalId:"));
        hospitalComboBox = new JComboBox<>();
        formPanel.add(hospitalComboBox);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        formPanel.add(new JLabel("City:"));
        cityComboBox = new JComboBox<>();
        formPanel.add(cityComboBox);

        formPanel.add(new JLabel("Address:"));
        addressComboBox = new JComboBox<>();
        formPanel.add(addressComboBox);

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
        loadDonorData();
        loadComboBoxes();

        // Add Action Listeners
        addButton.addActionListener(e -> addDonor());
        updateButton.addActionListener(e -> updateDonor());
        deleteButton.addActionListener(e -> deleteDonor());

        // Table Selection Listener
        donorTable.getSelectionModel().addListSelectionListener(e -> loadSelectedDonor());

        // ComboBox Listener
        bloodGroupComboBox.addActionListener(e -> updateGroupName());
    }

    private void loadDonorData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT Donor.*, BloodGroup.GroupName FROM Donor INNER JOIN BloodGroup ON Donor.BloodGroupId = BloodGroup.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("DonorId"),
                        resultSet.getInt("UserId"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName"),
                        resultSet.getInt("HospitalId"),
                        resultSet.getInt("BloodUnit"),
                        resultSet.getString("City"),
                        resultSet.getString("Address"),
                        resultSet.getTimestamp("CreatedAt"),
                        resultSet.getTimestamp("UpdatedAt")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load UserIds
            String userQuery = "SELECT UserId FROM User WHERE UserType = 'Donor'";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            ResultSet userResultSet = userStatement.executeQuery();
            userComboBox.removeAllItems(); // Clear existing items
            while (userResultSet.next()) {
                userComboBox.addItem(userResultSet.getString("UserId"));
            }

            // Load BloodGroupIds
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            bloodGroupComboBox.removeAllItems(); // Clear existing items
            while (bloodGroupResultSet.next()) {
                bloodGroupComboBox.addItem(bloodGroupResultSet.getString("BloodGroupId"));
            }

            // Load Hospitals
            String hospitalQuery = "SELECT HospitalId, City, Address FROM Hospital WHERE UserType = 'Donor'";
            PreparedStatement hospitalStatement = connection.prepareStatement(hospitalQuery);
            ResultSet hospitalResultSet = hospitalStatement.executeQuery();
            hospitalComboBox.removeAllItems(); // Clear existing items
            cityComboBox.removeAllItems(); // Clear existing items
            addressComboBox.removeAllItems(); // Clear existing items
            while (hospitalResultSet.next()) {
                hospitalComboBox.addItem(hospitalResultSet.getString("HospitalId"));
                // Populate cityComboBox and addressComboBox based on hospital data
                cityComboBox.addItem(hospitalResultSet.getString("City"));
                addressComboBox.addItem(hospitalResultSet.getString("Address"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void updateGroupName() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        if (selectedBloodGroupId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(selectedBloodGroupId));
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    groupNameLabel.setText(resultSet.getString("GroupName"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadSelectedDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow != -1) {
            userComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1).toString());
            bloodGroupComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            groupNameLabel.setText(tableModel.getValueAt(selectedRow, 3).toString());
            hospitalComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
            bloodUnitField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            cityComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 6).toString());
            addressComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());
        }
    }

    private void addDonor() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
            String groupName = "";

            if (selectedBloodGroupId != null) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(selectedBloodGroupId));
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    groupName = resultSet.getString("GroupName");
                }
            }

            String query = "INSERT INTO Donor (UserId, BloodGroupId, GroupName, HospitalId, BloodUnit, City, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt((String) userComboBox.getSelectedItem()));
            statement.setInt(2, Integer.parseInt((String) bloodGroupComboBox.getSelectedItem()));
            statement.setString(3, groupName);
            statement.setInt(4, Integer.parseInt((String) hospitalComboBox.getSelectedItem()));
            statement.setInt(5, Integer.parseInt(bloodUnitField.getText()));
            statement.setString(6, (String) cityComboBox.getSelectedItem());
            statement.setString(7, (String) addressComboBox.getSelectedItem());
            statement.executeUpdate();

            loadDonorData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow != -1) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String selectedDonorId = tableModel.getValueAt(selectedRow, 0).toString();
                String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
                String groupName = "";

                if (selectedBloodGroupId != null) {
                    String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(selectedBloodGroupId));
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        groupName = resultSet.getString("GroupName");
                    }
                }

                String query = "UPDATE Donor SET UserId = ?, BloodGroupId = ?, GroupName = ?, HospitalId = ?, BloodUnit = ?, City = ?, Address = ? WHERE DonorId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt((String) userComboBox.getSelectedItem()));
                statement.setInt(2, Integer.parseInt((String) bloodGroupComboBox.getSelectedItem()));
                statement.setString(3, groupName);
                statement.setInt(4, Integer.parseInt((String) hospitalComboBox.getSelectedItem()));
                statement.setInt(5, Integer.parseInt(bloodUnitField.getText()));
                statement.setString(6, (String) cityComboBox.getSelectedItem());
                statement.setString(7, (String) addressComboBox.getSelectedItem());
                statement.setInt(8, Integer.parseInt(selectedDonorId));
                statement.executeUpdate();

                loadDonorData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteDonor() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow != -1) {
            int donorId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Move donor data to history table
                String insertQuery = "INSERT INTO History (DonorId, UserId,BloodGroupID, GroupName, BloodUnit, City, Address, DeletedAt) " +
                        "SELECT DonorId, UserId,BloodGroupID, GroupName, BloodUnit, City, Address, NOW() " +
                                     "FROM Donor WHERE DonorId = ?";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, donorId);
                insertStatement.executeUpdate();

                // Delete donor record from the Donor table
                String deleteQuery = "DELETE FROM Donor WHERE DonorId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, donorId);
                deleteStatement.executeUpdate();

                // Refresh the donor data table
                loadDonorData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No donor selected for deletion.");
        }
    
    }

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        DonorFrame donorFrame = new DonorFrame();
        donorFrame.setVisible(true);
    });
}
}
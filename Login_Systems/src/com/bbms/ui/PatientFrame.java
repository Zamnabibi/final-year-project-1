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
public class PatientFrame extends JFrame {
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> hospitalComboBox;
    private JTextField bloodUnitField;
    private JComboBox<String> cityComboBox; // Changed to JComboBox
    private JComboBox<String> addressComboBox; // Changed to JComboBox
    private JLabel groupNameLabel; // JLabel to display GroupName
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public PatientFrame() {
        setTitle("Patient Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        patientTable = new JTable(tableModel);
        add(new JScrollPane(patientTable), BorderLayout.CENTER);

        tableModel.addColumn("PatientId");
        tableModel.addColumn("UserId");
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName"); // Added GroupName column
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
        cityComboBox = new JComboBox<>(); // Changed to JComboBox
        formPanel.add(cityComboBox);

        formPanel.add(new JLabel("Address:"));
        addressComboBox = new JComboBox<>(); // Changed to JComboBox
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
        loadPatientData();
        loadComboBoxes();

        // Add Action Listeners
        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());

        // Table Selection Listener
        patientTable.getSelectionModel().addListSelectionListener(e -> loadSelectedPatient());

        // BloodGroup ComboBox Selection Listener
        bloodGroupComboBox.addActionListener(e -> updateGroupName());
    }

    private void loadPatientData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT p.PatientId, p.UserId, p.BloodGroupId, p.GroupName, p.HospitalId, p.BloodUnit, p.City, p.Address, p.CreatedAt, p.UpdatedAt " +
                           "FROM Patient p";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("PatientId"),
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
            // Load UserIds where UserType is 'Patient'
            String userQuery = "SELECT UserId FROM User WHERE UserType = 'Patient'";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            ResultSet userResultSet = userStatement.executeQuery();
            userComboBox.removeAllItems(); // Clear existing items
            while (userResultSet.next()) {
                userComboBox.addItem(userResultSet.getString("UserId"));
            }

            // Load BloodGroupIds
            String bloodGroupQuery = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            bloodGroupComboBox.removeAllItems(); // Clear existing items
            while (bloodGroupResultSet.next()) {
                bloodGroupComboBox.addItem(bloodGroupResultSet.getString("BloodGroupId"));
            }

            // Load HospitalIds
            String hospitalQuery = "SELECT HospitalId FROM Hospital WHERE UserType = 'Patient'";
            PreparedStatement hospitalStatement = connection.prepareStatement(hospitalQuery);
            ResultSet hospitalResultSet = hospitalStatement.executeQuery();
            hospitalComboBox.removeAllItems(); // Clear existing items
            while (hospitalResultSet.next()) {
                hospitalComboBox.addItem(hospitalResultSet.getString("HospitalId"));
            }

            // Load Cities and Addresses
            // Assuming City and Address are retrieved from Hospital table or another relevant table
            // You may need to adjust this query based on your database schema
            String cityAddressQuery = "SELECT DISTINCT City, Address FROM Hospital WHERE UserType = 'Patient'";
            PreparedStatement cityAddressStatement = connection.prepareStatement(cityAddressQuery);
            ResultSet cityAddressResultSet = cityAddressStatement.executeQuery();
            cityComboBox.removeAllItems(); // Clear existing items
            addressComboBox.removeAllItems(); // Clear existing items
            while (cityAddressResultSet.next()) {
                cityComboBox.addItem(cityAddressResultSet.getString("City"));
                addressComboBox.addItem(cityAddressResultSet.getString("Address"));
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

    private void loadSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
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

    private void addPatient() {
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

            String query = "INSERT INTO Patient (UserId, BloodGroupId, GroupName, HospitalId, BloodUnit, City, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt((String) userComboBox.getSelectedItem()));
            statement.setInt(2, Integer.parseInt((String) bloodGroupComboBox.getSelectedItem()));
            statement.setString(3, groupName);
            statement.setInt(4, Integer.parseInt((String) hospitalComboBox.getSelectedItem()));
            statement.setInt(5, Integer.parseInt(bloodUnitField.getText()));
            statement.setString(6, (String) cityComboBox.getSelectedItem());
            statement.setString(7, (String) addressComboBox.getSelectedItem());

            statement.executeUpdate();
            loadPatientData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String selectedPatientId = tableModel.getValueAt(selectedRow, 0).toString();
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

                String query = "UPDATE Patient SET UserId = ?, BloodGroupId = ?, GroupName = ?, HospitalId = ?, BloodUnit = ?, City = ?, Address = ? WHERE PatientId = ?";
                PreparedStatement updateStatement = connection.prepareStatement(query);
                updateStatement.setInt(1, Integer.parseInt((String) userComboBox.getSelectedItem()));
                updateStatement.setInt(2, Integer.parseInt((String) bloodGroupComboBox.getSelectedItem()));
                updateStatement.setString(3, groupName);
                updateStatement.setInt(4, Integer.parseInt((String) hospitalComboBox.getSelectedItem()));
                updateStatement.setInt(5, Integer.parseInt(bloodUnitField.getText()));
                updateStatement.setString(6, (String) cityComboBox.getSelectedItem());
                updateStatement.setString(7, (String) addressComboBox.getSelectedItem());
                updateStatement.setInt(8, Integer.parseInt(selectedPatientId));

                updateStatement.executeUpdate();
                loadPatientData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow != -1) {
            int patientId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Move patient data to history table
                String insertQuery = "INSERT INTO History (PatientId, UserId,BloodGroupID, GroupName, BloodUnit, City, Address, DeletedAt) " +
                                     "SELECT PatientId, UserId,BloodGroupID, GroupName, BloodUnit, City, Address, NOW() " +
                                     "FROM Patient WHERE PatientId = ?";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, patientId);
                insertStatement.executeUpdate();

                // Delete patient record from the Patient table
                String deleteQuery = "DELETE FROM Patient WHERE PatientId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, patientId);
                deleteStatement.executeUpdate();

                // Refresh the patient data table
                loadPatientData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No patient selected for deletion.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PatientFrame frame = new PatientFrame();
            frame.setVisible(true);
        });
    }
}

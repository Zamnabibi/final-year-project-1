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
public class BloodRequestFrame extends JFrame {
    private JTable bloodRequestTable;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> patientIdComboBox;
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JComboBox<String> groupNameComboBox; // New JComboBox for GroupName
    private JTextField requestDateField;
    private JComboBox<String> requestStatusComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;

    public BloodRequestFrame() {
        setTitle("Blood Request Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        bloodRequestTable = new JTable(tableModel);
        add(new JScrollPane(bloodRequestTable), BorderLayout.CENTER);

        tableModel.addColumn("BloodRequestId");
        tableModel.addColumn("PatientId");
        tableModel.addColumn("BloodGroupId");
        tableModel.addColumn("GroupName"); // New column for GroupName
        tableModel.addColumn("RequestDate");
        tableModel.addColumn("RequestStatus");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.add(new JLabel("PatientId:"));
        patientIdComboBox = new JComboBox<>();
        formPanel.add(patientIdComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("GroupName:")); // New JLabel for GroupName
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("RequestDate (YYYY-MM-DD):"));
        requestDateField = new JTextField();
        formPanel.add(requestDateField);

        formPanel.add(new JLabel("RequestStatus:"));
        requestStatusComboBox = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        formPanel.add(requestStatusComboBox);

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
        loadBloodRequestData();
        loadComboBoxData();

        // Add Action Listeners
        addButton.addActionListener(e -> addBloodRequest());
        updateButton.addActionListener(e -> updateBloodRequest());
        deleteButton.addActionListener(e -> deleteBloodRequest());

        // Table Selection Listener
        bloodRequestTable.getSelectionModel().addListSelectionListener(e -> loadSelectedBloodRequest());
    }

    private void loadBloodRequestData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodRequest";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("BloodRequestId"),
                        resultSet.getInt("PatientId"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName"),
                        resultSet.getDate("RequestDate"),
                        resultSet.getString("RequestStatus")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load PatientId data
            String patientQuery = "SELECT PatientId FROM Patient";
            PreparedStatement patientStatement = connection.prepareStatement(patientQuery);
            ResultSet patientResultSet = patientStatement.executeQuery();
            while (patientResultSet.next()) {
                patientIdComboBox.addItem(patientResultSet.getInt("PatientId"));
            }

            // Load BloodGroupId data
            String bloodGroupQuery = "SELECT BloodGroupId FROM Patient";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupIdComboBox.addItem(bloodGroupResultSet.getInt("BloodGroupId"));
            }

            // Load GroupName data
            String groupNameQuery = "SELECT GroupName FROM Patient";
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

    private void loadSelectedBloodRequest() {
        int selectedRow = bloodRequestTable.getSelectedRow();
        if (selectedRow != -1) {
            patientIdComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
            bloodGroupIdComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 2));
            groupNameComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
            requestDateField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            requestStatusComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
        }
    }

    private void addBloodRequest() {
        Integer patientId = (Integer) patientIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem(); // Get selected GroupName
        String requestDateText = requestDateField.getText();

        if (patientId == null || bloodGroupId == null || groupName == null || requestDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (!isValidPatientId(patientId)) {
                JOptionPane.showMessageDialog(this, "Invalid PatientId. Please select a valid PatientId.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidBloodGroupId(bloodGroupId)) {
                JOptionPane.showMessageDialog(this, "Invalid BloodGroupId. Please select a valid BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO BloodRequest (PatientId, BloodGroupId, GroupName, RequestDate, RequestStatus) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, patientId);
                statement.setInt(2, bloodGroupId);
                statement.setString(3, groupName);
                statement.setDate(4, java.sql.Date.valueOf(requestDateText));
                statement.setString(5, (String) requestStatusComboBox.getSelectedItem());

                statement.executeUpdate();
                loadBloodRequestData();
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for PatientId and BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidPatientId(int patientId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT COUNT(*) FROM Patient WHERE PatientId = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, patientId);
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

    private void updateBloodRequest() {
        int selectedRow = bloodRequestTable.getSelectedRow();
        if (selectedRow != -1) {
            int bloodRequestId = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "UPDATE BloodRequest SET PatientId = ?, BloodGroupId = ?, RequestDate = ?, RequestStatus = ? WHERE BloodRequestId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, (Integer) patientIdComboBox.getSelectedItem());
                statement.setInt(2, (Integer) bloodGroupIdComboBox.getSelectedItem());
                statement.setDate(3, java.sql.Date.valueOf(requestDateField.getText()));
                statement.setString(4, (String) requestStatusComboBox.getSelectedItem());
                statement.setInt(5, bloodRequestId);

                statement.executeUpdate();
                loadBloodRequestData();
                clearForm();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBloodRequest() {
        int selectedRow = bloodRequestTable.getSelectedRow();
        if (selectedRow != -1) {
            int bloodRequestId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection connection = DatabaseConnection.getConnection()) {
                // Retrieve data from the BloodDonation table
                String selectQuery = "SELECT PatientId, BloodGroupId, RequestedDate, RequestedStatus FROM BloodRequest WHERE BloodRequestId = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setInt(1, bloodRequestId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int donorId = resultSet.getInt("PatientId");
                    int bloodGroupId = resultSet.getInt("BloodGroupId");
                    String donationDate = resultSet.getString("RequestedDate");
                    String donationStatus = resultSet.getString("RequestedStatus");

                    // Insert data into the History table
                    String insertQuery = "INSERT INTO History (BloodRequestId, PatientId, BloodGroupId, RequestedDate, RequestedStatus, DeletedAt) " +
                                         "VALUES (?, ?, ?, ?, ?, NOW())";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1, bloodRequestId);
                    insertStatement.setInt(2, donorId);
                    insertStatement.setInt(3, bloodGroupId);
                    insertStatement.setString(4, donationDate);
                    insertStatement.setString(5, donationStatus);
                    insertStatement.executeUpdate();

                    // Delete the record from the BloodDonation table
                    String deleteQuery = "DELETE FROM BloodDonation WHERE BloodDonationId = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, bloodRequestId);
                    deleteStatement.executeUpdate();

                    // Refresh the table and clear the form
                    loadBloodRequestData();
                    clearForm();
            } }catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        patientIdComboBox.setSelectedIndex(-1);
        bloodGroupIdComboBox.setSelectedIndex(-1);
        requestDateField.setText("");
        requestStatusComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BloodRequestFrame frame = new BloodRequestFrame();
            frame.setVisible(true);
        });
    }
}

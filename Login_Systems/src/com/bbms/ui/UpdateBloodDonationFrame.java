package com.bbms.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class UpdateBloodDonationFrame extends JFrame {
    private JComboBox<Integer> bloodDonationIdComboBox;
    private JComboBox<Integer> donorIdComboBox;
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JLabel groupNameLabel;
    private JTextField donationDateField;
    private JComboBox<String> donationStatusComboBox;
    private JButton updateButton;
    private JButton closeButton;
    private JTable bloodDonationTable;
    private DefaultTableModel tableModel;

    public UpdateBloodDonationFrame() {
        setTitle("Update Blood Donation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize the table model and JTable
        tableModel = new DefaultTableModel(new Object[]{"BloodDonationId", "DonorId", "BloodGroupId", "GroupName", "DonationDate", "DonationStatus"}, 0);
        bloodDonationTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(bloodDonationTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        inputPanel.setBackground(Color.PINK);

        inputPanel.add(new JLabel("BloodDonationId:"));
        bloodDonationIdComboBox = new JComboBox<>();
        inputPanel.add(bloodDonationIdComboBox);

        inputPanel.add(new JLabel("DonorId:"));
        donorIdComboBox = new JComboBox<>();
        inputPanel.add(donorIdComboBox);

        inputPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupIdComboBox = new JComboBox<>();
        bloodGroupIdComboBox.addActionListener(e -> updateGroupNameLabel());
        inputPanel.add(bloodGroupIdComboBox);

        inputPanel.add(new JLabel("GroupName:"));
        groupNameLabel = new JLabel();
        inputPanel.add(groupNameLabel);

        inputPanel.add(new JLabel("DonationDate (YYYY-MM-DD):"));
        donationDateField = new JTextField();
        inputPanel.add(donationDateField);

        inputPanel.add(new JLabel("DonationStatus:"));
        donationStatusComboBox = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        inputPanel.add(donationStatusComboBox);

        updateButton = new JButton("Update");
        inputPanel.add(updateButton);

        closeButton = new JButton("Close");
        inputPanel.add(closeButton);

        add(inputPanel, BorderLayout.NORTH);

        updateButton.addActionListener(e -> updateBloodDonation());
        closeButton.addActionListener(e -> dispose());

        loadComboBoxData();
        loadBloodDonationTable();

        // Add a ListSelectionListener to the table
        bloodDonationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = bloodDonationTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        // Fill input fields with the selected row data
                        bloodDonationIdComboBox.setSelectedItem(bloodDonationTable.getValueAt(selectedRow, 0));
                        donorIdComboBox.setSelectedItem(bloodDonationTable.getValueAt(selectedRow, 1));
                        bloodGroupIdComboBox.setSelectedItem(bloodDonationTable.getValueAt(selectedRow, 2));
                        groupNameLabel.setText((String) bloodDonationTable.getValueAt(selectedRow, 3));
                        donationDateField.setText(bloodDonationTable.getValueAt(selectedRow, 4).toString());
                        donationStatusComboBox.setSelectedItem(bloodDonationTable.getValueAt(selectedRow, 5));
                    }
                }
            }
        });
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load BloodDonationId data
            String bloodDonationQuery = "SELECT BloodDonationId FROM BloodDonation";
            PreparedStatement bloodDonationStatement = connection.prepareStatement(bloodDonationQuery);
            ResultSet bloodDonationResultSet = bloodDonationStatement.executeQuery();
            while (bloodDonationResultSet.next()) {
                bloodDonationIdComboBox.addItem(bloodDonationResultSet.getInt("BloodDonationId"));
            }

            // Load DonorId data
            String donorQuery = "SELECT DonorId FROM Donor";
            PreparedStatement donorStatement = connection.prepareStatement(donorQuery);
            ResultSet donorResultSet = donorStatement.executeQuery();
            while (donorResultSet.next()) {
                donorIdComboBox.addItem(donorResultSet.getInt("DonorId"));
            }

            // Load BloodGroupId data
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupIdComboBox.addItem(bloodGroupResultSet.getInt("BloodGroupId"));
            }

            // Set initial data
            bloodDonationIdComboBox.addActionListener(e -> loadExistingData());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo box data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGroupNameLabel() {
        Integer selectedBloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        if (selectedBloodGroupId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedBloodGroupId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    groupNameLabel.setText(resultSet.getString("GroupName"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading group name.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadExistingData() {
        Integer selectedId = (Integer) bloodDonationIdComboBox.getSelectedItem();
        if (selectedId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM BloodDonation WHERE BloodDonationId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, selectedId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    donorIdComboBox.setSelectedItem(resultSet.getInt("DonorId"));
                    bloodGroupIdComboBox.setSelectedItem(resultSet.getInt("BloodGroupId"));
                    updateGroupNameLabel(); // Update group name label based on selected BloodGroupId
                    donationDateField.setText(resultSet.getDate("DonationDate").toString());
                    donationStatusComboBox.setSelectedItem(resultSet.getString("DonationStatus"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error loading existing data.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateBloodDonation() {
        Integer bloodDonationId = (Integer) bloodDonationIdComboBox.getSelectedItem();
        Integer donorId = (Integer) donorIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = groupNameLabel.getText();
        String donationDateText = donationDateField.getText();

        if (bloodDonationId == null || donorId == null || bloodGroupId == null || groupName.isEmpty() || donationDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE BloodDonation SET DonorId = ?, BloodGroupId = ?, DonationDate = ?, DonationStatus = ? WHERE BloodDonationId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, donorId);
            statement.setInt(2, bloodGroupId);
            statement.setDate(3, java.sql.Date.valueOf(donationDateText));
            statement.setString(4, (String) donationStatusComboBox.getSelectedItem());
            statement.setInt(5, bloodDonationId);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Blood donation updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBloodDonationTable(); // Refresh the table data
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update blood donation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating blood donation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBloodDonationTable() {
        tableModel.setRowCount(0); // Clear existing data

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT b.BloodDonationId, b.DonorId, b.BloodGroupId, bg.GroupName, b.DonationDate, b.DonationStatus " +
                    "FROM BloodDonation b JOIN BloodGroup bg ON b.BloodGroupId = bg.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

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
            JOptionPane.showMessageDialog(this, "Error loading blood donation data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateBloodDonationFrame().setVisible(true));
    }
}

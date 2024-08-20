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
public class BloodStockFrame extends JFrame {
    private JTable bloodStockTable;
    private DefaultTableModel stockTableModel;
    private JLabel groupNameLabel;
    private JTextField bloodUnitField;
    private JComboBox<String> bloodGroupComboBox; // For user input
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton showBloodDonationDataButton;
    private JButton showBloodRequestDataButton;
    private JButton showBloodGroupDataButton;

    public BloodStockFrame() {
        setTitle("Blood Stock Management");
        setSize(1800, 600); // Adjust size as needed
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Stock Table setup
        stockTableModel = new DefaultTableModel();
        bloodStockTable = new JTable(stockTableModel);
        JScrollPane stockScrollPane = new JScrollPane(bloodStockTable);
        stockScrollPane.setBorder(BorderFactory.createTitledBorder("Blood Stock"));
        add(stockScrollPane, BorderLayout.CENTER);

        stockTableModel.addColumn("StockId");
        stockTableModel.addColumn("BloodGroupId");
        stockTableModel.addColumn("BloodUnit");
        stockTableModel.addColumn("GroupName");
        stockTableModel.addColumn("LastUpdated");

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2)); // Updated layout
        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameLabel = new JLabel();
        formPanel.add(groupNameLabel);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        add(formPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        showBloodDonationDataButton = new JButton("Show Blood Donation Data");
        showBloodRequestDataButton = new JButton("Show Blood Request Data");
        showBloodGroupDataButton = new JButton("Show Blood Group Data");

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(showBloodDonationDataButton);
        buttonsPanel.add(showBloodRequestDataButton);
        buttonsPanel.add(showBloodGroupDataButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Load Data
        loadBloodStockData();
        loadComboBoxes();

        // Add Action Listeners
        addButton.addActionListener(e -> addBloodStock());
        updateButton.addActionListener(e -> updateBloodStock());
        deleteButton.addActionListener(e -> deleteBloodStock());
        showBloodDonationDataButton.addActionListener(e -> showBloodDonationData());
        showBloodRequestDataButton.addActionListener(e -> showBloodRequestData());
        showBloodGroupDataButton.addActionListener(e -> showBloodGroupData());

        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        
        // Table Selection Listeners
        bloodStockTable.getSelectionModel().addListSelectionListener(e -> loadSelectedBloodStock());
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load BloodGroupIds
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupComboBox.addItem(bloodGroupResultSet.getString("BloodGroupId"));
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

    private void loadBloodStockData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT bs.StockId, bs.BloodGroupId, bs.BloodUnit, bg.GroupName, bs.LastUpdated " +
                           "FROM BloodStock bs " +
                           "JOIN BloodGroup bg ON bs.BloodGroupId = bg.BloodGroupId";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            stockTableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                stockTableModel.addRow(new Object[]{
                        resultSet.getInt("StockId"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getInt("BloodUnit"),
                        resultSet.getString("GroupName"),
                        resultSet.getTimestamp("LastUpdated")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showBloodDonationData() {
        JFrame bloodDonationFrame = new JFrame("Blood Donation Data");
        bloodDonationFrame.setSize(800, 400);
        bloodDonationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        DefaultTableModel donationTableModel = new DefaultTableModel();
        JTable bloodDonationTable = new JTable(donationTableModel);
        JScrollPane donationScrollPane = new JScrollPane(bloodDonationTable);
        donationScrollPane.setBorder(BorderFactory.createTitledBorder("Blood Donation Data"));
        bloodDonationFrame.add(donationScrollPane, BorderLayout.CENTER);

        donationTableModel.addColumn("DonationId");
        donationTableModel.addColumn("DonorId");
        donationTableModel.addColumn("DonationDate");
        donationTableModel.addColumn("BloodGroupId");
        donationTableModel.addColumn("BloodUnit");

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodDonation";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            donationTableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                donationTableModel.addRow(new Object[]{
                        resultSet.getInt("DonationId"),
                        resultSet.getInt("DonorId"),
                        resultSet.getDate("DonationDate"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getInt("BloodUnit")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bloodDonationFrame.setVisible(true);
    }

    private void showBloodRequestData() {
        JFrame bloodRequestFrame = new JFrame("Blood Request Data");
        bloodRequestFrame.setSize(800, 400);
        bloodRequestFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        DefaultTableModel requestTableModel = new DefaultTableModel();
        JTable bloodRequestTable = new JTable(requestTableModel);
        JScrollPane requestScrollPane = new JScrollPane(bloodRequestTable);
        requestScrollPane.setBorder(BorderFactory.createTitledBorder("Blood Request Data"));
        bloodRequestFrame.add(requestScrollPane, BorderLayout.CENTER);

        requestTableModel.addColumn("RequestId");
        requestTableModel.addColumn("PatientId");
        requestTableModel.addColumn("RequestDate");
        requestTableModel.addColumn("BloodGroupId");
        requestTableModel.addColumn("BloodUnit");

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodRequest";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            requestTableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                requestTableModel.addRow(new Object[]{
                        resultSet.getInt("RequestId"),
                        resultSet.getInt("PatientId"),
                        resultSet.getDate("RequestDate"),
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getInt("BloodUnit")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bloodRequestFrame.setVisible(true);
    }

    private void showBloodGroupData() {
        JFrame bloodGroupFrame = new JFrame("Blood Group Data");
        bloodGroupFrame.setSize(800, 400);
        bloodGroupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        DefaultTableModel groupTableModel = new DefaultTableModel();
        JTable bloodGroupTable = new JTable(groupTableModel);
        JScrollPane groupScrollPane = new JScrollPane(bloodGroupTable);
        groupScrollPane.setBorder(BorderFactory.createTitledBorder("Blood Group Data"));
        bloodGroupFrame.add(groupScrollPane, BorderLayout.CENTER);

        groupTableModel.addColumn("BloodGroupId");
        groupTableModel.addColumn("GroupName");

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            groupTableModel.setRowCount(0); // Clear existing rows

            while (resultSet.next()) {
                groupTableModel.addRow(new Object[]{
                        resultSet.getInt("BloodGroupId"),
                        resultSet.getString("GroupName")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bloodGroupFrame.setVisible(true);
    }

    private void addBloodStock() {
        String bloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String bloodUnitText = bloodUnitField.getText();

        if (bloodGroupId == null || bloodUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        int bloodUnit;
        try {
            bloodUnit = Integer.parseInt(bloodUnitText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "BloodUnit must be a number.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO BloodStock (BloodGroupId, BloodUnit) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(bloodGroupId));
            statement.setInt(2, bloodUnit);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood stock added successfully.");
            loadBloodStockData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding blood stock.");
        }
    }

    private void updateBloodStock() {
        int selectedRow = bloodStockTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to update.");
            return;
        }

        String bloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String bloodUnitText = bloodUnitField.getText();
        int stockId = (int) bloodStockTable.getValueAt(selectedRow, 0);

        if (bloodGroupId == null || bloodUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields.");
            return;
        }

        int bloodUnit;
        try {
            bloodUnit = Integer.parseInt(bloodUnitText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "BloodUnit must be a number.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE BloodStock SET BloodGroupId = ?, BloodUnit = ? WHERE StockId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(bloodGroupId));
            statement.setInt(2, bloodUnit);
            statement.setInt(3, stockId);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood stock updated successfully.");
            loadBloodStockData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating blood stock.");
        }
    }

    private void deleteBloodStock() {
        int selectedRow = bloodStockTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
            return;
        }

        int stockId = (int) bloodStockTable.getValueAt(selectedRow, 0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Retrieve data from the BloodStock table
            String selectQuery = "SELECT StockId, BloodGroupId, BloodUnit, GroupName FROM BloodStock WHERE StockId = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, stockId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                int bloodGroupId = resultSet.getInt("BloodGroupId");
                int bloodUnit = resultSet.getInt("BloodUnit");
                String GroupName = resultSet.getString("GroupName");
               

                // Insert data into the History table
                String insertQuery = "INSERT INTO History (StockId, BloodGroupId, BloodUnit, GroupName, DeletedAt) VALUES (?, ?, ?, ?,  NOW())";
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, stockId);
                insertStatement.setInt(2, bloodGroupId);
                insertStatement.setInt(3, bloodUnit);
                insertStatement.setString(4, GroupName);
              
                insertStatement.executeUpdate();

                // Delete the record from the BloodStock table
                String deleteQuery = "DELETE FROM BloodStock WHERE StockId = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, stockId);
                deleteStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Blood stock deleted successfully.");
                loadBloodStockData();
            } else {
                JOptionPane.showMessageDialog(this, "Error retrieving blood stock.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting blood stock.");
        }
    }


    private void loadSelectedBloodStock() {
        int selectedRow = bloodStockTable.getSelectedRow();

        if (selectedRow != -1) {
            int bloodGroupId = (int) bloodStockTable.getValueAt(selectedRow, 1);
            int bloodUnit = (int) bloodStockTable.getValueAt(selectedRow, 2);

            bloodGroupComboBox.setSelectedItem(bloodGroupId);
            bloodUnitField.setText(String.valueOf(bloodUnit));
            groupNameLabel.setText((String) bloodStockTable.getValueAt(selectedRow, 3));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BloodStockFrame().setVisible(true));
    }
}

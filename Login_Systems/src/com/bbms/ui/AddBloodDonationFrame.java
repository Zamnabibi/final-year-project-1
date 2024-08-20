package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class AddBloodDonationFrame extends JFrame {
    private JComboBox<Integer> donorIdComboBox;
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JComboBox<String> groupNameComboBox;
    private JTextField donationDateField;
    private JComboBox<String> donationStatusComboBox;
    private JButton addButton;
    private JButton closeButton;
    private JTable bloodDonationTable;
    private DefaultTableModel tableModel;

    public AddBloodDonationFrame() {
        setTitle("Add Blood Donation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Set background color to pink
        getContentPane().setBackground(Color.PINK);

        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        inputPanel.setBackground(Color.PINK); // Set input panel background color
        add(inputPanel, BorderLayout.NORTH);

        inputPanel.add(new JLabel("DonorId:"));
        donorIdComboBox = new JComboBox<>();
        inputPanel.add(donorIdComboBox);

        inputPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupIdComboBox = new JComboBox<>();
        inputPanel.add(bloodGroupIdComboBox);

        inputPanel.add(new JLabel("GroupName:"));
        groupNameComboBox = new JComboBox<>();
        inputPanel.add(groupNameComboBox);

        inputPanel.add(new JLabel("DonationDate (YYYY-MM-DD):"));
        donationDateField = new JTextField();
        inputPanel.add(donationDateField);

        inputPanel.add(new JLabel("DonationStatus:"));
        donationStatusComboBox = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        inputPanel.add(donationStatusComboBox);

        addButton = new JButton("Add");
        inputPanel.add(addButton);
        closeButton = new JButton("Close");
        inputPanel.add(closeButton);

        addButton.addActionListener(e -> addBloodDonation());
        closeButton.addActionListener(e -> dispose());

        // Table setup
        tableModel = new DefaultTableModel(new String[]{"BloodDonationId", "DonorId", "BloodGroupId", "GroupName", "DonationDate", "DonationStatus"}, 0);
        bloodDonationTable = new JTable(tableModel);
        bloodDonationTable.setBackground(Color.white); // Set table background color
        bloodDonationTable.setSelectionBackground(Color.white); // Set selection background color
        JScrollPane tableScrollPane = new JScrollPane(bloodDonationTable);
        tableScrollPane.getViewport().setBackground(Color.PINK); // Set viewport background color
        add(tableScrollPane, BorderLayout.CENTER);

        loadComboBoxData();
        loadTableData();
        
        // Add the footer panel at the bottom
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Ensure the footer has a fixed height
        add(footerPanel, BorderLayout.SOUTH);
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
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupIdComboBox.addItem(bloodGroupResultSet.getInt("BloodGroupId"));
            }

            // Load GroupName data
            String groupNameQuery = "SELECT GroupName FROM BloodGroup";
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

    private void addBloodDonation() {
        Integer donorId = (Integer) donorIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem();
        String donationDateText = donationDateField.getText();

        if (donorId == null || bloodGroupId == null || groupName == null || donationDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Blood donation added successfully.");
            loadTableData(); // Refresh table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding record.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTableData() {
        tableModel.setRowCount(0); // Clear existing rows

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM BloodDonation";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] row = {
                    resultSet.getInt("BloodDonationId"),
                    resultSet.getInt("DonorId"),
                    resultSet.getInt("BloodGroupId"),
                    resultSet.getString("GroupName"),
                    resultSet.getDate("DonationDate"),
                    resultSet.getString("DonationStatus")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading table data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddBloodDonationFrame().setVisible(true));
    }
}

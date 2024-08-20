package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class AddBloodStock extends JFrame {
    private JTextField bloodUnitField;
    private JComboBox<String> bloodGroupComboBox;
    private JLabel groupNameLabel; // JLabel for displaying GroupName
    private JButton addButton;
    private JButton closeButton;
    private JTable bloodStockTable;
    private DefaultTableModel tableModel;

    public AddBloodStock() {
        setTitle("Add Blood Stock");
        setSize(800, 400); // Adjusted size to fit the table with GroupName
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the background color of the content pane
        getContentPane().setBackground(Color.PINK);

        // Panel for form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2)); // Adjusted layout to fit the new label
        formPanel.setBackground(Color.PINK); // Set the background color of the panel

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:")); // Label for GroupName
        groupNameLabel = new JLabel(""); // Initially empty
        formPanel.add(groupNameLabel);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        addButton = new JButton("Add");
        formPanel.add(addButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);

        add(formPanel, BorderLayout.NORTH);

        // Table for displaying blood stock
        String[] columnNames = {"StockId", "BloodGroupId", "GroupName", "BloodUnit"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bloodStockTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bloodStockTable);
        add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> addBloodStock());
        closeButton.addActionListener(e -> dispose());

        bloodGroupComboBox.addActionListener(e -> updateGroupNameLabel());

        loadComboBoxes();
        loadBloodStockData();
        // Add the footer panel at the bottom
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Ensure the footer has a fixed height
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bloodGroupComboBox.addItem(resultSet.getString("BloodGroupId"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGroupNameLabel() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();

        if (selectedBloodGroupId != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(selectedBloodGroupId));
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String groupName = resultSet.getString("GroupName");
                    groupNameLabel.setText(groupName);
                } else {
                    groupNameLabel.setText("GroupName not found");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                groupNameLabel.setText("Error retrieving GroupName");
            }
        }
    }

    private void addBloodStock() {
        String bloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String bloodUnitText = bloodUnitField.getText();
        String groupName = groupNameLabel.getText();

        if (bloodGroupId == null || bloodUnitText.isEmpty() || groupName.isEmpty()) {
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
            String query = "INSERT INTO BloodStock (BloodGroupId, GroupName, BloodUnit) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Integer.parseInt(bloodGroupId));
            statement.setString(2, groupName); // Correctly set the GroupName
            statement.setInt(3, bloodUnit);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blood stock added successfully.");
            loadBloodStockData(); // Refresh table data
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding blood stock.");
        }
    }

    private void loadBloodStockData() {
        // Clear the table
        tableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT bs.StockId, bs.BloodGroupId, bs.GroupName, bs.BloodUnit " +
                           "FROM BloodStock bs";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int stockId = resultSet.getInt("StockId");
                int bloodGroupId = resultSet.getInt("BloodGroupId");
                String groupName = resultSet.getString("GroupName");
                int bloodUnit = resultSet.getInt("BloodUnit");

                tableModel.addRow(new Object[]{stockId, bloodGroupId, groupName, bloodUnit});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading blood stock data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddBloodStock().setVisible(true));
    }
}

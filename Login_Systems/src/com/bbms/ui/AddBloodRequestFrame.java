package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import net.proteanit.sql.DbUtils;

@SuppressWarnings("serial")
public class AddBloodRequestFrame extends JFrame {
    private JComboBox<Integer> patientIdComboBox;
    private JComboBox<Integer> bloodGroupIdComboBox;
    private JComboBox<String> groupNameComboBox;
    private JTextField requestDateField;
    private JComboBox<String> requestStatusComboBox;
    private JButton addButton;
    private JButton closeButton;
    private JTable bloodRequestTable;
    private JScrollPane tableScrollPane;

    public AddBloodRequestFrame() {
        setTitle("Add Blood Request");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the background color of the frame
        getContentPane().setBackground(Color.PINK);

        JPanel formPanel = new JPanel(new GridLayout(6, 2));
        formPanel.setBackground(Color.PINK); // Set background color for the panel
        add(formPanel, BorderLayout.NORTH);

        formPanel.add(new JLabel("PatientId:"));
        patientIdComboBox = new JComboBox<>();
        formPanel.add(patientIdComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupIdComboBox = new JComboBox<>();
        formPanel.add(bloodGroupIdComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("RequestDate (YYYY-MM-DD):"));
        requestDateField = new JTextField();
        formPanel.add(requestDateField);

        formPanel.add(new JLabel("RequestStatus:"));
        requestStatusComboBox = new JComboBox<>(new String[]{"Pending", "Approved", "Rejected"});
        formPanel.add(requestStatusComboBox);

        addButton = new JButton("Add");
        formPanel.add(addButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);

        bloodRequestTable = new JTable();
        tableScrollPane = new JScrollPane(bloodRequestTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Set background color for the table and its scroll pane
        bloodRequestTable.setBackground(Color.PINK);
        tableScrollPane.setBackground(Color.PINK);

        // Load ComboBox Data
        loadComboBoxData();

        // Load Table Data
        loadTableData();

        // Add action listeners
        addButton.addActionListener(e -> addBloodRequest());
        closeButton.addActionListener(e -> dispose());

        // Add ActionListener for GroupName ComboBox
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());
        // Add the footer panel at the bottom
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setPreferredSize(new Dimension(getWidth(), 50)); // Ensure the footer has a fixed height
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void loadComboBoxData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load PatientId
            String patientQuery = "SELECT PatientId FROM Patient";
            PreparedStatement patientStatement = connection.prepareStatement(patientQuery);
            ResultSet patientResultSet = patientStatement.executeQuery();
            while (patientResultSet.next()) {
                patientIdComboBox.addItem(patientResultSet.getInt("PatientId"));
            }

            // Load BloodGroupId
            String bloodGroupQuery = "SELECT BloodGroupId FROM BloodGroup";
            PreparedStatement bloodGroupStatement = connection.prepareStatement(bloodGroupQuery);
            ResultSet bloodGroupResultSet = bloodGroupStatement.executeQuery();
            while (bloodGroupResultSet.next()) {
                bloodGroupIdComboBox.addItem(bloodGroupResultSet.getInt("BloodGroupId"));
            }

            // Load GroupName
            String groupNameQuery = "SELECT DISTINCT GroupName FROM BloodGroup";
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

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodRequestId, PatientId, BloodGroupId, GroupName, RequestDate, RequestStatus FROM BloodRequest";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            bloodRequestTable.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading table data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBloodRequest() {
        Integer patientId = (Integer) patientIdComboBox.getSelectedItem();
        Integer bloodGroupId = (Integer) bloodGroupIdComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem();
        String requestDateText = requestDateField.getText();

        // Validate date format
        if (!isValidDate(requestDateText)) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Use YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (patientId == null || bloodGroupId == null || groupName == null || requestDateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Blood request added successfully.");

            // Reload table data after adding
            loadTableData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding blood request.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidDate(String date) {
        try {
            java.sql.Date.valueOf(date);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void updateBloodGroupId() {
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
        if (selectedGroupName == null) return;

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT BloodGroupId FROM BloodGroup WHERE GroupName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedGroupName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Integer bloodGroupId = resultSet.getInt("BloodGroupId");
                bloodGroupIdComboBox.setSelectedItem(bloodGroupId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating BloodGroupId.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddBloodRequestFrame().setVisible(true));
    }
}

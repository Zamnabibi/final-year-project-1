package com.bbms.ui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DonorSearchFrame extends JFrame {

    private JTable donorTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> patientComboBox;
    private DefaultComboBoxModel<String> patientComboBoxModel;
    private Map<Integer, String> patientIdToNameMap;
    private Integer selectedDonorId = null; // To track the currently selected donor

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/blood";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "zamna0";

    public DonorSearchFrame() {
        // Frame settings
        setTitle("Donor List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table model
        tableModel = new DefaultTableModel(new String[]{"Donor ID", "Name", "Group Name", "Contact", "Email"}, 0);
        donorTable = new JTable(tableModel);
        donorTable.setFillsViewportHeight(true);

        // Scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(donorTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel for controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1)); // Adjusted layout to fit the new controls

        // Button for search
        JButton searchButton = new JButton("Search by Group Name");
        searchButton.addActionListener(e -> showGroupNameDialog());

        // ComboBox for selecting patients
        patientComboBoxModel = new DefaultComboBoxModel<>();
        patientComboBox = new JComboBox<>(patientComboBoxModel);
        controlPanel.add(new JLabel("Select Patient:"));
        controlPanel.add(patientComboBox);

        controlPanel.add(searchButton);
        add(controlPanel, BorderLayout.NORTH);

        // Initialize patient ID to name map
        patientIdToNameMap = new HashMap<>();

        // Load data into the ComboBox
        loadPatientData();

        // Add row selection listener
        donorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && donorTable.getSelectedRow() != -1) {
                    int selectedRow = donorTable.getSelectedRow();
                    Integer donorId = (Integer) tableModel.getValueAt(selectedRow, 0);
                    String contact = (String) tableModel.getValueAt(selectedRow, 3);
                    String email = (String) tableModel.getValueAt(selectedRow, 4);

                    // Update the selected donor ID
                    selectedDonorId = donorId;

                    int response = JOptionPane.showConfirmDialog(DonorSearchFrame.this,
                            "Contact me on this number: " + contact + "\nOr email me at: " + email + "\nDo you want to confirm this donor?",
                            "Contact Information",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);

                    if (response == JOptionPane.YES_OPTION) {
                        moveToDonorSelectedTable(selectedRow);
                    }
                }
            }
        });
    }

    private void showGroupNameDialog() {
        // Create and show a dialog to input the group name
        String groupName = JOptionPane.showInputDialog(this, "Enter Group Name:");
        if (groupName != null && !groupName.trim().isEmpty()) {
            searchDonorsByGroupName(groupName.trim());
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a valid Group Name to search.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDonorData(String groupName) {
        // Clear the existing data
        tableModel.setRowCount(0);

        String query = "SELECT u.UserId, u.Name, d.GroupName, u.ContactNo, u.Email " +
                       "FROM Donor d " +
                       "JOIN User u ON d.UserId = u.UserId " +
                       "WHERE d.GroupName = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, groupName);
            try (ResultSet rs = pstmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    int userId = rs.getInt("UserId");
                    String name = rs.getString("Name");
                    String groupNameFromDb = rs.getString("GroupName");
                    String contact = rs.getString("ContactNo");
                    String email = rs.getString("Email");

                    tableModel.addRow(new Object[]{userId, name, groupNameFromDb, contact, email});
                }

                if (!hasResults) {
                    JOptionPane.showMessageDialog(this, "No donors found for the specified group name.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading donor data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPatientData() {
        String query = "SELECT p.PatientId, u.Name FROM Patient p " +
                       "JOIN User u ON p.PatientId = u.UserId";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int patientId = rs.getInt("PatientId");
                String patientName = rs.getString("Name");
                patientComboBoxModel.addElement(patientId + " - " + patientName);
                patientIdToNameMap.put(patientId, patientName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchDonorsByGroupName(String groupName) {
        loadDonorData(groupName);
    }
    private void moveToDonorSelectedTable(int selectedRow) {
        if (selectedDonorId == null) {
            JOptionPane.showMessageDialog(this, "No donor is selected", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int donorId = (int) tableModel.getValueAt(selectedRow, 0);

        // Check if the donor still exists in the database
        String checkDonorQuery = "SELECT COUNT(*) FROM Donor WHERE UserId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(checkDonorQuery)) {

            pstmt.setInt(1, donorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    // Donor is no longer available
                    JOptionPane.showMessageDialog(this, "The selected donor is no longer available", "Error", JOptionPane.ERROR_MESSAGE);
                    // Optionally, remove the row from the table
                    tableModel.removeRow(selectedRow);
                    selectedDonorId = null; // Reset the selected donor ID
                    return;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking donor availability: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the donor already exists in the DonorSelected table
        String checkDonorSelectedQuery = "SELECT COUNT(*) FROM DonorSelected WHERE DonorId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(checkDonorSelectedQuery)) {

            pstmt.setInt(1, donorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Donor is already in the DonorSelected table
                    JOptionPane.showMessageDialog(this, "The selected donor is not available right now", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking DonorSelected table: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
       
        }

        // Proceed with moving data if the donor is available and not already selected
        String donorName = (String) tableModel.getValueAt(selectedRow, 1);
        String groupName = (String) tableModel.getValueAt(selectedRow, 2);
        String contact = (String) tableModel.getValueAt(selectedRow, 3);
        String email = (String) tableModel.getValueAt(selectedRow, 4);

        String selectedPatient = (String) patientComboBox.getSelectedItem();
        if (selectedPatient == null) {
            JOptionPane.showMessageDialog(this, "Please select a patient first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Extract patient ID and name from the ComboBox selection
        int patientId = Integer.parseInt(selectedPatient.split(" - ")[0]);
        String patientName = patientIdToNameMap.get(patientId);

        // Ensure patientName is not null
        if (patientName == null) {
            JOptionPane.showMessageDialog(this, "Error retrieving patient name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String insertQuery = "INSERT INTO DonorSelected (DonorId, DonorName, GroupName, ContactNo, Email, PatientId, PatientName) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setInt(1, donorId);
            pstmt.setString(2, donorName);
            pstmt.setString(3, groupName);
            pstmt.setString(4, contact);
            pstmt.setString(5, email);
            pstmt.setInt(6, patientId);
            pstmt.setString(7, patientName);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Donor and patient data moved to DonorSelected table", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error moving data to DonorSelected table", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DonorSearchFrame frame = new DonorSearchFrame();
            frame.setVisible(true);
        });
    }
}

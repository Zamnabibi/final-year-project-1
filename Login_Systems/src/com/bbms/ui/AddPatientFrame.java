package com.bbms.ui;

import javax.swing.*;


import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;

import java.util.HashSet;

import java.util.Set;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class AddPatientFrame extends JFrame {
    private JTextField bloodUnitField;
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> groupNameComboBox;
    private JComboBox<String> hospitalComboBox;
    private JButton addButton;
    private JButton closeButton;
    private JTable donorTable;
    private DefaultTableModel donorTableModel;
    
    private Set<Integer> selectedDonors = new HashSet<>();

    public AddPatientFrame() {
        setTitle("Add Patient");
        setSize(1000, 567);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.PINK);

        JPanel formPanel = new JPanel(null);
        formPanel.setBackground(Color.PINK);
        formPanel.setBounds(10, 10, 960, 246);
        getContentPane().add(formPanel);

        JLabel userIdLabel = new JLabel("UserId:");
        userIdLabel.setBounds(10, 10, 100, 25);
        formPanel.add(userIdLabel);

        userComboBox = new JComboBox<>();
        userComboBox.setBounds(120, 10, 200, 25);
        formPanel.add(userComboBox);

        JLabel bloodGroupIdLabel = new JLabel("BloodGroupId:");
        bloodGroupIdLabel.setBounds(10, 50, 100, 25);
        formPanel.add(bloodGroupIdLabel);

        bloodGroupComboBox = new JComboBox<>();
        bloodGroupComboBox.setBounds(120, 50, 200, 25);
        formPanel.add(bloodGroupComboBox);

        JLabel groupNameLabel = new JLabel("GroupName:");
        groupNameLabel.setBounds(10, 90, 100, 25);
        formPanel.add(groupNameLabel);

        groupNameComboBox = new JComboBox<>();
        groupNameComboBox.setBounds(120, 90, 200, 25);
        formPanel.add(groupNameComboBox);

        JLabel hospitalNameLabel = new JLabel("HospitalName:");
        hospitalNameLabel.setBounds(10, 130, 100, 25);
        formPanel.add(hospitalNameLabel);

        hospitalComboBox = new JComboBox<>(new String[]{
                "Shaukat Khanum Memorial Cancer Hospital & Research Centre, Johar Town, Lahore",
                "Mayo Hospital, Nila Gumbad, Anarkali, Lahore",
        });
        hospitalComboBox.setBounds(120, 130, 400, 25);
        formPanel.add(hospitalComboBox);

        JLabel bloodUnitLabel = new JLabel("BloodUnit:");
        bloodUnitLabel.setBounds(10, 170, 100, 25);
        formPanel.add(bloodUnitLabel);

        bloodUnitField = new JTextField();
        bloodUnitField.setBounds(120, 170, 200, 25);
        formPanel.add(bloodUnitField);

        addButton = new JButton("Search Donor");
        addButton.setBounds(120, 206, 150, 30);
        formPanel.add(addButton);

        closeButton = new JButton("Close");
        closeButton.setBounds(314, 206, 150, 30);
        formPanel.add(closeButton);

        loadIcons();
        loadComboBoxes();
    

        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());
        addButton.addActionListener(e -> addPatient());
        closeButton.addActionListener(e -> dispose());

        donorTableModel = new DefaultTableModel();
        donorTableModel.setColumnIdentifiers(new Object[]{"Donor ID", "Name", "Email", "Blood Group ID", "Group Name", "Amount", "ContactNo"});
        donorTable = new JTable(donorTableModel);
        donorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        donorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                handleTableSelection();
            }
        });

        JScrollPane donorTableScrollPane = new JScrollPane(donorTable);
        donorTableScrollPane.setBounds(10, 250, 960, 207);
        getContentPane().add(donorTableScrollPane);

        donorTable.setBackground(Color.PINK);
        donorTable.setOpaque(true);
        donorTable.getTableHeader().setBackground(Color.white);
        donorTable.getTableHeader().setForeground(Color.BLACK);
        // Footer panel setup
        JPanel footerPanel = new JPanel(null); // Using null layout for absolute positioning within the footer panel
        footerPanel.setBackground(Color.PINK);
        footerPanel.setBounds(10, 490, 960, 70); // Positioning the footer panel at the bottom of the frame
        getContentPane().add(footerPanel);

        // Footer label
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setBounds(0, -12, 960, 37); // Center the label within the footer panel
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.BLACK);
        footerPanel.add(footerLabel);
        loadDonors((String) groupNameComboBox.getSelectedItem());
    }

    private void loadIcons() {
        try {
            addButton.setIcon(new ImageIcon(getClass().getResource("/add new.png")));
        } catch (Exception e) {
            System.out.println("Add button icon not found.");
        }
        try {
            closeButton.setIcon(new ImageIcon(getClass().getResource("/close.png")));
        } catch (Exception e) {
            System.out.println("Close button icon not found.");
        }
    }

    private void handleTableSelection() {
        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow != -1) {
            int selectedDonorId = (Integer) donorTableModel.getValueAt(selectedRow, 0);
            if (selectedDonors.contains(selectedDonorId)) {
                JOptionPane.showMessageDialog(this,
                        "This donor is already selected by another patient.",
                        "Donor Not Available",
                        JOptionPane.WARNING_MESSAGE);
                donorTable.clearSelection();
            } else {
                String contactNo = getContactNumberForDonor(selectedDonorId);
                String message = String.format("Contact me on mail and contact me on my number.\nContact Number: %s", contactNo);
                JOptionPane.showMessageDialog(this,
                        message,
                        "Donor Information",
                        JOptionPane.INFORMATION_MESSAGE);
                selectedDonors.add(selectedDonorId);

                if (!isDonorInRequestTable(selectedDonorId)) {
                    moveDonorToRequestTable(selectedDonorId);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "This donor is not availablee.",
                            "Donor Not Available",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private boolean isDonorInRequestTable(int donorId) {
        String sql = "SELECT COUNT(*) FROM Request WHERE DonorId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking request table: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private void moveDonorToRequestTable(int donorId) {
        String sql = "INSERT INTO Request (DonorId, UserId,Name, BloodGroupId, GroupName, Amount, ContactNo) " +
                     "SELECT DonorId, UserId,Name, BloodGroupId, GroupName, Amount, ContactNo FROM Donor WHERE DonorId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donorId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Request is send by Email", "by providing info", JOptionPane.INFORMATION_MESSAGE);
                new EmailFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add donor to request table.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error moving donor to request table: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getContactNumberForDonor(int donorId) {
        String contactNumber = null;
        String sql = "SELECT ContactNo FROM Donor WHERE DonorId = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0");
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, donorId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    contactNumber = rs.getString("ContactNo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactNumber;
    }

    private void loadComboBoxes() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            loadUserComboBox(conn);
            loadBloodGroupComboBox(conn);
            loadGroupNameComboBox(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading combo boxes: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUserComboBox(Connection conn) throws SQLException {
        String sql = "SELECT UserId FROM Donor";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                userComboBox.addItem(rs.getString("UserId"));
            }
        }
    }

    private void loadBloodGroupComboBox(Connection conn) throws SQLException {
        String sql = "SELECT BloodGroupId FROM BloodGroup";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                bloodGroupComboBox.addItem(rs.getString("BloodGroupId"));
            }
        }
    }

    private void loadGroupNameComboBox(Connection conn) throws SQLException {
        String sql = "SELECT DISTINCT GroupName FROM BloodGroup";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                groupNameComboBox.addItem(rs.getString("GroupName"));
            }
        }
    }

    public void loadDonors(String groupName) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT d.DonorId, d.Name, u.Email, d.BloodGroupId, bg.GroupName, u.Amount, d.ContactNo " +
                           "FROM Donor d " +
                           "JOIN BloodGroup bg ON d.BloodGroupId = bg.BloodGroupId " +
                           "JOIN User u ON d.UserId = u.UserId " +
                           "WHERE bg.GroupName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, groupName);
                try (ResultSet resultSet = statement.executeQuery()) {

                    

                    donorTableModel.setRowCount(0); // Clear existing data

                    while (resultSet.next()) {
                        int donorId = resultSet.getInt("DonorId");
                        String name = resultSet.getString("Name");
                        String email = resultSet.getString("Email");
                        int bloodGroupId = resultSet.getInt("BloodGroupId");
                        String donorGroupName = resultSet.getString("GroupName");
                        BigDecimal amount = resultSet.getBigDecimal("Amount");
                        String contactNo = resultSet.getString("ContactNo");

                        
                        donorTableModel.addRow(new Object[]{donorId, name, email, bloodGroupId, donorGroupName, amount, contactNo});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading donors: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateGroupName() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        if (selectedBloodGroupId != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT GroupName FROM BloodGroup WHERE BloodGroupId = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, selectedBloodGroupId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            groupNameComboBox.setSelectedItem(rs.getString("GroupName"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating group name: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateBloodGroupId() {
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
        if (selectedGroupName != null) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT BloodGroupId FROM BloodGroup WHERE GroupName = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, selectedGroupName);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            bloodGroupComboBox.setSelectedItem(rs.getString("BloodGroupId"));
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating blood group ID: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addPatient() {
        String userIdText = (String) userComboBox.getSelectedItem();
        String bloodGroupIdText = (String) bloodGroupComboBox.getSelectedItem();
        String groupName = (String) groupNameComboBox.getSelectedItem();
        String hospitalName = (String) hospitalComboBox.getSelectedItem();
        String bloodUnitText = bloodUnitField.getText();

        // Validate input fields
        if (userIdText == null || bloodGroupIdText == null || groupName == null || hospitalName == null || bloodUnitText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill out all fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId, bloodGroupId, bloodUnit;

        try {
            userId = Integer.parseInt(userIdText);
            bloodGroupId = Integer.parseInt(bloodGroupIdText);
            bloodUnit = Integer.parseInt(bloodUnitText); // Ensure bloodUnit is also parsed

            // You can remove this line if 'selectedPatientRow' is not used
            // int selectedPatientRow = patientTable.getSelectedRow();

            String sql = "INSERT INTO Patient (UserId, BloodGroupId, GroupName, HospitalName, BloodUnit) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                // Set parameters for the PreparedStatement
                statement.setInt(1, userId);
                statement.setInt(2, bloodGroupId);
                statement.setString(3, groupName);
                statement.setString(4, hospitalName);
                statement.setInt(5, bloodUnit);
             

                // Execute the update
                statement.executeUpdate();

                // Notify the user
                JOptionPane.showMessageDialog(this, "Send Request.");

                // Reload table data and donor list based on the selected group
                
                loadDonors(groupName);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddPatientFrame().setVisible(true));
    }
}

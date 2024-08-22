package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.bbms.util.DatabaseConnection;


import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class AdddPatientFrame extends JFrame {
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> groupNameComboBox;
    private JTextField bloodUnitField;
    private JComboBox<String> hospitalComboBox;
    private JTable donorTable;
    private DefaultTableModel donorTableModel;
    private Map<String, String> bloodGroupMap = new HashMap<>();
    private Map<String, String> groupNameMap = new HashMap<>();
	private JPanel footerPanel;
	private JLabel footerLabel;

    public AdddPatientFrame() {
        setTitle("Patient Frame");
        setSize(1000, 536);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        formPanel.setBackground(Color.PINK);
        getContentPane().add(formPanel, BorderLayout.NORTH);

        formPanel.add(new JLabel("User ID:"));
        userComboBox = new JComboBox<>();
        formPanel.add(userComboBox);

        formPanel.add(new JLabel("Blood Group ID:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("Group Name:"));
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("Hospital Name:"));
        hospitalComboBox = new JComboBox<>(new String[]{
        		"Shaukat Khanum Memorial Cancer Hospital & Research Centre, Johar Town, Lahore",
                "Mayo Hospital, Nila Gumbad, Anarkali, Lahore",
                "Jinnah Hospital Allama Shabbir Ahmad Usmani Rd, Punjab University New Campus, Lahore",
                "Services Hospital Ghaus-ul-Azam, Jail Road, Lahore",
                "Ganga Ram Hospital Queens Road, Lahore",
                "Sheikh Zayed Hospital University Avenue, New Muslim Town, Lahore",
                "Sir Ganga Ram Hospital Queens Road, Lahore",
                "Punjab Institute of Cardiology Jail Road, Lahore",
                "Doctors Hospital & Medical Centre 152-G/1 Canal Bank, Johar Town, Lahore",
                "Fatima Memorial Hospital Shadman Rd, Shadman 1, Lahore",
                "Ittefaq Hospital Model Town, Lahore",
                "Hameed Latif Hospital 14 Abubakar Block Garden Town, Lahore",
                "Lahore General Hospital Ferozepur Road, Lahore",
                "National Hospital & Medical Centre 132/3, L-Block, DHA Phase 1, Lahore",
                "Farooq Hospital Westwood Colony, Lahore",
                "Chughtai Lab & Healthcare 6-A Jail Road, Main Gulberg, Lahore",
                "Shaikh Zayed Medical Complex University Avenue, New Muslim Town, Lahore",
                "Central Park Teaching Hospital Central Park Housing Scheme, Ferozepur Road, Lahore",
                "Children Hospital & Institute of Child Health Ferozepur Road, Lahore",
                "Surgimed Hospital 1 Zafar Ali Road, Gulberg V, Lahore",
                "Ali Medical Centre Main Boulevard, Allama Iqbal Town, Lahore",
                "Khan Hospital Model Town, Lahore",
                "Faisal Hospital New Garden Town, Lahore",
                "Hafeez Hospital Johar Town, Lahore",
                "Mughal Eye Hospital 66 Allama Iqbal Road, Mughalpura, Lahore",
                "Bahria International Hospital Takbeer Block, Sector B, Bahria Town, Lahore",
                "Akhter Saeed Trust Hospital E-Block, Main Boulevard, Johar Town, Lahore",
                "Hameed Latif Cardiac Centre Garden Town, Lahore",
                "Omar Hospital & Cardiac Centre Jail Road, Lahore",
                "Sharif Medical City Hospital Jati Umra, Raiwind Road, Lahore",
                "Life Care Hospital Wapda Town, Lahore",
                "Masood Hospital Garden Town, Lahore",
                "Mid City Hospital 3-A Shadman II, Lahore",
                "Chiniot General Hospital Allama Iqbal Town, Lahore",
                "Social Security Hospital Multan Road, Lahore",
                "United Christian Hospital Gulberg III, Lahore",
                "Abdul Sattar Edhi Hospital Sabzazar, Lahore",
                "Iqra Medical Complex Johar Town, Lahore",
                "Sir Syed Hospital GT Road, Lahore",
                "Horizon Hospital 7-J, Block E-2, Wapda Town, Lahore",
                "Prime Care Hospital Barkat Market, New Garden Town, Lahore",
                "Rashid Latif Hospital Ferozepur Road, Lahore",
                "Saira Memorial Hospital Model Town, Lahore",
                "Arif Memorial Teaching Hospital Ferozepur Road, Lahore",
                "Umer Hospital Gulshan-e-Ravi, Lahore",
                "Lahore Care Hospital Cantt, Lahore",
                "Mian Muhammad Trust Hospital Sanda Road, Lahore",
                "Mukhtar Munir Hospital Model Town, Lahore",
                "Al-Naeem Medical Centre Faisal Town, Lahore",
                "Saadan Hospital DHA Phase 5, Lahore",
                "Lahore Medical Complex Ferozepur Road, Lahore",
                "Aziz Fatimah Medical & Dental College Hospital Main Sheikhupura Road, Faisalabad, Lahore",
                "Mumtaz Bakhtawar Hospital Thokar Niaz Baig, Lahore",
                "KKT Orthopedic Spine Center Gulberg III, Lahore",
                "Sir Ganga Ram Hospital (Emergency Wing) Queens Road, Lahore",
                "Gulab Devi Chest Hospital Ferozepur Road, Lahore",
                "Aadil Hospital DHA Phase 3, Lahore",
                "Hijaz Hospital Gulberg III, Lahore",
                "Ibn-e-Sina Hospital DHA Phase 4, Lahore",
                "Surgimed Hospital (Emergency) Zafar Ali Road, Lahore",
                "Lahore Dental & Medical Hospital Canal Bank Road, Lahore",
                "Iffat Anwar Medical Complex Gulberg III, Lahore",
                "Muslim Town Hospital Muslim Town, Lahore",
                "Mansoora Hospital Multan Road, Lahore",
                "Iqra Medical Complex (Ext) Johar Town, Lahore",
                "National Hospital & Medical Centre (Emergency) DHA Phase 1, Lahore",
                "Shalamar Hospital Shalimar Link Road, Lahore",
                "Punjab Institute of Neurosciences Jail Road, Lahore",
                "Punjab Institute of Mental Health Ferozepur Road, Lahore",
                "Al Razi Healthcare Main Boulevard, Gulberg III, Lahore",
                "Yousaf Medical Centre Multan Road, Lahore",
                "Mughal Eye Hospital (Branch) Gulberg III, Lahore",
                "Sheikh Zayed Hospital (Emergency) New Muslim Town, Lahore",
                "Shalamar Hospital (Emergency) Shalimar Link Road, Lahore",
                "Lahore General Hospital (Emergency) Ferozepur Road, Lahore",
                "Punjab Institute of Cardiology (Emergency) Jail Road, Lahore",
                "Children Hospital (Emergency) Ferozepur Road, Lahore",
                "Doctors Hospital & Medical Centre (Emergency) Johar Town, Lahore",
                "Hameed Latif Hospital (Emergency) Garden Town, Lahore",
                "Shaikh Zayed Medical Complex (Emergency) New Muslim Town, Lahore",
                "Shaukat Khanum Cancer Hospital (Emergency) Johar Town, Lahore",
                "Ittefaq Hospital (Emergency) Model Town, Lahore",
                "Fatima Memorial Hospital (Emergency) Shadman, Lahore",
                "Mayo Hospital (Emergency) Anarkali, Lahore",
                "Jinnah Hospital (Emergency) New Campus, Lahore",
                "Services Hospital (Emergency) Jail Road, Lahore",
                "Sir Ganga Ram Hospital (Emergency) Queens Road, Lahore",
                "Central Park Teaching Hospital (Emergency) Ferozepur Road, Lahore",
                "Social Security Hospital (Emergency) Multan Road, Lahore",
                "Chughtai Lab & Healthcare (Emergency) Main Gulberg, Lahore",
                "Horizon Hospital (Emergency) Wapda Town, Lahore",
                "Prime Care Hospital (Emergency) Garden Town, Lahore",
                "Masood Hospital (Emergency) Garden Town, Lahore",
                "Omar Hospital & Cardiac Centre (Emergency) Jail Road, Lahore",
                "Rashid Latif Hospital (Emergency) Ferozepur Road, Lahore",
                "Saira Memorial Hospital (Emergency) Model Town, Lahore",
                "Mid City Hospital (Emergency) Shadman II, Lahore",
                "Bahria International Hospital (Emergency) Bahria Town, Lahore",
                "Gulab Devi Chest Hospital (Emergency) Ferozepur Road, Lahore",
                "Lahore Medical Complex (Emergency) Ferozepur Road, Lahore"
            // Add other options here...
        });
        formPanel.add(hospitalComboBox);

        formPanel.add(new JLabel("Blood Unit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);
        
        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Patient");
        JButton closeButton = new JButton("Close");
       
        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);

        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
            addButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Add icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }

        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buttonPanel);
        
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        donorTableModel = new DefaultTableModel(new Object[]{
            "Donor ID", "User ID", "Blood Group ID", "Name", "Email", "Contact No", "Amount", "Group Name"
        }, 0);
        donorTable = new JTable(donorTableModel);
        donorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane donorTableScrollPane = new JScrollPane(donorTable);
        getContentPane().add(donorTableScrollPane, BorderLayout.CENTER);

        loadComboBoxes();
        customizeTable();

        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());
        addButton.addActionListener(e -> addPatient());
        closeButton.addActionListener(e -> dispose());

        donorTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = donorTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int donorId = (Integer) donorTableModel.getValueAt(selectedRow, 0); // Donor ID column
                        moveToRequestTable(donorId);
                        String email = (String) donorTableModel.getValueAt(selectedRow, 4); // Email column
                        String contactNo = (String) donorTableModel.getValueAt(selectedRow, 5); // Contact No column
                        JOptionPane.showMessageDialog(
                            AdddPatientFrame.this,
                            "Contact me through email: " + email + "\nContact me through contact no: " + contactNo,
                            "Contact Information",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
            }
        });
    }

    private void loadComboBoxes() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Load User IDs
            String query = "SELECT UserId FROM User WHERE UserType = 'Patient'";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    userComboBox.addItem(resultSet.getString("UserId"));
                }
            }

            // Load Blood Groups
            query = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String bloodGroupId = resultSet.getString("BloodGroupId");
                    String groupName = resultSet.getString("GroupName");
                    bloodGroupComboBox.addItem(bloodGroupId);
                    groupNameComboBox.addItem(groupName);
                    bloodGroupMap.put(bloodGroupId, groupName);
                    groupNameMap.put(groupName, bloodGroupId);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading combo box data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateGroupName() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        if (selectedBloodGroupId != null) {
            String groupName = bloodGroupMap.get(selectedBloodGroupId);
            groupNameComboBox.setSelectedItem(groupName);
        }
    }

    private void updateBloodGroupId() {
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem();
        if (selectedGroupName != null) {
            String bloodGroupId = groupNameMap.get(selectedGroupName);
            bloodGroupComboBox.setSelectedItem(bloodGroupId);
        }
    }

    private void addPatient() {
        String userId = (String) userComboBox.getSelectedItem();
        String bloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        String hospitalName = (String) hospitalComboBox.getSelectedItem();
        String bloodUnit = bloodUnitField.getText();
        String selectedGroupName = (String) groupNameComboBox.getSelectedItem(); // Get selected GroupName

        if (userId == null || bloodGroupId == null || hospitalName.isEmpty() || bloodUnit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Show confirmation dialog
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to add this patient?",
            "Confirm Addition",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation != JOptionPane.YES_OPTION) {
            return; // If user clicks No, exit the method
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Add the patient to the database
            String query = "INSERT INTO Patient (UserId, BloodGroupId, HospitalName, BloodUnit) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userId);
                statement.setString(2, bloodGroupId);
                statement.setString(3, hospitalName);
                statement.setInt(4, Integer.parseInt(bloodUnit));
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Patient added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            // Load donors with the same GroupName
            loadDonorsByGroupName(selectedGroupName);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDonorsByGroupName(String groupName) {
        donorTableModel.setRowCount(0);

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT d.DonorId, d.UserId, d.BloodGroupId, u.Name, u.Email, u.ContactNo, d.BloodUnit AS Amount, bg.GroupName " +
                           "FROM Donor d " +
                           "JOIN BloodGroup bg ON d.BloodGroupId = bg.BloodGroupId " +
                           "JOIN User u ON d.UserId = u.UserId " +
                           "WHERE bg.GroupName = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, groupName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        JOptionPane.showMessageDialog(this, "No donors found for the selected Group Name.", "No Data", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        do {
                            donorTableModel.addRow(new Object[]{
                                resultSet.getInt("DonorId"),
                                resultSet.getString("UserId"),
                                resultSet.getString("BloodGroupId"),
                                resultSet.getString("Name"),
                                resultSet.getString("Email"),
                                resultSet.getString("ContactNo"),
                                resultSet.getInt("Amount"),
                                resultSet.getString("GroupName")
                            });
                        } while (resultSet.next());
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading donor data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moveToRequestTable(int donorId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if the donor already exists in the Request table
            String checkQuery = "SELECT COUNT(*) FROM Request WHERE DonorId = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setInt(1, donorId);
                try (ResultSet checkResultSet = checkStatement.executeQuery()) {
                    if (checkResultSet.next() && checkResultSet.getInt(1) > 0) {
                        // Donor already exists in the Request table
                        JOptionPane.showMessageDialog(
                            this,
                            "Donor is not available",
                            "Right Now",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return ; // Exit the method if the donor already exists
                        
                    }
                }
            }

            // Fetch donor details
            String fetchQuery = "SELECT UserId, BloodGroupId FROM Donor WHERE DonorId = ?";
            try (PreparedStatement fetchStatement = connection.prepareStatement(fetchQuery)) {
                fetchStatement.setInt(1, donorId);
                try (ResultSet fetchResultSet = fetchStatement.executeQuery()) {
                    if (fetchResultSet.next()) {
                        String userId = fetchResultSet.getString("UserId");
                        String bloodGroupId = fetchResultSet.getString("BloodGroupId");

                        // Insert into Request table
                        String insertQuery = "INSERT INTO Request (UserId, BloodGroupId, DonorId) VALUES (?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                            insertStatement.setString(1, userId);
                            insertStatement.setString(2, bloodGroupId);
                            insertStatement.setInt(3, donorId);

                            insertStatement.executeUpdate();

                            // Optionally, notify the user that the donor was successfully moved
                            JOptionPane.showMessageDialog(
                                this,
                                "Request send",
                                "Successfully",
                                JOptionPane.INFORMATION_MESSAGE
                                
                            );
                        }
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error moving donor data to request table: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void customizeTable() {
        JTableHeader header = donorTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        donorTable.setFont(new Font("Arial", Font.PLAIN, 12));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdddPatientFrame().setVisible(true));
    }
}

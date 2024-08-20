package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class UpdatePatientFrame extends JFrame {
    private JTextField bloodUnitField;
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> hospitalComboBox; // Changed to hospitalNameComboBox
    private JLabel groupNameLabel;
    private JButton updateButton;
    private JButton closeButton;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private Map<String, String> bloodGroupMap;
	private JPanel footerPanel;
	private JLabel footerLabel;

    public UpdatePatientFrame() {
        setTitle("Update Patient");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a pink background JPanel for the form
        JPanel formPanel = new JPanel(new GridLayout(7, 2)); // Updated to 7 rows
        formPanel.setBackground(Color.PINK);
        add(formPanel, BorderLayout.NORTH);

        // Form setup
        formPanel.add(new JLabel("UserId:"));
        userComboBox = new JComboBox<>();
        formPanel.add(userComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameLabel = new JLabel();
        formPanel.add(groupNameLabel);

        formPanel.add(new JLabel("HospitalName:"));
        hospitalComboBox = new JComboBox<>(new String[] { 
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
        formPanel.add(hospitalComboBox);// Initialize with empty items
        formPanel.add(hospitalComboBox);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        // Initialize buttons
        updateButton = new JButton("Update Patient");
        closeButton = new JButton("Close");

        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
            updateButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }

        // Add buttons to form panel
        formPanel.add(updateButton);
        formPanel.add(closeButton);


        // Initialize table and load data
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{"PatientId", "UserId", "BloodGroupId", "GroupName", "HospitalName", "BloodUnit"});
        patientTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(patientTable);

        // Set viewport background color to pink
        tableScrollPane.getViewport().setBackground(Color.PINK);

        add(tableScrollPane, BorderLayout.CENTER);
        
        // Footer Panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK);

        footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);

        getContentPane().add(footerPanel, BorderLayout.SOUTH);


        // Initialize bloodGroupMap
        bloodGroupMap = new HashMap<>();

        // Load data into the table and combo boxes
        loadComboBoxes();
        loadTableData();

        // Add action listeners
        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        updateButton.addActionListener(e -> updatePatient());
        closeButton.addActionListener(e -> dispose());

        patientTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedPatient();
            }
        });
    }

    private void loadComboBoxes() {
        // Load data into combo boxes
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT UserId FROM User WHERE UserType = 'Patient'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userComboBox.addItem(resultSet.getString("UserId"));
            }

            // Load Blood Group IDs and Group Names
            query = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String bloodGroupId = resultSet.getString("BloodGroupId");
                String groupName = resultSet.getString("GroupName");

                bloodGroupComboBox.addItem(bloodGroupId);
                bloodGroupMap.put(bloodGroupId, groupName);
            }

           
            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateGroupName() {
        String selectedBloodGroupId = (String) bloodGroupComboBox.getSelectedItem();
        if (selectedBloodGroupId != null) {
            String groupName = bloodGroupMap.get(selectedBloodGroupId);
            groupNameLabel.setText(groupName);
        }
    }

    private void loadTableData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT PatientId, UserId, BloodGroupId, GroupName, HospitalName, BloodUnit FROM Patient";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0); // Clear existing data

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getInt("PatientId"),
                    resultSet.getInt("UserId"),
                    resultSet.getInt("BloodGroupId"),
                    resultSet.getString("GroupName"),
                    resultSet.getString("HospitalName"),
                    resultSet.getInt("BloodUnit")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedPatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                int patientId = (int) patientTable.getValueAt(selectedRow, 0);
                try (Connection connection = DatabaseConnection.getConnection()) {
                    String query = "SELECT * FROM Patient WHERE PatientId = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, patientId);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        userComboBox.setSelectedItem(resultSet.getString("UserId"));
                        bloodGroupComboBox.setSelectedItem(resultSet.getString("BloodGroupId"));
                        groupNameLabel.setText(resultSet.getString("GroupName"));
                        hospitalComboBox.setSelectedItem(resultSet.getString("HospitalName"));
                        bloodUnitField.setText(resultSet.getString("BloodUnit"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePatient() {
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

            int selectedRow = patientTable.getSelectedRow();
            if (selectedRow >= 0) {
                int patientId = (int) patientTable.getValueAt(selectedRow, 0);

                String query = "UPDATE Patient SET UserId = ?, BloodGroupId = ?, GroupName = ?, HospitalName = ?, BloodUnit = ? WHERE PatientId = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt((String) userComboBox.getSelectedItem()));
                statement.setInt(2, Integer.parseInt((String) bloodGroupComboBox.getSelectedItem()));
                statement.setString(3, groupName);
                statement.setString(4, (String) hospitalComboBox.getSelectedItem());
                statement.setInt(5, Integer.parseInt(bloodUnitField.getText()));
                statement.setInt(6, patientId);

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Patient details updated successfully.");
                    loadTableData();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdatePatientFrame frame = new UpdatePatientFrame();
            frame.setVisible(true);
        });
    }
}

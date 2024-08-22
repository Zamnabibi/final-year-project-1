package com.bbms.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class UpdatePatientFrame extends JFrame {
    private JComboBox<String> userIdComboBox;
    private JTextField patientIdField;
    private JComboBox<String> hospitalNameComboBox;
    private JTextField bloodUnitField;
    private JButton updateButton;
    private JButton closeButton;
    private JPanel footerPanel;

    public UpdatePatientFrame() {
        setTitle("Update Patient Details");
        setSize(550, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        getContentPane().setBackground(Color.PINK); // Set background color to pink

        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setBounds(50, 30, 100, 25);
        getContentPane().add(userIdLabel);

        userIdComboBox = new JComboBox<>();
        userIdComboBox.setBounds(150, 30, 200, 25);
        getContentPane().add(userIdComboBox);

        JLabel patientIdLabel = new JLabel("Patient ID:");
        patientIdLabel.setBounds(50, 70, 100, 25);
        getContentPane().add(patientIdLabel);

        patientIdField = new JTextField();
        patientIdField.setBounds(150, 70, 200, 25);
        patientIdField.setEditable(false);
        getContentPane().add(patientIdField);

        JLabel hospitalNameLabel = new JLabel("Hospital Name:");
        hospitalNameLabel.setBounds(50, 110, 100, 25);
        getContentPane().add(hospitalNameLabel);

        hospitalNameComboBox = new JComboBox<>(new String[]{
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
        hospitalNameComboBox.setBounds(150, 110, 350, 25); // Adjusted width
        getContentPane().add(hospitalNameComboBox);

        JLabel bloodUnitLabel = new JLabel("Blood Unit:");
        bloodUnitLabel.setBounds(50, 150, 100, 25);
        getContentPane().add(bloodUnitLabel);

        bloodUnitField = new JTextField();
        bloodUnitField.setBounds(150, 150, 200, 25);
        getContentPane().add(bloodUnitField);
        
        // Create footer panel
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        footerPanel.setBackground(Color.PINK); // Set footer background color to pink

        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerLabel.setForeground(Color.black);

        footerPanel.add(footerLabel, BorderLayout.CENTER);
        footerPanel.setBounds(0, 270, 550, 30); // Position the footer at the bottom
        getContentPane().add(footerPanel);

        updateButton = new JButton("Update");
        updateButton.setBounds(150, 190, 100, 30);
        getContentPane().add(updateButton);
        
        closeButton = new JButton("Close");
        closeButton.setBounds(270, 190, 100, 30); // Adjusted position
        getContentPane().add(closeButton);

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

        populateUserIdComboBox();
        populateHospitalNameComboBox();

        userIdComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateFieldsBasedOnUserId();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updatePatientDetails();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the frame
            }
        });
    }

    private void populateUserIdComboBox() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0")) {
            String query = "SELECT UserId FROM Patient";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                userIdComboBox.addItem(rs.getString("UserId"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateHospitalNameComboBox() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0")) {
            String query = "SELECT DISTINCT HospitalName FROM Patient";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                hospitalNameComboBox.addItem(rs.getString("HospitalName"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateFieldsBasedOnUserId() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0")) {
            String selectedUserId = (String) userIdComboBox.getSelectedItem();
            String query = "SELECT PatientId, HospitalName, BloodUnit FROM Patient WHERE UserId = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, selectedUserId);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                patientIdField.setText(rs.getString("PatientId"));
                hospitalNameComboBox.setSelectedItem(rs.getString("HospitalName"));
                bloodUnitField.setText(rs.getString("BloodUnit"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updatePatientDetails() {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to update the patient details?", "Confirm Update", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0")) {
                String selectedUserId = (String) userIdComboBox.getSelectedItem();
                String selectedHospitalName = (String) hospitalNameComboBox.getSelectedItem();
                int bloodUnit = Integer.parseInt(bloodUnitField.getText());

                String query = "UPDATE Patient SET HospitalName = ?, BloodUnit = ? WHERE UserId = ?";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, selectedHospitalName);
                pst.setInt(2, bloodUnit);
                pst.setString(3, selectedUserId);

                int rowsUpdated = pst.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Patient details updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "No matching records found to update.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating patient details.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Update cancelled.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdatePatientFrame().setVisible(true));
    }
}

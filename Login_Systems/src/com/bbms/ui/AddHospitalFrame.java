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
public class AddHospitalFrame extends JFrame {
    private JComboBox<String> hospitalNameComboBox;
    private JTextField addressTextField;
    private JComboBox<String> cityComboBox;
    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> nameComboBox;
    private JComboBox<String> contactNoComboBox;
    private JButton addButton;
    private JButton closeButton;
    private JTable hospitalTable;

    public AddHospitalFrame() {
        setTitle("Add Hospital");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Set background color of the frame
        getContentPane().setBackground(Color.PINK);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(Color.PINK); // Set background color of the input panel

        // Initialize components
        hospitalNameComboBox = new JComboBox<>(new String[] {
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
            // Add other hospital names here
        });
        addressTextField = new JTextField(20);
        cityComboBox = new JComboBox<>(new String[] {
        		"Johar Town",
    	        "Gulberg",
    	        "DHA",
    	        "Model Town",
    	        "Bahria Town",
    	        "Garden Town",
    	        "Shadman",
    	        "Cantt",
    	        "Faisal Town",
    	        "Allama Iqbal Town",
    	        "Anarkali",
    	        "New Campus",
    	        "New Muslim Town",
    	        "Ferozepur Road",
    	        "Jail Road",
    	        "Shalimar Link Road",
    	        "Wapda Town",
    	        "Sabzazar",
    	        "Mughalpura",
    	        "Multan Road",
    	        "Raiwind Road",
    	        "Sanda Road",
    	        "Westwood Colony",
    	        "Main Boulevard",
    	        "Main Gulberg",
    	        "L-Block, DHA Phase 1",
    	        "GT Road",
    	        "University Avenue"
            // Add other cities here
        });
        userTypeComboBox = new JComboBox<>(new String[] { "Patient", "Donor" });
        nameComboBox = new JComboBox<>();
        contactNoComboBox = new JComboBox<>();
        addButton = new JButton("Add");
        closeButton = new JButton("Close");
        hospitalTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(hospitalTable);

        // Set background color of the table and scroll pane
        hospitalTable.setBackground(Color.white);
        scrollPane.setBackground(Color.PINK);

        // Add components to the input panel
        inputPanel.add(new JLabel("Hospital Name:"));
        inputPanel.add(hospitalNameComboBox);

        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressTextField);

        inputPanel.add(new JLabel("City:"));
        inputPanel.add(cityComboBox);

        inputPanel.add(new JLabel("User Type:"));
        inputPanel.add(userTypeComboBox);

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameComboBox);

        inputPanel.add(new JLabel("Contact No:"));
        inputPanel.add(contactNoComboBox);

        // Add the input panel and buttons to the frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.PINK); // Set background color of the button panel
        buttonPanel.add(addButton);
        buttonPanel.add(closeButton);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        loadNameAndContactComboBoxes();
        loadHospitalTable();

        
        
        // Action Listener for Add Button
        addButton.addActionListener(e -> {
            addHospital();
            loadHospitalTable();
        });

        // Action Listener for User Type ComboBox
        userTypeComboBox.addActionListener(e -> loadNameAndContactComboBoxes());

        // Action Listener for Close Button
        closeButton.addActionListener(e -> dispose()); // Close the frame
    }

    private void loadNameAndContactComboBoxes() {
        String selectedUserType = (String) userTypeComboBox.getSelectedItem();
        String query = "SELECT Name, ContactNo FROM User WHERE UserType = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, selectedUserType);
            try (ResultSet resultSet = statement.executeQuery()) {
                nameComboBox.removeAllItems();
                contactNoComboBox.removeAllItems();
                while (resultSet.next()) {
                    nameComboBox.addItem(resultSet.getString("Name"));
                    contactNoComboBox.addItem(resultSet.getString("ContactNo"));
                }

                if (nameComboBox.getItemCount() > 0) {
                    nameComboBox.setSelectedIndex(0);
                }
                if (contactNoComboBox.getItemCount() > 0) {
                    contactNoComboBox.setSelectedIndex(0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading user names and contact numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHospitalTable() {
        String query = "SELECT * FROM Hospital";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("HospitalId");
            model.addColumn("HospitalName");
            model.addColumn("Address");
            model.addColumn("City");
            model.addColumn("UserType");
            model.addColumn("Name");
            model.addColumn("ContactNo");

            while (resultSet.next()) {
                model.addRow(new Object[] {
                    resultSet.getInt("HospitalId"),
                    resultSet.getString("HospitalName"),
                    resultSet.getString("Address"),
                    resultSet.getString("City"),
                    resultSet.getString("UserType"),
                    resultSet.getString("Name"),
                    resultSet.getString("ContactNo")
                });
            }

            hospitalTable.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading hospital data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addHospital() {
        String hospitalName = (String) hospitalNameComboBox.getSelectedItem();
        String address = addressTextField.getText();
        String city = (String) cityComboBox.getSelectedItem();
        String userType = (String) userTypeComboBox.getSelectedItem();
        String name = (String) nameComboBox.getSelectedItem();
        String contactNo = (String) contactNoComboBox.getSelectedItem();

        String query = "INSERT INTO Hospital (HospitalName, Address, City, UserType, Name, ContactNo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, hospitalName);
            statement.setString(2, address);
            statement.setString(3, city);
            statement.setString(4, userType);
            statement.setString(5, name);
            statement.setString(6, contactNo);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Hospital added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding hospital.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AddHospitalFrame().setVisible(true));
    }
}

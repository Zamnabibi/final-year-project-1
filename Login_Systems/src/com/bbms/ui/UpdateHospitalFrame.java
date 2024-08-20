package com.bbms.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;

@SuppressWarnings("serial")
public class UpdateHospitalFrame extends JFrame {
    private JTable hospitalTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> hospitalNameComboBox;
    private JTextField addressTextField;
    private JComboBox<String> cityComboBox;
    private JComboBox<String> userTypeComboBox;
    private JComboBox<String> nameComboBox;
    private JComboBox<String> contactNoComboBox;

    private JButton updateButton;
    private JButton closeButton;

    public UpdateHospitalFrame() {
        setTitle("Hospital Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(Color.PINK);

        // Table setup
        tableModel = new DefaultTableModel();
        hospitalTable = new JTable(tableModel);
        add(new JScrollPane(hospitalTable), BorderLayout.CENTER);

        tableModel.addColumn("HospitalId");
        tableModel.addColumn("HospitalName");
        tableModel.addColumn("Address");
        tableModel.addColumn("City");
        tableModel.addColumn("UserType");
        tableModel.addColumn("Name");
        tableModel.addColumn("ContactNo");

        // Customize Table
        customizeTable();

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2));
        formPanel.setBackground(Color.PINK);
        formPanel.add(new JLabel("HospitalName:"));
        hospitalNameComboBox = new JComboBox<>(loadHospitalNames());
        formPanel.add(hospitalNameComboBox);

        formPanel.add(new JLabel("Address:"));
        addressTextField = new JTextField();
        formPanel.add(addressTextField);

        formPanel.add(new JLabel("City:"));
        cityComboBox = new JComboBox<>(loadCities());
        formPanel.add(cityComboBox);

        formPanel.add(new JLabel("UserType:"));
        userTypeComboBox = new JComboBox<>(new String[] { "Patient", "Donor" });
        formPanel.add(userTypeComboBox);

        formPanel.add(new JLabel("Name:"));
        nameComboBox = new JComboBox<>();
        formPanel.add(nameComboBox);

        formPanel.add(new JLabel("ContactNo:"));
        contactNoComboBox = new JComboBox<>();
        formPanel.add(contactNoComboBox);

        add(formPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(Color.PINK);
        updateButton = new JButton("Update");
        closeButton = new JButton("Close");

        buttonsPanel.add(updateButton);
        buttonsPanel.add(closeButton);

        add(buttonsPanel, BorderLayout.SOUTH);

        // Load Data
        loadHospitalData();
        loadNameAndContactComboBoxes();

        // Add Action Listeners
        updateButton.addActionListener(e -> updateHospital());
        closeButton.addActionListener(e -> dispose());

        // Table Selection Listener
        hospitalTable.getSelectionModel().addListSelectionListener(e -> loadSelectedHospital());

        // UserType ComboBox Action Listener
        userTypeComboBox.addActionListener(e -> loadNameAndContactComboBoxes());
    }

    private void customizeTable() {
        hospitalTable.setBackground(Color.WHITE); // Set table background to white or any other color
        hospitalTable.setSelectionBackground(Color.LIGHT_GRAY); // Optional: Set selection color to light gray

        JTableHeader tableHeader = hospitalTable.getTableHeader();
        tableHeader.setBackground(Color.LIGHT_GRAY); // Set table header background to light gray or any other color

        JScrollPane tableScrollPane = new JScrollPane(hospitalTable);
        tableScrollPane.getViewport().setBackground(Color.PINK); // Set viewport background color to pink
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private String[] loadHospitalNames() {
        return new String[] { "Shaukat Khanum Memorial Cancer Hospital & Research Centre, Johar Town, Lahore",
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
                "Lahore Medical Complex (Emergency) Ferozepur Road, Lahore" }; // Example values
    }

    private String[] loadCities() {
        return new String[] {"Johar Town",
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
    	        "University Avenue"}; // Example values
    }

    private void loadHospitalData() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT HospitalId, HospitalName, Address, City, UserType, Name, ContactNo FROM Hospital WHERE UserType IN ('Patient', 'Donor')";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            tableModel.setRowCount(0);

            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                        resultSet.getInt("HospitalId"),
                        resultSet.getString("HospitalName"),
                        resultSet.getString("Address"),
                        resultSet.getString("City"),
                        resultSet.getString("UserType"),
                        resultSet.getString("Name"),
                        resultSet.getString("ContactNo")
                });
            }
        } catch (SQLException e) {
            showError("Error loading hospital data: " + e.getMessage());
        }
    }

    private void loadNameAndContactComboBoxes() {
        String selectedUserType = (String) userTypeComboBox.getSelectedItem();
        if (selectedUserType == null) return;

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT Name, ContactNo FROM User WHERE UserType = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, selectedUserType);
            ResultSet resultSet = statement.executeQuery();

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

            // Optionally, load Address based on UserType
            query = "SELECT DISTINCT Address FROM Hospital WHERE UserType = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, selectedUserType);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                addressTextField.setText(resultSet.getString("Address"));
            }
        } catch (SQLException e) {
            showError("Error loading names and contact numbers: " + e.getMessage());
        }
    }

    private void loadSelectedHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        if (selectedRow != -1) {
            hospitalNameComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 1));
            addressTextField.setText((String) tableModel.getValueAt(selectedRow, 2));
            cityComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3));
            userTypeComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 4));
            nameComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 5));
            contactNoComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 6));
        }
    }

    private void updateHospital() {
        int selectedRow = hospitalTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("No hospital selected for update.");
            return;
        }

        int hospitalId = (int) tableModel.getValueAt(selectedRow, 0);
        String userType = (String) userTypeComboBox.getSelectedItem();
        String name = (String) nameComboBox.getSelectedItem();
        String contactNo = (String) contactNoComboBox.getSelectedItem();
        String address = addressTextField.getText();
        String hospitalName = (String) hospitalNameComboBox.getSelectedItem();
        String city = (String) cityComboBox.getSelectedItem();

        if (hospitalName == null || address.isEmpty() || city == null || userType == null || name == null || contactNo == null) {
            showError("All fields must be filled out.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE Hospital SET HospitalName = ?, Address = ?, City = ?, UserType = ?, Name = ?, ContactNo = ? WHERE HospitalId = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, hospitalName);
            statement.setString(2, address);
            statement.setString(3, city);
            statement.setString(4, userType);
            statement.setString(5, name);
            statement.setString(6, contactNo);
            statement.setInt(7, hospitalId);

            statement.executeUpdate();
            loadHospitalData();
        } catch (SQLException e) {
            showError("Error updating hospital: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateHospitalFrame().setVisible(true));
    }
}

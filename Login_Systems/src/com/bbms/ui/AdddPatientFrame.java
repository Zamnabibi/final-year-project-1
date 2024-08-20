package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.bbms.util.DatabaseConnection;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.mail.*;
import javax.mail.internet.*;

@SuppressWarnings("serial")
public class AdddPatientFrame extends JFrame {
    private JTextField bloodUnitField;
    private JComboBox<String> userComboBox;
    private JComboBox<String> bloodGroupComboBox;
    private JComboBox<String> groupNameComboBox;
    private JComboBox<String> hospitalComboBox;
    private JTextField recipientUsernameField;
    private JPasswordField recipientPasswordField;
    private JButton addButton;
    private JButton closeButton;
    private JButton sendRequestButton;
    
    private JTable donorTable;
    private DefaultTableModel donorTableModel;
    private String selectedDonorContactNumber;
    private final String placeholderText = "Enter App-Specific Password";
    private Map<String, String> bloodGroupMap = new HashMap<>();
    private Map<Integer, String> donorEmailMap = new HashMap<>();
    private Set<Integer> selectedDonors = new HashSet<>();


    public AdddPatientFrame() {
        setTitle("Add Patient");
        setSize(1000, 745);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PINK);

        JPanel formPanel = new JPanel(new GridLayout(10, 2)); // Adjusted grid layout
        formPanel.setBackground(Color.PINK);
        getContentPane().add(formPanel, BorderLayout.NORTH);

        formPanel.add(new JLabel("UserId:"));
        userComboBox = new JComboBox<>();
        formPanel.add(userComboBox);

        formPanel.add(new JLabel("BloodGroupId:"));
        bloodGroupComboBox = new JComboBox<>();
        formPanel.add(bloodGroupComboBox);

        formPanel.add(new JLabel("GroupName:"));
        groupNameComboBox = new JComboBox<>();
        formPanel.add(groupNameComboBox);

        formPanel.add(new JLabel("HospitalName:"));
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
            // Add more hospital names as needed
        });
        formPanel.add(hospitalComboBox);

        formPanel.add(new JLabel("BloodUnit:"));
        bloodUnitField = new JTextField();
        formPanel.add(bloodUnitField);

        formPanel.add(new JLabel("Recipient Username:"));
        recipientUsernameField = new JTextField();
        formPanel.add(recipientUsernameField);

        formPanel.add(new JLabel("Recipient Password:"));

        // Create the password field and placeholder label
        recipientPasswordField = new JPasswordField();
        recipientPasswordField.setColumns(20);

        // Placeholder text
        JLabel placeholderLabel = new JLabel(placeholderText);
        placeholderLabel.setForeground(Color.GRAY);
        placeholderLabel.setHorizontalAlignment(SwingConstants.LEFT);
        placeholderLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Panel to hold the password field and placeholder label
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.add(recipientPasswordField, BorderLayout.CENTER);
        passwordPanel.add(placeholderLabel, BorderLayout.WEST);

        formPanel.add(passwordPanel);

        // Handle placeholder visibility
        recipientPasswordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                placeholderLabel.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(recipientPasswordField.getPassword()).isEmpty()) {
                    placeholderLabel.setVisible(true);
                }
            }
        });

        

        addButton = new JButton("Add Patient");
        formPanel.add(addButton);

        sendRequestButton = new JButton("Send Request");
        formPanel.add(sendRequestButton);

        closeButton = new JButton("Close");
        formPanel.add(closeButton);
        
        // Load icons
        try {
            Image imgUpdate = new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
            addButton.setIcon(new ImageIcon(imgUpdate));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }
        
        try {
            Image imgsendRequest = new ImageIcon(this.getClass().getResource("/blood group.png")).getImage();
            sendRequestButton.setIcon(new ImageIcon(imgsendRequest));
        } catch (Exception e) {
            System.out.println("Update icon not found.");
        }

        try {
            Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
            closeButton.setIcon(new ImageIcon(imgClose));
        } catch (Exception e) {
            System.out.println("Close icon not found.");
        }

       
        loadComboBoxes();

        bloodGroupComboBox.addActionListener(e -> updateGroupName());
        groupNameComboBox.addActionListener(e -> updateBloodGroupId());

        addButton.addActionListener(e -> addPatient());
        closeButton.addActionListener(e -> dispose());
        sendRequestButton.addActionListener(e -> sendRequest());

      
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
        donorTableScrollPane.setBounds(10, 250, 960, 200);
        getContentPane().add(donorTableScrollPane);


        // Set donor table background color
        donorTable.setBackground(Color.PINK);
        donorTable.setOpaque(true);

        // Customize donor table header
        JTableHeader donorTableHeader = donorTable.getTableHeader();
        donorTableHeader.setBackground(Color.white);
        donorTableHeader.setForeground(Color.BLACK);
        


        loadDonors((String) groupNameComboBox.getSelectedItem());
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
                            "This donor is already in the request table.",
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
            String sql = "SELECT ContactNo FROM Donor WHERE DonorId = ?"; // Use correct column name

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0");
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, donorId); // Set the donor's ID for the query

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        contactNumber = rs.getString("ContactNo"); // Fetch the contact number if a match is found
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching contact number: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }

            return contactNumber != null ? contactNumber : "Contact number not available"; // Return default message if null
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

            // Load Blood Group IDs and Group Names
            query = "SELECT BloodGroupId, GroupName FROM BloodGroup";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String bloodGroupId = resultSet.getString("BloodGroupId");
                    String groupName = resultSet.getString("GroupName");

                    bloodGroupComboBox.addItem(bloodGroupId);
                    groupNameComboBox.addItem(groupName);

                    bloodGroupMap.put(bloodGroupId, groupName);
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
            for (Map.Entry<String, String> entry : bloodGroupMap.entrySet()) {
                if (entry.getValue().equals(selectedGroupName)) {
                    bloodGroupComboBox.setSelectedItem(entry.getKey());
                    break;
                }
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
                JOptionPane.showMessageDialog(this, "Patient added successfully.");

                // Reload table data and donor list based on the selected group
               
                loadDonors(groupName);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding patient: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number format: " + e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void sendRequest() {
        String recipientUsername = recipientUsernameField.getText();
        String recipientPassword = new String(recipientPasswordField.getPassword());

        if (recipientUsername.isEmpty() || recipientPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter recipient username and password.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selectedRow = donorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a donor from the table.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int donorId = (Integer) donorTableModel.getValueAt(selectedRow, 0);
        if (selectedDonors.contains(donorId)) {
            JOptionPane.showMessageDialog(this, "This donor is already selected by another patient.", "Donor Not Available", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String donorEmail = donorEmailMap.get(donorId);

        // Use the stored contact number
        String contectNumber = selectedDonorContactNumber;

        String from = "dollzuni468@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        
        String subject = "Blood Donation Request";
        String body = "Dear Donor,\n\n" +
                      "A patient has requested a blood donation.\n" +
                      "Please find the details below:\n" +
                      "Patient Username: " + recipientUsername + "\n" +
                      "Group Name: " + groupNameComboBox.getSelectedItem() + "\n" +
                      "Blood Unit Required: " + bloodUnitField.getText() + "\n" +
                      "Contect Number: " + contectNumber + "\n\n" +
                      "Thank you.";

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, recipientPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(donorEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            JOptionPane.showMessageDialog(this, "Request sent successfully.");
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, "Error sending email: " + e.getMessage(), "Email Error", JOptionPane.ERROR_MESSAGE);
        }
    }


   

    public void loadDonors(String groupName) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT d.DonorId, u.Name, u.Email, d.BloodGroupId, bg.GroupName, u.Amount, d.ContactNo " +
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

                        donorEmailMap.put(donorId, email);
                        donorTableModel.addRow(new Object[]{donorId, name, email, bloodGroupId, donorGroupName, amount, contactNo});
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading donors: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdddPatientFrame().setVisible(true));
    }
}


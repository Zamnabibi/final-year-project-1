package Login_Sys;

import javax.swing.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class Hospital extends JFrame {

    private String[] hospitals = {
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
        // Add more hospitals as needed
    };

    private String[] localities = {
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
        // Add more localities as needed
    };

    public Hospital() {
        // Set the title and size of the JFrame
        setTitle("Lahore Hospital Selection");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        // Create a JLabel for the role selection
        JLabel roleLabel = new JLabel("Select your role:");
        roleLabel.setBounds(10, 9, 150, 20);
        getContentPane().add(roleLabel);

        // Create radio buttons for role selection (Donor or Patient)
        JRadioButton donorRadio = new JRadioButton("Donor");
        donorRadio.setBounds(160, 5, 80, 20);
        JRadioButton patientRadio = new JRadioButton("Patient");
        patientRadio.setBounds(240, 5, 80, 20);
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(donorRadio);
        roleGroup.add(patientRadio);
        getContentPane().add(donorRadio);
        getContentPane().add(patientRadio);

        // Create a JLabel for locality selection
        JLabel localityLabel = new JLabel("Select your locality:");
        localityLabel.setBounds(10, 46, 150, 20);
        getContentPane().add(localityLabel);

        // Create a JComboBox to select localities
        JComboBox<String> localityList = new JComboBox<>(localities);
        localityList.setBounds(160, 43, 250, 20);
        getContentPane().add(localityList);

        // Create a JLabel for hospital selection
        JLabel hospitalLabel = new JLabel("Select a Hospital:");
        hospitalLabel.setBounds(10, 97, 150, 20);
        getContentPane().add(hospitalLabel);

        // Create a JComboBox to select hospitals
        JComboBox<String> hospitalList = new JComboBox<>(hospitals);
        hospitalList.setBounds(160, 94, 460, 20);
        getContentPane().add(hospitalList);

        // Create a button to confirm selection
        JButton selectButton = new JButton("Select");
        selectButton.setBounds(200, 150, 100, 30);
        getContentPane().add(selectButton);

        // Add action listener to the button
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedRole = donorRadio.isSelected() ? "Donor" : "Patient";
                String selectedLocality = (String) localityList.getSelectedItem();
                String selectedHospital = (String) hospitalList.getSelectedItem();

                // Show the selected details in a dialog box
                JOptionPane.showMessageDialog(null, "Role: " + selectedRole 
                                                   + "\nLocality: " + selectedLocality 
                                                   + "\nSelected Hospital: " + selectedHospital);

                // Insert the selected details into the database
                insertHospital(selectedRole, selectedHospital, selectedLocality);
            }
        });

        // Initial selection as donor by default
        donorRadio.setSelected(true);

        // Set the window to be visible
        setVisible(true);
        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 317, 634, 44); // Adjust size and position as needed
        getContentPane().add(footerPanel);
        
        JLabel backgroundLabel = new JLabel("");
        Image imgBackground = loadImage("/back.jpg");
        backgroundLabel.setIcon(new ImageIcon(imgBackground));
        backgroundLabel.setBounds(-59, -13, 693, 374);
        getContentPane().add(backgroundLabel);
    }

    public static void main(String[] args) {
        // Launch the application
        SwingUtilities.invokeLater(() -> new Hospital());
    }
    
    private Image loadImage(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL).getImage();
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private void insertHospital(String role, String hospitalName, String localityName) {
        String url = "jdbc:mysql://localhost:3306/bbms"; // Update with your database URL
        String user = "root"; // Update with your database username
        String password = "zamna0"; // Update with your database password

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO Hospitals (Role, HospitalName, LocalityName) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, role);
                preparedStatement.setString(2, hospitalName);
                preparedStatement.setString(3, localityName);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting data: " + e.getMessage());
        }
    }
}
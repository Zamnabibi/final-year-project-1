package Login_Sys;

import java.awt.EventQueue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Image;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class AddNewPatient extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private JLabel lblNewLabel_8;
    private Connection con;
    private JLabel timeLabel;
    private JComboBox<String> comboBox;
    private JComboBox<String> comboBox_1;
    private JComboBox<String> comboBoxCity;
    private JTextArea textArea;
    private JDateChooser dateChooser;
    

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddNewPatient frame = new AddNewPatient();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public AddNewPatient() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblNewLabel_8 = new JLabel("1");
        lblNewLabel_8.setBounds(176, 83, 46, 14);
        lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 18));
        contentPane.add(lblNewLabel_8);
        
        JLabel lblNewLabel_15 = new JLabel("UserType");
        lblNewLabel_15.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_15.setBounds(220, 79, 73, 24);
        contentPane.add(lblNewLabel_15);

        // Add time label
        timeLabel = new JLabel(); // Changed from JTextField to JLabel
        timeLabel.setBounds(640, 10, 184, 20);
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(timeLabel);

        // Set the timer to update the JLabel every second
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        // Initial time update
        updateTime();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Connection created");

            try (PreparedStatement pst = con.prepareStatement("select max(PatientId) from Patient")) {
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    int id = rs.getInt(1);
                    id = id + 1;
                    String str = String.valueOf(id);
                    lblNewLabel_8.setText(str);
                }
            }
        } catch (Exception m) {
            JOptionPane.showMessageDialog(null, m);
        }

        JLabel lblNewLabel = new JLabel("Add New Patient");
        lblNewLabel.setBounds(220, 11, 340, 54);
        lblNewLabel.setFont(new Font("Microsoft Himalaya", Font.BOLD | Font.ITALIC, 60));
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("New Patient ID");
        lblNewLabel_1.setBounds(26, 81, 119, 21);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Full Name");
        lblNewLabel_2.setBounds(26, 123, 112, 21);
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Father Name");
        lblNewLabel_3.setBounds(26, 155, 112, 21);
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Mother Name");
        lblNewLabel_4.setBounds(26, 187, 112, 21);
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Date Of Birth");
        lblNewLabel_5.setBounds(26, 219, 118, 21);
        lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Mobile no");
        lblNewLabel_6.setBounds(26, 262, 111, 21);
        lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Gender");
        lblNewLabel_7.setBounds(26, 294, 88, 21);
        lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_7);

        textField = new JTextField();
        textField.setBounds(167, 123, 161, 20);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
        textField.setFont(new Font("Dialog", Font.BOLD, 14));
        contentPane.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setBounds(167, 155, 160, 20);
        textField_1.setFont(new Font("Dialog", Font.BOLD, 14));
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setBounds(167, 262, 162, 21);
        textField_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(170, 219, 158, 20);
        contentPane.add(dateChooser);

        comboBox = new JComboBox<String>();
        comboBox.setBounds(167, 293, 157, 22);
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Male", "Female", "Others" }));
        comboBox.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(comboBox);

        JLabel lblNewLabel_9 = new JLabel("Email");
        lblNewLabel_9.setBounds(26, 326, 66, 18);
        lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_9);

        JLabel lblNewLabel_10 = new JLabel("Blood Group");
        lblNewLabel_10.setBounds(511, 81, 119, 21);
        lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_10);

        JLabel lblNewLabel_13 = new JLabel("Blood Units");
        lblNewLabel_13.setBounds(509, 118, 136, 30);
        lblNewLabel_13.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_13);

        JLabel lblNewLabel_11 = new JLabel("City");
        lblNewLabel_11.setBounds(509, 162, 66, 21);
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_11);

        comboBox_1 = new JComboBox<String>();
        comboBox_1.setBounds(670, 81, 145, 22);
        comboBox_1.setModel(new DefaultComboBoxModel<>(new String[] { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" }));
        comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(comboBox_1);

        textField_5 = new JTextField();
        textField_5.setBounds(669, 126, 146, 20);
        textField_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textField_5);
        textField_5.setColumns(10);

        // City ComboBox with Lahore areas
        comboBoxCity = new JComboBox<String>();
        comboBoxCity.setBounds(668, 164, 148, 22);
        comboBoxCity.setModel(new DefaultComboBoxModel<>(new String[] {  "Johar Town",
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
                "University Avenue" }));
        comboBoxCity.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(comboBoxCity);

        JLabel lblNewLabel_12 = new JLabel("Permanent Address");
        lblNewLabel_12.setBounds(459, 218, 201, 30);
        lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_12);
        
        JLabel lblNewLabel_15_1 = new JLabel("Patient");
        lblNewLabel_15_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_15_1.setBounds(296, 83, 88, 20);
        contentPane.add(lblNewLabel_15_1);

        textArea = new JTextArea();
        textArea.setBounds(670, 215, 159, 70);
        textArea.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textArea);
        
        textField_6 = new JTextField();
        textField_6.setBounds(167, 187, 160, 21);
        textField_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textField_6);
        textField_6.setColumns(10);

        

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);


        JButton btnNewButton = new JButton("Save");
        btnNewButton.setBounds(44, 413, 119, 30);
        Image img1 = new ImageIcon(this.getClass().getResource("/save.png")).getImage();
        btnNewButton.setIcon(new ImageIcon(img1));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	 // Validate form fields
                if (textField.getText().trim().isEmpty() ||
                    textField_1.getText().trim().isEmpty() ||
                    textField_2.getText().trim().isEmpty() ||
                    textField_4.getText().trim().isEmpty() ||
                    textField_5.getText().trim().isEmpty() ||
                    textField_6.getText().trim().isEmpty() ||
                    textArea.getText().trim().isEmpty() ||
                    dateChooser.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields");
                    return;
                }

            	
                String patientId = lblNewLabel_8.getText();
                String userType =lblNewLabel_15_1.getText();
                String fullName = textField.getText();
                String fatherName = textField_1.getText();
                String motherName = textField_6.getText();
                String mobile = textField_2.getText();
                String gender = (String) comboBox.getSelectedItem();
                String email = textField_4.getText();
                String bloodGroup = (String) comboBox_1.getSelectedItem();
                String bloodUnits = textField_5.getText();
                String city = (String) comboBoxCity.getSelectedItem();
                String address = textArea.getText();
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dob = dateFormat.format(dateChooser.getDate());

                try {
                    PreparedStatement pst = con.prepareStatement("insert into Patient(PatientId, UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnitRequired, City, Address) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");

                    pst.setString(1, patientId);
                    pst.setString(2, userType);
                    pst.setString(3, fullName);
                    pst.setString(4, fatherName);
                    pst.setString(5, motherName);
                    pst.setString(6, dob);
                    pst.setString(7, mobile);
                    pst.setString(8, gender);
                    pst.setString(9, email);
                    pst.setString(10, bloodGroup);
                    pst.setString(11, bloodUnits);
                    pst.setString(12, city);
                    pst.setString(13, address);

                    int rs = pst.executeUpdate();

                    if (rs == 1) {
                        JOptionPane.showMessageDialog(btnNewButton, "Record Inserted Successfully");
                        updateStockTable(bloodGroup, city, Integer.parseInt(bloodUnits));
                        resetFields();
                        setVisible(false);
                        new AddNewPatient().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(btnNewButton, "Failed to Insert Record");
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Close");
        btnNewButton_1.setBounds(705, 413, 119, 30);
        Image img2 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnNewButton_1.setIcon(new ImageIcon(img2));
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        contentPane.add(btnNewButton_1);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(331, 413, 119, 30);
        Image img3 = new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
        btnReset.setIcon(new ImageIcon(img3));
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnReset.addActionListener(e -> resetForm());
        contentPane.add(btnReset);
        
        
        textField_4 = new JTextField();
        textField_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_4.setColumns(10);
        textField_4.setBounds(168, 327, 160, 21);
        contentPane.add(textField_4);
        
        
        
        JLabel lblNewLabel_16 = new JLabel("");
        lblNewLabel_16.setBounds(-15, -42, 1121, 590);
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_16.setIcon(new ImageIcon(img4));
        contentPane.add(lblNewLabel_16);
       
        
        
    }

    // Update the time label with the current time
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }

    // Reset all input fields
    private void resetFields() {
        textField.setText("");
        textField_1.setText("");
        textField_2.setText("");
        textField_4.setText("");
        textField_5.setText("");
        textField_6.setText("");
        dateChooser.setDate(null);
        comboBox.setSelectedIndex(-1);
        comboBox_1.setSelectedIndex(-1);
        comboBoxCity.setSelectedIndex(-1);
        textArea.setText("");
    }
    private void resetForm() {
        dispose();
        EventQueue.invokeLater(() -> {
            try {
                AddNewPatient frame = new AddNewPatient();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private void updateStockTable(String bloodGroup, String city, int bloodUnits) {
        try (PreparedStatement pst = con.prepareStatement(
                "INSERT INTO stock (bloodGroup, city, units, AddDate) VALUES (?, ?, ?, NOW()) " +
                "ON DUPLICATE KEY UPDATE units = units - VALUES(units), AddDate = NOW()")) {
            pst.setString(1, bloodGroup);
            pst.setString(2, city);
            pst.setInt(3, bloodUnits);
            pst.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to update stock table: " + e.getMessage());
        }
    }

}

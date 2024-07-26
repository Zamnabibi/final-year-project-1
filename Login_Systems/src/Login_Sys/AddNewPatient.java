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
//import javax.swing.JSeparator;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class AddNewPatient extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private JLabel lblNewLabel_8;
    private Connection con;
    private JLabel timeLabel;
    private JComboBox<String> comboBox;
    private JComboBox<String> comboBox_1;
    private JTextArea textArea;
    private JDateChooser dateChooser;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox_2;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddNewPatient frame = new AddNewPatient();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
        
        comboBox_2 = new JComboBox<>();
        comboBox_2.setBounds(298, 82, 88, 22);
        comboBox_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"Donor", "Patient"}));
        contentPane.add(comboBox_2);
        

        // Add time label
        timeLabel = new JLabel(); // Changed from JTextField to JLabel
        timeLabel.setBounds(640, 10, 184, 20);
        timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(timeLabel);

        // Set the timer to update the JLabel every second
        Timer timer = new Timer(1000, e -> updateTime());
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
                    lblNewLabel_8.setText(String.valueOf(id + 1));
                }
            }
        } catch (Exception m) {
            JOptionPane.showMessageDialog(null, "Database Error: " + m.getMessage());
        }

        JLabel lblNewLabel = new JLabel("Add New Patient");
        lblNewLabel.setBounds(220, 11, 340, 54);
        lblNewLabel.setFont(new Font("Microsoft Himalaya", Font.BOLD | Font.ITALIC, 60));
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("New Patient ID");
        lblNewLabel_1.setBounds(20, 81, 146, 21);
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
        textField_2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && !Character.isWhitespace(c)) {
                    e.consume();
                }
            }
        });
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(170, 219, 158, 20);
        contentPane.add(dateChooser);

        /*JSeparator separator = new JSeparator();
        separator.setBounds(10, 56, 1339, 9);
        contentPane.add(separator);*/

        comboBox = new JComboBox<>();
        comboBox.setBounds(167, 293, 157, 22);
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Male", "Female", "Other" }));
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
        lblNewLabel_11.setBounds(511, 155, 61, 21);
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_11);

        JLabel lblNewLabel_12 = new JLabel("Permanent Address");
        lblNewLabel_12.setBounds(480, 182, 165, 30);
        lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 16));
        contentPane.add(lblNewLabel_12);

        textField_3 = new JTextField();
        textField_3.setBounds(167, 325, 184, 20);
        textField_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textField_3);
        textField_3.setColumns(10);

        comboBox_1 = new JComboBox<>();
        comboBox_1.setBounds(640, 80, 184, 22);
        comboBox_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        comboBox_1.setModel(new DefaultComboBoxModel<>(new String[] { "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-" }));
        contentPane.add(comboBox_1);

        textField_4 = new JTextField();
        textField_4.setBounds(640, 155, 184, 20);
        textField_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textField_4);
        textField_4.setColumns(10);

        textArea = new JTextArea();
        textArea.setBounds(642, 186, 182, 97);
        textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        contentPane.add(textArea);

        textField_5 = new JTextField();
        textField_5.setBounds(169, 187, 158, 20);
        textField_5.setFont(new Font("Dialog", Font.BOLD, 14));
        contentPane.add(textField_5);
        textField_5.setColumns(10);

        textField_6 = new JTextField();
        textField_6.setBounds(640, 118, 184, 20);
        textField_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(textField_6);
        textField_6.setColumns(10);

        /*JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 355, 1339, 9);
        contentPane.add(separator_1);*/

        JButton btnSave = new JButton("Save");
        btnSave.setBounds(56, 393, 111, 30);
        Image img2 = new ImageIcon(this.getClass().getResource("/save.png")).getImage();
        btnSave.setIcon(new ImageIcon(img2));
        btnSave.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnSave.addActionListener(e -> savePatient());
        contentPane.add(btnSave);

        JButton btnReset = new JButton("Reset");
        btnReset.setBounds(352, 391, 119, 35);
        Image img3 = new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
        btnReset.setIcon(new ImageIcon(img3));
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnReset.addActionListener(e -> resetForm());
        contentPane.add(btnReset);

        JButton btnClose = new JButton("Close");
        btnClose.setBounds(661, 391, 119, 35);
        Image img1 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnClose.setIcon(new ImageIcon(img1));
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnClose.addActionListener(e -> dispose());
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
             setVisible(false);
            }
        });
        contentPane.add(btnClose);
       
        
     // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);

        JLabel lblNewLabel_14 = new JLabel("");
        lblNewLabel_14.setBounds(-21, -145, 1370, 749);
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_14.setIcon(new ImageIcon(img4));
        contentPane.add(lblNewLabel_14);
        
       
        
    }

    private void updateTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
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

    private void savePatient() {
        // Validate form fields
        if (textField.getText().trim().isEmpty() ||
            textField_1.getText().trim().isEmpty() ||
            textField_5.getText().trim().isEmpty() ||
            textField_2.getText().trim().isEmpty() ||
            textField_3.getText().trim().isEmpty() ||
            textField_4.getText().trim().isEmpty() ||
            textArea.getText().trim().isEmpty() ||
            dateChooser.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Please fill all fields");
            return;
        }

       
        // Get current timestamp
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Retrieve form values
        String PatientId = lblNewLabel_8.getText();
        String UserType = (String) comboBox_2.getSelectedItem();
        String Name = textField.getText().trim();
        String FatherName = textField_1.getText().trim();
        String MotherName = textField_5.getText().trim();
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format for SQL DATE
        String DOB = dFormat.format(dateChooser.getDate());
        String MobileNo = textField_2.getText().trim();
        String Gender = (String) comboBox.getSelectedItem();
        String Email = textField_3.getText().trim();
        String BloodGroup = (String) comboBox_1.getSelectedItem();
        String BloodUnit = textField_6.getText().trim();
        String City = textField_4.getText().trim();
        String Address = textArea.getText().trim();

        // Prepare SQL with column names
        String sql = "INSERT INTO Patient (PatientId,UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnit, City, Address, CreatedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, Integer.parseInt(PatientId));
            pst.setString(2, UserType);
            pst.setString(3, Name);
            pst.setString(4, FatherName);
            pst.setString(5, MotherName);
            pst.setString(6, DOB);
            pst.setString(7, MobileNo);
            pst.setString(8, Gender);
            pst.setString(9, Email);
            pst.setString(10, BloodGroup);
            pst.setString(11, BloodUnit);
            pst.setString(12, City);
            pst.setString(13, Address);
            pst.setTimestamp(14, now); // Set CreatedAt

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Successfully Updated");
            resetForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Patient ID format");
        }
    }
}

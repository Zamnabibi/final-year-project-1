package Login_Sys;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;

public class UpdateDetailsPatient extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_4;
    private JTextField textField_5;
    private JTextField textField_6;
    private JTextField textField_7;
    private JTextField textField_8;
    private JTextField textField_9;
    private JTextArea textArea;
    private Connection con;
    private JTextField textField_10;
    private JLabel timeLabel;
    private JTextField textField_11;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	UpdateDetailsPatient frame = new UpdateDetailsPatient();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	public UpdateDetailsPatient() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

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
        } catch (Exception m) {
            JOptionPane.showMessageDialog(null, m);
        }

        JLabel lblNewLabel = new JLabel("Update Patient ");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 60));
        lblNewLabel.setBounds(130, 0, 666, 69);
        contentPane.add(lblNewLabel);

        
        /*JSeparator separator = new JSeparator();
        separator.setBounds(10, 67, 1339, 2);
        contentPane.add(separator);*/

        JLabel lblNewLabel_1 = new JLabel("Patient ID");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel_1.setBounds(140, 86, 100, 23);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.BOLD, 16));
        textField.setBounds(250, 80, 130, 33);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Search");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String PatientId = textField.getText();
                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM Patient WHERE PatientId=" + PatientId);
                    if (rs.next()) {
                    	textField_1.setText(rs.getString(3));
                        textField_2.setText(rs.getString(4));
                        textField_3.setText(rs.getString(5));
                        textField_9.setText(rs.getString(6));
                        textField_6.setText(rs.getString(7));
                        textField_8.setText(rs.getString(8));
                        textField_4.setText(rs.getString(9));
                        textField_7.setText(rs.getString(10));
                        textField_10.setText(rs.getString(11));
                        textField_5.setText(rs.getString(12));
                        textArea.setText(rs.getString(13));
                        textField_11.setText(rs.getString(2));
                        textField.setEditable(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "PatientId does not exist");
                    }
                } catch (Exception c) {
                    JOptionPane.showMessageDialog(null, "Error occurred while fetching data.");
                    c.printStackTrace();
                }
            }
        });

        Image img = new ImageIcon(this.getClass().getResource("/search 1.png")).getImage();
        btnNewButton.setIcon(new ImageIcon(img));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnNewButton.setBounds(403, 80, 116, 35);
        contentPane.add(btnNewButton);
        
        JLabel lblNewLabel_15 = new JLabel("UserType");
        lblNewLabel_15.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_15.setBounds(540, 85, 73, 24);
        contentPane.add(lblNewLabel_15);
        
        textField_11 = new JTextField();
        textField_11.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_11.setBounds(641, 87, 183, 20);
        contentPane.add(textField_11);
        textField_11.setColumns(10);
        
        /*JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 120, 1339, 2);
        contentPane.add(separator_1);*/

        JLabel lblNewLabel_2 = new JLabel("Full Name");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_2.setBounds(20, 133, 81, 23);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Father Name");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_3.setBounds(20, 167, 100, 23);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Mother Name");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_4.setBounds(20, 201, 100, 23);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Date Of Birth");
        lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_5.setBounds(20, 235, 100, 26);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Mobile NO");
        lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_6.setBounds(20, 272, 81, 20);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Gender");
        lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_7.setBounds(20, 317, 81, 23);
        contentPane.add(lblNewLabel_7);

        JLabel lblNewLabel_9 = new JLabel("Blood Group");
        lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_9.setBounds(500, 135, 93, 19);
        contentPane.add(lblNewLabel_9);

        JLabel lblNewLabel_10 = new JLabel("City");
        lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_10.setBounds(512, 201, 58, 19);
        contentPane.add(lblNewLabel_10);

        JLabel lblNewLabel_11 = new JLabel("Email");
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_11.setBounds(30, 351, 58, 23);
        contentPane.add(lblNewLabel_11);

        JLabel lblNewLabel_12 = new JLabel("Permanent Address");
        lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_12.setBounds(491, 221, 140, 20);
        contentPane.add(lblNewLabel_12);

        JLabel lblNewLabel_9_1 = new JLabel("Blood Unit");
        lblNewLabel_9_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_9_1.setBounds(500, 167, 93, 19);
        contentPane.add(lblNewLabel_9_1);

        textField_10 = new JTextField();
        textField_10.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_10.setColumns(10);
        textField_10.setBounds(641, 170, 183, 20);
        contentPane.add(textField_10);

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_1.setBounds(130, 133, 183, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_2.setBounds(130, 168, 183, 20);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_3.setBounds(130, 202, 183, 20);
        contentPane.add(textField_3);
        textField_3.setColumns(10);

        textField_4 = new JTextField();
        textField_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_4.setBounds(130, 357, 183, 20);
        contentPane.add(textField_4);
        textField_4.setColumns(10);

        textField_5 = new JTextField();
        textField_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_5.setBounds(641, 200, 183, 20);
        contentPane.add(textField_5);
        textField_5.setColumns(10);

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        textArea.setBounds(641, 226, 183, 114);
        contentPane.add(textArea);

        /*JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(10, 399, 1339, 2);
        contentPane.add(separator_2);*/

        JButton btnNewButton_1 = new JButton("Update");
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                String PatientId = textField.getText();
                String UserType = textField_11.getText();
                String Name = textField_1.getText();
                String FatherName = textField_2.getText();
                String MotherName = textField_3.getText();
                String DOB = textField_9.getText();
                String MobileNo = textField_6.getText();
                String Gender = textField_8.getText();
                String Email = textField_4.getText();
                String BloodGroup = textField_7.getText();
                String City = textField_5.getText();
                String Address = textArea.getText();
                try {
                    String query = "UPDATE Patient SET UserType=?,Name=?, FatherName=?, MotherName=?, DOB=?, MobileNo=?, Gender=?, Email=?, BloodGroup=?, City=?, Address=?, UpdatedAt=? WHERE PatientId=?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setString(1, UserType);
                    pst.setString(2, Name);
                    pst.setString(3, FatherName);
                    pst.setString(4, MotherName);
                    pst.setString(5, DOB);
                    pst.setString(6, MobileNo);
                    pst.setString(7, Gender);
                    pst.setString(8, Email);
                    pst.setString(9, BloodGroup);
                    pst.setString(10, City);
                    pst.setString(11, Address);
                    pst.setTimestamp(12, now); // Corrected index for UpdatedAt
                    pst.setString(13, PatientId); // Corrected index for DonorId
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Successfully Updated");
                    pst.close();
                    con.close();
                    setVisible(false);
                    new UpdateDetailsPatient().setVisible(true);
                } catch (Exception b) {
                    JOptionPane.showMessageDialog(null, "Error occurred while updating data.");
                    b.printStackTrace();
                }
            }
        });
        Image img5 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
        btnNewButton_1.setIcon(new ImageIcon(img5));
        btnNewButton_1.setBounds(74, 399, 116, 33);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Reset");
        btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new UpdateDetailsPatient().setVisible(true);
            }
        });
        Image img3 = new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
        btnNewButton_2.setIcon(new ImageIcon(img3));
        btnNewButton_2.setBounds(403, 399, 116, 33);
        contentPane.add(btnNewButton_2);

        JButton btnNewButton_3 = new JButton("Close");
        btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        Image img2 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnNewButton_3.setIcon(new ImageIcon(img2));
        btnNewButton_3.setBounds(708, 399, 116, 33);
        contentPane.add(btnNewButton_3);

        textField_6 = new JTextField();
        textField_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_6.setBounds(130, 272, 183, 20);
        contentPane.add(textField_6);
        textField_6.setColumns(10);

        textField_7 = new JTextField();
        textField_7.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_7.setBounds(641, 133, 183, 20);
        contentPane.add(textField_7);
        textField_7.setColumns(10);

        textField_8 = new JTextField();
        textField_8.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_8.setBounds(130, 318, 183, 20);
        contentPane.add(textField_8);
        textField_8.setColumns(10);

        textField_9 = new JTextField();
        textField_9.setBounds(130, 240, 183, 20);
        contentPane.add(textField_9);
        textField_9.setColumns(10);
        
     // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 467, 850, 44); // Adjust size and position as needed
        contentPane.add(footerPanel);

       JLabel lblNewLabel_8 = new JLabel("");
        lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 14));
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_8.setIcon(new ImageIcon(img4));
        lblNewLabel_8.setBounds(-21, -121, 1370, 749);
        contentPane.add(lblNewLabel_8);
    }

    private void updateTime() {
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String currentTime = sdf.format(new java.util.Date());
         timeLabel.setText(currentTime);
    }
}

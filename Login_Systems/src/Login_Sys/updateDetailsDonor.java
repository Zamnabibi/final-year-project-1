package Login_Sys;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class updateDetailsDonor extends JFrame {

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
    private PreparedStatement pst;
    private Connection con;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    updateDetailsDonor frame = new updateDetailsDonor();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public updateDetailsDonor() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Connection created");
            pst = con.prepareStatement("select max(DonorId) from donor");
        } catch (Exception m) {
            JOptionPane.showMessageDialog(null, m);
        }

        JLabel lblNewLabel = new JLabel("Update Donor Details");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 38));
        lblNewLabel.setBounds(158, -3, 666, 69);
        contentPane.add(lblNewLabel);

        JSeparator separator = new JSeparator();
        separator.setBounds(10, 77, 1339, 2);
        contentPane.add(separator);

        JLabel lblNewLabel_1 = new JLabel("Donor ID");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel_1.setBounds(49, 99, 81, 14);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.BOLD, 16));
        textField.setBounds(184, 90, 130, 33);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Search");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String DonorId = textField.getText();
                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("select * from donor where DonorId=" + DonorId);
                    if (rs.next()) {
                        textField_1.setText(rs.getString(2));
                        textField_2.setText(rs.getString(3));
                        textField_3.setText(rs.getString(4));
                        textField_9.setText(rs.getString(5));
                        textField_6.setText(rs.getString(6));
                        textField_8.setText(rs.getString(7));
                        textField_4.setText(rs.getString(8));
                        textField_7.setText(rs.getString(9));
                        textField_5.setText(rs.getString(10));
                        textArea.setText(rs.getString(11));
                        textField.setEditable(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "DonorId does not exist");
                    }
                } catch (Exception c) {
                    c.printStackTrace();
                }
            }
        });

        Image img = new ImageIcon(this.getClass().getResource("/search 1.png")).getImage();
        btnNewButton.setIcon(new ImageIcon(img));
        btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnNewButton.setBounds(419, 89, 116, 35);
        contentPane.add(btnNewButton);

        JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 131, 1339, 2);
        contentPane.add(separator_1);

        JLabel lblNewLabel_2 = new JLabel("Full Name");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_2.setBounds(20, 144, 81, 23);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Father Name");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_3.setBounds(20, 178, 100, 23);
        contentPane.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Mother Name");
        lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_4.setBounds(20, 212, 100, 23);
        contentPane.add(lblNewLabel_4);

        JLabel lblNewLabel_5 = new JLabel("Date Of Birth");
        lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_5.setBounds(20, 241, 100, 26);
        contentPane.add(lblNewLabel_5);

        JLabel lblNewLabel_6 = new JLabel("Mobile NO");
        lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_6.setBounds(20, 274, 81, 20);
        contentPane.add(lblNewLabel_6);

        JLabel lblNewLabel_7 = new JLabel("Gender");
        lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_7.setBounds(20, 305, 81, 23);
        contentPane.add(lblNewLabel_7);

        JLabel lblNewLabel_9 = new JLabel("Blood Group");
        lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_9.setBounds(494, 146, 93, 19);
        contentPane.add(lblNewLabel_9);

        JLabel lblNewLabel_10 = new JLabel("City");
        lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_10.setBounds(494, 180, 58, 19);
        contentPane.add(lblNewLabel_10);

        JLabel lblNewLabel_11 = new JLabel("Email");
        lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_11.setBounds(20, 339, 58, 17);
        contentPane.add(lblNewLabel_11);

        JLabel lblNewLabel_12 = new JLabel("Complete Address");
        lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_12.setBounds(494, 213, 140, 20);
        contentPane.add(lblNewLabel_12);

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_1.setBounds(130, 150, 183, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_2.setBounds(130, 179, 183, 20);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_3.setBounds(130, 213, 183, 20);
        contentPane.add(textField_3);
        textField_3.setColumns(10);

        textField_4 = new JTextField();
        textField_4.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_4.setBounds(130, 337, 183, 20);
        contentPane.add(textField_4);
        textField_4.setColumns(10);

        textField_5 = new JTextField();
        textField_5.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_5.setBounds(637, 181, 183, 20);
        contentPane.add(textField_5);
        textField_5.setColumns(10);

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        textArea.setBounds(637, 212, 187, 116);
        contentPane.add(textArea);

        JSeparator separator_2 = new JSeparator();
        separator_2.setBounds(10, 367, 1339, 2);
        contentPane.add(separator_2);

        JButton btnNewButton_1 = new JButton("Update");
        btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String DonorId = textField.getText();
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
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
                    String query = "UPDATE donor SET Name=?, FatherName=?, MotherName=?, DOB=?, MobileNo=?, Gender=?, Email=?, BloodGroup=?, City=?, Address=? WHERE DonorId=?";
                    pst = con.prepareStatement(query);
                    pst.setString(1, Name);
                    pst.setString(2, FatherName);
                    pst.setString(3, MotherName);
                    pst.setString(4, DOB);
                    pst.setString(5, MobileNo);
                    pst.setString(6, Gender);
                    pst.setString(7, Email);
                    pst.setString(8, BloodGroup);
                    pst.setString(9, City);
                    pst.setString(10, Address);
                    pst.setString(11, DonorId);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Successfully Updated");
                    con.close();
                    setVisible(false);
                    new updateDetailsDonor().setVisible(true);
                } catch (Exception b) {
                    JOptionPane.showMessageDialog(null, "Connection error");
                }
            }
        });
        Image img2 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
        btnNewButton_1.setIcon(new ImageIcon(img2));
        btnNewButton_1.setBounds(73, 396, 115, 33);
        contentPane.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Reset");
        btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new updateDetailsDonor().setVisible(true);
            }
        });
        Image img3 = new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
        btnNewButton_2.setIcon(new ImageIcon(img3));
        btnNewButton_2.setBounds(333, 396, 109, 33);
        contentPane.add(btnNewButton_2);

        JButton btnNewButton_3 = new JButton("Close");
        btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        Image img1 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnNewButton_3.setIcon(new ImageIcon(img1));
        btnNewButton_3.setBounds(637, 396, 102, 33);
        contentPane.add(btnNewButton_3);

        textField_6 = new JTextField();
        textField_6.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_6.setBounds(131, 274, 183, 20);
        contentPane.add(textField_6);
        textField_6.setColumns(10);

        textField_7 = new JTextField();
        textField_7.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_7.setBounds(637, 150, 183, 20);
        contentPane.add(textField_7);
        textField_7.setColumns(10);

        textField_8 = new JTextField();
        textField_8.setFont(new Font("Tahoma", Font.BOLD, 14));
        textField_8.setBounds(130, 306, 183, 20);
        contentPane.add(textField_8);
        textField_8.setColumns(10);

        textField_9 = new JTextField();
        textField_9.setBounds(130, 246, 183, 20);
        contentPane.add(textField_9);
        textField_9.setColumns(10);

        JLabel lblNewLabel_8 = new JLabel("");
        lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 14));
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_8.setIcon(new ImageIcon(img4));
        lblNewLabel_8.setBounds(-23, -116, 1370, 749);
        contentPane.add(lblNewLabel_8);
    }
}

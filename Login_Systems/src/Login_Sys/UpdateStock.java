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

public class UpdateStock extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JTextField textField_1;
    private JTextField textField_2;
    private Connection con;
    private JLabel timeLabel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UpdateStock frame = new UpdateStock();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UpdateStock() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 550); // Adjusted size for additional space
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Add time label
        timeLabel = new JLabel();
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

        JLabel lblNewLabel = new JLabel("Update Stock");
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 60));
        lblNewLabel.setBounds(130, 0, 666, 69);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Stock ID");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblNewLabel_1.setBounds(140, 86, 100, 23);
        contentPane.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.BOLD, 16));
        textField.setBounds(250, 80, 130, 33);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String stockId = textField.getText();
                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM stock WHERE StockID=" + stockId);
                    if (rs.next()) {
                        textField_1.setText(rs.getString("City"));
                        textField_2.setText(rs.getString("BloodGroup"));
                        textField.setEditable(false);
                    } else {
                        JOptionPane.showMessageDialog(null, "Stock ID does not exist");
                    }
                } catch (Exception c) {
                    JOptionPane.showMessageDialog(null, "Error occurred while fetching data.");
                    c.printStackTrace();
                }
            }
        });

        Image img = new ImageIcon(this.getClass().getResource("/search 1.png")).getImage();
        btnSearch.setIcon(new ImageIcon(img));
        btnSearch.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnSearch.setBounds(403, 80, 116, 35);
        contentPane.add(btnSearch);

        JLabel lblNewLabel_2 = new JLabel("City");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_2.setBounds(20, 133, 81, 23);
        contentPane.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Blood Group");
        lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_3.setBounds(20, 167, 100, 23);
        contentPane.add(lblNewLabel_3);

        textField_1 = new JTextField();
        textField_1.setBounds(130, 134, 243, 20);
        contentPane.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setBounds(130, 168, 243, 20);
        contentPane.add(textField_2);
        textField_2.setColumns(10);

        JButton btnUpdate = new JButton("Update");
        Image img2 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
        btnUpdate.setIcon(new ImageIcon(img2));
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String stockId = textField.getText();
                String city = textField_1.getText();
                String bloodGroup = textField_2.getText();
                try {
                    PreparedStatement ps = con.prepareStatement(
                        "UPDATE stock SET City = ?, BloodGroup = ? WHERE StockID = ?");
                    ps.setString(1, city);
                    ps.setString(2, bloodGroup);
                    ps.setString(3, stockId);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Stock details updated successfully");
                        resetFields();
                        setVisible(false);
                        new UpdateStock().setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Error occurred while updating data.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while updating data.");
                    ex.printStackTrace();
                }
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnUpdate.setBounds(250, 210, 130, 33);
        contentPane.add(btnUpdate);

        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);
        
        JLabel lblNewLabel_8 = new JLabel("");
        Image img5 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_8.setIcon(new ImageIcon(img5));
        lblNewLabel_8.setBounds(-21, -121, 1370, 749);
        contentPane.add(lblNewLabel_8);
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeLabel.setText(sdf.format(new java.util.Date()));
    }
    
    private void resetFields() {
        textField.setText("");
        textField_1.setText("");}
}

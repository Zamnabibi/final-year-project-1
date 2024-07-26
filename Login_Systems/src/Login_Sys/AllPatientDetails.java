package Login_Sys;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
//import javax.swing.JSeparator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Color;

public class AllPatientDetails extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private Connection con;
    private JLabel timeLabel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	AllPatientDetails frame = new AllPatientDetails();
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
    public AllPatientDetails() {
    	setForeground(Color.PINK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1180, 550);
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

        JLabel lblNewLabel = new JLabel("Patient Details");
        lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 60));
        lblNewLabel.setBounds(99, 11, 556, 76);
        contentPane.add(lblNewLabel);

        /*JSeparator separator = new JSeparator();
        separator.setBounds(10, 85, 1323, 2);
        contentPane.add(separator);*/

        table = new JTable();
        table.setFont(new Font("Tahoma", Font.BOLD, 14));
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "PatientId","UserType", "Name", "FatherName", "MotherName", "DOB", "MobileNo", "Gender", "Email", "BloodGroup","BloodUnit", "City", "Permanent Address", "CreatedAt", "UpdatedAt"
            }
        ));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 98, 1154, 284);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        contentPane.add(scrollPane);

        JButton btnPrint = new JButton("Print");
        btnPrint.setFont(new Font("Tahoma", Font.BOLD, 14));
        Image img2 = new ImageIcon(this.getClass().getResource("/print.png")).getImage();
        btnPrint.setIcon(new ImageIcon(img2));
        btnPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    table.print(JTable.PrintMode.NORMAL);
                } catch (Exception t) {
                    JOptionPane.showMessageDialog(null, t);
                }
            }
        });
        btnPrint.setBounds(39, 406, 116, 31);
        contentPane.add(btnPrint);

        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 14));
        Image img1 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnClose.setIcon(new ImageIcon(img1));
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        btnClose.setBounds(690, 405, 106, 33);
        contentPane.add(btnClose);
        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 462, 1164, 49); // Adjust size and position as needed
        contentPane.add(footerPanel);

        /*JSeparator separator_1 = new JSeparator();
        separator_1.setBounds(10, 393, 1323, 2);
        contentPane.add(separator_1);*/

        JButton btnDisplay = new JButton("Display");
        btnDisplay.setFont(new Font("Tahoma", Font.BOLD, 14));
        Image img3 = new ImageIcon(this.getClass().getResource("/display.png")).getImage();
        btnDisplay.setIcon(new ImageIcon(img3));
        btnDisplay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        btnDisplay.setBounds(340, 405, 135, 33);
        contentPane.add(btnDisplay);

        JLabel lblBackground = new JLabel("");
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img4));
        lblBackground.setBounds(0, -137, 1370, 749);
        contentPane.add(lblBackground);
        
        JLabel lblNewLabel_1 = new JLabel("");
        Image img5 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_1.setIcon(new ImageIcon(img5));
        lblNewLabel_1.setBounds(840, 0, 324, 511);
        contentPane.add(lblNewLabel_1);

        // Initialize the database connection
        initializeDatabaseConnection();
    }

    private void initializeDatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Connection created");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing rows
        String query = "SELECT * FROM Patient";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                String PatientId = rs.getString(1);
				String UserType = rs.getString(2);
                String Name = rs.getString(3);
                String FatherName = rs.getString(4);
                String MotherName = rs.getString(5);
                String DOB = rs.getString(6);
                String MobileNo = rs.getString(7);
                String Gender = rs.getString(8);
                String Email = rs.getString(9);
                String BloodGroup = rs.getString(10);
                String BloodUnit = rs.getString(11);
                String City = rs.getString(12);
                String Address = rs.getString(13);
                String CreatedAt = rs.getString(14);
                String UpdatedAt = rs.getString(15);
                String[] row = { PatientId,UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup,BloodUnit, City, Address, CreatedAt, UpdatedAt};
                model.addRow(row);
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
    private void updateTime() {
   	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
   }
}

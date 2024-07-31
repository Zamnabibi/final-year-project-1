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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SearchBloodDonorBloodGroup extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable table;
    private JLabel timeLabel;
    private Connection con;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SearchBloodDonorBloodGroup frame = new SearchBloodDonorBloodGroup();
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
    public SearchBloodDonorBloodGroup() {
        setForeground(Color.PINK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1180, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Search Blood Donor(Blood Group)");
        lblNewLabel.setFont(new Font("Sitka Text", Font.BOLD | Font.ITALIC, 40));
        lblNewLabel.setBounds(138, 0, 701, 76);
        contentPane.add(lblNewLabel);
        
     // Add time label
        timeLabel = new JLabel(); // Changed from JTextField to JLabel
        timeLabel.setBounds(934, 57, 184, 20);
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

        table = new JTable();
        table.setFont(new Font("Tahoma", Font.BOLD, 14));
        table.setModel(new DefaultTableModel(
            new Object[][] {},
            new String[] {
                "DonorId","UserType", "Name", "FatherName", "MotherName", "DOB", "MobileNo", "Gender", "Email", "BloodGroup","BloodUnit", "City", "Permanent Address", "CreatedAt", "UpdatedAt"
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

        JButton btnDisplay = new JButton("Search");
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

        // Add listener for row selection
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        saveSelectedRowToBloodBank(selectedRow);
                    }
                }
            }
        });
        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 467, 1164, 44); // Adjust size and position as needed
        contentPane.add(footerPanel);


        JLabel lblBackground = new JLabel("");
        Image img4 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img4));
        lblBackground.setBounds(-11, -120, 1370, 749);
        contentPane.add(lblBackground);

        JLabel lblNewLabel_11 = new JLabel("");
        Image img5 = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblNewLabel_11.setIcon(new ImageIcon(img5));
        lblNewLabel_11.setBounds(840, 0, 324, 511);
        contentPane.add(lblNewLabel_11);

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

        String bloodGroup = JOptionPane.showInputDialog(this, "Enter Blood Group to search:", "Enter Blood Group", JOptionPane.PLAIN_MESSAGE);
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Blood Group cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String query = "SELECT * FROM donor WHERE BloodGroup=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, bloodGroup);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(this, "No records found for the entered blood group.", "No Records", JOptionPane.INFORMATION_MESSAGE);
            } else {
                while (rs.next()) {
                    String DonorId = rs.getString(1);
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
                    String[] row = {DonorId, UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnit, City, Address, CreatedAt, UpdatedAt};
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void saveSelectedRowToBloodBank(int rowIndex) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        @SuppressWarnings("unused")
		String DonorId = model.getValueAt(rowIndex, 0).toString();
        String UserType = model.getValueAt(rowIndex, 1).toString();
        String Name = model.getValueAt(rowIndex, 2).toString();
        String FatherName = model.getValueAt(rowIndex, 3).toString();
        String MotherName = model.getValueAt(rowIndex, 4).toString();
        String DOB = model.getValueAt(rowIndex, 5).toString();
        String MobileNo = model.getValueAt(rowIndex, 6).toString();
        String Gender = model.getValueAt(rowIndex, 7).toString();
        String Email = model.getValueAt(rowIndex, 8).toString();
        String BloodGroup = model.getValueAt(rowIndex, 9).toString();
        String BloodUnit = model.getValueAt(rowIndex, 10).toString();
        String City = model.getValueAt(rowIndex, 11).toString();
        String Address = model.getValueAt(rowIndex, 12).toString();
        new SearchBloodDonorBloodGroup().setVisible(true);
        saveToBloodBank(UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnit, City, Address);
    }

    private void saveToBloodBank(String userType, String name, String fatherName, String motherName, String dob, String mobileNo, String gender, String email, String bloodGroup, String bloodUnit, String city, String address) {
        try {
            String sql = "INSERT INTO bloodbank (UserType, Name, FatherName, MotherName, DOB, MobileNo, Gender, Email, BloodGroup, BloodUnit, City, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, userType);
            stmt.setString(2, name);
            stmt.setString(3, fatherName);
            stmt.setString(4, motherName);
            stmt.setString(5, dob);
            stmt.setString(6, mobileNo);
            stmt.setString(7, gender);
            stmt.setString(8, email);
            stmt.setString(9, bloodGroup);
            stmt.setString(10, bloodUnit);
            stmt.setString(11, city);
            stmt.setString(12, address);

            stmt.executeUpdate();
            System.out.println("Data saved to bloodbank table.");
            resetForm();
            setVisible(false);
          
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving data to blood bank: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void resetForm() {
        dispose();
    }
    
    private void updateTime() {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }
}

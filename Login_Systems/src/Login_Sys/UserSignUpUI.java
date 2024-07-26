package Login_Sys;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
//import javax.swing.JSeparator;

public class UserSignUpUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Connection con;
    private PreparedStatement pst;
    private JLabel timeLabel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	UserSignUpUI frame = new UserSignUpUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public UserSignUpUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 859, 553);
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
        
        // Database connection setup
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Connection created");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblNewLabel = new JLabel("Patient");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 60));
        lblNewLabel.setBounds(198, 0, 445, 74);
        contentPane.add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUsername.setBounds(198, 110, 96, 17);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPassword.setBounds(198, 196, 89, 23);
        contentPane.add(lblPassword);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
        txtUsername.setBounds(550, 108, 131, 20);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        txtPassword.setBounds(550, 197, 131, 20);
        contentPane.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        Image img = new ImageIcon(getClass().getResource("/OK-icon.png")).getImage();
        btnLogin.setIcon(new ImageIcon(img));
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            if (validateLogin(username, password)) {
                txtPassword.setText(null);
                txtUsername.setText(null);
                setVisible(false);
                new UserHome().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login Details", "Login Error", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText(null);
                txtUsername.setText(null);
            }
        });
        btnLogin.setBounds(47, 336, 107, 34);
        contentPane.add(btnLogin);

        JButton btnClose = new JButton("Close");
        Image img2 = new ImageIcon(getClass().getResource("/close.png")).getImage();
        btnClose.setIcon(new ImageIcon(img2));
        btnClose.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnClose.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this, "Confirm if you want to close", "Login System", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                closeResources();
                System.exit(0);
            }
        });
        btnClose.setBounds(307, 336, 119, 34);
        contentPane.add(btnClose);

        JButton btnReset = new JButton("Reset");
        Image img3 = new ImageIcon(getClass().getResource("/reset-icon.png")).getImage();
        btnReset.setIcon(new ImageIcon(img3));
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnReset.addActionListener(e -> {
            txtUsername.setText(null);
            txtPassword.setText(null);
        });
        btnReset.setBounds(566, 336, 124, 34);
        contentPane.add(btnReset);

     

        JLabel lblCreateAccount = new JLabel("Create Account");
        lblCreateAccount.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCreateAccount.setBounds(682, 379, 131, 34);
        lblCreateAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SignupSystem().setVisible(true);
            }
        });
        contentPane.add(lblCreateAccount);
        
        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);
        
        // Background Image
        JLabel lblBackground = new JLabel("");
        Image img1 = new ImageIcon(getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img1));
        lblBackground.setBounds(0, 0, 843, 500); // Adjusted bounds
        contentPane.add(lblBackground);
    }

    private boolean validateLogin(String username, String password) {
        try {
            // Assuming there is a 'Status' field in the 'users' table
            String query = "SELECT * FROM users WHERE UserName = ? AND Password = ? AND Status = 'Accepted'";
            pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            boolean valid = rs.next(); // Check if any record is returned
            rs.close();
            pst.close();
            return valid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private void closeResources() {
        try {
            if (pst != null) pst.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void updateTime() {
   	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
   }

}

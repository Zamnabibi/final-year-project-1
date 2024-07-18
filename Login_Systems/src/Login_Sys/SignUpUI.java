package Login_Sys;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
//import javax.swing.JSeparator;

public class SignUpUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private Connection con;
    private PreparedStatement pst;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	SignUpUI frame = new SignUpUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SignUpUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Database connection setup
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Connection created");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel lblNewLabel = new JLabel("Login");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 60));
        lblNewLabel.setBounds(193, 0, 445, 74);
        contentPane.add(lblNewLabel);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblUsername.setBounds(321, 110, 96, 17);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblPassword.setBounds(321, 196, 89, 23);
        contentPane.add(lblPassword);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Tahoma", Font.BOLD, 16));
        txtUsername.setBounds(682, 108, 131, 20);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Tahoma", Font.BOLD, 16));
        txtPassword.setBounds(682, 197, 131, 20);
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
                new Login().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Login Details", "Login Error", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText(null);
                txtUsername.setText(null);
            }
        });
        btnLogin.setBounds(49, 286, 107, 34);
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
        btnClose.setBounds(355, 286, 119, 34);
        contentPane.add(btnClose);

        JButton btnReset = new JButton("Reset");
        Image img3 = new ImageIcon(getClass().getResource("/reset-icon.png")).getImage();
        btnReset.setIcon(new ImageIcon(img3));
        btnReset.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnReset.addActionListener(e -> {
            txtUsername.setText(null);
            txtPassword.setText(null);
        });
        btnReset.setBounds(689, 286, 124, 34);
        contentPane.add(btnReset);

       // JSeparator separator = new JSeparator();
        //separator.setBounds(23, 85, 1300, 2);
       // contentPane.add(separator);

       // JSeparator separator_1 = new JSeparator();
       // separator_1.setBounds(23, 248, 1300, 2);
        //contentPane.add(separator_1);

        JLabel lblCreateAccount = new JLabel("Create Account");
        lblCreateAccount.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblCreateAccount.setBounds(1004, 354, 131, 34);
        lblCreateAccount.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new DonorSignupSystem().setVisible(true);
            }
        });
        contentPane.add(lblCreateAccount);

        // Background Image
        JLabel lblBackground = new JLabel("");
        Image img1 = new ImageIcon(getClass().getResource("/image.jpeg")).getImage();
        lblBackground.setIcon(new ImageIcon(img1));
        lblBackground.setBounds(0, 0, 1200, 600); // Adjusted bounds
        contentPane.add(lblBackground);
    }

    private boolean validateLogin(String username, String password) {
        try {
            String query = "SELECT * FROM donors WHERE UserName = ? AND Password = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            boolean valid = rs.next();
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
}

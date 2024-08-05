package Login_Sys;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddAdmin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private Connection con;
    private JLabel timeLabel;
    private JTextField textFieldEmail;
    private JTextField textFieldFullName;

    public AddAdmin() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 850, 550);
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

        // Title Label
        JLabel lblTitle = new JLabel("Add New Admin");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblTitle.setBounds(186, 11, 352, 50);
        contentPane.add(lblTitle);

        // Username Label and Field
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(69, 100, 80, 25);
        contentPane.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setBounds(516, 102, 200, 25);
        contentPane.add(txtUsername);
        txtUsername.setColumns(10);

        // Password Label and Field
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(69, 153, 80, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(516, 155, 200, 25);
        contentPane.add(txtPassword);

        // Confirm Password Label and Field
        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblConfirmPassword.setBounds(69, 205, 130, 25);
        contentPane.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBounds(516, 207, 200, 25);
        contentPane.add(txtConfirmPassword);

        // Email Label and Field
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(69, 256, 104, 17);
        contentPane.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(516, 254, 200, 25);
        contentPane.add(textFieldEmail);
        textFieldEmail.setColumns(10);

        // Full Name Label and Field
        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblFullName.setBounds(69, 305, 100, 20);
        contentPane.add(lblFullName);

        textFieldFullName = new JTextField();
        textFieldFullName.setBounds(516, 300, 200, 25);
        contentPane.add(textFieldFullName);
        textFieldFullName.setColumns(10);

        // Add Admin Button
        JButton btnAdd = new JButton("Add Admin");
        Image img14 = new ImageIcon(this.getClass().getResource("/add new.png")).getImage();
        btnAdd.setIcon(new ImageIcon(img14));
        btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnAdd.setBounds(171, 356, 150, 30);
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAdmin();
            }
        });
        contentPane.add(btnAdd);

        // Cancel Button
        JButton btnCancel = new JButton("Cancel");
        Image img13 = new ImageIcon(this.getClass().getResource("/close.png")).getImage();
        btnCancel.setIcon(new ImageIcon(img13));
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnCancel.setBounds(623, 356, 150, 30);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        contentPane.add(btnCancel);

        // Already Account Label
        JLabel lblNewLabel = new JLabel("Already Account");
        lblNewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Login_S().setVisible(true);
            }
        });
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(650, 409, 130, 25);
        contentPane.add(lblNewLabel);

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50);
        contentPane.add(footerPanel);

        // Background Image
        JLabel lblBackground = new JLabel("");
        lblBackground.setBounds(0, 0, 850, 500);
        Image img = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img));
        contentPane.add(lblBackground);

        // Database connection setup
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAdmin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String email = textFieldEmail.getText();
        String fullName = textFieldFullName.getText();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        try {
            String query = "INSERT INTO Admin (UserName, Password, Email, FullName, Status, Type) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, fullName);
            pst.setString(5, "Accepted");
            pst.setString(6, "Admin");
            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Admin added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                txtUsername.setText("");
                txtPassword.setText("");
                txtConfirmPassword.setText("");
                textFieldEmail.setText("");
                textFieldFullName.setText("");
                new Home().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add admin", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                AddAdmin frame = new AddAdmin();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new java.util.Date());
        timeLabel.setText(currentTime);
    }
}

package Login_Sys;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SignupSystem extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JTextField textFieldEmail;
    private JTextField textFieldFullName;
    private JPasswordField confirmPasswordField;
    private Connection con;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                SignupSystem frame = new SignupSystem();
                frame.setVisible(true);
                frame.connectToDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SignupSystem() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 850, 500);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        
        JLabel lblTitle = new JLabel("Add New Patient");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblTitle.setBounds(183, 11, 352, 50);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(27, 122, 100, 17);
        contentPane.add(lblUsername);

        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(170, 119, 200, 20);
        contentPane.add(textFieldUsername);
        textFieldUsername.setColumns(10);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(27, 159, 100, 17);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(170, 156, 200, 20);
        contentPane.add(passwordField);

        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblConfirmPassword.setBounds(27, 202, 150, 17);
        contentPane.add(lblConfirmPassword);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(170, 199, 200, 20);
        contentPane.add(confirmPasswordField);

        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(34, 244, 100, 17);
        contentPane.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(170, 238, 200, 20);
        contentPane.add(textFieldEmail);
        textFieldEmail.setColumns(10);

        JLabel lblFullName = new JLabel("Full Name");
        lblFullName.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblFullName.setBounds(34, 283, 100, 20);
        contentPane.add(lblFullName);

        textFieldFullName = new JTextField();
        textFieldFullName.setBounds(170, 283, 200, 20);
        contentPane.add(textFieldFullName);
        textFieldFullName.setColumns(10);

        JButton btnSignup = new JButton("Sign Up");
        btnSignup.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        btnSignup.setBounds(455, 347, 122, 23);
        contentPane.add(btnSignup);
        
        JButton resetButton = new JButton("Reset");
        Image img3 = new ImageIcon(this.getClass().getResource("/reset-icon.png")).getImage();
        resetButton.setIcon(new ImageIcon(img3));
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	textFieldUsername.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                textFieldEmail.setText("");
                textFieldFullName.setText("");
            }
        });
        resetButton.setBounds(600, 347, 122, 23);
        contentPane.add(resetButton);

        setContentPane(contentPane);

        
        JLabel lblNewLabel = new JLabel("Already Account");
        lblNewLabel.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		new UserSignUpUI().setVisible(true);
        	}
        });
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(694, 387, 130, 25);
        contentPane.add(lblNewLabel);

        // Background Image
        JLabel lblBackground = new JLabel("");
        lblBackground.setBounds(0, 0, 850, 500);
        Image img = new ImageIcon(this.getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img));
        contentPane.add(lblBackground);
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbms", "root", "zamna0");
            System.out.println("Database connected");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUp() {
        String username = textFieldUsername.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = textFieldEmail.getText();
        String fullName = textFieldFullName.getText();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username, Password, and Email are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        try {
            String query = "INSERT INTO users (Username, Password, Email, FullName) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password); // Consider hashing the password before storing it
            pst.setString(3, email);
            pst.setString(4, fullName);
            int result = pst.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "User added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                textFieldUsername.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                textFieldEmail.setText("");
                textFieldFullName.setText("");
                new UserHome().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

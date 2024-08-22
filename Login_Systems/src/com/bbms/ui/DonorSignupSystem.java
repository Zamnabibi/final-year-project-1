package com.bbms.ui;

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
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class DonorSignupSystem extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textFieldUsername;
    private JPasswordField passwordField;
    private JTextField textFieldEmail;
    private JPasswordField confirmPasswordField;
    private Connection con;
    private JLabel timeLabel;
    private PreparedStatement pst; // Corrected from Connection to PreparedStatement

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                DonorSignupSystem frame = new DonorSignupSystem();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

   
       

    public DonorSignupSystem() {
    	 connectToDatabase();

         // Check if the connection is null
         if (con == null) {
             JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
         }
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

        JLabel lblTitle = new JLabel(" New Donor");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblTitle.setBounds(183, 11, 352, 50);
        contentPane.add(lblTitle);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblUsername.setBounds(69, 100, 80, 25);
        contentPane.add(lblUsername);

        textFieldUsername = new JTextField();
        textFieldUsername.setBounds(516, 102, 200, 25);
        contentPane.add(textFieldUsername);
        textFieldUsername.setColumns(10);

        // Password Label and Field
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPassword.setBounds(69, 153, 80, 25);
        contentPane.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(516, 155, 200, 25);
        contentPane.add(passwordField);

        // Confirm Password Label and Field
        JLabel lblConfirmPassword = new JLabel("Confirm Password");
        lblConfirmPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblConfirmPassword.setBounds(69, 205, 130, 25);
        contentPane.add(lblConfirmPassword);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(516, 207, 200, 25);
        contentPane.add(confirmPasswordField);

        // Email Label and Field
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblEmail.setBounds(69, 256, 104, 17);
        contentPane.add(lblEmail);

        textFieldEmail = new JTextField();
        textFieldEmail.setBounds(516, 254, 200, 25);
        contentPane.add(textFieldEmail);
        textFieldEmail.setColumns(10);
        JButton btnSignup = new JButton("Login");
        Image img4 = new ImageIcon(this.getClass().getResource("/Ok-icon.png")).getImage();
        btnSignup.setIcon(new ImageIcon(img4));
        btnSignup.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSignup.setBounds(350, 347, 122, 23);

        btnSignup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Prompt user for confirmation
                int confirmResult = JOptionPane.showConfirmDialog(
                    btnSignup, 
                    "Are you sure you want to proceed with the login?", 
                    "Confirm Login", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE
                );

                // If the user confirms, proceed with the signup
                if (confirmResult == JOptionPane.YES_OPTION) {
                    signUp();
                }
            }
        });

        contentPane.add(btnSignup);


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
        btnClose.setBounds(80, 347, 122, 23);
        contentPane.add(btnClose);

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
            }
        });
        resetButton.setBounds(600, 347, 122, 23);
        contentPane.add(resetButton);

        setContentPane(contentPane);

        JLabel lblNewLabel = new JLabel("Already Account");
        lblNewLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new SignUpUI().setVisible(true);
            }
        });
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(694, 387, 130, 25);
        contentPane.add(lblNewLabel);

        // Add the footer panel
        FooterPanel footerPanel = new FooterPanel();
        footerPanel.setBounds(0, 475, 850, 50); // Adjust size and position as needed
        contentPane.add(footerPanel);

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
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bloods", "root", "zamna0");
            System.out.println("Database connected");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void signUp() {
    	 // Ensure the connection is not null
        if (con == null) {
            JOptionPane.showMessageDialog(this, "No database connection. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String username = textFieldUsername.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String email = textFieldEmail.getText();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "UserName, Password, and Email are required", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords does not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into database
        try {
        	String query = "INSERT INTO Admin (UserName, Password, Email, Status, Type) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);
            pst.setString(4, "Accepted");
            pst.setString(5, "Donor");
            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Signup successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Close the current frame and open the login frame
                closeResources();
                dispose();
                new DonorHomeFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Signup failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during signup: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
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

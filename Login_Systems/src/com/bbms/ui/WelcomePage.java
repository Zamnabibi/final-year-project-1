package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel animationLabel;
    private Timer animationTimer;
    private int xCoordinate = 0;
    private int direction = 1;

    public WelcomePage() {
        setTitle("Welcome Page");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Create a panel for content
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);

        // Create and set up the animation label
        animationLabel = new JLabel();
        animationLabel.setBounds(0, 0, 700, 360); // Adjusted size to fit the content area
        ImageIcon animationIcon = new ImageIcon(getClass().getResource("/animation.gif")); // Replace with your animation gif path
        animationLabel.setIcon(animationIcon);
        contentPane.add(animationLabel);

        // Timer for animation movement
        animationTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animateImage();
            }
        });
        animationTimer.start();

        // Create buttons and place them outside the animation area
        JButton btnAdmin = new JButton("Admin");
        btnAdmin.setBounds(750, 80, 100, 30);
        btnAdmin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddAdmin().setVisible(true);
            }
        });
        contentPane.add(btnAdmin);

        JButton btnDonor = new JButton("Donor");
        btnDonor.setBounds(750, 120, 100, 30);
        btnDonor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DonorSignupSystem().setVisible(true);
            }
        });
        contentPane.add(btnDonor);

        JButton btnPatient = new JButton("Patient");
        btnPatient.setBounds(750, 160, 100, 30);
        btnPatient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SignupSystem().setVisible(true);
            }
        });
        contentPane.add(btnPatient);

        // Create and add label
        JLabel lblSignUp = new JLabel("Login As:");
        lblSignUp.setFont(new Font("Tahoma", Font.BOLD, 34));
        lblSignUp.setBounds(750, 20, 210, 52);
        contentPane.add(lblSignUp);

        // Create and add close button
        JButton btnClose = new JButton("Close");
        btnClose.setBounds(750, 201, 100, 30);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Close the application
            }
        });
        contentPane.add(btnClose);

        setContentPane(contentPane);
        setVisible(true);
    }

    // Method to animate the image
    private void animateImage() {
        // Restrict the animation within a smaller area to the left
        int maxWidth = 700 - animationLabel.getWidth();
        xCoordinate += direction;
        animationLabel.setLocation(xCoordinate, 0);

        // Reverse direction when image reaches the edge of the restricted area
        if (xCoordinate >= maxWidth || xCoordinate <= 0) {
            direction *= -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WelcomePage();
            }
        });
    }
}

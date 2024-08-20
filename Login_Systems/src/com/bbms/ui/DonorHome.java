package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class DonorHome extends JFrame {

    public DonorHome() {
        // Set up the frame
        setTitle("Home Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the buttons (acting as tabs)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Left aligned, with gaps

        // Create buttons for each action
        JButton adminButton = new JButton("Admin");
        JButton userButton = new JButton("User");
        JButton donorButton = new JButton("Donor");
        JButton patientButton = new JButton("Patient");
        JButton bloodDonationButton = new JButton("Blood Donation");
        JButton bloodRequestButton = new JButton("Blood Request");
        JButton bloodGroupButton = new JButton("Blood Group");
        JButton stockButton = new JButton("Stock");
        JButton hospitalButton = new JButton("Hospital");
        JButton historyButton = new JButton("History");

        // Add buttons to the panel
        buttonPanel.add(adminButton);
        buttonPanel.add(userButton);
        buttonPanel.add(donorButton);
        buttonPanel.add(patientButton);
        buttonPanel.add(bloodDonationButton);
        buttonPanel.add(bloodRequestButton);
        buttonPanel.add(bloodGroupButton);
        buttonPanel.add(stockButton);
        buttonPanel.add(hospitalButton);
        buttonPanel.add(historyButton);

        // Add the button panel to the top of the frame
        add(buttonPanel, BorderLayout.NORTH);

        // Define action listeners for each button to open new frames
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame().setVisible(true);
            }
        });

        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserFrame().setVisible(true);
            }
        });

        donorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DonorFrame().setVisible(true);
            }
        });

        patientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PatientFrame().setVisible(true);
            }
        });

        bloodDonationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BloodDonationFrame().setVisible(true);
            }
        });

        bloodRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BloodRequestFrame().setVisible(true);
            }
        });

        bloodGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BloodGroupFrame().setVisible(true);
            }
        });

        stockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BloodStockFrame().setVisible(true);
            }
        });

        hospitalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HospitalFrame().setVisible(true);
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new HistoryFrame().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        // Create and show the HomePage
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DonorHome().setVisible(true);
            }
        });
    }
}

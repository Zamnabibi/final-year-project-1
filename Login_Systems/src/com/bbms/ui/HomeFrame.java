package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class HomeFrame extends JFrame {

    public HomeFrame() {
        // Set up the frame
        setTitle("Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a JTabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create and add panels to the tabs
        tabbedPane.addTab("Admin", new AdminFrame().getContentPane());
        tabbedPane.addTab("User", new UserFrame().getContentPane());
        tabbedPane.addTab("Donor", new DonorFrame().getContentPane());
        tabbedPane.addTab("Patient", new PatientFrame().getContentPane());
        tabbedPane.addTab("Blood Donation", new BloodDonationFrame().getContentPane());
        tabbedPane.addTab("Blood Request", new BloodRequestFrame().getContentPane());
        tabbedPane.addTab("Blood Group", new BloodGroupFrame().getContentPane());
        tabbedPane.addTab("Stock", new BloodStockFrame().getContentPane());
        tabbedPane.addTab("Hospital", new HospitalFrame().getContentPane());
        tabbedPane.addTab("History", new HistoryFrame().getContentPane());

        // Add the tabbed pane to the frame
        add(tabbedPane, BorderLayout.CENTER);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        // Create and add the Logout menu item
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        logoutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        menu.add(logoutMenuItem);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);

        setVisible(true);
    }

    // Method to handle logout
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            // Close the current frame and return to login screen
            this.dispose(); // Close the current frame
            //new LoginPage().setVisible(true); // Open the login page (assuming you have a LoginPage class)
        }
    }

    public static void main(String[] args) {
        // Create and show the HomeFrame
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HomeFrame();
            }
        });
    }
}

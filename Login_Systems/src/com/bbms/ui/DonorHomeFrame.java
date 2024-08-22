package com.bbms.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DonorHomeFrame extends JFrame {

    public DonorHomeFrame() {
        // Set up the frame
        setTitle("Donor Home Page");
        setSize(854, 517);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        createUserMenu(menuBar);
        createDonorMenu(menuBar);
        createLogoutMenu(menuBar);
        setJMenuBar(menuBar);

        // Create a panel with a background color
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BorderLayout()); // Use BorderLayout for positioning

        // Add the background image
        JLabel lblBackground = new JLabel();
        Image img1 = new ImageIcon(getClass().getResource("/back.jpg")).getImage();
        lblBackground.setIcon(new ImageIcon(img1.getScaledInstance(854, 517, Image.SCALE_SMOOTH)));
        backgroundPanel.add(lblBackground, BorderLayout.CENTER);

        // Create and add the footer panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.pink);
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel footerLabel = new JLabel("© 2024 Blood Bank Management System. All rights reserved.");
        footerPanel.add(footerLabel);
        backgroundPanel.add(footerPanel, BorderLayout.SOUTH);

        // Add the background panel to the frame
        setContentPane(backgroundPanel);
    }

    private void createUserMenu(JMenuBar menuBar) {
        JMenu userMenu = new JMenu("User");
        // Set the icon for the User menu
        try {
            Image img = new ImageIcon(getClass().getResource("/add donor.png")).getImage(); // Update with your actual icon path
            userMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("User menu icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"Add User", "Update User", "Delete User"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png"}; // Update with your actual icon paths

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the menu item
            try {
                Image img = new ImageIcon(getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            userMenu.add(menuItem);
        }

        menuBar.add(userMenu);
    }

    private void createDonorMenu(JMenuBar menuBar) {
        JMenu donorMenu = new JMenu("Donor Unit");
        // Set the icon for the Donor menu
        try {
            Image img = new ImageIcon(getClass().getResource("/add new.png")).getImage(); // Update with your actual icon path
            donorMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Donor menu icon not found.");
        }

        // Define actions and corresponding icons
        String[] actions = {"Add Donor Unit", "Update Donor Unit", "Delete Donor Unit"};
        String[] icons = {"/add new.png", "/update.png", "/delete.png"}; // Update with your actual icon paths

        for (int i = 0; i < actions.length; i++) {
            final String action = actions[i]; // Make the variable final for use in lambda expression
            JMenuItem menuItem = new JMenuItem(action);
            // Set icon for the donor menu item
            try {
                Image img = new ImageIcon(getClass().getResource(icons[i])).getImage();
                menuItem.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.out.println("Icon not found for action: " + action);
            }
            menuItem.addActionListener(e -> openFrame(action));
            addMenuShortcut(menuItem, action);
            donorMenu.add(menuItem);
        }

        menuBar.add(donorMenu);
    }

    private void createLogoutMenu(JMenuBar menuBar) {
        JMenu logoutMenu = new JMenu("Logout");
        // Set the icon for the Logout menu
        try {
            Image img = new ImageIcon(getClass().getResource("/logout.jpg")).getImage();
            logoutMenu.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Logout icon not found.");
        }
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        // Set icon for the logout menu item
        try {
            Image img = new ImageIcon(getClass().getResource("/logout.jpg")).getImage();
            logoutMenuItem.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("Logout item icon not found.");
        }
        logoutMenuItem.addActionListener(e -> logout());
        logoutMenu.add(logoutMenuItem);
        menuBar.add(logoutMenu);
    }

    private void openFrame(String action) {
        switch (action) {
            case "Add User": new AddUserFrame().setVisible(true); break;
            case "Update User": new UpdateUserFrame().setVisible(true); break;
            case "Delete User": new DeleteUserFrame().setVisible(true); break;
            
            case "Add Donor Unit": new AddDonorFrame().setVisible(true); break;
            case "Update Donor Unit": new UpdateDonorFrame().setVisible(true); break;
            case "Delete Donor Unit": new DeleteDonorFrame().setVisible(true); break;
            
        }
    }

    private void addMenuShortcut(JMenuItem menuItem, String action) {
        Action openFrameAction = new AbstractAction(action) {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFrame(action);
            }
        };
        getRootPane().getActionMap().put(action, openFrameAction);
        KeyStroke keyStroke = switch (action) {
            case "Add User" -> KeyStroke.getKeyStroke("ctrl shift U");
            case "Update User" -> KeyStroke.getKeyStroke("ctrl shift E");
            case "Delete User" -> KeyStroke.getKeyStroke("ctrl shift R");
            case "View User" -> KeyStroke.getKeyStroke("ctrl shift O");
            case "Add Donor Unit" -> KeyStroke.getKeyStroke("ctrl shift A");
            case "Update Donor Unit" -> KeyStroke.getKeyStroke("ctrl shift J");
            case "Delete Donor Unit" -> KeyStroke.getKeyStroke("ctrl shift D");
            case "View Donor Units" -> KeyStroke.getKeyStroke("ctrl shift J");
            default -> null;
        };
        if (keyStroke != null) {
            getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, action);
            menuItem.setAccelerator(keyStroke);
        }
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the current frame
            // Uncomment the line below to open the login page after logout
            new SignUpUI().setVisible(true); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonorHomeFrame().setVisible(true));
    }
}
